package com.puente.service;


import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.persistence.repository.SeguridadCanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeguridadCanalService {
    private SeguridadCanalRepository seguridadRepository;

    @Autowired
    public SeguridadCanalService(SeguridadCanalRepository seguridadRepository){
        this.seguridadRepository = seguridadRepository;
    }

    public List<SeguridadCanalEntity> getAll() {
        return this.seguridadRepository.findAll();
    }

    public SeguridadCanalEntity get(String contrasenia){
        return this.seguridadRepository.findById(contrasenia).orElse(null);
    }

}
