package com.puente.persistence.entity;

import lombok.Getter;

@Getter

public enum TipoErrorEnum {
    ERROR("Error"),
    WARNING("Advertencia"),
    INFO("Informacion"),
    SUCCESS("Correcto"),
    Error_Validacion("Error Validacion"),
    Error_Datos("Error Validacion de Datos Cliente"),
    Alerta("Alerta"),
    Error_Servicio("Error Interno del Servicio");

    private final String error;
    TipoErrorEnum(String error) {
        this.error = error;
    }
}