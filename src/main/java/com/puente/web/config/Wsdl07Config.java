package com.puente.web.config;

import com.puente.client.Wsdl07Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Wsdl07Config {
    @Autowired
    private MyProperties myProperties;
    @Bean
    public Jaxb2Marshaller marshaller07() {
        Jaxb2Marshaller marshaller07 = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in pom.xml
        marshaller07.setContextPath("com.soap.wsdl.service07");
        return marshaller07;
    }

    @Bean
    public Wsdl07Client clientService07(Jaxb2Marshaller marshaller07) {
        Wsdl07Client client07 = new Wsdl07Client();
        client07.setDefaultUri(this.myProperties.getAwssireon007());
        client07.setMarshaller(marshaller07);
        client07.setUnmarshaller(marshaller07);
        return client07;
    }
}
