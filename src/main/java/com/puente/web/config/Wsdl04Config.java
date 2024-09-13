package com.puente.web.config;

import com.puente.client.Wsdl04Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Wsdl04Config {
    @Autowired
    private MyProperties myProperties;

    @Bean
    public Jaxb2Marshaller marshaller04() {
        Jaxb2Marshaller marshaller04 = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in pom.xml
        marshaller04.setContextPath("com.soap.wsdl.service04");
        return marshaller04;
    }

    @Bean
    public Wsdl04Client clientService04(Jaxb2Marshaller marshaller04) {
        Wsdl04Client client04 = new Wsdl04Client();
        client04.setDefaultUri(this.myProperties.getAwssireon004());
        client04.setMarshaller(marshaller04);
        client04.setUnmarshaller(marshaller04);
        return client04;
    }
}
