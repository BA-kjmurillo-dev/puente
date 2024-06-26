package com.puente.service;

import com.puente.service.dto.RemittanceAlgorithmDto;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@ToString
@NoArgsConstructor
public class ConsultaService {
    @Autowired
    private SeguridadCanalService seguridadCanalService;
    private UtilService utilService;

    public RemittanceAlgorithmDto ConsultaRemesadora(String remesa){
        return utilService.ConsultaRemesadora(remesa);
    }
}
