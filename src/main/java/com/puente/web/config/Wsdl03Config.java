package com.puente.web.config;

import com.puente.client.Wsdl03Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class Wsdl03Config {
    @Autowired
    private MyProperties myProperties;
    @Bean
    public ClientInterceptor[] clientInterceptors() {
        return new ClientInterceptor[]{new SoapBpInterceptor()};
    }
    @Bean
    public Jaxb2Marshaller marshaller03() {
        Jaxb2Marshaller marshaller03 = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in pom.xml
        marshaller03.setContextPath("com.soap.wsdl.service03");
        return marshaller03;
    }

    @Bean
    public Wsdl03Client clientService03(Jaxb2Marshaller marshaller03) {
        Wsdl03Client client03 = new Wsdl03Client();
        client03.setDefaultUri(this.myProperties.getAwssireon003());
        client03.setMarshaller(marshaller03);
        client03.setUnmarshaller(marshaller03);
        client03.setInterceptors(clientInterceptors()); //Imprime en consola el request a enviarse
        return client03;
    }
}
