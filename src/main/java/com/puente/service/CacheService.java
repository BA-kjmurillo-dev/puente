package com.puente.service;

import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.persistence.repository.ValoresGlobalesRemesasRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(CacheService.class);
    @Autowired
    private ValoresGlobalesRemesasRepository remesasRepository;

    @Getter
    private String cacheEvictFixedRate;

    @PostConstruct
    public void init() {
        ValoresGlobalesRemesasEntity clear = remesasRepository.findByCodigoAndItem("clear", "cache").orElse(null);
        if ((clear != null ? clear.getValor() : null) !=null ) {
            cacheEvictFixedRate = clear.getValor();
        } else {
            cacheEvictFixedRate = "600000";// Valor por defecto 10 minutos
        }
    }
}
