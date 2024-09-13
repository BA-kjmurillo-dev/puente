package com.puente.web.config;

import com.puente.client.WsdlBpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class WsdlBpConfig {
    @Bean
    public ClientInterceptor[] clientInterceptors() {
        return new ClientInterceptor[]{new SoapBpInterceptor()};
    }

    @Bean
    public Jaxb2Marshaller marshallerBp() {
        Jaxb2Marshaller marshallerbp = new Jaxb2Marshaller();
        // this package must match the package in the <generatePackage> specified in pom.xml
        marshallerbp.setContextPath("com.soap.wsdl.serviceBP");
        return marshallerbp;
    }

    @Bean
    public WsdlBpClient clientServiceBp(@Qualifier("marshallerBp")Jaxb2Marshaller marshallerBP) {
        WsdlBpClient client = new WsdlBpClient();
        //client.setDefaultUri("http://hndespombapp.adbancat.hn:50000/dir/wsdl?p=ic/6518edb3378632a48ae14890ca9b5a38");
        client.setMarshaller(marshallerBP);
        client.setUnmarshaller(marshallerBP);
        client.setInterceptors(clientInterceptors());
        return client;
    }
}
