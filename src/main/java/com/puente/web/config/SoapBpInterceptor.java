package com.puente.web.config;

import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

public class SoapBpInterceptor implements ClientInterceptor {
    /**
     * Descripcion: Metodo que se utiliza para visualizar el request antes de ser enviado.
     * @return Boolean
     * */
    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        SaajSoapMessage soapMessage = (SaajSoapMessage) messageContext.getRequest();
        try (StringWriter writer = new StringWriter()) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(soapMessage.getPayloadSource(), new StreamResult(writer));
            System.out.println("SOAP Request: " + writer.toString());
        } catch (IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Descripcion: Metodo que se utiliza para visualizar el response.
     * @return Boolean
     * */
    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        SaajSoapMessage soapMessage = (SaajSoapMessage) messageContext.getResponse();
        try (StringWriter writer = new StringWriter()) {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(soapMessage.getPayloadSource(), new StreamResult(writer));
            System.out.println("SOAP Response: " + writer.toString());
        } catch (IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception e) throws WebServiceClientException {

    }
}
