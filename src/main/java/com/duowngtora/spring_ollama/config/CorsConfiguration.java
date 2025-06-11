package com.duowngtora.spring_ollama.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Value("${application.cors.allowed-origins:}")
    private String allowedOrigins;

    private List<String> getAllowedOrigins(){
        if(allowedOrigins.isEmpty()) return new ArrayList<>();
        return Arrays.stream(allowedOrigins.split(",")).map(String::trim).toList();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {


        CorsRegistration corsRegistration = registry.addMapping("/**");// Áp dụng cho tất cả endpoints

        List<String> allowedOrigins = this.getAllowedOrigins();
        if(allowedOrigins.isEmpty()){
            corsRegistration.allowedOrigins("*");
        }else{
            for(String origin : allowedOrigins)
                corsRegistration.allowedOrigins(origin);
        }
        corsRegistration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization", "Content-Disposition")
            .allowCredentials(true)
            .maxAge(3600);  // Pre-flight cache trong 1 giờ
    }
}
