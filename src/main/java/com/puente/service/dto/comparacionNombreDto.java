package com.puente.service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class comparacionNombreDto {

    private beneficiarioBp beneficiarioBp;
    private beneficiarioSireon beneficiarioSireon;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class beneficiarioBp {
        private String primerNombreBp;
        private String segundoNombreBp;
        private String primerApellidoBp;
        private String segundoApellidoBp;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class beneficiarioSireon {
        private String primerNombreSireon;
        private String segundoNombreSireon;
        private String primerApellidoSireon;
        private String segundoApellidoSireon;
    }
}
