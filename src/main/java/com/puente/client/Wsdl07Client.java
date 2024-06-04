package com.puente.client;

import com.puente.web.config.MyProperties;
import com.soap.wsdl.service07.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class Wsdl07Client extends WebServiceGatewaySupport {
    @Autowired
    private MyProperties myProperties;
    private static final Logger log = LoggerFactory.getLogger(Wsdl07Client.class);

    public WSSIREON007CONSULTAPORIDENTIFICADORResponse getRemittanceByIdentifier(ServicesRequest007ItemSolicitud itemSolicitudRequest) {
        //Credentials
        com.soap.wsdl.service07.ServicesCredentials credentials  = new ServicesCredentials();
        credentials.setServicesUser(myProperties.getServicesUser());
        credentials.setServicesPassword(myProperties.getServicesPassword());
        credentials.setServicesToken("");

        //Item Solicitud
        ServicesRequest007ItemSolicitud itemSolicitud = new ServicesRequest007ItemSolicitud();
        itemSolicitud.setCanal(itemSolicitudRequest.getCanal());
        itemSolicitud.setCodigoBanco(itemSolicitudRequest.getCodigoBanco());
        itemSolicitud.setIdentificadorRemesa(itemSolicitudRequest.getIdentificadorRemesa());

        //Request
        ServicesRequest007 request007 = new ServicesRequest007();
        request007.setServicesCredentials(credentials);
        request007.setItemSolicitud(itemSolicitud);

        WSSIREON007CONSULTAPORIDENTIFICADOR remittanceByIdentifier = new WSSIREON007CONSULTAPORIDENTIFICADOR();
        remittanceByIdentifier.setServicesrequest007(request007);

        String url = myProperties.getAwssireon007();
        log.info(url);
        System.out.println(request007);
        SoapActionCallback callback = new SoapActionCallback(url);
        //Response
        WSSIREON007CONSULTAPORIDENTIFICADORResponse response = (WSSIREON007CONSULTAPORIDENTIFICADORResponse) getWebServiceTemplate().marshalSendAndReceive(
            url,
            remittanceByIdentifier,
            callback
        );
        return response;
    }
}
