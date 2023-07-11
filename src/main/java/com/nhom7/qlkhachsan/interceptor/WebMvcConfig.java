package com.nhom7.qlkhachsan.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Đăng ký interceptor
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/register"); // Áp dụng cho URL đăng ký
    }
}
