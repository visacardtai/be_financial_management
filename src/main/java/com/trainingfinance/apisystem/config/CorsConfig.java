package com.trainingfinance.apisystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Đường dẫn API bạn muốn áp dụng CORS
                .allowedOrigins("http://localhost:3000") // Thay đổi địa chỉ nguồn của bạn tại đây
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Các phương thức bạn muốn cho phép
                .allowedHeaders("*") // Cho phép tất cả các tiêu đề
                .allowCredentials(true); // Cho phép chuyển cookie (nếu cần thiết)
    }
}
//}
