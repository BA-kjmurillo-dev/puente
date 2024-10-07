package com.puente.web.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@ToString
@Configuration
@Getter
@Setter
@NoArgsConstructor
public class MyProperties {
    @Value("${ServicesUser}")
    private String ServicesUser;
    @Value("${ServicesPassword}")
    private String ServicesPassword;
    @Value("${MotivoRemesa}")
    private String MotivoRemesa;
    @Value("${Awssireon003}")
    private String Awssireon003;
    @Value("${Awssireon004}")
    private String Awssireon004;
    @Value("${Awssireon005}")
    private String Awssireon005;
    @Value("${Awssireon007}")
    private String Awssireon007;
    @Value("${CodigoRemesadora}")
    private String CodigoRemesadora;
    @Value("${NumeroVigo}")
    private String NumeroVigo;
    @Value("${NumeroUni}")
    private String NumeroUni;
    @Value("${NumeroMg}")
    private String NumeroMg;
    @Value("${NumeroBts}")
    private String NumeroBts;
    @Value("${NumeroRia}")
    private String NumeroRia;
    @Value("${NumeroMaxi}")
    private String NumeroMaxi;
    @Value("${ServicioSrvBasa010}")
    private String ServicioSrvBasa010;
    @Value("${ServicioSrvBasa002}")
    private String ServicioSrvBasa002;
    @Value("${ServicioSrvBasa003}")
    private String ServicioSrvBasa003;
    @Value("${codigoTransaccion}")
    private String codigoTransaccion;
    @Value("${codigoPais}")
    private String codigoPais;
    @Value("${codigoCoreBanking}")
    private String codigoCoreBanking;
    @Value("usuarioPeticion")
    private String usuarioPeticion;
    @Value("direccionIPPeticion")
    private String direccionIPPeticion;
    @Value("${CodigoBanco}")
    private String CodigoBanco;
    @Value("${codigoCanal}")
    private String codigoCanal;
    @Value("${CodigoPeticionUnica}")
    private String CodigoPeticionUnica;
    @Value("${jwtPassword}")
    private String jwtPassword;
    @Value("${ServicesUserJteller}")
    private String ServicesUserJteller;
    @Value("${ServicesPasswordJteller}")
    private String ServicesPasswordJteller;
    @Value("${ServicesUserOcb}")
    private String ServicesUserOcb;
    @Value("${ServicesPasswordOcb}")
    private String ServicesPasswordOcb;
    @Value("${ServicesUserAbas}")
    private String ServicesUserAbas;
    @Value("${ServicesPasswordAbas}")
    private String ServicesPasswordAbas;
    @Value("${ServicesUserDilo}")
    private String ServicesUserDilo;
    @Value("${ServicesPasswordDilo}")
    private String ServicesPasswordDilo;
    @Value("${ServicesUserAbi}")
    private String ServicesUserAbi;
    @Value("${ServicesPasswordAbi}")
    private String ServicesPasswordAbi;
}
