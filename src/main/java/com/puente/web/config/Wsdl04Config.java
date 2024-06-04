package com.puente.web.config;

import com.puente.service.Wsdl04Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Wsdl04Config {
    @Bean
    public Jaxb2Marshaller marshaller04() {
        Jaxb2Marshaller marshaller04 = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller04.setContextPath("com.soap.wsdl.service04");
        return marshaller04;
    }

    @Bean
    public Wsdl04Service clientService04(Jaxb2Marshaller marshaller04) {
        Wsdl04Service client04 = new Wsdl04Service();
        client04.setDefaultUri("http://10.128.248.118/SIREONGFA/awssireon004.aspx?wsdl");
        client04.setMarshaller(marshaller04);
        client04.setUnmarshaller(marshaller04);
        return client04;
    }
}
