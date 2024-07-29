package com.puente.web.config;

import com.puente.web.interceptor.ValidacionBPInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ValidacionBPConfig implements WebMvcConfigurer {

    @Autowired
    private ValidacionBPInterceptor validacionBPInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(validacionBPInterceptor).addPathPatterns("/test/wsdlbp");
        registry.addInterceptor(validacionBPInterceptor).addPathPatterns("/crear/add");
    }
}