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

    public EjecutarSrvBasa010Response getResponse010(String agencia,String sucursal) {
        String url = myProperties.getServicioSrvBasa010();

        PeticionSrvBasa010 peticionSrvBasa010 = new PeticionSrvBasa010();
        peticionSrvBasa010.setCodigoAgencia(agencia);
        peticionSrvBasa010.setCodigoBanco(myProperties.getCodigoBanco());
        peticionSrvBasa010.setCodigoCanal(myProperties.getCodigoCanal());
        peticionSrvBasa010.setCodigoPais(myProperties.getCodigoPais());
        peticionSrvBasa010.setCodigoTransaccion(myProperties.getCodigoTransaccion());
        peticionSrvBasa010.setCodigoSucursal(sucursal);
        peticionSrvBasa010.setCodigoCoreBanking(myProperties.getCodigoCoreBanking());
        peticionSrvBasa010.setCodigoPeticionUnica(myProperties.getCodigoPeticionUnica());
        peticionSrvBasa010.setDireccionIPPeticion(myProperties.getDireccionIPPeticion());
        peticionSrvBasa010.setUsuarioPeticion(myProperties.getUsuarioPeticion());

        ParametroAdicional parametroAdicional = new ParametroAdicional();
        parametroAdicional.setNumeroLinea(0);
        parametroAdicional.setDescripcionValor("");
        parametroAdicional.setValor("");

        peticionSrvBasa010.getParametroAdicionalColeccion().add(parametroAdicional);

        EjecutarSrvBasa010 ejecutarSrvBasa010 = new EjecutarSrvBasa010();
        ejecutarSrvBasa010.setPeticionSrvBasa010(peticionSrvBasa010);

        SoapActionCallback callback = new SoapActionCallback("");
        Object ejecutarSrvBasa010Response = getWebServiceTemplate().marshalSendAndReceive(
                url,
                ejecutarSrvBasa010,
                callback
        );
        EjecutarSrvBasa010Response basa010Response = null;

        if (ejecutarSrvBasa010Response instanceof JAXBElement){
            JAXBElement<?> jaxbElement = (JAXBElement<?>) ejecutarSrvBasa010Response;
            Object value = jaxbElement.getValue();
            if (value instanceof EjecutarSrvBasa010Response){
                basa010Response = (EjecutarSrvBasa010Response) value;
            }else{
                throw new ClassCastException("El valor no es una instancia de EjecutarSrvBasa010Response");
            }
        }else{
            throw new ClassCastException("response no es una instancia de JAXBElement");
        }
        return basa010Response;
    }
}
