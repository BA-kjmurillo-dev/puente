package com.puente.service;

import com.soap.wsdl.service07.*;
import com.puente.client.Wsdl07Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

@Service
public class Wsdl07Service extends WebServiceGatewaySupport {
    private Wsdl07Client wsdl07Client;

    @Autowired
    public Wsdl07Service(Wsdl07Client wsdl07Client) {
        this.wsdl07Client = wsdl07Client;
    }

    public ServicesResponse007 getRemittanceByIdentifier(
        ServicesRequest007ItemSolicitud itemSolicitudRequest
    ) {
        WSSIREON007CONSULTAPORIDENTIFICADORResponse response = this.wsdl07Client.getRemittanceByIdentifier(itemSolicitudRequest);
        return response.getServicesresponse007();
    }
}
