package com.puente.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="PARAMETRO_REMESADORA")
@Getter
@Setter
@NoArgsConstructor
public class ParametroRemesadoraEntity {

    @Id
    @Column(nullable = false, length = 25)
    private String mrecod;//Remesadora

    @Column(nullable = false, length = 25)
    private String corval;//Correlativo de Validacion

    @Column(nullable = false, length = 4)
    private String longMin;//Longitud Minima

    @Column(nullable = false, length = 4)
    private String longMax;//Longitud Maxima

    @Column(nullable = false, length = 1)
    private String tipoDato;//Tipo de Dato

    @Column(nullable = true, length = 1)
    private String inicio;//Caracter de Inicio
}
