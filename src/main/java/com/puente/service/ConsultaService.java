package com.puente.service;

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

    public String ConsultaRemesadora(String remesa){
        return this.utilService.ConsultaRemesadora(remesa);
    }

    public String getPaymentType(
        String paymentMethod
    ) {
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("01", "CASH");
        keyValueMap.put("02", "DEPOSIT_ACCOUNT");
        return keyValueMap.get(paymentMethod) == null ? "CASH" : keyValueMap.get(paymentMethod);
    }
}
