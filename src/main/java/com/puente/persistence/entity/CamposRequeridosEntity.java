package com.puente.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="CamposRequeridos")
@IdClass(CamposRequeridosId.class)
@Getter
@Setter
@NoArgsConstructor
public class CamposRequeridosEntity {
    @Id
    @Column(nullable = false, length = 25)
    private String servicio;

    @Id
    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "es_requerido")
    private boolean esRequerido;

    @Column(length = 100)
    private String descripcion;
}
