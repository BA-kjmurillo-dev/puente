package com.puente.service.dto;

import com.soap.wsdl.service03.SDTServicioVentanillaOutItemRemesa;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGetRemittanceDataDto {
    private String type;
    private String code;
    private String message;
    private SDTServicioVentanillaOutItemRemesa data;
    private Boolean existeBp;
    private String datosExtras;
    DTCreaBusinessPartnerResp bpInfo;
}
