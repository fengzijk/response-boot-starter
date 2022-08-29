/*
 *   All rights Reserved, Designed By ZTE-ITS
 *   Copyright:    Copyright(C) 2019-2025
 *   Company       FENGZIJK LTD.
 *   @Author:    fengzijk
 *   @Email: guozhifengvip@gmail.com
 *   @Version    V1.0
 *   @Date:   2022年06月19日 13时33分
 *   Modification       History:
 *   ------------------------------------------------------------------------------------
 *   Date                  Author        Version        Description
 *   -----------------------------------------------------------------------------------
 *  2022-06-19 13:33:40    fengzijk         1.0         Why & What is modified: <修改原因描述>
 *
 *
 */

package com.fengzijk.response.config;


import com.fengzijk.response.GlobalResponseHandler;
import com.fengzijk.response.properties.GlobalResponseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import java.util.List;

/**
 * <pre>统一返回值以及异常处理</pre>
 *
 * @author : fengzijk
 * @date : 2021/10/4 0:38
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@EnableConfigurationProperties(GlobalResponseProperties.class)
@ConditionalOnProperty(value = GlobalResponseProperties.PREFIX + ".enabled", havingValue = "true")
public class GlobalResponseAutoConfig {

    @Bean
    public GlobalResponseHandler commonResponseDataAdvice() {
        return new GlobalResponseHandler();
    }

     static class ResponseResultConfig extends DelegatingWebMvcConfiguration {
         @Override
         protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
             converters.add(0, new MappingJackson2HttpMessageConverter());
             super.configureMessageConverters(converters);
         }
     }
}
