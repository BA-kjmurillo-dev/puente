package com.puente.service;

import com.puente.client.Wsdl05Client;
import com.puente.service.dto.Wsdl05Dto;
import com.soap.wsdl.service05.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Wsdl05Service {
    private final Wsdl05Client wsdl05Client;

    @Autowired
    public Wsdl05Service(Wsdl05Client wsdl05Client) {
        this.wsdl05Client = wsdl05Client;
    }

    public Wsdl05Dto getRemittersListByChannel(
        String canal
    ) {
        try {
            WSSIREON005LISTADOREMESADORESCANALResponse servicesresponse = this.wsdl05Client.getRemittersListByChannel(canal);
            ServicesResponse005 servicesResponse005 = servicesresponse.getServicesresponse005();

            ServicesResponse responseStatus = servicesResponse005.getServicesResponse();
            String messageCode = responseStatus.getMessageCode();
            String message = responseStatus.getMessage();

            Wsdl05Dto remittancesListResponse = new Wsdl05Dto();
            remittancesListResponse.setMessageCode(messageCode);
            remittancesListResponse.setMessage(message);

            if (messageCode.equals("000000")) {
                List<ServicesResponse005ItemRemesadora> remittancesList = servicesResponse005.getColeccionRemesadoras().getItemRemesadora();

                if (remittancesList != null && !remittancesList.isEmpty()) {
                    List<Wsdl05Dto.Awsdl05Data> remittancesDataList = mapRemittersData(remittancesList);
                    remittancesListResponse.setData(remittancesDataList);
                }
            }
            return remittancesListResponse;

        } catch (Exception e) {
            throw new Wsdl05ServiceException("Error al obtener lista de remesadoras", e);
        }
    }

    private List<Wsdl05Dto.Awsdl05Data> mapRemittersData(
        List<ServicesResponse005ItemRemesadora> remittancesList
    ) {
        return remittancesList.stream()
            .map(this::mapRemitterData)
            .collect(Collectors.toList());
    }

    private Wsdl05Dto.Awsdl05Data mapRemitterData(
        ServicesResponse005ItemRemesadora remittance
    ){
        return new Wsdl05Dto.Awsdl05Data(
            remittance.getCodigoRemesador(),
            remittance.getDescriptorRemesador(),
            remittance.getTipoRemesador()
        );
    }

    public static class Wsdl05ServiceException extends RuntimeException {
        public Wsdl05ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}