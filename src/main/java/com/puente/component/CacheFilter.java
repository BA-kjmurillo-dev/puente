package com.puente.component;

import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.persistence.repository.ValoresGlobalesRemesasRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.cache.CacheManager;

import java.io.IOException;
import java.util.Optional;

public class CacheFilter implements Filter {

    private final CacheManager cacheManager;
    private final ValoresGlobalesRemesasRepository remesasRepository;

    public CacheFilter(ValoresGlobalesRemesasRepository remesasRepository, CacheManager cacheManager) {
        this.remesasRepository = remesasRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Optional<ValoresGlobalesRemesasEntity> clearOptional = remesasRepository.findByCodigoAndItem("cache", "cache");
        clearOptional.ifPresent(clear -> {
            if ("true".equals(clear.getValor())) {
                limpiarCache();
                clear.setValor("false");
                remesasRepository.save(clear);
                System.out.println("Limpia Cache");
            }
        });
        chain.doFilter(request, response);
    }

    private void limpiarCache() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            if (cacheManager.getCache(cacheName) != null) {
                cacheManager.getCache(cacheName).clear();
            }
        });
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
