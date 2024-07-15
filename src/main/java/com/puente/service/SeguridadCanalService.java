package com.puente.service;

import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.persistence.repository.SeguridadCanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;

@Service
public class SeguridadCanalService {
    @Autowired
    private SeguridadCanalRepository seguridadRepository;
    @Autowired
    private CacheService cacheService;

    @Cacheable(value = "miCache")
    public List<SeguridadCanalEntity> getAllSeguridadCanal() {
        return this.seguridadRepository.findAll();
    }

    @Cacheable(value = "miCache", key = "#contrasenia")
    public SeguridadCanalEntity get(String contrasenia) {
        return this.seguridadRepository.findById(contrasenia).orElse(null);
    }

    @Cacheable(value = "miCache", key = "#channel")
    public SeguridadCanalEntity findBychannelCode(String channel) {
        return this.seguridadRepository.findByCodigoCanal(channel);
    }
}
