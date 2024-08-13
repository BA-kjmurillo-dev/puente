package com.puente.service;


import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.persistence.repository.ValoresGlobalesRemesasRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Optional;

@Service
public class ValoresGlobalesRemesasService {
    @Autowired
    private ValoresGlobalesRemesasRepository remesasRepository;
    @Autowired
    private CacheService cacheService;

    @Getter
    private boolean cacheEnabled;

    @Cacheable(value = "miCache")
    public List<ValoresGlobalesRemesasEntity> getAll() {
        return this.remesasRepository.findAll();
    }

    @Cacheable(value = "miCache")
    public ValoresGlobalesRemesasEntity get(String codigo) {
        return this.remesasRepository.findById(codigo).orElse(null);
    }

    @Cacheable(value = "miCache")
    public ValoresGlobalesRemesasEntity findByCodeAndItem(String code, String item) {
        return this.remesasRepository.findByCodigoAndItem(code, item).orElse(null);
    }

    @Cacheable(value = "miCache")
    public List<ValoresGlobalesRemesasEntity> getListByItem(String item) {
        return this.remesasRepository.findByItem(item);
    }

    @Cacheable(value = "miCache")
    public ValoresGlobalesRemesasEntity save(ValoresGlobalesRemesasEntity valoresGlobalesRemesasEntity) {
        return this.remesasRepository.save(valoresGlobalesRemesasEntity);
    }

}
