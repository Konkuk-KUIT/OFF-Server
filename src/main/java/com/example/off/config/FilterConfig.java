package com.example.off.config;

import com.example.off.jwt.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(
            JwtAuthenticationFilter filter
    ) {
        FilterRegistrationBean<JwtAuthenticationFilter> bean =
                new FilterRegistrationBean<>();

        bean.setFilter(filter);
        bean.addUrlPatterns("/members/*"); // 보호할 경로
        bean.setOrder(1);

        return bean;
    }
}
