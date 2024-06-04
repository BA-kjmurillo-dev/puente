package com.puente.web.config;

import com.puente.service.Wsdl03Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class Wsdl03Config {
    @Bean
    public Jaxb2Marshaller marshaller03() {
        Jaxb2Marshaller marshaller03 = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller03.setContextPath("com.soap.wsdl.service03");
        return marshaller03;
    }

    @Bean
    public Wsdl03Service clientService03(Jaxb2Marshaller marshaller03) {
        Wsdl03Service client03 = new Wsdl03Service();
        client03.setDefaultUri("http://10.128.248.118/SIREONGFA/awssireon003.aspx?wsdl");
        client03.setMarshaller(marshaller03);
        client03.setUnmarshaller(marshaller03);
        return client03;
    }
}
