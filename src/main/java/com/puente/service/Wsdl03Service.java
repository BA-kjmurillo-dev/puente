package com.puente.service;

import com.soap.wsdl.service03.*;
import com.puente.client.Wsdl03Client;
import com.puente.service.dto.Wsdl03Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Wsdl03Service {
    private final Wsdl03Client wsdl03Client;

    @Autowired
    public Wsdl03Service(Wsdl03Client wsdl03Client) {
        this.wsdl03Client = wsdl03Client;
    }

    public Wsdl03Dto getMessage (){
        WSSIREON003SERVICIOVENTANILLAResponse wsdl03Response = this.wsdl03Client.getMessage();
        ServicesResponse servicesResponse = wsdl03Response.getServicesresponse003().getVentanilla().getServicesResponse();
        SDTServicioVentanillaOutItemRemesa itemRemesa = wsdl03Response.getServicesresponse003().getVentanilla().getItemRemesa();
        Wsdl03Dto response = new Wsdl03Dto();
        response.setMessage(servicesResponse.getMessage());
        response.setMessageCode(servicesResponse.getMessageCode());
        response.setData(itemRemesa);
        return response;
    }
}
