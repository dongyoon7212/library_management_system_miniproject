package com.study.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) { // cors setting
        registry.addMapping("/**") // 모든 주소
                .allowedMethods("*") // 모든 메소드
                .allowedOrigins("*");
    }
}
