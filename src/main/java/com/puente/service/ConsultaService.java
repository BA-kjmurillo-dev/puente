package com.puente.service;

import lombok.NoArgsConstructor;
import lombok.ToString;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@ToString
@NoArgsConstructor
public class ConsultaService {

    //private static final Logger log = LoggerFactory.getLogger(ConsultaService.class);
    @Autowired
    private UtilServices utilServices;

    public String ConsultaRemesadora(String remesa){
        //log.info("Constoller remesa:"+remesa);
        return this.utilServices.ConsultaRemesadora(remesa);
    }

    public String getPaymentType(
        String paymentMethod
    ) {
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("01", "CASH");
        keyValueMap.put("02", "DEPOSIT_ACCOUNT");
        return keyValueMap.get(paymentMethod);
    }
}
