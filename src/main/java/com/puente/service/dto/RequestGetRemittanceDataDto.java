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
    @NotNull
    @NotEmpty
    private String canal;
    @NotNull
    @NotEmpty
    private String identificadorRemesa;
    @NotNull
    @NotEmpty
    private String identificacion;
    @NotNull
    @NotEmpty
    private String agencia;
    @NotNull
    @NotEmpty
    private String sucursal;
    private String cuenta;
}
