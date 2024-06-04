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
    public Wsdl04Dto getMessage (){
        WSSIREON004ECHOResponse response = this.wsdl04Client.getMessage();
        String code = response.getServicesresponse004().getServicesResponse().getMessageCode();
        String message = response.getServicesresponse004().getServicesResponse().getMessage();
        Wsdl04Dto service04 = new Wsdl04Dto();
        service04.setMessageCode(code);
        service04.setMessage(message);
        return service04;
    }
}
