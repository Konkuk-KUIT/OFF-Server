package com.example.off.config;

import com.example.off.jwt.JwtAuthenticationFilter;
import com.example.off.jwt.JwtTokenService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(
            JwtTokenService jwtTokenService
    ) {
        FilterRegistrationBean<JwtAuthenticationFilter> bean =
                new FilterRegistrationBean<>();

        // Servlet Filter 직접 등록
        bean.setFilter(new JwtAuthenticationFilter(jwtTokenService));

        // 인증 필요한 URL만
        bean.addUrlPatterns("/members/*");

        // 다른 필터들보다 먼저
        bean.setOrder(1);

        return bean;
    }
}
