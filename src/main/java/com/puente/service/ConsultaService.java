package com.puente.service;

import com.puente.persistence.entity.SeguridadCanalEntity;
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
    @Autowired
    private SeguridadCanalService seguridadCanalService;
    private UtilService utilService;

    public String ConsultaRemesadora(String remesa){
        //log.info("Constoller remesa:"+remesa);
        return this.utilService.ConsultaRemesadora(remesa);
    }

    public String getPaymentType(
            String channel
    ) {
        SeguridadCanalEntity channelSecurity = this.seguridadCanalService.findBychannelCode(channel);
        String paymentMethod = channelSecurity.getMetodoPago();
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("01", "CASH");
        keyValueMap.put("02", "DEPOSIT_ACCOUNT");
        return keyValueMap.get(paymentMethod);
    }
}
