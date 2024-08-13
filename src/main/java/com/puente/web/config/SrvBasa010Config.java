package com.puente.web.config;


import com.puente.client.SrvBasa010Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class SrvBasa010Config {
    @Bean
    public ClientInterceptor[] clientInterceptors() {
        return new ClientInterceptor[]{new SoapBpInterceptor()};
    }
    @Bean
    public Jaxb2Marshaller marshallerBasa010() {
        Jaxb2Marshaller marshallerBasa010 = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in pom.xml
        marshallerBasa010.setContextPath("com.soap.wsdl.ServicioSrvBasa010");
        return marshallerBasa010;
    }

    @Bean
    public SrvBasa010Client clientServiceBasa010(Jaxb2Marshaller marshallerBasa010) {
        SrvBasa010Client basa010Client = new SrvBasa010Client();
        basa010Client.setMarshaller(marshallerBasa010);
        basa010Client.setUnmarshaller(marshallerBasa010);
        basa010Client.setInterceptors(clientInterceptors()); //Imprime en consola el request a enviarse
        return basa010Client;
    }
}
