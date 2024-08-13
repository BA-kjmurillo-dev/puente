package com.puente.service;

import com.puente.client.SrvBasa003Client;
import com.soap.wsdl.ServicioSrvBasa003.EjecutarSrvBasa003Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SrvBasa003Service {

    @Autowired
    private SrvBasa003Client srvBasa003Client;

    public EjecutarSrvBasa003Response getInfoCuenta(String cuenta) {
        return srvBasa003Client.getResponse003(cuenta);
    }
}
