package com.puente.service;

import com.puente.client.SrvBasa010Client;
import com.puente.persistence.entity.SeguridadCanalEntity;
import com.puente.service.dto.ComparacionNombreDto;
import com.puente.service.dto.RemittanceAlgorithmDto;
import com.puente.service.dto.ValidacionCanalDto;
import com.puente.web.controller.ConsultaController;
import com.soap.wsdl.ServicioSrvBasa002.EjecutarSrvBasa002Response;
import com.soap.wsdl.ServicioSrvBasa003.EjecutarSrvBasa003Response;
import com.soap.wsdl.ServicioSrvBasa010.EjecutarSrvBasa010Response;
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
    @Autowired
    private static SrvBasa010Service srvBasa010Service;
    @Autowired
    private SrvBasa010Client srvBasa010Client;
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
    public String validacionCanalConsulta(ValidacionCanalDto data,String cuenta,String agencia,String sucursal){
        SeguridadCanalEntity canal = data.getCanal();
        String result = "000000";
        switch (canal.getCodigoCanal()) {
            case "0002":
                // validaciones de canal 0002 JTELLER
                // validacionCanalJtellerConsulta
                break;
            case "0003":
                // Validaciones de canal 0003 OCB
                result = validacionCanalOcbConsulta(data,cuenta,agencia,sucursal);
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

    public String validacionCanalOcbConsulta(ValidacionCanalDto data,String cuenta,String agencia,String sucursal){

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

        boolean result = validaAgenciaSucursal(agencia,sucursal);

        if (!result) {
            boolean result2 = this.utilService.validaAgenciaSucursal(cuenta);
            if (!result2) {
                return "000031";
            }
        }

        boolean similarity = this.utilService.CompararNombre(comparacion,itemRemesa.getCodigoRemesadora());
        if (similarity) {
            return "000000";
        }else {
            return "000030";
        }

    }
    public boolean validaAgenciaSucursal(String agencia, String sucursal){
        if (agencia == null || sucursal == null || agencia.isEmpty() || sucursal.isEmpty()) {
            return false;
        }
        EjecutarSrvBasa010Response ejecutarSrvBasa010Response = srvBasa010Client.getResponse010(agencia,sucursal);
        //EjecutarSrvBasa010Response ejecutarSrvBasa010Response = srvBasa010Service.getInfoAgenciaSucursal(agencia,sucursal);
        if ( ejecutarSrvBasa010Response.getRespuestaSrvBasa010().getCodigoMensaje().isEmpty() &&
                (ejecutarSrvBasa010Response.getRespuestaSrvBasa010().getCodigoMensaje() == null)
        ) {

            return false;
        }
        if (!ejecutarSrvBasa010Response.getRespuestaSrvBasa010().getCodigoMensaje().equals("00")){
            return false;
        }
        return true;
    }
}
