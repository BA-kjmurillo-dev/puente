package com.puente.service;


import com.puente.persistence.entity.CamposRequeridosEntity;
import com.puente.persistence.repository.CamposRequeridosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CamposRequeridosService {
    private CamposRequeridosRepository camposRequeridosRepository;

    @Autowired
    public CamposRequeridosService(CamposRequeridosRepository camposRequeridosRepository) {
        this.camposRequeridosRepository = camposRequeridosRepository;
    }

    public List<CamposRequeridosEntity> getAll() {
        return this.camposRequeridosRepository.findAll();
    }

    public CamposRequeridosEntity get(String codigo) {
        return this.camposRequeridosRepository.findById(codigo).orElse(null);
    }
}
