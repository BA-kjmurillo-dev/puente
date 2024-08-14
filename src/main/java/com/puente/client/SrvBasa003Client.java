package com.puente.client;

import com.puente.web.config.MyProperties;
import com.soap.wsdl.ServicioSrvBasa003.ParametroAdicional;
import com.soap.wsdl.ServicioSrvBasa003.EjecutarSrvBasa003;
import com.soap.wsdl.ServicioSrvBasa003.EjecutarSrvBasa003Response;
import com.soap.wsdl.ServicioSrvBasa003.PeticionSrvBasa003;
import jakarta.xml.bind.JAXBElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class SrvBasa003Client extends WebServiceGatewaySupport {
    @Autowired
    private MyProperties myProperties;

    private PeticionSrvBasa003 crearPeticionSrvBasa003(String cuenta) {
        PeticionSrvBasa003 peticion = new PeticionSrvBasa003();

        peticion.setCodigoPeticionUnica(myProperties.getCodigoPeticionUnica());
        peticion.setCodigoTransaccion(myProperties.getCodigoTransaccion());
        peticion.setCodigoCanal(myProperties.getCodigoCanal());
        peticion.setUsuarioPeticion(""); // UsuarioPeticion
        peticion.setDireccionIPPeticion("");
        peticion.setCodigoPais(myProperties.getCodigoPais());
        peticion.setCodigoBanco(myProperties.getCodigoBanco());
        peticion.setCodigoCoreBanking(myProperties.getCodigoCoreBanking());
        peticion.setTipoBusqueda("2"); // Busqueda por número de cuenta
        peticion.setValorBusqueda(cuenta);

        ParametroAdicional parametroAdicional = new ParametroAdicional();
        parametroAdicional.setNumeroLinea(0);
        parametroAdicional.setDescripcionValor("");
        parametroAdicional.setValor("");
        peticion.getParametroAdicionalColeccion().add(parametroAdicional);

        return peticion;
    }

    public EjecutarSrvBasa003Response getResponse003(String cuenta) {
        String url = myProperties.getServicioSrvBasa003();
        PeticionSrvBasa003 peticionSrvBasa003 = crearPeticionSrvBasa003(cuenta);

        EjecutarSrvBasa003 srvBasa003Req = new EjecutarSrvBasa003();
        srvBasa003Req.setPeticionSrvBasa003(peticionSrvBasa003);

        return enviarPeticion(srvBasa003Req, url);
    }

    private EjecutarSrvBasa003Response enviarPeticion(EjecutarSrvBasa003 srvBasa003Req, String url) {
        try {
            SoapActionCallback callback = new SoapActionCallback("");
            Object responseBasa003 = getWebServiceTemplate().marshalSendAndReceive(
                    url,
                    srvBasa003Req,
                    callback
            );

            if (responseBasa003 instanceof JAXBElement) {
                JAXBElement<?> jaxbElement = (JAXBElement<?>) responseBasa003;
                Object value = jaxbElement.getValue();
                if (value instanceof EjecutarSrvBasa003Response) {
                    return (EjecutarSrvBasa003Response) value;
                } else {
                    throw new SrvBasa003ClientException("El valor no es una instancia de EjecutarSrvBasa003Response");
                }
            } else {
                throw new SrvBasa003ClientException("Response no es una instancia de JAXBElement");
            }
        } catch (ClassCastException e) {
            throw new SrvBasa003ClientException("Error al procesar la respuesta del servicio", e);
        } catch (Exception e) {
            throw new SrvBasa003ClientException("Error al enviar la petición al servicio SOAP", e);
        }
    }
    public static class SrvBasa003ClientException extends RuntimeException {
        public SrvBasa003ClientException(String message) {
            super(message);
        }

        public SrvBasa003ClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}
