package com.puente.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="MESSAGE_CODES")
@Getter
@Setter
@NoArgsConstructor
public class MessageCodesEntity {
    @Id
    @Column(nullable = false, length = 25)
    private String messageCode;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(length = 100)
    private String message;
}
