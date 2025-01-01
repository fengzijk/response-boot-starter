package com.fengzijk.response.config;

import com.fengzijk.response.filter.RequestAndResponseLoggingFilter;
import com.fengzijk.response.properties.GlobalResponseProperties;
import com.fengzijk.response.util.StringUtilEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnProperty(value = GlobalResponseProperties.PREFIX + ".enabled", havingValue = "true")
@EnableConfigurationProperties({GlobalResponseProperties.class})
public class RequestAndResponseLoggingFilterConfig {


    @Autowired
    private GlobalResponseProperties globalResponseProperties;

    @Bean
    public RequestAndResponseLoggingFilter requestAndResponseLoggingFilter() {
        return new RequestAndResponseLoggingFilter();
    }

    @Bean
    public FilterRegistrationBean<RequestAndResponseLoggingFilter> loggingFilter() {
        if (StringUtilEx.isBlank(globalResponseProperties.getRequestLogUrlPattern())) {
            return null;
        }
            FilterRegistrationBean<RequestAndResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new RequestAndResponseLoggingFilter());
            registrationBean.addUrlPatterns(globalResponseProperties.getRequestLogUrlPattern());  // 根据需要设置 URL 模式
            return registrationBean;

    }
}
