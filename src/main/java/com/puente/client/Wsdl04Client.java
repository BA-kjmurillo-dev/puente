package com.puente.client;

import com.puente.web.config.MyProperties;
import com.soap.wsdl.service04.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class Wsdl04Client extends WebServiceGatewaySupport {
    @Autowired
    private MyProperties myProperties;
    private static final Logger log = LoggerFactory.getLogger(Wsdl04Client.class);

    public WSSIREON004ECHOResponse getMessage (){
        //Credentials
        com.soap.wsdl.service04.ServicesCredentials credentials = new ServicesCredentials();
        credentials.setServicesUser(myProperties.getServicesUser());
        credentials.setServicesPassword(myProperties.getServicesPassword());
        credentials.setServicesToken("");

        //Item echo
        ServicesRequest004ItemEcho itemEcho = new ServicesRequest004ItemEcho();
        itemEcho.setCanal("0002");
        itemEcho.setTexto("Echoooo");

        //Request
        ServicesRequest004 request004 = new ServicesRequest004();
        request004.setServicesCredentials(credentials);
        request004.setItemEcho(itemEcho);

        WSSIREON004ECHO echo = new WSSIREON004ECHO();
        echo.setServicesrequest004(request004);

        String url = myProperties.getAwssireon004();
        log.info(url);
        System.out.println(request004);
        SoapActionCallback callback = new SoapActionCallback(url);
        //Response
        WSSIREON004ECHOResponse response = (WSSIREON004ECHOResponse) getWebServiceTemplate().marshalSendAndReceive(
            url,
            echo,
            callback
        );
        return response;
    }
}
