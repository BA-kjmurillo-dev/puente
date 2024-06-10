package com.puente.web.controller;

import com.puente.service.dto.*;
import com.soap.wsdl.service05.ServicesRequest005ItemSolicitud;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.puente.service.*;

@RestController
@RequestMapping("/test")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    private final ConsultaServices consultaServices;
    private final ConsultaRemesadoraService consultaRemesadoraServices;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl05Service wsdl05Service;
    private final Wsdl07Service wsdl07Service;

    @Autowired
    public TestController(
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

    @PostMapping("/remittanceAlgorithm")
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
            respuesta.equals("000018")) {
            return ResponseEntity.ok("Remesa: "+remesa+" Remesadora: "+respuesta);
        } else if (respuesta.equals("000006")) {
            return ResponseEntity.ok("Remesa: "+remesa+" Remesadora: "+respuesta + " Redireccion a SIREMU");
        } else {
            return ResponseEntity.status(400).body("Error");
        }
    }

    @GetMapping("/wsdl03")
    public ResponseEntity<Wsdl03Dto> wsdl03Test() {
        RequestGetRemittanceDataDto request03 = new RequestGetRemittanceDataDto();
        request03.setCanal("0002");
        request03.setIdentificadorRemesa("202405230001");
        request03.setCodigoBanco("2000");
        request03.setCodigoRemesadora("000016");
        return ResponseEntity.ok(this.wsdl03Service.getRemittanceData(request03));
    }

    @GetMapping("/wsdl04")
    public ResponseEntity<Wsdl04Dto> wsdl04Test() {
        String canal = "0002";
        return ResponseEntity.ok(this.wsdl04Service.testSireonConection(canal));
    }

    @GetMapping("/wsdl05")
    public ResponseEntity<Wsdl05Dto> wsdl05Test() {
        String canal = "0002";
        return ResponseEntity.ok(this.wsdl05Service.getRemittersListByChannel(canal));
    }

    @GetMapping("/wsdl07")
    public ResponseEntity<Wsdl07Dto> wsdl07Test() {
        ServicesRequest007ItemSolicitud itemSolicitudRequest = new ServicesRequest007ItemSolicitud();
        itemSolicitudRequest.setCodigoBanco("2000");
        itemSolicitudRequest.setCanal("0002");
        itemSolicitudRequest.setIdentificadorRemesa("202405230001");
        return ResponseEntity.ok(this.wsdl07Service.getRemittanceByIdentifier(itemSolicitudRequest));
    }
}
