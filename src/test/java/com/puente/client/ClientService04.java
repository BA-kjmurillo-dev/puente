package com.puente.client;

import com.puente.model.RespService04;
import com.soap.wsdl.service04.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class ClientService04 extends WebServiceGatewaySupport {
//    public RespService04 respService04(){
//        //Credenciales
//        ServicesCredentials credentials = new ServicesCredentials();
//        credentials.setServicesUser("SIREON_TELLER");
//        credentials.setServicesPassword("JTEgi4sCvTg1H67");
//        credentials.setServicesToken("");
//
//        //Item echo
//        ServicesRequest004ItemEcho itemEcho = new ServicesRequest004ItemEcho();
//        itemEcho.setCanal("0002");
//        itemEcho.setTexto("Echoooo");
//
//        //Request
//        ServicesRequest004 request004 = new ServicesRequest004();
//        request004.setServicesCredentials(credentials);
//        request004.setItemEcho(itemEcho);
//
//        WSSIREON004ECHO echo = new WSSIREON004ECHO();
//        echo.setServicesrequest004(request004);
//
//        SoapActionCallback callback = new SoapActionCallback("SIREONGFA");
//        WSSIREON004ECHOResponse response = (WSSIREON004ECHOResponse) getWebServiceTemplate().marshalSendAndReceive("http://10.128.248.118/SIREONGFA/awssireon004.aspx?wsdl", echo, new SoapActionCallback(
//                "http://10.128.248.118/SIREONGFA/awssireon004.aspx?wsdl"));
//
//        String code = response.getServicesresponse004().getServicesResponse().getMessageCode();
//        String message = response.getServicesresponse004().getServicesResponse().getMessage();
//
//        RespService04 respService04 = new RespService04();
//        respService04.setMessageCode(code);
//        respService04.setMessage(message);
//
//        return respService04;
//    }
}
