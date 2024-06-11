package com.puente.service;


import com.puente.persistence.entity.MessageCodesEntity;
import com.puente.persistence.repository.MessageCodesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageCodesService {
    private MessageCodesRepository messageCodesRepository;

    @Autowired
    public MessageCodesService(MessageCodesRepository messageCodesRepository) {
        this.messageCodesRepository = messageCodesRepository;
    }

    public List<MessageCodesEntity> getAll() {
        return this.messageCodesRepository.findAll();
    }

    public MessageCodesEntity get(String codigo) {
        return this.messageCodesRepository.findById(codigo).orElse(null);
    }
}
