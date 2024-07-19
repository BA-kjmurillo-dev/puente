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

    public EjecutarSrvBasa002Response getResponse002(long numeroCliente,String tipoBusqueda) {
        String url = myProperties.getServicioSrvBasa002();

        EjecutarSrvBasa002 srvBasa002Req = new EjecutarSrvBasa002();
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

        srvBasa002Req.setPeticionSrvBasa002(peticionSrvBasa002);

        SoapActionCallback callback = new SoapActionCallback("");
        Object responseBasa002 = getWebServiceTemplate().marshalSendAndReceive(
                url,
                srvBasa002Req,
                callback
        );
        EjecutarSrvBasa002Response basa002Response = null;

        if (responseBasa002 instanceof JAXBElement){
            JAXBElement<?> jaxbElement = (JAXBElement<?>) responseBasa002;
            Object value = jaxbElement.getValue();
            if (value instanceof EjecutarSrvBasa002Response){
                basa002Response = (EjecutarSrvBasa002Response) value;
            }else{
                throw new ClassCastException("El valor no es una instancia de EjecutarSrvBasa002Response");
            }
        }else{
            throw new ClassCastException("response no es una instancia de JAXBElement");
        }

        return basa002Response;
    }

}
