package com.puente.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "log")
@IdClass(LogId.class)
@Getter
@Setter
@NoArgsConstructor
public class LogEntity {
    @Id
    @Column(nullable = false, length = 50)
    private String remesa;

    @Id
    @Column(name = "fecha_log", columnDefinition = "DATE")
    private LocalDate fechaLog;

    @Id
    @Column(name = "hora_log", columnDefinition = "TIMESTAMP")
    private LocalTime horaLog;

    @Id
    @Column(length = 50)
    private String canal;

    @Column(length = 50)
    private String remesadora;

    @Column(name = "identidad_beneficiario", length = 50)
    private String identidadBeneficiario;

    @Column(length = 50)
    private String accion;

    @Column(name = "registro_id", length = 50)
    private String registroId;

    @Column(length = 6)
    private String banco;

    @Column(length = 50)
    private String agencia;

    @Column(length = 50)
    private String sucursal;

    @Column(length = 50)
    private String terminal;

    @Column(name = "usuario_canal", length = 50)
    private String usuarioCanal;

    @Column(name = "mensaje_salida", length = 100)
    private String mensajeSalida;

    @Column(name = "codig_salida", columnDefinition = "INT")
    private Integer codigoSalida;

    @Column(name = "estado_remesa", length = 25)
    private String estadoRemesa;

    @Column(length = 255)
    private String adicional1;

    @Column(length = 255)
    private String adicional2;

    @Column(length = 255)
    private String adicional3;
}
