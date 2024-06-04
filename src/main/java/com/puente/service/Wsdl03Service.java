package com.puente.service;

import com.soap.wsdl.service03.*;
import com.puente.client.Wsdl03Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Wsdl03Service {
    private Wsdl03Client wsdl03Client;

    @Autowired
    public Wsdl03Service(Wsdl03Client wsdl03Client) {
        this.wsdl03Client = wsdl03Client;
    }

    public String getMessage (){
        WSSIREON003SERVICIOVENTANILLAResponse response = this.wsdl03Client.getMessage();
        String code = response.getServicesresponse003().getVentanilla().getServicesResponse().getMessageCode();
        String message = response.getServicesresponse003().getVentanilla().getServicesResponse().getMessage();
        return code +" "+message;
    }
}
