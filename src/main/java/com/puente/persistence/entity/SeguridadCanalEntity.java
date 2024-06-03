package com.puente.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="SEGURIDAD_CANAL")
@Getter
@Setter
@NoArgsConstructor
public class SeguridadCanalEntity {
    @Id
    @Column(nullable = false, length = 50)
    private String usuario;

    @Column(nullable = false, length = 255)
    private String contrasenia;

    @Column(length = 50)
    private String canal;

    @Column(name = "codigo_canal_sireon", length = 50)
    private String codigoCanalSireon;

    @Column(name = "codigo_canal_po", length = 50)
    private String codigoCanalPo;

    @Column(name = "codigo_canal_was", length = 50)
    private String codigoCanalWas;

    @Column(name = "codigo_canal_siremu", length = 50)
    private String codigoCanalSiremu;

    @Column(name = "codigo_canal_srv", length = 50)
    private String codigoCanalSrv;

    @Column(name = "codigo_canal", length = 50)
    private String codigoCanal;

    @Column(name = "descripcion_canal", length = 255)
    private String descripcionCanal;

    @Column(name = "moneda_local", length = 3)
    private String monedaLocal;

    @Column(name = "moneda_extranjera", length = 3)
    private String monedaExtranjera;

    @Column(name = "metodo_pago", length = 2)
    private String metodoPago;

    @Column(name = "monto_limite", columnDefinition = "INT")
    private Integer montoLimite;

    @Column(length = 255)
    private String adicional1;

    @Column(length = 255)
    private String adicional2;

    @Column(length = 255)
    private String adicional3;

}
