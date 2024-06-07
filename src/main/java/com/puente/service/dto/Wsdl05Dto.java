package com.puente.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class Wsdl05Dto {
    private String messageCode;
    private String message;
    private List<Awsdl05Data> data;

    @Data
    public static class Awsdl05Data {
        private String codigoRemesador;
        private String descriptorRemesador;
        private String tipoRemesador;

        public Awsdl05Data(String codigoRemesador, String descriptorRemesador, String tipoRemesador) {
            this.codigoRemesador = codigoRemesador;
            this.descriptorRemesador = descriptorRemesador;
            this.tipoRemesador = tipoRemesador;
        }
    }
}
