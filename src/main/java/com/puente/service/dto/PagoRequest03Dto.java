package com.puente.service.dto;


import com.soap.wsdl.service03.SDTServicioVentanillaIn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PagoRequest03Dto {
    private String code;
    private SDTServicioVentanillaIn request03;


}
