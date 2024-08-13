package com.puente.service.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wsdl07Dto extends ResponseDto {
    private Awsdl07Data data;
    @Setter
    @Getter
    public static class Awsdl07Data {
        private String codigoRemesadora;
        private String estadoRemesa;
        private String descriptorEstadoRemesa;
    }
}
