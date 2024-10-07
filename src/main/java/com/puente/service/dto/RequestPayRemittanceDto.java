package com.puente.service.dto;

import com.soap.wsdl.service03.SDTServicioVentanillaOutItemRemesa;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestPayRemittanceDto {
    @NotNull
    @NotEmpty
    private String canal;
    @NotNull
    @NotEmpty
    private String identificacion;
    @NotNull
    @NotEmpty
    private String tipoIdentificacion;
    @NotNull
    @NotEmpty
    private String agenciaPago;
    @NotNull
    @NotEmpty
    private String sucursalPago;
    @NotNull
    @NotEmpty
    private String cajero;
    @NotNull
    @NotEmpty
    private String banco;
    @NotNull
    @NotEmpty
    private String motivo;
    @NotNull
    private SDTServicioVentanillaOutItemRemesa remesa;
    //@NotNull
    //@NotEmpty
    //private String agencia;
    //@NotNull
    //@NotEmpty
    //private String sucursal;
    private String cuentaDebito;
    private String cuentaCredito;

}