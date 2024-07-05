package com.puente.service;


import com.puente.persistence.entity.MessageCodesEntity;
import com.puente.persistence.repository.MessageCodesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageCodesService {
    @Autowired
    private MessageCodesRepository messageCodesRepository;

    @Cacheable(value = "miCache")
    public List<MessageCodesEntity> getAll() {
        return this.messageCodesRepository.findAll();
    }
    @Cacheable(value = "miCache", key = "#codigo")
    public MessageCodesEntity get(String codigo) {
        return this.messageCodesRepository.findById(codigo).orElse(null);
    }
}
