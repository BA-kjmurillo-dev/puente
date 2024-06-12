package com.puente.web.controller;

import com.puente.persistence.entity.SeguridadCanalEntity;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    @GetMapping()
    public ResponseEntity<? extends ResponseDto> validateRemittance(
        @RequestBody RequestGetRemittanceDataDto requestData
    ) {
        try {
            // get channel information
            SeguridadCanalEntity channelInfo = this.seguridadCanalService.findBychannelCode(requestData.getCanal());
            ValoresGlobalesRemesasEntity bank = valoresGlobalesService.findByCodeAndItem( "01", "bank");
            if(channelInfo == null) {
                ResponseDto responseDto = new ResponseDto();
                responseDto.setMessageCode("CUSTOM01"); // channel is not parameterized
                ResponseDto formattedResponse = this.utilService.getFormattedMessageCode(responseDto);
                return ResponseEntity.ok(formattedResponse);
            }

            // Validate Sireon Active Wsdl04
            Wsdl04Dto wsdl04Response = this.wsdl04Service.testSireonConection(channelInfo.getCodigoCanalSireon());
            if(!this.utilService.isResponseSuccess(wsdl04Response)) {
                ResponseDto formattedResponse = this.utilService.getFormattedMessageCode(wsdl04Response);
                return ResponseEntity.ok(formattedResponse);
            }

            // get Remitters by channel Wsdl05 async
            CompletableFuture<Wsdl05Dto> getRemittersListByChannelAsync = CompletableFuture.supplyAsync(() -> {
                return this.wsdl05Service.getRemittersListByChannel(channelInfo.getCodigoCanalSireon());
            });

            // Validate Remittance in Sireon Wsdl07 async
            CompletableFuture<Wsdl07Dto> getRemittanceByIdentifierAsync = CompletableFuture.supplyAsync(() -> {
                ServicesRequest007ItemSolicitud request07 = new ServicesRequest007ItemSolicitud();
                request07.setCanal(channelInfo.getCodigoCanalSireon());
                request07.setCodigoBanco(bank.getValor());
                request07.setIdentificadorRemesa(requestData.getIdentificadorRemesa());
                return this.wsdl07Service.getRemittanceByIdentifier(request07);
            });

            return CompletableFuture.allOf(
                getRemittersListByChannelAsync, //Wsdl05
                getRemittanceByIdentifierAsync //Wsdl07
            ).thenApply(v -> {
                try {
                    Wsdl05Dto wsdl05Response = getRemittersListByChannelAsync.get();
                    Wsdl07Dto wsdl07Response = getRemittanceByIdentifierAsync.get();

                    if(!this.utilService.isResponseSuccess(wsdl05Response)) {
                        ResponseDto formattedResponse = this.utilService.getFormattedMessageCode(wsdl05Response);
                        return ResponseEntity.ok(formattedResponse);
                    }

                    List<Wsdl05Dto.Awsdl05Data> remittersList = wsdl05Response.getData();
                    String remitterCode;

                    if(wsdl07Response.getData() != null) {
                        String remittanceStatus = wsdl07Response.getData().getEstadoRemesa();
                        Boolean isValidStatus = this.wsdl07Service.isValidStatus(remittanceStatus);
                        if(isValidStatus) {
                            remitterCode = wsdl07Response.getData().getCodigoRemesadora();
                        } else {
                            ResponseDto responseDto = new ResponseDto();
                            responseDto.setMessageCode("CUSTOM03"); // Status remittance not valid
                            ResponseDto formattedResponse = this.utilService.getFormattedMessageCode(responseDto);
                            return ResponseEntity.ok(formattedResponse);
                        }
                    } else {
                        // TO DO: devolver un objeto con una bandera y la remesadora y modificar el if "error"
                        // Validate Remittance Algorithm
                        remitterCode = this.utilService.ConsultaRemesadora(requestData.getIdentificadorRemesa());
                    }

                    if("error".equals(remitterCode)) {
                        ResponseDto responseDto = new ResponseDto();
                        responseDto.setMessageCode("SIREMU"); // Pay for Siremu
                        ResponseDto formattedResponse = this.utilService.getFormattedMessageCode(responseDto);
                        return ResponseEntity.ok(formattedResponse);
                    }

                    boolean isChannelValid = remittersList.stream()
                            .anyMatch(remitter -> remitter.getCodigoRemesador().equals(remitterCode));

                    if(!isChannelValid) {
                        ResponseDto responseDto = new ResponseDto();
                        responseDto.setMessageCode("CUSTOM02"); // This remitter cannot pay for this channel.
                        ResponseDto formattedResponse = this.utilService.getFormattedMessageCode(responseDto);
                        return ResponseEntity.ok(formattedResponse);
                    }

                    // Get Remittance Data Wsdl03
                    RequestGetRemittanceDataDto request03 = new RequestGetRemittanceDataDto();
                    request03.setCanal(channelInfo.getCodigoCanalSireon());
                    request03.setIdentificadorRemesa(requestData.getIdentificadorRemesa());
                    request03.setCodigoBanco(bank.getValor());
                    request03.setCodigoRemesadora(remitterCode);
                    request03.setTipoFormaPago(consultaService.getPaymentType(channelInfo.getMetodoPago()));

                    Wsdl03Dto wsdl03Response = this.wsdl03Service.getRemittanceData(request03);

                    if(!this.utilService.isResponseSuccess(wsdl03Response)) {
                        ResponseDto formattedResponse = this.utilService.getFormattedMessageCode(wsdl03Response);
                        return ResponseEntity.ok(formattedResponse);
                    }

                    return ResponseEntity.ok(wsdl03Response);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error while processing async requests", e);
                    ResponseDto formattedResponse = this.utilService.getExceptionMessageCode(e);
                    return ResponseEntity.ok(formattedResponse);
                }
            }).join();
        } catch (Exception e) {
            log.error("Error while processing async requests", e);
            ResponseDto formattedResponse = this.utilService.getExceptionMessageCode(e);
            return ResponseEntity.ok(formattedResponse);
        }
    }
}
