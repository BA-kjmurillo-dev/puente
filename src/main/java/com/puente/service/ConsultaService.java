package com.puente.service;

import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.service.dto.ComparacionNombreDto;
import com.puente.service.dto.RemittanceAlgorithmDto;
import com.puente.service.dto.ValidacionCanalDto;
import com.puente.web.controller.ConsultaController;
import com.soap.wsdl.service03.SDTServicioVentanillaOutItemRemesa;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.Common.Person;

import java.util.HashMap;
import java.util.Map;

@Service
@ToString
@NoArgsConstructor
public class ConsultaService {
    @Autowired
    private SeguridadCanalService seguridadCanalService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(ConsultaService.class);

    public RemittanceAlgorithmDto ConsultaRemesadora(String remesa){
        return utilService.ConsultaRemesadora(remesa);
    }

    public String getPaymentType(
        String paymentMethod
    ) {
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("01", "CASH");
        keyValueMap.put("02", "DEPOSIT_ACCOUNT");
        return keyValueMap.get(paymentMethod) == null ? "CASH" : keyValueMap.get(paymentMethod);
    }
    public String validacionCanalConsulta(ValidacionCanalDto data){
        SeguridadCanalEntity canal = data.getCanal();
        String result = "000000";
        switch (canal.getCodigoCanal()) {
            case "0002":
                // validaciones de canal 0002 JTELLER
                // validacionCanalJtellerConsulta
                break;
            case "0003":
                // Validaciones de canal 0003 OCB
                result = validacionCanalOcbConsulta(data);
                break;
            case "0004":
                // Validaciones de canal 0004 ABAS
                // validacionCanalAbasConsulta
                break;
            case "0005":
                // Validaciones de canal 0005 DILO
                // validacionCanalDiloConsulta
                break;
            case "0006":
                // Validaciones de canal 0006 ABI
                // validacionCanalAbiConsulta
                break;
            default:
                result = "false";
                break; // code block for default case
        }
        return result;
    }

    public String validacionCanalOcbConsulta(ValidacionCanalDto data){

        DTCreaBusinessPartnerResp bpInfo = data.getBpInfo();
        SDTServicioVentanillaOutItemRemesa itemRemesa = data.getItemRemesa();
        SeguridadCanalEntity canal = data.getCanal();
        ComparacionNombreDto comparacion = new ComparacionNombreDto();
        ComparacionNombreDto.beneficiarioBp bp = new ComparacionNombreDto.beneficiarioBp();
        ComparacionNombreDto.beneficiarioSireon benSireion = new ComparacionNombreDto.beneficiarioSireon();

        Person personBp= utilService.getBpBeneficiaryInfo(bpInfo);
        bp.setPrimerNombreBp(personBp.getName().getGivenName());
        bp.setSegundoNombreBp(personBp.getName().getMiddleName());
        bp.setPrimerApellidoBp(personBp.getName().getFamilyName());
        bp.setSegundoApellidoBp(personBp.getName().getAdditionalFamilyName());
        comparacion.setBeneficiarioBp(bp);
        benSireion.setPrimerNombreSireon(itemRemesa.getBeneficiario().getPrimerNombre());
        benSireion.setSegundoNombreSireon(itemRemesa.getBeneficiario().getSegundoNombre());
        benSireion.setPrimerApellidoSireon(itemRemesa.getBeneficiario().getPrimerApellido());
        benSireion.setSegundoApellidoSireon(itemRemesa.getBeneficiario().getSegundoApellido());
        comparacion.setBeneficiarioSireon(benSireion);

        boolean similarity = this.utilService.CompararNombre(comparacion,itemRemesa.getCodigoRemesadora());
        if (similarity) {
            return "000000";
        }else {
            return "000030";
        }

    }

}
