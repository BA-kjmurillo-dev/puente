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
    @NotNull(message = "Canal es un campo requerido")
    @NotEmpty(message = "Canal no puede ir vacío")
    private String canal;
    @NotNull(message = "Identificacion es un campo requerido")
    @NotEmpty(message = "Identificacion no puede ir vacío")
    private String identificacion;
    @NotNull(message = "TipoIdentificacion es un campo requerido")
    @NotEmpty(message = "TipoIdentificacion no puede ir vacío")
    private String tipoIdentificacion;
    @NotNull(message = "AgenciaPago es un campo requerido")
    @NotEmpty(message = "AgenciaPago no puede ir vacío")
    private String agenciaPago;
    @NotNull(message = "SucursalPago es un campo requerido")
    @NotEmpty(message = "SucursalPago no puede ir vacío")
    private String sucursalPago;
    @NotNull(message = "Cajero es un campo requerido")
    @NotEmpty(message = "Cajero no puede ir vacío")
    private String cajero;
    @NotNull(message = "Banco es un campo requerido")
    @NotEmpty(message = "Banco no puede ir vacío")
    private String banco;
    @NotNull(message = "Motivo es un campo requerido")
    @NotEmpty(message = "Motivo no puede ir vacío")
    private String motivo;
    @NotNull(message = "Remesa es un campo requerido")
    private SDTServicioVentanillaOutItemRemesa remesa;
}