package com.puente.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Dto de credenciales para los servicios de ser necesario agegar mas campos
public class CredencialesDto {
    private String user;
    private String password;
    private String token;

}
