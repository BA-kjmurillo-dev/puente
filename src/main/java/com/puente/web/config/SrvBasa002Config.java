package com.puente.web.config;

import com.puente.client.SrvBasa002Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class SrvBasa002Config {
    @Bean
    public ClientInterceptor[] clientInterceptors() {
        return new ClientInterceptor[]{new SoapBpInterceptor()};
    }

    @Bean
    public Jaxb2Marshaller marshallerBasa002() {
        Jaxb2Marshaller marshallerBasa002 = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in pom.xml
        marshallerBasa002.setContextPath("com.soap.wsdl.ServicioSrvBasa002");
        return marshallerBasa002;
    }

    @Bean
    public SrvBasa002Client clientServiceBasa002(Jaxb2Marshaller marshallerBasa002) {
        SrvBasa002Client Basa002Client = new SrvBasa002Client();
        //Basa000Client.setDefaultUri("http://10.128.254.75:30002/SRVBASA002-1.0/ServicioSrvBasa002Interfaz?wsdl");
        Basa002Client.setMarshaller(marshallerBasa002);
        Basa002Client.setUnmarshaller(marshallerBasa002);
        Basa002Client.setInterceptors(clientInterceptors()); //Imprime en consola el request a enviarse
        return Basa002Client;
    }
}
