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
            Transformer transformer = createSecureTransformer();
            transformer.transform(soapMessage.getPayloadSource(), new StreamResult(writer));
        } catch (IOException | TransformerException e) {
            throw new SoapBpInterceptorException("Error processing SOAP response", e);
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
            Transformer transformer = createSecureTransformer();
            transformer.transform(soapMessage.getPayloadSource(), new StreamResult(writer));
        } catch (IOException | TransformerException e) {
            throw new SoapBpInterceptorException("Error processing SOAP response", e);
        }
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    // Implementar o eliminar si no es necesario
    @Override
    public void afterCompletion(MessageContext messageContext, Exception e) throws WebServiceClientException {
    // Implementar o eliminar si no es necesario
    }

    public static class SoapBpInterceptorException extends RuntimeException {
        public SoapBpInterceptorException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    private Transformer createSecureTransformer() throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(javax.xml.XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        return factory.newTransformer();
    }
}
