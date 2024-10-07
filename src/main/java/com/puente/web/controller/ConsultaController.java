package com.puente.web.controller;

import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.service.*;
import com.puente.service.dto.*;
import com.soap.wsdl.service03.SDTServicioVentanillaIn;
import com.soap.wsdl.service03.SDTServicioVentanillaInItemRemesa;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import java.util.List;


@Tag(name = "Consulta", description = "Este controlador permite consultar la información del Business Partner y los " +
                                      "servicio de SIREON.")
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

    /**
     * @description Método que se usa para consultar el Business Partner, los servicios de SIREON, el servicio Basa10 y
     *              valida el estado de la remesa para devolver la información del usuario y remesa.
     * @param requestData Recibe un objeto de tipo RequestGetRemittanceDataDto
     * @return ResponseGetRemittanceDataDto Retorna un objeto de tipo ResponseGetRemittanceDataDto.
     * */
    @Operation(summary = "Consulta" , description = "Método que permite consultar la información del Business Partner y " +
            "lo servicio de SIREON.")
    @ApiResponses({
            @ApiResponse(responseCode = "08", description = "Canal no esta parametrizado."),
            @ApiResponse(responseCode = "WSDL04", description = "Error en el servicio wsdl04."),
            @ApiResponse(responseCode = "WSDL05", description = "Error en el servicio wsdl05."),
            @ApiResponse(responseCode = "ERROR03", description = "Estado de la remesa no valido."),
            @ApiResponse(responseCode = "SIREMU3", description = "Remesa no disponible."),
            @ApiResponse(responseCode = "11", description = "Error, se debe Crear BP."),
            @ApiResponse(responseCode = "12", description = "Error al obtener la información del cliente."),
            @ApiResponse(responseCode = "09", description = "Esta remesadora no puede pagar por este canal."),
            @ApiResponse(responseCode = "WSDL03", description = "Error en el servicio wsdl03."),
            @ApiResponse(responseCode = "07", description = "Remesa solicitada no se encuentra en el repositorio de datos.")
    })
    @Parameters({
            @Parameter(name = "canal", description = "Canal", example = "0003"),
            @Parameter(name = "identificadorRemesa", description = "Identificador de la remesa", example = "202405230001"),
            @Parameter(name = "identificacion", description = "Identificación", example = "1207199500016"),
            @Parameter(name = "agencia", description = "Agencia", example = "101"),
            @Parameter(name = "sucursal", description = "Sucursal", example = "0001"),
            @Parameter(name = "cuenta", description = "Cuenta", example = "1200218152")
    })
    @PostMapping()
    public ResponseEntity<ResponseGetRemittanceDataDto> validateRemittance(
        @Valid @RequestBody RequestGetRemittanceDataDto requestData
    ) {
        try {
            // get channel information
            SeguridadCanalEntity channelInfo = this.seguridadCanalService.findBychannelCode(requestData.getCanal());
            ValoresGlobalesRemesasEntity bank = valoresGlobalesService.findByCodeAndItem( "01", "bank");

            CredencialesDto credencialesCanal = utilService.getCredenciales(requestData.getCanal());

            com.soap.wsdl.service03.ServicesCredentials credenciales003 = new com.soap.wsdl.service03.ServicesCredentials();
            com.soap.wsdl.service04.ServicesCredentials credenciales004 = new com.soap.wsdl.service04.ServicesCredentials();
            com.soap.wsdl.service05.ServicesCredentials credenciales005 = new com.soap.wsdl.service05.ServicesCredentials();
            com.soap.wsdl.service07.ServicesCredentials credenciales007 = new com.soap.wsdl.service07.ServicesCredentials();

            log.info(credencialesCanal.getUser());
            log.info(credencialesCanal.getPassword());
            log.info(credencialesCanal.getToken());

            credenciales003.setServicesUser(credencialesCanal.getUser());
            credenciales003.setServicesPassword(credencialesCanal.getPassword());
            credenciales003.setServicesToken(credencialesCanal.getToken());

            credenciales004.setServicesUser(credencialesCanal.getUser());
            credenciales004.setServicesPassword(credencialesCanal.getPassword());
            credenciales004.setServicesToken(credencialesCanal.getToken());

            credenciales005.setServicesUser(credencialesCanal.getUser());
            credenciales005.setServicesPassword(credencialesCanal.getPassword());
            credenciales005.setServicesToken(credencialesCanal.getToken());

            credenciales007.setServicesUser(credencialesCanal.getUser());
            credenciales007.setServicesPassword(credencialesCanal.getPassword());
            credenciales007.setServicesToken(credencialesCanal.getToken());


            if(channelInfo == null) {
                return ResponseEntity.status(400).body(
                    this.utilService.getCustomMessageCode("ERROR01") // channel is not parameterized
                );
            }

            // Validate Sireon Active Wsdl04
            Wsdl04Dto wsdl04Response = this.wsdl04Service.testSireonConection(channelInfo.getCodigoCanalSireon());
            if(!this.utilService.isResponseSuccess(wsdl04Response)) {
                // error Wsdl04
                this.utilService.getCustomTechnicalMessage("ERRORWSDL04");
                ResponseGetRemittanceDataDto formattedResponse = this.utilService.getWsdlMessageCode(wsdl04Response);
                return ResponseEntity.status(400).body(formattedResponse);
            }

            // get Remitters by channel Wsdl05 async
            CompletableFuture<Wsdl05Dto> getRemittersListByChannelAsync = CompletableFuture.supplyAsync(() -> {
                return this.wsdl05Service.getRemittersListByChannel(channelInfo.getCodigoCanalSireon());
            });

            // Validate Remittance in Sireon Wsdl07 async
            CompletableFuture<Wsdl07Dto> getRemittanceByIdentifierAsync = CompletableFuture.supplyAsync(() -> {
                ServicesRequest007ItemSolicitud request07 = this.getRequest07(
                    requestData, bank, channelInfo
                );
                return this.wsdl07Service.getRemittanceByIdentifier(request07);
            });

            // Validate Remittance Algorithm async
            CompletableFuture<RemittanceAlgorithmDto> validateRemittanceAlgorithmAsync = CompletableFuture.supplyAsync(() -> {
                return this.utilService.consultaRemesadora(requestData.getIdentificadorRemesa());
            });

            return CompletableFuture.allOf(
                getRemittersListByChannelAsync, //Wsdl05
                getRemittanceByIdentifierAsync, //Wsdl07
                validateRemittanceAlgorithmAsync //Algorithm
            ).thenApply(v -> {
                try {
                    Wsdl05Dto wsdl05Response = getRemittersListByChannelAsync.get(); //Wsdl05 response
                    Wsdl07Dto wsdl07Response = getRemittanceByIdentifierAsync.get(); //Wsdl07 response
                    RemittanceAlgorithmDto remittanceAlgorithmResponse = validateRemittanceAlgorithmAsync.get(); //Algorithm response

                    if(!this.utilService.isResponseSuccess(wsdl05Response)) {
                        // error Wsdl05
                        this.utilService.getCustomTechnicalMessage("ERRORWSDL05");
                        ResponseGetRemittanceDataDto formattedResponse = this.utilService.getWsdlMessageCode(wsdl05Response);
                        return ResponseEntity.status(400).body(formattedResponse);
                    }

                    List<Wsdl05Dto.Awsdl05Data> remittersList = wsdl05Response.getData();
                    String remitterCode;
                    String remitterMesage;
                    String datosExtras;

                    if(wsdl07Response.getData() != null) {
                        String remittanceStatus = wsdl07Response.getData().getEstadoRemesa();
                        Boolean isValidStatus = this.wsdl07Service.isValidStatus(remittanceStatus);
                        datosExtras = "false";
                        if(isValidStatus) {
                            remitterCode = wsdl07Response.getData().getCodigoRemesadora();
                            remitterMesage = "true";
                        } else {
                            return ResponseEntity.status(400).body(
                                this.utilService.getCustomMessageCode("ERROR03") // Status remittance not valid
                            );
                        }
                    } else {
                        remitterCode = remittanceAlgorithmResponse.getMrecod();
                        remitterMesage = remittanceAlgorithmResponse.getMessage();
                        datosExtras = remittanceAlgorithmResponse.getDatosExtras();
                    }

                    if("false".equals(remitterMesage)) {
                        return ResponseEntity.status(400).body(
                            this.utilService.getCustomMessageCode("SIREMU") // Pay for Siremu
                        );
                    }

                    // get BP info
                    DTCreaBusinessPartnerResp bpInfo = this.wsdlBpService.getBpInfo(requestData.getIdentificacion());
                    boolean existBp = this.utilService.existBp(bpInfo);

                    boolean isJteller = channelInfo.getCanal().equals("JTELLER");
                    if(!existBp) {
                        if(!isJteller) {
                            return ResponseEntity.status(400).body(
                                this.utilService.getCustomMessageCode("ERROR04") // Error, se debe Crear BP
                            );
                        }
                    }

                    //validate BP Status
                    if(bpInfo.getCheckLists().getStatus() != null) {
                        return ResponseEntity.status(400).body(
                             this.utilService.getCustomMessageCode("ERROR05") // Error in BP Status
                        );
                    }

                    // Validate Remitter in channel
                    boolean isChannelValid = remittersList.stream()
                            .anyMatch(remitter -> remitter.getCodigoRemesador().equals(remitterCode));

                    if(!isChannelValid) {
                        return ResponseEntity.status(400).body(
                            this.utilService.getCustomMessageCode("ERROR02") // This remitter cannot pay for this channel.
                        );
                    }

                    // Get Remittance Data Wsdl03
                    SDTServicioVentanillaIn request03 = this.getRequest03(
                        requestData, bank, remitterCode, channelInfo
                    );
                    Wsdl03Dto wsdl03Response = this.wsdl03Service.getRemittanceData(request03,credenciales003);
                    if(!this.utilService.isResponseSuccess(wsdl03Response)) {
                        // error Wsdl03
                        this.utilService.getCustomTechnicalMessage("ERRORWSDL03");
                        ResponseGetRemittanceDataDto formattedResponse = this.utilService.getWsdlMessageCode(wsdl03Response);
                        return ResponseEntity.status(400).body(formattedResponse);
                    }

                    // validate empty wsdl03 response
                    if(wsdl03Response.getData().getIdentificadorRemesa().isEmpty()) {
                        return ResponseEntity.status(400).body(
                            this.utilService.getCustomMessageCode("V30201") // remittance data not found
                        );
                    }

                    //Validaciones de canal

                    ValidacionCanalDto dataCompleta = new ValidacionCanalDto();
                    dataCompleta.setExisteBp(existBp);
                    dataCompleta.setItemRemesa(wsdl03Response.getData());
                    dataCompleta.setBpInfo(bpInfo);
                    dataCompleta.setCanal(channelInfo);
                    String codeValidation = this.consultaService.validacionCanalConsulta(dataCompleta,requestData.getCuenta(),requestData.getAgencia(),requestData.getSucursal());

                    if (!codeValidation.equals("000000")) {
                        return ResponseEntity.status(400).body(
                                this.utilService.getCustomMessageCode(codeValidation) // remittance data not found
                        );
                    }

                    // DTCreaBusinessPartnerResp bpInfo
                    ResponseGetRemittanceDataDto responseGetRemittanceDataDto = new ResponseGetRemittanceDataDto();
                    responseGetRemittanceDataDto.setMessage(wsdl03Response.getMessage());
                    responseGetRemittanceDataDto.setCode(wsdl03Response.getCode());
                    responseGetRemittanceDataDto.setData(wsdl03Response.getData());
                    responseGetRemittanceDataDto.setDatosExtras(datosExtras);
                    if(isJteller) { responseGetRemittanceDataDto.setExisteBp(existBp); responseGetRemittanceDataDto.setBpInfo(bpInfo); }
                    return ResponseEntity.ok(responseGetRemittanceDataDto);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error while processing async requests", e);
                    ResponseGetRemittanceDataDto formattedResponse = this.utilService.getExceptionMessageCode(e);
                    return ResponseEntity.status(400).body(formattedResponse);
                }
            }).join();
        } catch (Exception e) {
            log.error("Error while processing async requests", e);
            ResponseGetRemittanceDataDto formattedResponse = this.utilService.getExceptionMessageCode(e);
            return ResponseEntity.status(400).body(formattedResponse);
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

        SDTServicioVentanillaIn request03 = new SDTServicioVentanillaIn();
        request03.setCanal(channelInfo.getCodigoCanalSireon());
        request03.setItemRemesa(sdtServicioVentanillaInItemRemesa);
        return request03;
    }

    private ServicesRequest007ItemSolicitud getRequest07(
        RequestGetRemittanceDataDto requestData,
        ValoresGlobalesRemesasEntity bank,
        SeguridadCanalEntity channelInfo
    ) {
        ServicesRequest007ItemSolicitud request07 = new ServicesRequest007ItemSolicitud();
        request07.setCanal(channelInfo.getCodigoCanalSireon());
        request07.setCodigoBanco(bank.getValor());
        request07.setIdentificadorRemesa(requestData.getIdentificadorRemesa());
        return request07;
    }
}
