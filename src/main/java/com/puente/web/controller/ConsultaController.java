package com.puente.web.controller;

import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.service.*;
import com.puente.service.dto.*;
import com.soap.wsdl.service03.SDTServicioVentanillaIn;
import com.soap.wsdl.service03.SDTServicioVentanillaInItemRemesa;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
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
    private final WsdlBpService wsdlBpService;

    @Autowired
    public ConsultaController(
        UtilService utilService,
        ValoresGlobalesRemesasService valoresGlobalesService,
        SeguridadCanalService seguridadCanalService,
        ConsultaService consultaService,
        Wsdl03Service wsdl03Service,
        Wsdl04Service wsdl04Service,
        Wsdl05Service wsdl05Service,
        Wsdl07Service wsdl07Service,
        WsdlBpService wsdlBpService
    ) {
        this.utilService = utilService;
        this.valoresGlobalesService = valoresGlobalesService;
        this.seguridadCanalService = seguridadCanalService;
        this.consultaService = consultaService;
        this.wsdl03Service = wsdl03Service;
        this.wsdl04Service = wsdl04Service;
        this.wsdl05Service = wsdl05Service;
        this.wsdl07Service = wsdl07Service;
        this.wsdlBpService = wsdlBpService;
    }

    @PostMapping()
    public ResponseEntity<ResponseGetRemittanceDataDto> validateRemittance(
        @RequestBody RequestGetRemittanceDataDto requestData
    ) {
        try {
            // get channel information
            SeguridadCanalEntity channelInfo = this.seguridadCanalService.findBychannelCode(requestData.getCanal());
            ValoresGlobalesRemesasEntity bank = valoresGlobalesService.findByCodeAndItem( "01", "bank");
            if(channelInfo == null) {
                return ResponseEntity.ok(
                    this.utilService.getCustomMessageCode("ERROR01") // channel is not parameterized
                );
            }

            // Validate Sireon Active Wsdl04
            Wsdl04Dto wsdl04Response = this.wsdl04Service.testSireonConection(channelInfo.getCodigoCanalSireon());
            if(!this.utilService.isResponseSuccess(wsdl04Response)) {
                // error Wsdl04
                ResponseGetRemittanceDataDto formattedResponse = this.utilService.getWsdlMessageCode(wsdl04Response);
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
                    Wsdl05Dto wsdl05Response = getRemittersListByChannelAsync.get(); //Wsdl05 response
                    Wsdl07Dto wsdl07Response = getRemittanceByIdentifierAsync.get(); //Wsdl07 response

                    if(!this.utilService.isResponseSuccess(wsdl05Response)) {
                        // error Wsdl05
                        ResponseGetRemittanceDataDto formattedResponse = this.utilService.getWsdlMessageCode(wsdl05Response);
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
                            return ResponseEntity.ok(
                                this.utilService.getCustomMessageCode("ERROR03") // Status remittance not valid
                            );
                        }
                    } else {
                        // TO DO: devolver un objeto con una bandera y la remesadora y modificar el if "error"
                        // Validate Remittance Algorithm
                        remitterCode = this.utilService.ConsultaRemesadora(requestData.getIdentificadorRemesa());
                    }

                    if("error".equals(remitterCode)) {
                        return ResponseEntity.ok(
                            this.utilService.getCustomMessageCode("SIREMU") // Pay for Siremu
                        );
                    }

                    // get BP info
                    DTCreaBusinessPartnerResp bpInfo = this.wsdlBpService.getBpInfo(requestData.getIdentificacion());
                    boolean existBp = this.utilService.existBp(bpInfo);
                    if(!existBp) {
                        boolean isJteller = channelInfo.getCanal().equals("jteller");
                        if(!isJteller) {
                            return ResponseEntity.ok(
                                this.utilService.getCustomMessageCode("ERROR04") // Error, se debe Crear BP
                            );
                        }
                    }

                    //validate BP Status
                    if(bpInfo.getCheckLists().getStatus() != null) {
                        return ResponseEntity.ok(
                             this.utilService.getCustomMessageCode("ERROR05") // Error in BP Status
                        );
                    }

                    // Validate Remitter in channel
                    boolean isChannelValid = remittersList.stream()
                            .anyMatch(remitter -> remitter.getCodigoRemesador().equals(remitterCode));

                    if(!isChannelValid) {
                        return ResponseEntity.ok(
                            this.utilService.getCustomMessageCode("ERROR02") // This remitter cannot pay for this channel.
                        );
                    }

                    // Get Remittance Data Wsdl03
                    SDTServicioVentanillaIn request03 = this.getRequest03(
                        requestData, bank, remitterCode, channelInfo
                    );
                    Wsdl03Dto wsdl03Response = this.wsdl03Service.getRemittanceData(request03);
                    if(!this.utilService.isResponseSuccess(wsdl03Response)) {
                        // error Wsdl03
                        ResponseGetRemittanceDataDto formattedResponse = this.utilService.getWsdlMessageCode(wsdl03Response);
                        return ResponseEntity.ok(formattedResponse);
                    }

                    ResponseGetRemittanceDataDto responseGetRemittanceDataDto = new ResponseGetRemittanceDataDto();
                    responseGetRemittanceDataDto.setData(wsdl03Response.getData());
                    return ResponseEntity.ok(responseGetRemittanceDataDto);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error while processing async requests", e);
                    ResponseGetRemittanceDataDto formattedResponse = this.utilService.getExceptionMessageCode(e);
                    return ResponseEntity.ok(formattedResponse);
                }
            }).join();
        } catch (Exception e) {
            log.error("Error while processing async requests", e);
            ResponseGetRemittanceDataDto formattedResponse = this.utilService.getExceptionMessageCode(e);
            return ResponseEntity.ok(formattedResponse);
        }
    }

    private SDTServicioVentanillaIn getRequest03(
        RequestGetRemittanceDataDto requestData,
        ValoresGlobalesRemesasEntity bank,
        String remitterCode,
        SeguridadCanalEntity channelInfo
    ) {
        SDTServicioVentanillaInItemRemesa sdtServicioVentanillaInItemRemesa = new SDTServicioVentanillaInItemRemesa();
        sdtServicioVentanillaInItemRemesa.setIdentificadorRemesa(requestData.getIdentificadorRemesa());
        sdtServicioVentanillaInItemRemesa.setCodigoBanco(bank.getValor());
        sdtServicioVentanillaInItemRemesa.setCodigoRemesadora(remitterCode);
        sdtServicioVentanillaInItemRemesa.setTipoFormaPago(consultaService.getPaymentType(channelInfo.getMetodoPago()));

        SDTServicioVentanillaIn request03 = new SDTServicioVentanillaIn();
        request03.setCanal(channelInfo.getCodigoCanalSireon());
        request03.setItemRemesa(sdtServicioVentanillaInItemRemesa);
        return request03;
    }
}
