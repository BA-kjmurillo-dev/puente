package com.puente.web.controller;

import com.puente.service.Wsdl03Service;
import com.puente.service.Wsdl04Service;
import com.puente.service.Wsdl07Service;
import com.puente.service.ConsultaServices;
import com.puente.service.ConsultaRemesadoraService;
import com.puente.service.dto.Wsdl03Dto;
import com.puente.service.dto.Wsdl04Dto;
import com.puente.service.dto.Wsdl07Dto;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
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
    private final ConsultaServices consultaServices;
    private final ConsultaRemesadoraService consultaRemesadoraServices;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl07Service wsdl07Service;

    @Autowired
    public ConsultaController(
        ConsultaServices consultaServices,
        ConsultaRemesadoraService consultaRemesadoraServices,
        Wsdl03Service wsdl03Service,
        Wsdl04Service wsdl04Service,
        Wsdl07Service wsdl07Service
    ) {
        this.consultaServices = consultaServices;
        this.consultaRemesadoraServices = consultaRemesadoraServices;
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

    @GetMapping("/validateRemittance")
    public ResponseEntity<Object> validateRemittance(
        @RequestBody ServicesRequest007ItemSolicitud itemSolicitudRequest
    ) {
        // Validate Sireon Active
        Wsdl04Dto wsdl04Response = this.wsdl04Service.getMessage(itemSolicitudRequest.getCanal());
        Boolean isSireonActive = this.wsdl04Service.isSireonActive(wsdl04Response);
        log.info("isSireonActive:"+ isSireonActive);
        if(isSireonActive) {
            // Validate Channel Valid WSDL05
            Boolean isChannelValid = true;
            if(isChannelValid) {
                String remittanceCode = null;
                // Validate Remittance Sireon
                Wsdl07Dto wsdl07Response = this.wsdl07Service.getRemittanceByIdentifier(itemSolicitudRequest);
                if(wsdl07Response.getData() != null) {
                    String remittanseStatus = wsdl07Response.getData().getEstadoRemesa();
                    Boolean isValidStatus = this.wsdl07Service.isValidStatus(remittanseStatus);
                    if(isValidStatus) {
                        remittanceCode = wsdl07Response.getData().getCodigoRemesadora();
                    } else {
                        // Retornar mensaje de status error Sireon
                    }
                } else {
                    // Validate Remittance Algorithm
                    String remittanse = this.consultaRemesadoraServices.ConsultaRemesadora(itemSolicitudRequest.getIdentificadorRemesa());
                    log.info("Remittance Algorithm:"+ remittanse);
                    remittanceCode = remittanse;
                }
                return ResponseEntity.ok(remittanceCode);
            }
        }
        return ResponseEntity.ok("Sireon no esta activo");
    }

    @GetMapping("/wsdl03Test")
    public ResponseEntity<Wsdl03Dto> wsdl03Test() {
        return ResponseEntity.ok(this.wsdl03Service.getMessage());
    }

    @GetMapping("/wsdl04Test")
    public ResponseEntity<Wsdl04Dto> wsdl04Test() {
        String canal = "0002";
        return ResponseEntity.ok(this.wsdl04Service.getMessage(canal));
    }

    @GetMapping("/wsdl07Test")
    public ResponseEntity<Wsdl07Dto> wsdl07Test(
        @RequestBody ServicesRequest007ItemSolicitud itemSolicitudRequest
    ) {
        return ResponseEntity.ok(this.wsdl07Service.getRemittanceByIdentifier(itemSolicitudRequest));
    }
}
