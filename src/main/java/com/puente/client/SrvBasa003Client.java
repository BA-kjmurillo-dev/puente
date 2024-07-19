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

    public EjecutarSrvBasa003Response getResponse003(String cuenta) {
        String url = myProperties.getServicioSrvBasa003();

        EjecutarSrvBasa003 srvBasa003Req = new EjecutarSrvBasa003();
        PeticionSrvBasa003 peticionSrvBasa003 = new PeticionSrvBasa003();

        peticionSrvBasa003.setCodigoPeticionUnica(myProperties.getCodigoPeticionUnica());
        peticionSrvBasa003.setCodigoTransaccion(myProperties.getCodigoTransaccion());
        peticionSrvBasa003.setCodigoCanal(myProperties.getCodigoCanal());
        peticionSrvBasa003.setUsuarioPeticion(""); //usuarioPeticion
        peticionSrvBasa003.setDireccionIPPeticion("");
        peticionSrvBasa003.setCodigoPais(myProperties.getCodigoPais());
        peticionSrvBasa003.setCodigoBanco(myProperties.getCodigoBanco());
        peticionSrvBasa003.setCodigoCoreBanking(myProperties.getCodigoCoreBanking());
        peticionSrvBasa003.setTipoBusqueda("2");//busqueda por numero de cuenta
        peticionSrvBasa003.setValorBusqueda(cuenta);

        ParametroAdicional parametroAdicional = new ParametroAdicional();
        parametroAdicional.setNumeroLinea(0);
        parametroAdicional.setDescripcionValor("");
        parametroAdicional.setValor("");
        peticionSrvBasa003.getParametroAdicionalColeccion().add(parametroAdicional);

        srvBasa003Req.setPeticionSrvBasa003(peticionSrvBasa003);

        SoapActionCallback callback = new SoapActionCallback("");
        Object responseBasa003 = getWebServiceTemplate().marshalSendAndReceive(
                url,
                srvBasa003Req,
                callback
        );
        EjecutarSrvBasa003Response basa003Response = null;
        if (responseBasa003 instanceof JAXBElement){
            JAXBElement<?> jaxbElement = (JAXBElement<?>) responseBasa003;
            Object value = jaxbElement.getValue();
            if (value instanceof EjecutarSrvBasa003Response){
                basa003Response = (EjecutarSrvBasa003Response) value;
            }else{
                throw new ClassCastException("El valor no es una instancia de EjecutarSrvBasa003Response");
            }
        }else{
            throw new ClassCastException("response no es una instancia de JAXBElement");
        }

        return basa003Response;
    }

}
