package com.puente.web.controller;

import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.service.*;
import com.puente.service.dto.RequestPayRemittanceDto;
import com.puente.service.dto.ResponseGetRemittanceDataDto;
import com.puente.service.dto.Wsdl03Dto;
import com.soap.wsdl.service03.SDTServicioVentanillaIn;
import com.soap.wsdl.service03.SDTServicioVentanillaInItemRemesa;
import com.soap.wsdl.service03.SDTServicioVentanillaInItemRemesaBeneficiario;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.Common.Person;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.Identification;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.AddressInformation.AddressUsage;
import jakarta.validation.Valid;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.datatype.DatatypeConfigurationException;

@ToString
@RestController
@RequestMapping("/pago")
public class PagoController {
    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    private final UtilService utilService;
    private final ValoresGlobalesRemesasService valoresGlobalesService;
    private final SeguridadCanalService seguridadCanalService;
    private final ConsultaService consultaService;
    private final Wsdl03Service wsdl03Service;
    private final WsdlBpService wsdlBpService;

    @Autowired
    public PagoController(
        UtilService utilService,
        ValoresGlobalesRemesasService valoresGlobalesService,
        SeguridadCanalService seguridadCanalService,
        ConsultaService consultaService,
        Wsdl03Service wsdl03Service,
        WsdlBpService wsdlBpService
    ) {
        this.utilService = utilService;
        this.valoresGlobalesService = valoresGlobalesService;
        this.seguridadCanalService = seguridadCanalService;
        this.consultaService = consultaService;
        this.wsdl03Service = wsdl03Service;
        this.wsdlBpService = wsdlBpService;
    }

