package com.puente.service;

import com.puente.client.SrvBasa010Client;
import com.soap.wsdl.ServicioSrvBasa010.EjecutarSrvBasa010Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SrvBasa010Service {

    @Autowired
    private SrvBasa010Client srvBasa010Client;

    public EjecutarSrvBasa010Response getInfoAgencia(String agencia){
        EjecutarSrvBasa010Response response = srvBasa010Client.getResponse010(agencia,"*");
        if (response == null) {
            return null;
        }
        return response;
    }
    public EjecutarSrvBasa010Response getInfoAgenciaSucursal(String agencia, String sucursal){
        EjecutarSrvBasa010Response response = srvBasa010Client.getResponse010(agencia,sucursal);
        if (response == null) {
            return null;
        }
        return response;
    }
}
