package com.puente.client;

import com.puente.service.dto.RequestGetRemittanceDataDto;
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
        SDTServicioVentanillaIn SDTServicioVentanillaIn = new SDTServicioVentanillaIn();
        SDTServicioVentanillaIn.setOperacion("Consulta");
        SDTServicioVentanillaIn.setCanal(request03.getCanal());

        //Item Remesa
        SDTServicioVentanillaInItemRemesa SDTServicioVentanillaInItemRemesa = new SDTServicioVentanillaInItemRemesa();
        SDTServicioVentanillaInItemRemesa.setCodigoBanco(request03.getItemRemesa().getCodigoBanco());
        SDTServicioVentanillaInItemRemesa.setCodigoRemesadora(request03.getItemRemesa().getCodigoRemesadora());
        SDTServicioVentanillaInItemRemesa.setIdentificadorRemesa(request03.getItemRemesa().getIdentificadorRemesa());
        SDTServicioVentanillaInItemRemesa.setTipoFormaPago(request03.getItemRemesa().getTipoFormaPago());

        SDTServicioVentanillaIn.setItemRemesa(SDTServicioVentanillaInItemRemesa);

        //Request
        ServicesRequest003 servicesrequest003 = new ServicesRequest003();
        servicesrequest003.setServicesCredentials(credentials);
        servicesrequest003.setVentanilla(SDTServicioVentanillaIn);

        WSSIREON003SERVICIOVENTANILLA WSSIREON003 = new WSSIREON003SERVICIOVENTANILLA();
        WSSIREON003.setServicesrequest003(servicesrequest003);

        String url = myProperties.getAwssireon003();
        log.info(url);
        System.out.println(servicesrequest003);
        SoapActionCallback callback = new SoapActionCallback(url);
        //Response
        WSSIREON003SERVICIOVENTANILLAResponse response = (WSSIREON003SERVICIOVENTANILLAResponse) getWebServiceTemplate().marshalSendAndReceive(
            url,
            WSSIREON003,
            callback
        );
        return response;
    }
}
