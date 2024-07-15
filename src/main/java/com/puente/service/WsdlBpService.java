package com.puente.service;

import com.puente.client.WsdlBpClient;
import com.puente.web.controller.ConsultaController;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WsdlBpService {
    @Autowired
    private WsdlBpClient wsdlBpClient;
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(WsdlBpService.class);

    @Cacheable(value = "miCache", key = "#idNumber")
    public DTCreaBusinessPartnerResp getBpInfo(String idNumber) {
        return getBpInfo2(idNumber);
    }

    public DTCreaBusinessPartnerResp getBpInfo2(String idNumber) {
        return wsdlBpClient.getResponse(idNumber);
    }
}
