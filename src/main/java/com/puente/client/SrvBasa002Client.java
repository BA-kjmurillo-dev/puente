package com.puente.client;

import com.puente.web.config.MyProperties;
import com.soap.wsdl.ServicioSrvBasa002.EjecutarSrvBasa002;
import com.soap.wsdl.ServicioSrvBasa002.EjecutarSrvBasa002Response;
import com.soap.wsdl.ServicioSrvBasa002.PeticionSrvBasa002;
import com.soap.wsdl.ServicioSrvBasa002.ParametroAdicional;
import jakarta.xml.bind.JAXBElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class SrvBasa002Client extends WebServiceGatewaySupport {
    @Autowired
    private MyProperties myProperties;

    private PeticionSrvBasa002 crearPeticionSrvBasa002(long numeroCliente, String tipoBusqueda) {
        PeticionSrvBasa002 peticionSrvBasa002 = new PeticionSrvBasa002();
        peticionSrvBasa002.setCodigoPeticionUnica(myProperties.getCodigoPeticionUnica());
        peticionSrvBasa002.setCodigoTransaccion(myProperties.getCodigoTransaccion());
        peticionSrvBasa002.setCodigoCanal(myProperties.getCodigoCanal());
        peticionSrvBasa002.setUsuarioPeticion(""); //usuarioPeticion
        peticionSrvBasa002.setDireccionIPPeticion("");
        peticionSrvBasa002.setCodigoPais(myProperties.getCodigoPais());
        peticionSrvBasa002.setCodigoBanco(myProperties.getCodigoBanco());
        peticionSrvBasa002.setCodigoCoreBanking(myProperties.getCodigoCoreBanking());
        peticionSrvBasa002.setNumeroCliente(numeroCliente);
        peticionSrvBasa002.setTipoBusqueda(tipoBusqueda);

        ParametroAdicional parametroAdicional = new ParametroAdicional();
        parametroAdicional.setNumeroLinea(0);
        parametroAdicional.setDescripcionValor("");
        parametroAdicional.setValor("");
        peticionSrvBasa002.getParametroAdicionalColeccion().add(parametroAdicional);

        return peticionSrvBasa002;
    }

    private EjecutarSrvBasa002Response enviarPeticion(EjecutarSrvBasa002 srvBasa002Req, String url) {
        SoapActionCallback callback = new SoapActionCallback("");
        Object responseBasa002 = getWebServiceTemplate().marshalSendAndReceive(
                url,
                srvBasa002Req,
                callback
        );

        if (responseBasa002 instanceof JAXBElement) {
            JAXBElement<?> jaxbElement = (JAXBElement<?>) responseBasa002;
            Object value = jaxbElement.getValue();
            if (value instanceof EjecutarSrvBasa002Response) {
                return (EjecutarSrvBasa002Response) value;
            } else {
                throw new ClassCastException("El valor no es una instancia de EjecutarSrvBasa002Response");
            }
        } else {
            throw new ClassCastException("response no es una instancia de JAXBElement");
        }
    }

    public EjecutarSrvBasa002Response getResponse002(long numeroCliente, String tipoBusqueda) {
        String url = myProperties.getServicioSrvBasa002();
        PeticionSrvBasa002 peticionSrvBasa002 = crearPeticionSrvBasa002(numeroCliente, tipoBusqueda);
        EjecutarSrvBasa002 srvBasa002Req = new EjecutarSrvBasa002();
        srvBasa002Req.setPeticionSrvBasa002(peticionSrvBasa002);

        return enviarPeticion(srvBasa002Req, url);
    }


}
