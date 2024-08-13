package com.puente.client;

import com.puente.web.config.MyProperties;
import com.soap.wsdl.ServicioSrvBasa010.EjecutarSrvBasa010;
import com.soap.wsdl.ServicioSrvBasa010.EjecutarSrvBasa010Response;
import com.soap.wsdl.ServicioSrvBasa010.ParametroAdicional;
import com.soap.wsdl.ServicioSrvBasa010.PeticionSrvBasa010;
import jakarta.xml.bind.JAXBElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class SrvBasa010Client extends WebServiceGatewaySupport {
    @Autowired
    private MyProperties myProperties;

    private PeticionSrvBasa010 crearPeticionSrvBasa010(String agencia, String sucursal) {
        PeticionSrvBasa010 peticion = new PeticionSrvBasa010();
        peticion.setCodigoAgencia(agencia);
        peticion.setCodigoBanco(myProperties.getCodigoBanco());
        peticion.setCodigoCanal(myProperties.getCodigoCanal());
        peticion.setCodigoPais(myProperties.getCodigoPais());
        peticion.setCodigoTransaccion(myProperties.getCodigoTransaccion());
        peticion.setCodigoSucursal(sucursal);
        peticion.setCodigoCoreBanking(myProperties.getCodigoCoreBanking());
        peticion.setCodigoPeticionUnica(myProperties.getCodigoPeticionUnica());
        peticion.setDireccionIPPeticion(myProperties.getDireccionIPPeticion());
        peticion.setUsuarioPeticion(myProperties.getUsuarioPeticion());

        ParametroAdicional parametroAdicional = new ParametroAdicional();
        parametroAdicional.setNumeroLinea(0);
        parametroAdicional.setDescripcionValor("");
        parametroAdicional.setValor("");
        peticion.getParametroAdicionalColeccion().add(parametroAdicional);

        return peticion;
    }

    public EjecutarSrvBasa010Response getResponse010(String agencia, String sucursal) {
        String url = myProperties.getServicioSrvBasa010();
        PeticionSrvBasa010 peticionSrvBasa010 = crearPeticionSrvBasa010(agencia, sucursal);

        EjecutarSrvBasa010 ejecutarSrvBasa010 = new EjecutarSrvBasa010();
        ejecutarSrvBasa010.setPeticionSrvBasa010(peticionSrvBasa010);

        return enviarPeticion(ejecutarSrvBasa010, url);
    }

    private EjecutarSrvBasa010Response enviarPeticion(EjecutarSrvBasa010 ejecutarSrvBasa010, String url) {
        try {
            SoapActionCallback callback = new SoapActionCallback("");
            Object ejecutarSrvBasa010Response = getWebServiceTemplate().marshalSendAndReceive(
                    url,
                    ejecutarSrvBasa010,
                    callback
            );

            if (ejecutarSrvBasa010Response instanceof JAXBElement) {
                JAXBElement<?> jaxbElement = (JAXBElement<?>) ejecutarSrvBasa010Response;
                Object value = jaxbElement.getValue();
                if (value instanceof EjecutarSrvBasa010Response) {
                    return (EjecutarSrvBasa010Response) value;
                } else {
                    throw new SrvBasa010ClientException("El valor no es una instancia de EjecutarSrvBasa010Response");
                }
            } else {
                throw new SrvBasa010ClientException("response no es una instancia de JAXBElement");
            }
        } catch (ClassCastException e) {
            throw new SrvBasa010ClientException("Error al procesar la respuesta del servicio", e);
        } catch (Exception e) {
            throw new SrvBasa010ClientException("Error al enviar la petición al servicio SOAP", e);
        }
    }


    public static class SrvBasa010ClientException extends RuntimeException {
        public SrvBasa010ClientException(String message) {
            super(message);
        }

        public SrvBasa010ClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
