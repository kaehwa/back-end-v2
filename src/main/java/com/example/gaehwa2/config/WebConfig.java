package com.example.gaehwa2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // 허용할 프론트 주소들
                        .allowedOrigins("http://localhost:8081", "http://172.31.239.54:8081", "http://172.31.239.54:8080", "http://localhost:8080")
                        .allowedMethods("*") // GET, POST, PUT, DELETE 등
                        .allowedHeaders("*") // 모든 헤더 허용
                        .allowCredentials(true); // 쿠키/인증정보 포함 허용
            }
        };
    }
}


