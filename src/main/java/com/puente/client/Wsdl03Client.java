package com.puente.client;

import com.puente.web.config.MyProperties;
import com.soap.wsdl.service03.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class Wsdl03Client extends WebServiceGatewaySupport {
    @Autowired
    private MyProperties myProperties;
    private static final Logger log = LoggerFactory.getLogger(Wsdl03Client.class);

    public WSSIREON003SERVICIOVENTANILLAResponse getRemittanceData(
        SDTServicioVentanillaIn request03
    ) {
        //Credentials
        ServicesCredentials credentials = new ServicesCredentials();
        credentials.setServicesUser(myProperties.getServicesUser());
        credentials.setServicesPassword(myProperties.getServicesPassword());
        credentials.setServicesToken("");

        //Ventanilla In
        SDTServicioVentanillaIn ventanillaIn = new SDTServicioVentanillaIn();
        ventanillaIn.setOperacion("Consulta");
        ventanillaIn.setCanal(request03.getCanal());

        //Item Remesa
        SDTServicioVentanillaInItemRemesa itemRemesa = new SDTServicioVentanillaInItemRemesa();
        itemRemesa.setCodigoBanco(request03.getItemRemesa().getCodigoBanco());
        itemRemesa.setCodigoRemesadora(request03.getItemRemesa().getCodigoRemesadora());
        itemRemesa.setIdentificadorRemesa(request03.getItemRemesa().getIdentificadorRemesa());

        ventanillaIn.setItemRemesa(itemRemesa);

        //Request
        return this.servicesrequest003(credentials, ventanillaIn);
    }

    public WSSIREON003SERVICIOVENTANILLAResponse payRemittance(
        SDTServicioVentanillaIn request03
    ) {
        //Credentials
        ServicesCredentials credentials = new ServicesCredentials();
        credentials.setServicesUser(myProperties.getServicesUser());
        credentials.setServicesPassword(myProperties.getServicesPassword());
        credentials.setServicesToken("");

        //Ventanilla In
        SDTServicioVentanillaIn ventanillaIn = new SDTServicioVentanillaIn();
        ventanillaIn.setOperacion("Pago");
        ventanillaIn.setCanal(request03.getCanal());
        ventanillaIn.setDescriptorCanal(""); // No required
        ventanillaIn.setAgenciaPago(request03.getAgenciaPago());
        ventanillaIn.setDescriptorAgenciaPago(""); // No required
        ventanillaIn.setSucursalPago(request03.getSucursalPago());
        ventanillaIn.setDescriptorSucursalPago(""); // No required
        ventanillaIn.setCajero(request03.getCajero());

        //Item Remesa
        SDTServicioVentanillaInItemRemesa itemRemesa = new SDTServicioVentanillaInItemRemesa();
        itemRemesa.setCodigoBanco(request03.getItemRemesa().getCodigoBanco());
        itemRemesa.setCodigoRemesadora(request03.getItemRemesa().getCodigoRemesadora());
        itemRemesa.setIdentificadorRemesa(request03.getItemRemesa().getIdentificadorRemesa());
        itemRemesa.setMotivoRemesa(request03.getItemRemesa().getMotivoRemesa());
        itemRemesa.setTipoFormaPago(request03.getItemRemesa().getTipoFormaPago());
        itemRemesa.setCuentaDeposito(""); // No required

        //Beneficiary
        SDTServicioVentanillaInItemRemesaBeneficiario beneficiaryData = request03.getItemRemesa().getBeneficiario();
        SDTServicioVentanillaInItemRemesaBeneficiario itemRemesaBeneficiary = new SDTServicioVentanillaInItemRemesaBeneficiario();
        itemRemesaBeneficiary.setIdentificacion(beneficiaryData.getIdentificacion());
        itemRemesaBeneficiary.setTipoIdentificacion(beneficiaryData.getTipoIdentificacion());
        itemRemesaBeneficiary.setNombreCompleto(beneficiaryData.getNombreCompleto());
        itemRemesaBeneficiary.setPrimerNombre(beneficiaryData.getPrimerNombre());
        itemRemesaBeneficiary.setSegundoNombre(beneficiaryData.getSegundoNombre());
        itemRemesaBeneficiary.setPrimerApellido(beneficiaryData.getPrimerApellido());
        itemRemesaBeneficiary.setSegundoApellido(beneficiaryData.getSegundoApellido());
        itemRemesaBeneficiary.setFechaEmisionIdentificacion(beneficiaryData.getFechaEmisionIdentificacion());
        itemRemesaBeneficiary.setFechaVencimientoIdentificacion(beneficiaryData.getFechaEmisionIdentificacion());
        itemRemesaBeneficiary.setPaisResidencia(beneficiaryData.getPaisResidencia());
        itemRemesaBeneficiary.setDescriptorPaisResidencia(""); // No required
        itemRemesaBeneficiary.setDepartamentoResidencia(beneficiaryData.getDepartamentoResidencia());
        itemRemesaBeneficiary.setDescriptorDepaResidencia(""); // No required
        itemRemesaBeneficiary.setMunicipioResidencia(""); // No required
        itemRemesaBeneficiary.setDescriptorMuniResidencia(""); // No required
        itemRemesaBeneficiary.setCiudadResidencia(beneficiaryData.getCiudadResidencia());
        itemRemesaBeneficiary.setDireccionResidencia(beneficiaryData.getDireccionResidencia());
        itemRemesaBeneficiary.setTelefonoContacto(""); // No required
        itemRemesaBeneficiary.setCelularContacto(beneficiaryData.getCelularContacto());
        itemRemesaBeneficiary.setEmailContacto(""); // No required
        itemRemesaBeneficiary.setGenero(""); // No required
        itemRemesaBeneficiary.setFechaNacimiento(beneficiaryData.getFechaNacimiento());
        itemRemesaBeneficiary.setCodigoNacionalidad(beneficiaryData.getCodigoNacionalidad());
        itemRemesaBeneficiary.setDescriptorNacionalidad(""); // No required
        itemRemesaBeneficiary.setCodigoEstadoCivil(beneficiaryData.getCodigoEstadoCivil());
        itemRemesaBeneficiary.setDescriptorEstadoCivil(beneficiaryData.getDescriptorEstadoCivil());
        itemRemesaBeneficiary.setCodigoOcupacion(beneficiaryData.getCodigoOcupacion());
        itemRemesaBeneficiary.setDescriptorOcupacion(""); // No required
        itemRemesaBeneficiary.setCodigoRelacionRemitente(beneficiaryData.getCodigoRelacionRemitente());
        itemRemesaBeneficiary.setDescriptorRelacionRemitente(""); // No required

        itemRemesa.setBeneficiario(itemRemesaBeneficiary);

        ventanillaIn.setItemRemesa(itemRemesa);

        //Request
        return this.servicesrequest003(credentials, ventanillaIn);
    }

    public WSSIREON003SERVICIOVENTANILLAResponse servicesrequest003(
        ServicesCredentials credentials,
        SDTServicioVentanillaIn sDTServicioVentanillaIn
    ) {
        ServicesRequest003 servicesrequest003 = new ServicesRequest003();
        servicesrequest003.setServicesCredentials(credentials);
        servicesrequest003.setVentanilla(sDTServicioVentanillaIn);

        WSSIREON003SERVICIOVENTANILLA wssireon003 = new WSSIREON003SERVICIOVENTANILLA();
        wssireon003.setServicesrequest003(servicesrequest003);

        String url = myProperties.getAwssireon003();
        log.info(url);

        SoapActionCallback callback = new SoapActionCallback(url);
        //Response
        return (WSSIREON003SERVICIOVENTANILLAResponse) getWebServiceTemplate().marshalSendAndReceive(
            url,
                wssireon003,
            callback
        );
    }
}
