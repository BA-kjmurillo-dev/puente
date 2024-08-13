package com.puente.service;

import com.puente.client.SrvBasa002Client;
import com.soap.wsdl.ServicioSrvBasa002.EjecutarSrvBasa002Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class SrvBasa002Service {

    @Autowired
    private SrvBasa002Client srvBasa002Client;

    public EjecutarSrvBasa002Response getClienteInfoXId(long idNumber) {
        return srvBasa002Client.getResponse002(idNumber,"1");
    }

    public EjecutarSrvBasa002Response getClienteInfoXBp(long BpNumber) {
        return srvBasa002Client.getResponse002(BpNumber,"2");
    }
}
