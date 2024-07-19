package com.puente.web.config;

import com.puente.client.SrvBasa003Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class SrvBasa003Config {
    @Bean
    public ClientInterceptor[] clientInterceptors() {
        return new ClientInterceptor[]{new SoapBpInterceptor()};
    }

    @Bean
    public Jaxb2Marshaller marshallerBasa003() {
        Jaxb2Marshaller marshallerBasa003 = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in pom.xml
        marshallerBasa003.setContextPath("com.soap.wsdl.ServicioSrvBasa003");
        return marshallerBasa003;
    }

    @Bean
    public SrvBasa003Client clientServiceBasa003(Jaxb2Marshaller marshallerBasa003) {
        SrvBasa003Client Basa003Client = new SrvBasa003Client();
        //Basa000Client.setDefaultUri("http://srvbasa.bancatlan.hn:30003/SRVBASA003-1.0/ServicioSrvBasa003Interfaz?wsdl");
        Basa003Client.setMarshaller(marshallerBasa003);
        Basa003Client.setUnmarshaller(marshallerBasa003);
        Basa003Client.setInterceptors(clientInterceptors()); //Imprime en consola el request a enviarse
        return Basa003Client;
    }
}
