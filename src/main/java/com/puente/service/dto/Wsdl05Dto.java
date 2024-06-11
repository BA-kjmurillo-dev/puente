package com.puente.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wsdl05Dto extends ResponseDto {
    private List<Awsdl05Data> data;

    @Setter
    @Getter
    public static class Awsdl05Data  {
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
