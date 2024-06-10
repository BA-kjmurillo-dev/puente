package com.puente.web.config;


import com.puente.client.Wsdl05Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Wsdl05Config {
    @Bean
    public Jaxb2Marshaller marshaller05(){
     Jaxb2Marshaller marshaller05 = new Jaxb2Marshaller();
     marshaller05.setContextPath("com.soap.wsdl.service05");
     return marshaller05;
    }

    @Bean
    public Wsdl05Client clientService05(Jaxb2Marshaller marshaller05) {
        Wsdl05Client client05 = new Wsdl05Client();
        client05.setDefaultUri("http://10.128.248.118/SIREONGFA/awssireon005.aspx?wsdl");
        client05.setMarshaller(marshaller05);
        client05.setUnmarshaller(marshaller05);
        return client05;
    }

}
