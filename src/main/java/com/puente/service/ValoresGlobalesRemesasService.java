package com.puente.service;


import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.persistence.repository.ValoresGlobalesRemesasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValoresGlobalesRemesasService {
    private ValoresGlobalesRemesasRepository remesasRepository;

    @Autowired
    public ValoresGlobalesRemesasService(ValoresGlobalesRemesasRepository remesasRepository) {
        this.remesasRepository = remesasRepository;
    }

    public List<ValoresGlobalesRemesasEntity> getAll(){
        return this.remesasRepository.findAll();
    }

    public ValoresGlobalesRemesasEntity get(String codigo){
        return this.remesasRepository.findById(codigo).orElse(null);
    }

    public ValoresGlobalesRemesasEntity save(ValoresGlobalesRemesasEntity valoresGlobalesRemesasEntity){
        return this.remesasRepository.save(valoresGlobalesRemesasEntity);
    }
}
