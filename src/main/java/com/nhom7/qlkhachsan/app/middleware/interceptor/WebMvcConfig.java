package com.nhom7.qlkhachsan.app.middleware.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = "com.nhom7.qlkhachsan.interceptor")
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private EmailFormatInterceptor emailFormatInterceptor;

    @Autowired
    public WebMvcConfig(EmailFormatInterceptor emailFormatInterceptor) {
        this.emailFormatInterceptor = emailFormatInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Đăng ký interceptor
        registry.addInterceptor(emailFormatInterceptor).addPathPatterns("/register");
    }
}
