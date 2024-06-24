package com.puente.service.dto;

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
public class RequestGetRemittanceDataDto {
    @NotNull(message = "Canal es un campo requerido")
    @NotEmpty(message = "Canal no puede ir vacío")
    private String canal;
    @NotNull(message = "IdentificadorRemesa es un campo requerido")
    @NotEmpty(message = "IdentificadorRemesa no puede ir vacío")
    private String identificadorRemesa;
    @NotNull(message = "Identificacion es un campo requerido")
    @NotEmpty(message = "Identificacion no puede ir vacío")
    private String identificacion;
}
