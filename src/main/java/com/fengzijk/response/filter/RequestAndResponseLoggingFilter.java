package com.fengzijk.response.filter;

import com.fengzijk.response.util.StringUtilEx;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * 请求响应日志过滤器
 *
 * @author : guozhifeng
 * @since : 2024/12/3 18:23
 */
@Component
@WebFilter(urlPatterns = {"/**"})
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {



     private static final Long MAX_BODY_SIZE = 4 * 1024L;


     Logger logger = LoggerFactory.getLogger(RequestAndResponseLoggingFilter.class);
     
    // 可见的类型
    private static final List<MediaType> VISIBLE_TYPES =
            Arrays.asList(MediaType.valueOf("text/*"), MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
                    MediaType.valueOf("application/*+json"), MediaType.valueOf("application/*+xml"));

    /**
     * 不应记录其值的 HTTP 标头列表。
     */
    private static final List<String> SENSITIVE_HEADERS = Arrays.asList("authorization", "proxy-authorization");


    private boolean enabled = true;



    //
    private static final List<String> NOT_LOG_CONTENT_TYPE = Arrays.asList(MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_MIXED_VALUE);

    private static void logRequestHeader(ContentCachingRequestWrapper request, StringBuilder msg) {

        Collections.list(request.getHeaderNames()).forEach(headerName -> Collections.list(request.getHeaders(headerName)).forEach(headerValue -> {
            if (isSensitiveHeader(headerName)) {
                msg.append(String.format("%s %s: %s", "请求头内容 :", headerName, "*******")).append("\n");
            } else {
                msg.append(String.format("%s %s: %s", "请求头内容 :", headerName, headerValue)).append("\n");
            }
        }));
    }

    private static void logRequestBody(ContentCachingRequestWrapper request, StringBuilder msg) {

        String queryString = request.getQueryString();

        msg.append("请求参数   : ").append(queryString).append("\n");

        msg.append("请求体内容 : ").append("\n");
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, request.getContentType(), msg);
        }
    }

    private static void logResponse(ContentCachingResponseWrapper response, StringBuilder msg) {
        int status = response.getStatus();
        msg.append(String.format("%s %s %s", "响应状态 : ", status, HttpStatus.valueOf(status).getReasonPhrase())).append("\n");

        String requestStartTimeStr = MDC.get("requestStartTime");
        if (!StringUtilEx.isBlank(requestStartTimeStr) && StringUtilEx.isNumeric(requestStartTimeStr)) {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - Long.parseLong(requestStartTimeStr);
            msg.append("请求耗时 : ").append(executionTime).append(" ms ").append("\n");
        }

        response.getHeaderNames().forEach(headerName -> response.getHeaders(headerName).forEach(headerValue -> {
            if (isSensitiveHeader(headerName)) {
                msg.append(String.format("%s %s: %s", "响应头内容 : ", headerName, "*******")).append("\n");
            } else {
                msg.append(String.format("%s %s: %s", "响应头内容 : ", headerName, headerValue)).append("\n");
            }
        }));
        msg.append("响应结果 : ");
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, response.getContentType(), msg);
        }
    }

    private static void logContent(byte[] content, String contentType, StringBuilder msg) {
        MediaType mediaType = MediaType.valueOf(contentType);

        boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible && content.length < MAX_BODY_SIZE) {
            String contentString = new String(content, StandardCharsets.UTF_8);
            Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> msg.append(" ").append(line).append("\n"));
        } else {
            msg.append(String.format("[%d bytes content]", content.length)).append("\n");
        }
    }

    /**
     * Determine if a given header name should have its value logged.
     *
     * @param headerName HTTP header name.
     * @return True if the header is sensitive (i.e. its value should <b>not</b> be logged).
     */
    private static boolean isSensitiveHeader(String headerName) {
        return SENSITIVE_HEADERS.contains(headerName.toLowerCase());
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new CustomRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    @ManagedOperation(description = "Enable logging of HTTP requests and responses")
    public void enable() {
        this.enabled = true;
    }

    @ManagedOperation(description = "Disable logging of HTTP requests and responses")
    public void disable() {
        this.enabled = false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        MDC.put("traceId", traceId);
        MDC.put("requestStartTime", String.valueOf(System.currentTimeMillis()));
        boolean ignoreContentType = NOT_LOG_CONTENT_TYPE.stream().anyMatch(contentType -> contentType.equalsIgnoreCase(request.getContentType()));

        if (isAsyncDispatch(request) || ignoreContentType) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {

        StringBuilder msg = new StringBuilder();

        try {
            beforeRequest(request, response, msg);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response, msg);
            if (logger.isInfoEnabled()) {
                logger.info(msg.toString());
            }
            response.copyBodyToResponse();
        }
    }

    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, StringBuilder msg) {
        if (enabled && logger.isInfoEnabled()) {
            msg.append("\n------------------- REQUEST ------------------- \n");
            msg.append("请求URI : ").append(request.getRequestURI()).append("\n");
            msg.append("请求URL : ").append(request.getRequestURL()).append("\n");
            msg.append("请求时间 : ").append(LocalDateTime.now().format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
            msg.append("请求方式 : ").append(request.getMethod()).append("\n");
            msg.append("traceId : ").append(MDC.get("traceId")).append("\n");
            msg.append("请求IP  : ").append(getIpAddress(request)).append("\n");

            logRequestHeader(request, msg);
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, StringBuilder msg) {


        if (enabled && logger.isInfoEnabled()) {
            logRequestBody(request, msg);
            //  msg.append("\n------------------- RESPONSE ------------------- \n");
            logResponse(response, msg);
            msg.append("\n------------------------------------------------------\n");
        }
    }

    public static class CustomRequestWrapper extends ContentCachingRequestWrapper {

        private final byte[] cachedContent;

        public CustomRequestWrapper(HttpServletRequest request) {
            super(request);
            try {
                this.cachedContent = StreamUtils.copyToByteArray(request.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public byte[] getContentAsByteArray() {
            return cachedContent;
        }

        @Override
        public ServletInputStream getInputStream() {
            return new ContentCachingInputStream(cachedContent);
        }

        public static class ContentCachingInputStream extends ServletInputStream {

            private final ByteArrayInputStream is;

            public ContentCachingInputStream(byte[] bytes) {
                this.is = new ByteArrayInputStream(Objects.isNull(bytes) ? new byte[0] : bytes);
            }

            @Override
            public int read() {
                return this.is.read();
            }

            @Override
            public int read(byte[] b) throws IOException {
                return this.is.read(b);
            }

            @Override
            public int read(final byte[] b, final int off, final int len) throws IOException {
                return this.is.read(b, off, len);
            }

            @Override
            public boolean isFinished() {
                return this.is.available() > 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public void close() throws IOException {
                this.is.close();
            }
        }
    }



    public static String getIpAddress(HttpServletRequest request) {
        // 首先, 获取 X-Forwarded-For 中的 IP 地址，它在 HTTP 扩展协议中能表示真实的客户端 IP
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (!StringUtilEx.isBlank(ipAddress) && !"unknown".equalsIgnoreCase(ipAddress)) {
            // 多次反向代理后会有多个 ip 值，第一个 ip 才是真实 ip, 例: X-Forwarded-For: client, proxy1, proxy2，proxy…
            int index = ipAddress.indexOf(",");
            if (index != -1) {
                return ipAddress.substring(0, index);
            }

            return ipAddress;
        }

        // 如果 X-Forwarded-For 获取不到, 就去获取 X-Real-IP
        ipAddress = request.getHeader("X-Real-IP");
        if (StringUtilEx.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 如果 X-Real-IP 获取不到, 就去获取 Proxy-Client-IP
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtilEx.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 如果 Proxy-Client-IP 获取不到, 就去获取 WL-Proxy-Client-IP
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtilEx.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 如果 WL-Proxy-Client-IP 获取不到, 就去获取 HTTP_CLIENT_IP
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtilEx.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 如果 HTTP_CLIENT_IP 获取不到, 就去获取 HTTP_X_FORWARDED_FOR
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtilEx.isBlank(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            // 都获取不到, 最后才通过 request.getRemoteAddr() 获取IP
            ipAddress = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ipAddress) ? "127.0.0.1" : ipAddress;
    }
}
