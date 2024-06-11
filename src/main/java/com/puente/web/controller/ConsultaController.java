package com.puente.web.controller;

import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.service.*;
import com.puente.service.dto.*;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
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
    private final UtilService utilService;
    private final ValoresGlobalesRemesasService valoresGlobalesService;
    private final SeguridadCanalService seguridadCanalService;
    private final ConsultaService consultaService;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl05Service wsdl05Service;
    private final Wsdl07Service wsdl07Service;

    @Autowired
    public ConsultaController(
        UtilService commonService,
        ValoresGlobalesRemesasService valoresGlobalesService,
        SeguridadCanalService seguridadCanalService,
        ConsultaService consultaService,
        Wsdl03Service wsdl03Service,
        Wsdl04Service wsdl04Service,
        Wsdl05Service wsdl05Service,
        Wsdl07Service wsdl07Service
    ) {
        this.utilService = commonService;
        this.valoresGlobalesService = valoresGlobalesService;
        this.seguridadCanalService = seguridadCanalService;
        this.consultaService = consultaService;
        this.wsdl03Service = wsdl03Service;
        this.wsdl04Service = wsdl04Service;
        this.wsdl05Service = wsdl05Service;
        this.wsdl07Service = wsdl07Service;
    }

    @GetMapping("/validateRemittance")
    public ResponseEntity<Object> validateRemittance(
        @RequestBody ServicesRequest007ItemSolicitud itemSolicitudRequest
    ) {
        // Validate Sireon Active Wsdl04
        Wsdl04Dto wsdl04Response = this.wsdl04Service.testSireonConection(itemSolicitudRequest.getCanal());
        Boolean isSireonActive = this.wsdl04Service.isSireonActive(wsdl04Response);
        log.info("isSireonActive:"+ isSireonActive);
        if(!this.utilService.isResponseSuccess(wsdl04Response)) {
            ResponseDto formattedResponse = this.utilService.getErrorMessageCode(wsdl04Response);
            return ResponseEntity.ok(formattedResponse);
        }

        // get Remitters by channel Wsdl05
        Wsdl05Dto wsdl05Response = this.wsdl05Service.getRemittersListByChannel(itemSolicitudRequest.getCanal());
        if(!this.utilService.isResponseSuccess(wsdl05Response)) {
            ResponseDto formattedResponse = this.utilService.getErrorMessageCode(wsdl05Response);
            return ResponseEntity.ok(formattedResponse);
        }
        List<Wsdl05Dto.Awsdl05Data> remittersList = wsdl05Response.getData();
        String remitterCode;

        // Validate Remittance Sireon Wsdl07
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
            remitterCode = this.utilService.ConsultaRemesadora(itemSolicitudRequest.getIdentificadorRemesa());
            log.info("Remittance Algorithm:" + remitterCode);
        }

        if(remitterCode == "error") {
            return ResponseEntity.ok("No se encontro información de la remesa.");
        }

        boolean isChannelValid = remittersList.stream().anyMatch(remitter -> remitter.getCodigoRemesador().equals(remitterCode));

        if(!isChannelValid) {
            return ResponseEntity.ok("Este canal no puede procesar esta remesa.");
        }

        // get Remittance Data Wsdl03
        RequestGetRemittanceDataDto request03 = new RequestGetRemittanceDataDto();
        request03.setCanal(itemSolicitudRequest.getCanal());
        request03.setIdentificadorRemesa(itemSolicitudRequest.getIdentificadorRemesa());
        request03.setCodigoBanco(bank.getValor());
        request03.setCodigoRemesadora(remitterCode);
        request03.setTipoFormaPago(consultaService.getPaymentType(itemSolicitudRequest.getCanal()));

        Wsdl03Dto wsdl03Response = this.wsdl03Service.getRemittanceData(request03);

        if(!this.utilService.isResponseSuccess(wsdl03Response)) {
            ResponseDto formattedResponse = this.utilService.getErrorMessageCode(wsdl03Response);
            return ResponseEntity.ok(formattedResponse);
        }

        return ResponseEntity.ok(wsdl03Response);
    }
}
