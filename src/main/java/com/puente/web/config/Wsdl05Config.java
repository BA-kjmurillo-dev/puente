package com.puente.web.config;


import com.puente.client.Wsdl05Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Wsdl05Config {
    @Autowired
    private MyProperties myProperties;
    @Bean
    public Jaxb2Marshaller marshaller05(){
     Jaxb2Marshaller marshaller05 = new Jaxb2Marshaller();
     marshaller05.setContextPath("com.soap.wsdl.service05");
     return marshaller05;
    }

    @Bean
    public Wsdl05Client clientService05(Jaxb2Marshaller marshaller05) {
        Wsdl05Client client05 = new Wsdl05Client();
        client05.setDefaultUri(this.myProperties.getAwssireon005());
        client05.setMarshaller(marshaller05);
        client05.setUnmarshaller(marshaller05);
        return client05;
    }

}
