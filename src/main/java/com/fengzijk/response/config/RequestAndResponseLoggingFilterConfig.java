package com.fengzijk.response.config;

import com.fengzijk.response.filter.RequestAndResponseLoggingFilter;
import com.fengzijk.response.properties.GlobalResponseProperties;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(value = GlobalResponseProperties.PREFIX + ".request-log.enabled", havingValue = "true")
@EnableConfigurationProperties({GlobalResponseProperties.class})
public class RequestAndResponseLoggingFilterConfig {


    @Autowired
    private GlobalResponseProperties globalResponseProperties;


    @Bean
    public FilterRegistrationBean<RequestAndResponseLoggingFilter> loggingFilter() {

        String[] urlPatternList = getUrlPatternList();

        FilterRegistrationBean<RequestAndResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestAndResponseLoggingFilter(globalResponseProperties));
        registrationBean.addUrlPatterns(urlPatternList);  // 根据需要设置 URL 模式
        return registrationBean;
    }

    private String[] getUrlPatternList() {
        String[] urlPatternList = new String[0];

        if (Objects.nonNull(globalResponseProperties.getRequestLog()) && Objects.nonNull(globalResponseProperties.getRequestLog().getRequestLogUrlPatternList())
                && !globalResponseProperties.getRequestLog().getRequestLogUrlPatternList().isEmpty()) {
            urlPatternList = globalResponseProperties.getRequestLog().getRequestLogUrlPatternList().toArray(urlPatternList);
        } else {
            urlPatternList = new String[] {"/*"};
        }
        return urlPatternList;
    }
}
