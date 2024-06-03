package com.puente.web.config;

import com.puente.client.ClientService04;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ConfigService04 {
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath("com.soap.wsdl.service04");
        return marshaller;
    }

    @Bean
    public ClientService04 clientService04(Jaxb2Marshaller marshaller) {
        ClientService04 client = new ClientService04();
        client.setDefaultUri("http://10.128.248.118/SIREONGFA/awssireon004.aspx?wsdl");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
