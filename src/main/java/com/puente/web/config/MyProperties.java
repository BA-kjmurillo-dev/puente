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
    @Value("${CodigoBanco}")
    private String CodigoBanco;
    @Value("${IdentificadorRemesa}")
    private String IdentificadorRemesa;
    @Value("${TipoFormaPago}")
    private String TipoFormaPago;
    @Value("${MotivoRemesa}")
    private String MotivoRemesa;
    @Value("${Awssireon004}")
    private String Awssireon004;
    @Value("${Awssireon003}")
    private String Awssireon003;
    @Value("${CodigoRemesadora}")
    private String CodigoRemesadora;
}
