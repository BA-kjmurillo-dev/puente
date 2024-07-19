package com.puente.service;

import com.puente.client.SrvBasa002Client;
import com.soap.wsdl.ServicioSrvBasa002.EjecutarSrvBasa002Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SrvBasa002Service {

    @Autowired
    private SrvBasa002Client srvBasa002Client;

    public EjecutarSrvBasa002Response getClienteInfoXId(long idNumber) {
        EjecutarSrvBasa002Response ejecutarSrvBasa020Response = srvBasa002Client.getResponse002(idNumber,"1"); //ejecutarSrvBasa020Client
        return ejecutarSrvBasa020Response;
    }

    public EjecutarSrvBasa002Response getClienteInfoXBp(long BpNumber) {
        EjecutarSrvBasa002Response ejecutarSrvBasa020Response = srvBasa002Client.getResponse002(BpNumber,"2"); //ejecutarSrvBasa020Client
        return ejecutarSrvBasa020Response;
    }
}
