// src/main/java/com/example/PizzUMBurgUM/config/WebConfig.java
package com.example.PizzUMBurgUM.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/cliente/**", "/funcionario/**")
                .excludePathPatterns("/auth/**", "/publico/**", "/css/**", "/js/**", "/images/**");
    }
}
