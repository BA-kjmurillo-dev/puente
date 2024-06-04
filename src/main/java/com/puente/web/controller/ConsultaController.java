package com.puente.web.controller;


import com.puente.service.Wsdl03Service;
import com.puente.service.ConsultaServices;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ToString
@RestController
@RequestMapping("/consulta")
public class ConsultaController {
    @Autowired
    private ConsultaServices remesa;
    @Autowired
    private Wsdl03Service awssireon003;

    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    @GetMapping("/{remesa}")
    public String  ConsultaController(@PathVariable String remesa){

        if (remesa == null){
            log.error("Remesa no puede ser null");
            throw new IllegalArgumentException("Remesa no puede ser null");
        }
        log.info("Constoller remesa:"+remesa);
        return this.remesa.ConsultaRemesadora(remesa);
    }
    @GetMapping("/awssireon003")
    public ResponseEntity<String> awssireon003(){
        return ResponseEntity.ok(this.awssireon003.getMessage());
    }
}
