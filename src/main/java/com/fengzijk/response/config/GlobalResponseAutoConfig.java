

package com.fengzijk.response.config;


import com.fengzijk.response.GlobalResponseHandler;
import com.fengzijk.response.properties.GlobalResponseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@EnableConfigurationProperties(GlobalResponseProperties.class)
@ConditionalOnProperty(value = GlobalResponseProperties.PREFIX + ".enabled", havingValue = "true")
public class GlobalResponseAutoConfig {

    @Bean
    public GlobalResponseHandler commonResponseDataAdvice() {
        return new GlobalResponseHandler();
    }
}
