package com.puente.web.controller;

import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.service.*;
import com.puente.service.dto.*;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
import com.puente.service.UtilServices;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@ToString
@RestController
@RequestMapping("/consulta")
public class ConsultaController {
    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    private ValoresGlobalesRemesasService valoresGlobalesService;
    private SeguridadCanalService seguridadCanalService;
    private final ConsultaService consultaService;
    private final UtilServices consultaRemesadoraServices;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl05Service wsdl05Service;
    private final Wsdl07Service wsdl07Service;
    @Autowired
     private UtilServices utilServices;

    @Autowired
    public ConsultaController(
        ValoresGlobalesRemesasService valoresGlobalesService,
        SeguridadCanalService seguridadCanalService,
        ConsultaService consultaService,
        UtilServices consultaRemesadoraServices,
        Wsdl03Service wsdl03Service,
        Wsdl04Service wsdl04Service,
        Wsdl05Service wsdl05Service,
        Wsdl07Service wsdl07Service
    ) {
        this.valoresGlobalesService = valoresGlobalesService;
        this.seguridadCanalService = seguridadCanalService;
        this.consultaService = consultaService;
        this.consultaRemesadoraServices = consultaRemesadoraServices;
        this.wsdl03Service = wsdl03Service;
        this.wsdl04Service = wsdl04Service;
        this.wsdl05Service = wsdl05Service;
        this.wsdl07Service = wsdl07Service;
    }

    @GetMapping("/validateRemittance")
    public ResponseEntity<Object> validateRemittance(
        @RequestBody ServicesRequest007ItemSolicitud itemSolicitudRequest
    ) {
        // Validate Sireon Active
        Wsdl04Dto wsdl04Response = this.wsdl04Service.testSireonConection(itemSolicitudRequest.getCanal());
        Boolean isSireonActive = this.wsdl04Service.isSireonActive(wsdl04Response);
        log.info("isSireonActive:"+ isSireonActive);
        if(!isSireonActive) {
            return ResponseEntity.ok("Sireon no esta activo");
        }

        // get Remitters by channel
        Wsdl05Dto wsdl05Response = this.wsdl05Service.getRemittersListByChannel(itemSolicitudRequest.getCanal());
        // Validate WSDL05
        if(!wsdl05Response.getMessageCode().equals("000000")) {
            return ResponseEntity.ok("error wsdl05");
        }
        List<Wsdl05Dto.Awsdl05Data> remittersList = wsdl05Response.getData();
        String remitterCode;

        // Validate Remittance Sireon
        ValoresGlobalesRemesasEntity bank = valoresGlobalesService.findByCodeAndItem( "01", "bank");
        itemSolicitudRequest.setCodigoBanco(bank.getValor());
        Wsdl07Dto wsdl07Response = this.wsdl07Service.getRemittanceByIdentifier(itemSolicitudRequest);
        if(wsdl07Response.getData() != null) {
            String remittanseStatus = wsdl07Response.getData().getEstadoRemesa();
            Boolean isValidStatus = this.wsdl07Service.isValidStatus(remittanseStatus);
            if(isValidStatus) {
                remitterCode = wsdl07Response.getData().getCodigoRemesadora();
            } else {
                remitterCode = null;
                // Retornar mensaje de status error Sireon
            }
        } else {
            // Validate Remittance Algorithm
            remitterCode = this.consultaRemesadoraServices.ConsultaRemesadora(itemSolicitudRequest.getIdentificadorRemesa());
            log.info("Remittance Algorithm:" + remitterCode);
        }

        if(remitterCode == "error") {
            return ResponseEntity.ok("No se encontro información de la remesa.");
        }

        boolean isChannelValid = remittersList.stream().anyMatch(remitter -> remitter.getCodigoRemesador().equals(remitterCode));

        if(!isChannelValid) {
            return ResponseEntity.ok("Este canal no puede procesar esta remesa.");
        }

        RequestGetRemittanceDataDto request03 = new RequestGetRemittanceDataDto();
        request03.setCanal(itemSolicitudRequest.getCanal());
        request03.setIdentificadorRemesa(itemSolicitudRequest.getIdentificadorRemesa());
        request03.setCodigoBanco(bank.getValor());
        request03.setCodigoRemesadora(remitterCode);

        SeguridadCanalEntity canal = seguridadCanalService.findBychannelCode(itemSolicitudRequest.getCanal());
        String paymentMethod = canal.getMetodoPago();
        request03.setTipoFormaPago(consultaService.getPaymentType(paymentMethod));

        Wsdl03Dto wsdl03Response = this.wsdl03Service.getRemittanceData(request03);

        if(!wsdl03Response.getMessageCode().equals("000000")) {
            return ResponseEntity.ok("error wsdl03");
        }

        return ResponseEntity.ok(wsdl03Response);
    }


}
