package com.puente.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="CamposRequeridos")
@Getter
@Setter
@NoArgsConstructor
public class CamposRequeridosEntity {
    @Id
    @Column(nullable = false, length = 25)
    private String servicio;

    @Column(length = 50)
    private String nombre;

    @Column(name = "es_requerido")
    private boolean esRequerido;

    @Column(length = 100)
    private boolean descripcion;
}
