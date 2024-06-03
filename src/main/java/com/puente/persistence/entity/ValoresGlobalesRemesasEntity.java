package com.puente.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="valoresGlobalesRemesa")
@Getter
@Setter
@NoArgsConstructor
public class ValoresGlobalesRemesasEntity {
    @Id
    @Column(nullable = false, length = 25)
    private String codigo;

    @Column(length = 50)
    private String item;

    @Column(length = 100)
    private String valor;

    @Column(length = 50)
    private String adicional1;

    @Column(length = 50)
    private String adicional2;

    @Column(length = 50)
    private String adicional3;
}
