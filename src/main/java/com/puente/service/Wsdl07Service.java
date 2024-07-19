package com.puente.service;

import com.soap.wsdl.service07.*;
import com.puente.client.Wsdl07Client;
import com.puente.service.dto.Wsdl07Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.util.List;

@Service
public class Wsdl07Service extends WebServiceGatewaySupport {
    private final Wsdl07Client wsdl07Client;

    @Autowired
    public Wsdl07Service(Wsdl07Client wsdl07Client) {
        this.wsdl07Client = wsdl07Client;
    }

    public Wsdl07Dto getRemittanceByIdentifier(
        ServicesRequest007ItemSolicitud itemSolicitudRequest
    ) {
        WSSIREON007CONSULTAPORIDENTIFICADORResponse servicesresponse = this.wsdl07Client.getRemittanceByIdentifier(itemSolicitudRequest);
        ServicesResponse007 servicesresponse007 = servicesresponse.getServicesresponse007();

        ServicesResponse responseStatus = servicesresponse007.getServicesResponse();
        String messageCode = responseStatus.getMessageCode();
        String message = responseStatus.getMessage();

        Wsdl07Dto remittanceByIdentifierResponse = new Wsdl07Dto();
        remittanceByIdentifierResponse.setCode(messageCode);
        remittanceByIdentifierResponse.setMessage(message);

        if(messageCode.equals("000000")) {
            List<ServicesResponse007ItemTransfer> remittanceList = servicesresponse007.getColeccionRemesas().getItemTransfer();
            if(remittanceList != null && !remittanceList.isEmpty()) {
                ServicesResponse007ItemTransfer remittance = remittanceList.get(0);

                Wsdl07Dto.Awsdl07Data remittanceData = new Wsdl07Dto.Awsdl07Data();
                remittanceData.setCodigoRemesadora(remittance.getCodigoRemesadora());
                remittanceData.setEstadoRemesa(remittance.getEstadoRemesa());
                remittanceData.setDescriptorEstadoRemesa(remittance.getDescriptorEstadoRemesa());

                remittanceByIdentifierResponse.setData(remittanceData);
            }
        }
        return remittanceByIdentifierResponse;
    }

    public Boolean isValidStatus(
        String remittanseStatus
    ) {
        List<String> validStatuses = List.of("Cargada", "Pendiente", "Reversada","EnProceso");
        return validStatuses.contains(remittanseStatus);
    }
}
