package com.puente.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Descripcion: Clase que representa los datos de un Cliente para su creación.
 * */
@Getter
@Setter
@NoArgsConstructor
public class DatosBpDto {
    /**
     * Descripcion: Representa el numero de documento
     * */
    private String identificador;

    private String tipoIdentificador;

    private String primerNombre;

    private String segundoNombre;

    private String primerApellido;

    private String segundoApellido;

    private String genero;

    private String fechaNacimiento;

    private String nacionalidad;

    private String telefono;

    private String codigoPais;

    private String direccion;

    private String paisNacimiento;

    private String ciudadNacimiento;

    private String fechaEmisionId;

    private String fechaVencimientoId;

}
