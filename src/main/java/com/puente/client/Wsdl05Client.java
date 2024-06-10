package com.puente.client;

import com.puente.web.config.MyProperties;
import com.soap.wsdl.service05.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class Wsdl05Client extends WebServiceGatewaySupport {
@Autowired

    private MyProperties myProperties;
    private static final Logger log = LoggerFactory.getLogger(Wsdl05Client.class);

    public WSSIREON005LISTADOREMESADORESCANALResponse getRemittersListByChannel(
        String canal
    ) {
        //Credentials
        com.soap.wsdl.service05.ServicesCredentials credentials = new ServicesCredentials();
        credentials.setServicesUser(myProperties.getServicesUser());
        credentials.setServicesPassword(myProperties.getServicesPassword());
        credentials.setServicesToken("");

        // Item Solicitud
        ServicesRequest005ItemSolicitud itemSolicitud = new ServicesRequest005ItemSolicitud();
        itemSolicitud.setCanal(canal);

        // Request
        ServicesRequest005 request005 = new ServicesRequest005();
        request005.setServicesCredentials(credentials);
        request005.setItemSolicitud(itemSolicitud);

        WSSIREON005LISTADOREMESADORESCANAL listadoremesadorescanal = new WSSIREON005LISTADOREMESADORESCANAL();
        listadoremesadorescanal.setServicesrequest005(request005);

        String url = myProperties.getAwssireon005();
        log.info(url);
        System.out.println(request005);
        SoapActionCallback callback = new SoapActionCallback(url);

        //Response
        WSSIREON005LISTADOREMESADORESCANALResponse response = (WSSIREON005LISTADOREMESADORESCANALResponse) getWebServiceTemplate().marshalSendAndReceive(
            url,
            listadoremesadorescanal,
            callback
        );

        return response;
    }
}
