package com.puente.component;

import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.persistence.repository.ValoresGlobalesRemesasRepository;
import com.puente.service.CacheService;
import com.puente.web.config.CacheConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@EnableScheduling
public class CacheEvictScheduler {
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ValoresGlobalesRemesasRepository remesasRepository;


    @Scheduled(fixedRateString  = "#{cacheService.getCacheEvictFixedRate()}")
    public void evictAllCaches() {
        log.info("EvictAllCaches: "+cacheService.getCacheEvictFixedRate());
        cacheManager.getCacheNames().forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }


}
