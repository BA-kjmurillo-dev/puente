package com.puente.web.controller;

import com.puente.service.*;
import com.puente.service.dto.Wsdl03Dto;
import com.puente.service.dto.Wsdl04Dto;
import com.puente.service.dto.Wsdl05Dto;
import com.puente.service.dto.Wsdl07Dto;
import com.soap.wsdl.service05.ServicesRequest005ItemSolicitud;
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
    private final ConsultaRemesadoraService consultaRemesadoraServices;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl05Service wsdl05Service;
    private final Wsdl07Service wsdl07Service;
    @Autowired
     private ConsultaRemesadoraService consultaRemesadoraService;

    @Autowired
    public ConsultaController(
        ConsultaServices consultaServices,
        ConsultaRemesadoraService consultaRemesadoraServices,
        Wsdl03Service wsdl03Service,
        Wsdl04Service wsdl04Service,
        Wsdl05Service wsdl05Service,
        Wsdl07Service wsdl07Service
    ) {
        this.consultaServices = consultaServices;
        this.consultaRemesadoraServices = consultaRemesadoraServices;
        this.wsdl03Service = wsdl03Service;
        this.wsdl04Service = wsdl04Service;
        this.wsdl05Service = wsdl05Service;
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

    @PostMapping("/wsdl05Test")
    public ResponseEntity<Wsdl05Dto> wsdl05Test(
            @RequestBody
            ServicesRequest005ItemSolicitud request005ItemSolicitud
    ){
        return ResponseEntity.ok(this.wsdl05Service.getListRemittances(request005ItemSolicitud));
    }
    @GetMapping("/wsdl07Test")
    public ResponseEntity<Wsdl07Dto> wsdl07Test(
        @RequestBody ServicesRequest007ItemSolicitud itemSolicitudRequest
    ) {
        return ResponseEntity.ok(this.wsdl07Service.getRemittanceByIdentifier(itemSolicitudRequest));
    }

}
