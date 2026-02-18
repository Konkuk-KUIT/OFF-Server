package com.example.off.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해
                .allowedOrigins(
                        "http://localhost:5173",              // 로컬 개발 (Vite)
                        "http://localhost:3000",              // 로컬 개발 (React)
                        "http://offf.kro.kr",                 // 프로덕션
                        "https://offf.kro.kr",                // 프로덕션 HTTPS
                        "https://off-web-eosin.vercel.app"   // Vercel 배포
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);  // preflight 요청 캐시 시간 (1시간)
    }
}