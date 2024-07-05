package com.puente.web.config;

import com.puente.component.CacheFilter;
import com.puente.persistence.repository.ValoresGlobalesRemesasRepository;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterCacheConfig {

    @Bean
    public FilterRegistrationBean<CacheFilter> cacheFilter(ValoresGlobalesRemesasRepository remesasRepository, CacheManager cacheManager) {
        CacheFilter cacheFilter = new CacheFilter(remesasRepository, cacheManager);
        FilterRegistrationBean<CacheFilter> registrationBean = new FilterRegistrationBean<>(cacheFilter);
        registrationBean.addUrlPatterns("/*"); // Apply filter to all URLs
        registrationBean.setOrder(1); // Set precedence
        return registrationBean;
    }
}
