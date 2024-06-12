package com.puente.service.dto;

import com.soap.wsdl.service03.SDTServicioVentanillaOutItemRemesa;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestGetRemittanceDataDto {
    private String canal;
    private String codigoBanco;
    private String codigoRemesadora;
    private String identificadorRemesa;
    private String tipoFormaPago;
}
