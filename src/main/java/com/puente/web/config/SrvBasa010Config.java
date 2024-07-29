package com.puente.web.config;


import com.puente.client.SrvBasa010Client;
import com.puente.client.Wsdl03Client;
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
        SrvBasa010Client Basa010Client = new SrvBasa010Client();
        //Basa010Client.setDefaultUri("http://10.128.254.75:30010/SRVBASA010-1.0/ServicioSrvBasa010Interfaz?wsdl");
        Basa010Client.setMarshaller(marshallerBasa010);
        Basa010Client.setUnmarshaller(marshallerBasa010);
        Basa010Client.setInterceptors(clientInterceptors()); //Imprime en consola el request a enviarse
        return Basa010Client;
    }
}
