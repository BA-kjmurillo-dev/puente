package com.puente.service;

import com.puente.client.WsdlBpClient;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WsdlBpService {
    @Autowired
    private WsdlBpClient wsdlBpClient;

    public DTCreaBusinessPartnerResp getBpInfo(String idNumber) {
        return wsdlBpClient.getResponse(idNumber);
    }
}
