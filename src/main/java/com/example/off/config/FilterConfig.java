package com.example.off.config;

import com.example.off.jwt.JwtAuthenticationFilter;
import com.example.off.jwt.JwtTokenProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    //Todo: config 및 jwtProvider dir 이동필요
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(
            JwtTokenProvider jwtTokenProvider
    ) {

        FilterRegistrationBean<JwtAuthenticationFilter> bean =
                new FilterRegistrationBean<>();

        // Servlet Filter 직접 등록
        bean.setFilter(new JwtAuthenticationFilter(jwtTokenProvider));

        // 인증 필요한 URL만
        bean.addUrlPatterns("/members/*", "/members/*/*", "/members/*/*/*");

        // 다른 필터들보다 먼저
        bean.setOrder(1);

        return bean;
    }
}
