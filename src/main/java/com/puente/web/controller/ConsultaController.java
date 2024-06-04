package com.puente.web.controller;

import com.puente.service.Wsdl03Service;
import com.puente.service.Wsdl04Service;
import com.puente.service.Wsdl07Service;
import com.puente.service.ConsultaServices;
import com.puente.service.dto.Wsdl04Dto;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
import com.soap.wsdl.service07.ServicesResponse007;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ToString
@RestController
@RequestMapping("/consulta")
public class ConsultaController {
    private ConsultaServices consultaServices;
    private Wsdl03Service wsdl03Service;
    private Wsdl04Service wsdl04Service;
    private Wsdl07Service wsdl07Service;

    @Autowired
    public ConsultaController(
        ConsultaServices consultaServices,
        Wsdl03Service wsdl03Service,
        Wsdl04Service wsdl04Service,
        Wsdl07Service wsdl07Service
    ) {
        this.consultaServices = consultaServices;
        this.wsdl03Service = wsdl03Service;
        this.wsdl04Service = wsdl04Service;
        this.wsdl07Service = wsdl07Service;
    }

    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    @GetMapping("/{remesa}")
    public String  ConsultaController(@PathVariable String remesa) {
        if (remesa == null){
            log.error("Remesa no puede ser null");
            throw new IllegalArgumentException("Remesa no puede ser null");
        }
        log.info("Constoller remesa:"+remesa);
        return this.consultaServices.ConsultaRemesadora(remesa);
    }
    @GetMapping("/wsdl03Test")
    public ResponseEntity<String> wsdl03Test() {
        return ResponseEntity.ok(this.wsdl03Service.getMessage());
    }

    @GetMapping("/wsdl04Test")
    public ResponseEntity<Wsdl04Dto> wsdl04Test() {
        return ResponseEntity.ok(this.wsdl04Service.getMessage());
    }

    @GetMapping("/wsdl07Test")
    public ResponseEntity<ServicesResponse007> wsdl07Test(
        @RequestBody ServicesRequest007ItemSolicitud itemSolicitudRequest
    ) {
        return ResponseEntity.ok(this.wsdl07Service.getRemittanceByIdentifier(itemSolicitudRequest));
    }
}
