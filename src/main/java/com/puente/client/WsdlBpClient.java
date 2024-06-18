package com.puente.client;

import com.puente.web.config.CreaBpProperties;
import com.soap.wsdl.serviceBP.*;
import jakarta.xml.bind.JAXBElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Base64;

public class WsdlBpClient extends WebServiceGatewaySupport {
    @Autowired
    private CreaBpProperties creaBpProperties;

    /**
     * Descripcion: Metodo que hace una petición al servicio SOAP Busca BP
     * @parm IdNumber Numero del cliente
     * @return DTCreaBusinessPartnerResp Retorna un objeto de tipo DTCreaBusinessPartnerResp, que es la respuesta.
     * */
    public DTCreaBusinessPartnerResp getResponse(String idNumber){
        DTCreaBusinessPartnerReq request = new DTCreaBusinessPartnerReq();

        request.setAction("R");

        //Check_lists
        DTCheckListsReq dtCheckListsReq = new DTCheckListsReq();
        dtCheckListsReq.setIdType("HN01");
        dtCheckListsReq.setIdNumber(idNumber); // Ejemplo -> "0122334455699"
        dtCheckListsReq.setIdNatio("HN");

        request.setCheckLists(dtCheckListsReq);

        //Creamos el objeto DTCreateBPReq
        //request.setCreate(null);
        //request.setUpdate(null);

        //request::consultation[]
        //Aquí estamos contruyendo el objeto Consultation que es parte del request
        DTConsultationBPReq consultation = new DTConsultationBPReq();

        //Creamos el objeto messageHeader que es una propiedad de DTConsultationBPReq
        DTConsultationBPReq.MessageHeader messageHeader = new DTConsultationBPReq.MessageHeader();
        messageHeader.setID("");

        consultation.setMessageHeader(messageHeader);

        //Creamos el objeto businessPartnerRetrievalSelectionByIdentifyingElements que es una propiedad de DTConsultationBPReq
        DTConsultationBPReq.BusinessPartnerRetrievalSelectionByIdentifyingElements elements = new DTConsultationBPReq.BusinessPartnerRetrievalSelectionByIdentifyingElements();

        //Creamos el objeto BusinessPartner que es una propiedad de BusinessPartnerRetrievalSelectionByIdentifyingElements
        DTConsultationBPReq.BusinessPartnerRetrievalSelectionByIdentifyingElements.BusinessPartner businessPartner = new DTConsultationBPReq.BusinessPartnerRetrievalSelectionByIdentifyingElements.BusinessPartner();
        businessPartner.setInternalID("");

        elements.setBusinessPartner(businessPartner);

        consultation.setBusinessPartnerRetrievalSelectionByIdentifyingElements(elements);

        //Creamos el objeto ResponseViewSpecification
        DTResponseViewSpecification responseViewSpecification = new DTResponseViewSpecification();

        //Creamos el objeto BusinessProcessChainAssignment
        DTResponseViewSpecification.BusinessProcessChainAssignment businessProcessChainAssignment = new DTResponseViewSpecification.BusinessProcessChainAssignment();
        businessProcessChainAssignment.setRequestedIndicator(true);

        responseViewSpecification.setBusinessProcessChainAssignment(businessProcessChainAssignment);

        //Creamos el objeto Common
        DTResponseViewSpecification.Common common = new DTResponseViewSpecification.Common();
        common.setRequestedIndicator(true);

        //Creamos el objeto FormattedDefaultAddress
        DTResponseViewSpecification.Common.FormattedDefaultAddress formattedDefaultAddress = new DTResponseViewSpecification.Common.FormattedDefaultAddress();
        formattedDefaultAddress.setRequestedIndicator(true);

        common.setFormattedDefaultAddress(formattedDefaultAddress);

        responseViewSpecification.setCommon(common);

        //Creamos el objeto AddressInformation
        DTResponseViewSpecification.AddressInformation addressInformation = new DTResponseViewSpecification.AddressInformation();
        addressInformation.setRequestedIndicator(true);

        //Creamos el objeto Address
        DTResponseViewSpecification.AddressInformation.Address address = new DTResponseViewSpecification.AddressInformation.Address();
        address.setRequestedIndicator(true);

        //Creamos el objeto FormattedAddress
        DTResponseViewSpecification.AddressInformation.Address.FormattedAddress formattedAddress = new DTResponseViewSpecification.AddressInformation.Address.FormattedAddress();
        formattedAddress.setRequestedIndicator(true);

        address.setFormattedAddress(formattedAddress);

        //Creamos el objeto PostalRegulationsCompliantAddress
        DTResponseViewSpecification.AddressInformation.Address.PostalRegulationsCompliantAddress postalRegulationsCompliantAddress = new DTResponseViewSpecification.AddressInformation.Address.PostalRegulationsCompliantAddress();
        postalRegulationsCompliantAddress.setRequestedIndicator(true);

        address.setPostalRegulationsCompliantAddress(postalRegulationsCompliantAddress);

        addressInformation.setAddress(address);

        responseViewSpecification.setAddressInformation(addressInformation);

        //Creamos el objeto CommunicationData
        DTResponseViewSpecification.CommunicationData communicationData = new DTResponseViewSpecification.CommunicationData();
        communicationData.setRequestedIndicator(true);

        responseViewSpecification.setCommunicationData(communicationData);

        //Creamos el objeto Role
        DTResponseViewSpecification.Role role = new DTResponseViewSpecification.Role();
        role.setRequestedIndicator(true);

        responseViewSpecification.setRole(role);

        //Creamos el objeto BankDetails
        DTResponseViewSpecification.BankDetails bankDetails = new DTResponseViewSpecification.BankDetails();
        bankDetails.setRequestedIndicator(true);

        responseViewSpecification.setBankDetails(bankDetails);

        //Creamos el objeto PaymentCardDetails
        DTResponseViewSpecification.PaymentCardDetails paymentCardDetails = new DTResponseViewSpecification.PaymentCardDetails();
        paymentCardDetails.setRequestedIndicator(true);

        responseViewSpecification.setPaymentCardDetails(paymentCardDetails);

        //Creamos el objeto IndustrySector
        DTResponseViewSpecification.IndustrySector industrySector = new DTResponseViewSpecification.IndustrySector();
        industrySector.setRequestedIndicator(true);

        responseViewSpecification.setIndustrySector(industrySector);

        //Creamos el objeto Identification
        DTResponseViewSpecification.Identification identification = new DTResponseViewSpecification.Identification();
        identification.setRequestedIndicator(true);

        responseViewSpecification.setIdentification(identification);

        //Creamos el objeto TaxNumber
        DTResponseViewSpecification.TaxNumber taxNumber = new DTResponseViewSpecification.TaxNumber();
        taxNumber.setRequestedIndicator(true);

        responseViewSpecification.setTaxNumber(taxNumber);

        //Creamos el objeto GeneralProductTaxExemption
        DTResponseViewSpecification.GeneralProductTaxExemption generalProductTaxExemption = new DTResponseViewSpecification.GeneralProductTaxExemption();
        generalProductTaxExemption.setRequestedIndicator(true);

        responseViewSpecification.setGeneralProductTaxExemption(generalProductTaxExemption);

        //Creamos el objeto TextCollection
        DTResponseViewSpecification.TextCollection textCollection = new DTResponseViewSpecification.TextCollection();
        textCollection.setRequestedIndicator(true);

        responseViewSpecification.setTextCollection(textCollection);

        //Creamos el objeto CreditRating
        DTResponseViewSpecification.CreditRating creditRating = new DTResponseViewSpecification.CreditRating();
        creditRating.setRequestedIndicator(true);

        responseViewSpecification.setCreditRating(creditRating);

        //Creamos el objeto CreditStanding
        DTResponseViewSpecification.CreditStanding creditStanding = new DTResponseViewSpecification.CreditStanding();
        creditStanding.setRequestedIndicator(true);

        responseViewSpecification.setCreditStanding(creditStanding);

        //Creamos el objeto Occupation
        DTResponseViewSpecification.Occupation occupation = new DTResponseViewSpecification.Occupation();
        occupation.setRequestedIndicator(true);

        responseViewSpecification.setOccupation(occupation);

        //Creamos el objeto FinancialStatement
        DTResponseViewSpecification.FinancialStatement financialStatement = new DTResponseViewSpecification.FinancialStatement();
        financialStatement.setRequestedIndicator(true);

        responseViewSpecification.setFinancialStatement(financialStatement);

        //Creamos el objeto Alias
        DTResponseViewSpecification.Alias alias = new DTResponseViewSpecification.Alias();
        alias.setRequestedIndicator(true);

        responseViewSpecification.setAlias(alias);

        //Creamos el objeto TaxCompliance
        DTResponseViewSpecification.TaxCompliance taxCompliance = new DTResponseViewSpecification.TaxCompliance();
        taxCompliance.setRequestedIndicator(true);

        responseViewSpecification.setTaxCompliance(taxCompliance);

        //Creamos el objeto BankRegulations
        DTResponseViewSpecification.BankRegulations bankRegulations = new DTResponseViewSpecification.BankRegulations();
        bankRegulations.setRequestedIndicator(true);

        responseViewSpecification.setBankRegulations(bankRegulations);

        consultation.setResponseViewSpecification(responseViewSpecification);
        //request::consultation[]

        //Completamos los datos del Tag MT_CreaBusinessPartner_Req
        request.setConsultation(consultation);

        //Realizamos la petición
        Object response = getWebServiceTemplate().marshalSendAndReceive(
                creaBpProperties.getUrl(),
                request,
                getrequestCallback());

        DTCreaBusinessPartnerResp businessPartnerResp = null;
        if (response instanceof JAXBElement) {
            JAXBElement<?> jaxbElement = (JAXBElement<?>) response;
            Object value = jaxbElement.getValue();
            if (value instanceof DTCreaBusinessPartnerResp) {
                businessPartnerResp = (DTCreaBusinessPartnerResp) value;
                // Ahora puedes usar businessPartnerResp como desees
            } else {
                // Maneja el caso en el que el valor no es del tipo esperado
                throw new ClassCastException("El valor no es una instancia de DTCreaBusinessPartnerResp");
            }
        } else {
            // Maneja el caso en el que response no es un JAXBElement
            throw new ClassCastException("response no es una instancia de JAXBElement");
        }

        return businessPartnerResp;
    }

    /**
     * Descripcion: Metodo que obtiene los valores de unsername y password del archivo de propiedades
     * @parm
     * @return String Retorna un String cons el usuario y contraseña en Base64 para la autenticación.
     * */
    public String getCredenciales(){
        String cadenaCredenciales = creaBpProperties.getUsername() + ":" + creaBpProperties.getPassword();
        return "Basic " + Base64.getEncoder().encodeToString(cadenaCredenciales.getBytes());
    }

    /**
     * Descripcion: Metodo que se utiliza para agregar las credenciales de la autorización Basic.
     * @parm
     * @return WebServiceMessageCallback Retorna un objeto de tipo WebServiceMessageCallback que
     * */
    public WebServiceMessageCallback getrequestCallback(){
        return new WebServiceMessageCallback() {
            @Override
            public void doWithMessage(WebServiceMessage webServiceMessage) throws IOException, TransformerException {
                TransportContext context = TransportContextHolder.getTransportContext();
                HttpUrlConnection connection = (HttpUrlConnection) context.getConnection();
                connection.addRequestHeader("Authorization", getCredenciales());
            }
        };
    }
}