    @PostMapping()
    public ResponseEntity<ResponseGetRemittanceDataDto> validateRemittance(
        @Valid @RequestBody RequestPayRemittanceDto requestData
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

            // get BP info
            DTCreaBusinessPartnerResp bpInfo = this.wsdlBpService.getBpInfo(requestData.getIdentificacion());
            boolean existBp = this.utilService.existBp(bpInfo);
            if(!existBp) {
                return ResponseEntity.ok(
                    this.utilService.getCustomMessageCode("ERROR04") // Error, se debe Crear BP
                );
            }

            // request pay remittance Wsdl03
            SDTServicioVentanillaIn request03 = this.getRequest03(
                requestData, bank, channelInfo, bpInfo
            );

            Wsdl03Dto wsdl03Response = this.wsdl03Service.payRemittance(request03);
            if(!this.utilService.isResponseSuccess(wsdl03Response)) {
                // error Wsdl03
                this.utilService.getCustomTechnicalMessage("ERRORWSDL03");
                ResponseGetRemittanceDataDto formattedResponse = this.utilService.getWsdlMessageCode(wsdl03Response);
                return ResponseEntity.ok(formattedResponse);
            }

            ResponseGetRemittanceDataDto responseGetRemittanceDataDto = new ResponseGetRemittanceDataDto();
            responseGetRemittanceDataDto.setMessage(wsdl03Response.getMessage());
            responseGetRemittanceDataDto.setCode(wsdl03Response.getCode());
            responseGetRemittanceDataDto.setData(wsdl03Response.getData());
            return ResponseEntity.ok(responseGetRemittanceDataDto);
        } catch (Exception e) {
            log.error("Error while processing requests", e);
            ResponseGetRemittanceDataDto formattedResponse = this.utilService.getExceptionMessageCode(e);
            return ResponseEntity.ok(formattedResponse);
        }
    }

    private SDTServicioVentanillaIn getRequest03(
        RequestPayRemittanceDto requestData,
        ValoresGlobalesRemesasEntity bank,
        SeguridadCanalEntity channelInfo,
        DTCreaBusinessPartnerResp bpInfo
    ) throws DatatypeConfigurationException {
        Person bpBeneficiaryInfo = this.utilService.getBpBeneficiaryInfo(bpInfo);
        Identification bpIdentificationInfo = this.utilService.getBpIdentificationInfo(bpInfo);
        AddressUsage bpAddressInfo = this.utilService.getBpAddressInfo(bpInfo);

        SDTServicioVentanillaInItemRemesa itemRemesa = new SDTServicioVentanillaInItemRemesa();
        itemRemesa.setCodigoBanco(bank.getValor());
        itemRemesa.setCodigoRemesadora(requestData.getRemesa().getCodigoRemesadora());
        itemRemesa.setIdentificadorRemesa(requestData.getRemesa().getIdentificadorRemesa());
        itemRemesa.setMotivoRemesa(requestData.getMotivo());
        itemRemesa.setTipoFormaPago(this.utilService.getPaymentType(channelInfo.getMetodoPago()));

        //Beneficiary
        SDTServicioVentanillaInItemRemesaBeneficiario beneficiary = new SDTServicioVentanillaInItemRemesaBeneficiario();
        beneficiary.setIdentificacion(requestData.getIdentificacion());
        beneficiary.setTipoIdentificacion(requestData.getTipoIdentificacion());
        beneficiary.setNombreCompleto(this.utilService.getFullName(bpBeneficiaryInfo));
        beneficiary.setPrimerNombre(bpBeneficiaryInfo.getName().getGivenName());
        beneficiary.setSegundoNombre(bpBeneficiaryInfo.getName().getMiddleName());
        beneficiary.setPrimerApellido(bpBeneficiaryInfo.getName().getFamilyName());
        beneficiary.setSegundoApellido(bpBeneficiaryInfo.getName().getAdditionalFamilyName());
        beneficiary.setFechaEmisionIdentificacion(
            this.utilService.getXmlFormattedDate(bpIdentificationInfo.getValidityPeriod().getStartDate())
        );
        beneficiary.setFechaVencimientoIdentificacion(
            this.utilService.getXmlFormattedDate(bpIdentificationInfo.getValidityPeriod().getEndDate())
        );
        beneficiary.setPaisResidencia(bpBeneficiaryInfo.getOriginCountryCode());
        // beneficiary.setDescriptorPaisResidencia(""); // No required
        beneficiary.setDepartamentoResidencia("true"); // TODO Requerido
        // beneficiary.setDescriptorDepaResidencia(""); // No required
        // beneficiary.setMunicipioResidencia(""); // No required
        // beneficiary.setDescriptorMuniResidencia(""); // No required
        beneficiary.setCiudadResidencia("true"); // TODO Requerido
        beneficiary.setDireccionResidencia(bpAddressInfo.getAddressUsageName());
        // beneficiary.setTelefonoContacto(""); // No required
        beneficiary.setCelularContacto("true"); // TODO Requerido
        // beneficiary.setEmailContacto(""); // No required
        // beneficiary.setGenero(""); // No required
        beneficiary.setFechaNacimiento(
            this.utilService.getXmlFormattedDate(bpBeneficiaryInfo.getBirthDate())
        );
        beneficiary.setCodigoNacionalidad(bpBeneficiaryInfo.getNationalityCountryCode());
        // beneficiary.setDescriptorNacionalidad(""); // No required
        beneficiary.setCodigoEstadoCivil("true"); // TODO Requerido
        beneficiary.setDescriptorEstadoCivil("true"); // TODO Requerido
        beneficiary.setCodigoOcupacion("true"); // TODO Requerido
        // beneficiary.setDescriptorOcupacion(""); // No required
        beneficiary.setCodigoRelacionRemitente("true"); // TODO Requerido
        // beneficiary.setDescriptorRelacionRemitente(""); // No required

        itemRemesa.setBeneficiario(beneficiary);

        SDTServicioVentanillaIn ventanillaIn = new SDTServicioVentanillaIn();
        ventanillaIn.setCanal(channelInfo.getCodigoCanalSireon());
        // ventanillaIn.setDescriptorCanal(""); // No required
        ventanillaIn.setAgenciaPago(requestData.getAgenciaPago());
        // ventanillaIn.setDescriptorAgenciaPago(""); // No required
        ventanillaIn.setSucursalPago(requestData.getSucursalPago());
        // ventanillaIn.setDescriptorSucursalPago(""); // No required
        ventanillaIn.setCajero(requestData.getCajero());

        ventanillaIn.setItemRemesa(itemRemesa);

        return ventanillaIn;
    }
}