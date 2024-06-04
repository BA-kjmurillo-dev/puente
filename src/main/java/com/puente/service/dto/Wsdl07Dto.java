package com.puente.service.dto;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class Wsdl07Dto {
    private String code;
    private String message;
    private Awsdl07Data data;
    @Setter
    @Getter
    public static class Awsdl07Data {
        private String CodigoRemesadora;
        private String EstadoRemesa;
        private String DescriptorEstadoRemesa;
    }
}
