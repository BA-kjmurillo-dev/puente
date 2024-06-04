package com.puente.service;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ToString
@NoArgsConstructor
public class ConsultaServices {

    private static final Logger log = LoggerFactory.getLogger(ConsultaServices.class);
    @Autowired
    private ConsultaRemesadoraService consultaRemesadoraService ;

    public String ConsultaRemesadora(String remesa){
        log.info("Constoller remesa:"+remesa);
        return this.consultaRemesadoraService.ConsultaRemesadora(remesa);
    }

}
