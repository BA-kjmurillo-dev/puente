package com.puente.web.controller;

import com.puente.service.Wsdl03Service;
import com.puente.service.Wsdl04Service;
import com.puente.service.Wsdl07Service;
import com.puente.service.ConsultaServices;
import com.puente.service.dto.Wsdl04Dto;
import com.puente.service.dto.Wsdl07Dto;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
import com.puente.service.ConsultaRemesadoraService;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@ToString
@RestController
@RequestMapping("/consulta")
public class ConsultaController {
    private final ConsultaServices consultaServices;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl07Service wsdl07Service;
    @Autowired
     private ConsultaRemesadoraService consultaRemesadoraService;

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
    @PostMapping("/remesa")
    public ResponseEntity<String>ConsulaRenesadora(@RequestBody String remesa) {
        if (remesa == null){
            log.error("Remesa no puede ser null");
            throw new IllegalArgumentException("Remesa no puede ser null");
        }
        remesa = remesa.replaceAll("\"", "");
        remesa = remesa.replaceAll("\\s+", "");
        log.info("Constoller remesa:"+remesa);
        String respuesta = this.consultaServices.ConsultaRemesadora(remesa);
        if (respuesta.equals("000004") ||
                respuesta.equals("000007") ||
                respuesta.equals("000016") ||
                respuesta.equals("000018")){

            return ResponseEntity.ok("Remesa: "+remesa+" Remesadora: "+respuesta);
        } else if (respuesta.equals("000006")) {
            return ResponseEntity.ok("Remesa: "+remesa+" Remesadora: "+respuesta + " Redireccion a SIREMU");
        } else {
            return ResponseEntity.status(400).body("Error");
        }
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
    public ResponseEntity<Wsdl07Dto> wsdl07Test(
        @RequestBody ServicesRequest007ItemSolicitud itemSolicitudRequest
    ) {
        return ResponseEntity.ok(this.wsdl07Service.getRemittanceByIdentifier(itemSolicitudRequest));
    }

}
