package com.puente.service;

import com.soap.wsdl.service04.*;
import com.puente.client.Wsdl04Client;
import com.puente.service.dto.Wsdl04Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Wsdl04Service {
    private final Wsdl04Client wsdl04Client;

    @Autowired
    public Wsdl04Service(Wsdl04Client wsdl04Client) {
        this.wsdl04Client = wsdl04Client;
    }
    public Wsdl04Dto testSireonConection(String canal) {
        WSSIREON004ECHOResponse wsdl04Response = this.wsdl04Client.testSireonConection(canal);
        ServicesResponse servicesResponse = wsdl04Response.getServicesresponse004().getServicesResponse();
        Wsdl04Dto response = new Wsdl04Dto();
        response.setMessage(servicesResponse.getMessage());
        response.setCode(servicesResponse.getMessageCode());
        return response;
    }

    public Boolean isSireonActive(Wsdl04Dto servicesResponse) {
        return servicesResponse.getCode().equals("000000");
    }
}
