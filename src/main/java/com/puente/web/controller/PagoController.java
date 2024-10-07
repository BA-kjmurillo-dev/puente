package com.puente.web.controller;

import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.persistence.entity.TipoErrorEnum;
import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.service.*;
import com.puente.service.dto.*;
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
import javax.xml.datatype.XMLGregorianCalendar;

@ToString
@RestController
@RequestMapping("/pago")
public class PagoController {
    private static final Logger log = LoggerFactory.getLogger(PagoController.class);
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
    public ResponseEntity<ResponseGetRemittanceDataDto> pagoRemesa(
        @Valid @RequestBody RequestPayRemittanceDto requestData
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
                return ResponseEntity.status(400).body((
                    this.utilService.getCustomMessageCode("ERROR01", TipoErrorEnum.ERROR.getError()) // channel is not parameterized
                ));
            }

            // get BP info
            DTCreaBusinessPartnerResp bpInfo = this.wsdlBpService.getBpInfo(requestData.getIdentificacion());
            boolean existBp = this.utilService.existBp(bpInfo);
            if(!existBp) {
                return ResponseEntity.status(400).body(
                    this.utilService.getCustomMessageCode("ERROR04", TipoErrorEnum.Error_Validacion.getError()) // Error, se debe Crear BP
                );
            }

            // request pay remittance Wsdl03

            PagoRequest03Dto resp03 =this.getRequest03(
                    requestData, bank, channelInfo, bpInfo
            );

            if (!resp03.getCode().equals("000000")) {
                return ResponseEntity.status(400).body(
                        this.utilService.getCustomMessageCode("ERROR06", TipoErrorEnum.Error_Datos.getError()) // Error, se debe Crear BP
                );
            }else {
                SDTServicioVentanillaIn request03 = resp03.getRequest03();
                Wsdl03Dto wsdl03Response = this.wsdl03Service.payRemittance(request03,credenciales003);
                if(!this.utilService.isResponseSuccess(wsdl03Response)) {
                    // error Wsdl03
                    this.utilService.getCustomTechnicalMessage("ERRORWSDL03");
                    ResponseGetRemittanceDataDto formattedResponse = this.utilService.getWsdlMessageCode(wsdl03Response,
                            TipoErrorEnum.Error_Servicio.getError());
                    return ResponseEntity.status(400).body(formattedResponse);
                }

                ResponseGetRemittanceDataDto responseGetRemittanceDataDto = new ResponseGetRemittanceDataDto();
                responseGetRemittanceDataDto.setMessage(wsdl03Response.getMessage());
                responseGetRemittanceDataDto.setCode(wsdl03Response.getCode());
                responseGetRemittanceDataDto.setData(wsdl03Response.getData());
                responseGetRemittanceDataDto.setType(TipoErrorEnum.SUCCESS.getError());
                return ResponseEntity.ok(responseGetRemittanceDataDto);
            }
        } catch (Exception e) {
            log.error("Error while processing requests", e);
            ResponseGetRemittanceDataDto formattedResponse = this.utilService.getExceptionMessageCode(e);
            return ResponseEntity.status(400).body(formattedResponse);
        }
    }

    private PagoRequest03Dto getRequest03(
        RequestPayRemittanceDto requestData,
        ValoresGlobalesRemesasEntity bank,
        SeguridadCanalEntity channelInfo,
        DTCreaBusinessPartnerResp bpInfo
    ) throws DatatypeConfigurationException {
        SDTServicioVentanillaIn ventanillaIn;
        String cod0 = "000000";
        String cod1 = "000001";
        String cod2 = "000002";
        String cod3 = "000003";
        String fechaError = "00000000";

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

        // OJO con las fechas guardar en log
        String dateBirth;
        if(!bpIdentificationInfo.getValidityPeriod().getStartDate().equals(fechaError)) {
            dateBirth = bpIdentificationInfo.getValidityPeriod().getStartDate();
            XMLGregorianCalendar rsp1 = this.utilService.getXmlFormattedDate(dateBirth);
            if (rsp1 != null) {
                beneficiary.setFechaEmisionIdentificacion(rsp1);
            }else {
                ventanillaIn = null;
                return resp03(ventanillaIn, cod1);
            }
        }else {
            ventanillaIn = null;
            return resp03(ventanillaIn, cod1);

        }

        String dateExpiration;
        if(!bpIdentificationInfo.getValidityPeriod().getEndDate().equals(fechaError)) {
            dateExpiration = bpIdentificationInfo.getValidityPeriod().getEndDate();
            XMLGregorianCalendar rsp2 = this.utilService.getXmlFormattedDate(dateExpiration);
            if (rsp2 != null) {
                beneficiary.setFechaVencimientoIdentificacion(rsp2);
            }else {
                ventanillaIn = null;
                return resp03(ventanillaIn, cod2);
            }
        }else {
            ventanillaIn = null;
            return resp03(ventanillaIn, cod3);
        }

        String fechaNacimiento;
        if (!bpBeneficiaryInfo.getBirthDate().equals(fechaError)) {
            fechaNacimiento = bpBeneficiaryInfo.getBirthDate();
            XMLGregorianCalendar rsp3 = this.utilService.getXmlFormattedDate(fechaNacimiento);
            if (rsp3 != null) {
                beneficiary.setFechaNacimiento(rsp3);
            }else {
                ventanillaIn = null;
                return resp03(ventanillaIn, cod3);
            }
        }else {
            ventanillaIn = null;
            return resp03(ventanillaIn, cod3);
        }
        beneficiary.setPaisResidencia(bpBeneficiaryInfo.getOriginCountryCode());
        // beneficiary.setDescriptorPaisResidencia(""); // No required
        beneficiary.setDepartamentoResidencia("true"); //
        // beneficiary.setDescriptorDepaResidencia(""); // No required
        // beneficiary.setMunicipioResidencia(""); // No required
        // beneficiary.setDescriptorMuniResidencia(""); // No required
        beneficiary.setCiudadResidencia("true"); //
        beneficiary.setDireccionResidencia(bpAddressInfo.getAddressUsageName());
        // beneficiary.setTelefonoContacto(""); // No required
        beneficiary.setCelularContacto("true"); //
        // beneficiary.setEmailContacto(""); // No required
        // beneficiary.setGenero(""); // No required


        beneficiary.setCodigoNacionalidad(bpBeneficiaryInfo.getNationalityCountryCode());
        // beneficiary.setDescriptorNacionalidad(""); // No required
        beneficiary.setCodigoEstadoCivil("true"); //
        beneficiary.setDescriptorEstadoCivil("true"); //
        beneficiary.setCodigoOcupacion("true"); //
        // beneficiary.setDescriptorOcupacion(""); // No required
        beneficiary.setCodigoRelacionRemitente("true"); //
        // beneficiary.setDescriptorRelacionRemitente(""); // No required

        itemRemesa.setBeneficiario(beneficiary);

        ventanillaIn = getSdtServicioVentanillaIn(requestData, channelInfo, itemRemesa);

        return resp03(ventanillaIn, cod0);
    }

    private static SDTServicioVentanillaIn getSdtServicioVentanillaIn(RequestPayRemittanceDto requestData, SeguridadCanalEntity channelInfo, SDTServicioVentanillaInItemRemesa itemRemesa) {
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

    private static PagoRequest03Dto resp03(SDTServicioVentanillaIn ventanillaIn, String code) {
        PagoRequest03Dto response = new PagoRequest03Dto();
        response.setCode(code);
        response.setRequest03(ventanillaIn);
        return response;
    }
}