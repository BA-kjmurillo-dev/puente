package com.puente.service.dto;

import com.puente.persistence.entity.SeguridadCanalEntity;
import com.soap.wsdl.service03.SDTServicioVentanillaOutItemRemesa;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ValidacionCanalDto {
    private SDTServicioVentanillaOutItemRemesa itemRemesa;
    private DTCreaBusinessPartnerResp bpInfo;
    private SeguridadCanalEntity canal;
    private boolean existeBp;
}
