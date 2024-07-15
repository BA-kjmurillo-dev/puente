package com.puente.service;

import com.puente.persistence.entity.ParametroRemesadoraEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.puente.persistence.repository.ParametroRemesadoraRepository;

import java.util.List;

@Service
public class ParametroRemesadoraService {
    @Autowired
    private ParametroRemesadoraRepository parametroRemesadoraRepository;


    @Cacheable(value = "miCache", key = "#mrecod")
    public ParametroRemesadoraEntity getMrecod(String mrecod) {
        return this.parametroRemesadoraRepository.findByMrecod(mrecod);
    }
    @Cacheable(value = "miCache")
    List<ParametroRemesadoraEntity> getAll() {
        return this.parametroRemesadoraRepository.findAll();
    }

}
