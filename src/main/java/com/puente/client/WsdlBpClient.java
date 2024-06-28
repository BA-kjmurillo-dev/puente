package com.puente.client;

import com.puente.service.dto.CodigoPaisIsoDto;
import com.puente.service.dto.DatosBpDto;
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

    @Autowired
    private CodigoPaisIsoDto codigoPaisIso;

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

    /**
     * Descripcion: Metodo que hace una petición al servicio SOAP Busca BP
     * @parm IdNumber Numero del cliente
     * @return DTCreaBusinessPartnerResp Retorna un objeto de tipo DTCreaBusinessPartnerResp, que es la respuesta.
     * */
    public DTCreaBusinessPartnerResp getResponse(String idNumber){
        DTCreaBusinessPartnerReq request = new DTCreaBusinessPartnerReq();

        request.setAction(creaBpProperties.getActionR());

        //Check_lists
        request.setCheckLists(dtCheckListsReq(idNumber));

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
     * Descripcion: Metodo que hace una petición al servicio SOAP para crear el BP en caso de que no exista
     * @parm DatosBpDto Objeto que contiene los datos principales del cliente
     * @return DTCreaBusinessPartnerResp Retorna un objeto de tipo DTCreaBusinessPartnerResp
     * */

    public DTCreaBusinessPartnerResp getResponseCreateBp(DatosBpDto datosBpDto){
        DTCreaBusinessPartnerReq request = new DTCreaBusinessPartnerReq();
        request.setAction("I");

        //Check_lists
//        DTCheckListsReq dtCheckListsReq = new DTCheckListsReq();
//        dtCheckListsReq.setIdType("HN01");
//        dtCheckListsReq.setIdNumber("0701198209161"); // Ejemplo -> "0122334455699"
//        dtCheckListsReq.setIdNatio("HN");

        request.setCheckLists(dtCheckListsReq(datosBpDto));

        //[<Create>]
        //Creamos un objeto Create
        DTCreateBPReq create = new DTCreateBPReq();
        //[<MessageHeader>]
        //Creamos un objeto MessageHeader
        DTMessageHeaderReq messageHeader = new DTMessageHeaderReq();
        messageHeader.setID("");
        messageHeader.setUUID("");
        messageHeader.setReferenceID("");
        messageHeader.setReferenceUUID("");
        messageHeader.setCreationDateTime("");
        messageHeader.setTestDataIndicator("");
        messageHeader.setReconciliationIndicator("");
        messageHeader.setSenderBusinessSystemID("BS_CRM_DEV_300_HN75ODH2");
        messageHeader.setRecipientBusinessSystemID("");

        create.setMessageHeader(messageHeader);
        //[</MessageHeader>]

        //[<BusinessPartner>]
        //Creamos un objeto BusinessPartner
        DTBusinessPartnerReq businessPartner = new DTBusinessPartnerReq();
        businessPartner.setUUID("");
        businessPartner.setInternalID("");
        businessPartner.setNumberRangeIntervalBusinessPartnerGroupCode("Z010");
        businessPartner.setCategoryCode("1");

        //<BusinessProcessChainAssignment>
        DTBusinessPartnerReq.BusinessProcessChainAssignment businessProcessChainAssignment = new DTBusinessPartnerReq.BusinessProcessChainAssignment();
        businessProcessChainAssignment.setBusinessProcessChainUUID("");
        businessProcessChainAssignment.setBankingBusinessTransactionTypeCode("");

        businessPartner.setBusinessProcessChainAssignment(businessProcessChainAssignment);
        //</BusinessProcessChainAssignment>

        //<Common>
        DTBusinessPartnerReq.Common common = new DTBusinessPartnerReq.Common();
        common.setKeyWordsText("");
        common.setAdditionalKeyWordsText("");
        common.setVerbalCommunicationLanguageCode("Es");
        common.setDataOriginTypeCode("");
        common.setContactAllowedCode("");
        common.setCorrespondenceBrailleRequiredIndicator("");
        common.setCorrespondenceLargePrintRequiredIndicator("");
        common.setNaturalPersonIndicator("");
        common.setVIPIndicator("");
        common.setCustomerUndesirableIndicator("");
        common.setCustomerUndesirableReasonCode("");
        common.setCustomerUndesirableReasonNote("");
        common.setLastContactDate("20240229");
        common.setBusinessRelationshipEndDate("99991231");
        common.setCustomGroupCode("");
        common.setTargetMarketGroupCode("");
        common.setAuthorisationGroupCode("001");
        common.setAffiliatedCompanyID("");
        common.setAffiliatedCompanyGroupID("");

        businessPartner.setCommon(common);
        //<Person>
        DTBusinessPartnerReq.Common.Person person = new DTBusinessPartnerReq.Common.Person();
        //<Name>
        DTBusinessPartnerReq.Common.Person.Name name = new DTBusinessPartnerReq.Common.Person.Name();
        name.setFormOfAddressCode("0001");
        name.setGivenName(datosBpDto.getPrimerNombre());
        name.setMiddleName(datosBpDto.getSegundoNombre());
        name.setFamilyName(datosBpDto.getPrimerApellido());
        name.setAdditionalFamilyName(datosBpDto.getSegundoApellido());
        //</Name>
        person.setName(name);
        person.setGenderCode(getTipoGenero(datosBpDto));
        person.setBirthPlaceName(datosBpDto.getCiudadNacimiento());
        person.setBirthDate(datosBpDto.getFechaNacimiento().trim());
        person.setNonVerbalCommunicationLanguageCode("Es");
        person.setNationalityCountryCode(getNacionalidad(datosBpDto.getNacionalidad()));
        person.setOriginCountryCode(getNacionalidad(datosBpDto.getPaisNacimiento()));
        person.setEmployeeIndicator("");
        person.setEmployeeIDExistenceCheckIndicator("X");

        //<Identity userAccountIDDeletionRequiredIndicator="?">
        DTBusinessPartnerReq.Common.Person.Identity identity = new DTBusinessPartnerReq.Common.Person.Identity();
        //identity.setUserAccountIDDeletionRequiredIndicator("?");
        identity.setUserAccountID("");

        person.setIdentity(identity);
        //</Identity>
        common.setPerson(person);
        //</Person>
        //</Common>

        //<AddressInformation>
        DTBusinessPartnerReq.AddressInformation addressInformation = new DTBusinessPartnerReq.AddressInformation();

        //<ValidityPeriod>
        DTBusinessPartnerReq.AddressInformation.ValidityPeriod validityPeriod = new DTBusinessPartnerReq.AddressInformation.ValidityPeriod();
        validityPeriod.setStartDate("00010101");
        validityPeriod.setEndDate("99991231");

        addressInformation.setValidityPeriod(validityPeriod);
        //</ValidityPeriod>

        //<AddressUsage>
        DTBusinessPartnerReq.AddressInformation.AddressUsage addressUsage = new DTBusinessPartnerReq.AddressInformation.AddressUsage();
        addressUsage.setAddressUsageCode("XXDEFAULT");

        //<ValidityPeriod>
        DTBusinessPartnerReq.AddressInformation.AddressUsage.ValidityPeriod period = new DTBusinessPartnerReq.AddressInformation.AddressUsage.ValidityPeriod();
        period.setStartDate("00010101");
        period.setEndDate("99991231");

        addressUsage.setValidityPeriod(period);
        //</ValidityPeriod>

        addressUsage.setDefaultIndicator("X");

        addressInformation.getAddressUsage().add(addressUsage);
        //</AddressUsage>

        //<Address>
        DTBusinessPartnerReq.AddressInformation.Address address = new DTBusinessPartnerReq.AddressInformation.Address();

        //<PostalAddress> <!--Zero or more repetitions:-->
        DTBusinessPartnerReq.AddressInformation.Address.PostalAddress postalAddress = new DTBusinessPartnerReq.AddressInformation.Address.PostalAddress();
        postalAddress.setCountryCode("HN");
        postalAddress.setCityName("San Pedro Sula");
        postalAddress.setPOBoxIndicator("X");

        address.getPostalAddress().add(postalAddress);
        //</PostalAddress>

        //<Telephone>
        DTBusinessPartnerReq.AddressInformation.Address.Telephone telephone = new DTBusinessPartnerReq.AddressInformation.Address.Telephone();

        //<Number>
        DTBusinessPartnerReq.AddressInformation.Address.Telephone.Number number = new DTBusinessPartnerReq.AddressInformation.Address.Telephone.Number();
        number.setAreaID("");
        number.setSubscriberID(datosBpDto.getTelefono().trim());
        number.setExtensionID("");
        number.setCountryCode("HN");
        number.setCountryDiallingCode("");

        telephone.setNumber(number);
        //</Number>

        telephone.setUsageDeniedIndicator("");
        telephone.setMobilePhoneNumberIndicator("X");
        telephone.setSMSEnabledIndicator("X");
        telephone.setDefaultIndicator("X");

        address.getTelephone().add(telephone);
        //</Telephone>

        //<Telephone> // - 2
        DTBusinessPartnerReq.AddressInformation.Address.Telephone telephone2 = new DTBusinessPartnerReq.AddressInformation.Address.Telephone();

        //<Number>
        DTBusinessPartnerReq.AddressInformation.Address.Telephone.Number number2 = new DTBusinessPartnerReq.AddressInformation.Address.Telephone.Number();
        number2.setAreaID("");
        number2.setSubscriberID("33123455");
        number2.setExtensionID("");
        number2.setCountryCode("HN");
        number2.setCountryDiallingCode("");

        telephone2.setNumber(number2);
        //</Number>

        telephone2.setUsageDeniedIndicator("");
        telephone2.setMobilePhoneNumberIndicator("X");
        telephone2.setSMSEnabledIndicator("X");
        telephone2.setDefaultIndicator("X");

        address.getTelephone().add(telephone2);
        //</Telephone>

        addressInformation.setAddress(address);
        //</Address>

        businessPartner.getAddressInformation().add(addressInformation);
        //</AddressInformation>

        //<Role>
        DTBusinessPartnerReq.Role role = new DTBusinessPartnerReq.Role();
        role.setRoleCode("ZBUP09");
        role.setPartyBusinessCharacterCode("ZBUP09");

        //<ValidityPeriod>
        DTBusinessPartnerReq.Role.ValidityPeriod validityPeriod1 = new DTBusinessPartnerReq.Role.ValidityPeriod();
        validityPeriod1.setStartDate("");
        validityPeriod1.setEndDate("");
        role.setValidityPeriod(validityPeriod1);
        //</ValidityPeriod>
        businessPartner.getRole().add(role);
        //</Role>

        //<Identification> <!--Zero or more repetitions:-->
        //<//Identification>
        DTBusinessPartnerReq.Identification identification = new DTBusinessPartnerReq.Identification();
        identification.setPartyIdentifierTypeCode("HN01");
        identification.setEntryDate("20240229");
        identification.setAreaOfValidityCountryCode("HN");
        identification.setAreaOfValidityRegionCode("");

        //<ValidityPeriod>
        DTBusinessPartnerReq.Identification.ValidityPeriod validityPeriod2 = new DTBusinessPartnerReq.Identification.ValidityPeriod();
        validityPeriod2.setStartDate(datosBpDto.getFechaEmisionId());
        validityPeriod2.setEndDate(datosBpDto.getFechaVencimientoId());

        identification.setValidityPeriod(validityPeriod2);
        //<//ValidityPeriod>

        businessPartner.getIdentification().add(identification);
        create.setBusinessPartner(businessPartner);

        //</BusinessPartner>

        //<ConfirmationViewSpecification>
        DTConfirmationViewSpecification confirmationViewSpecification = new DTConfirmationViewSpecification();

        //<BusinessProcessChainAssignment>
        DTConfirmationViewSpecification.BusinessProcessChainAssignment businessProcessChainAssignment1 = new DTConfirmationViewSpecification.BusinessProcessChainAssignment();
        businessProcessChainAssignment1.setRequestedIndicator("true");
        confirmationViewSpecification.setBusinessProcessChainAssignment(businessProcessChainAssignment1);
        //</BusinessProcessChainAssignment>

        //<Common>
        DTConfirmationViewSpecification.Common common1 = new DTConfirmationViewSpecification.Common();
        common1.setRequestedIndicator("true");

        //<FormattedDefaultAddress>
        DTConfirmationViewSpecification.Common.FormattedDefaultAddress formattedDefaultAddress = new DTConfirmationViewSpecification.Common.FormattedDefaultAddress();
        formattedDefaultAddress.setRequestedIndicator("true");
        common1.setFormattedDefaultAddress(formattedDefaultAddress);
        //</FormattedDefaultAddress>
        confirmationViewSpecification.setCommon(common1);
        //</Common>

        //<AddressInformation>
        DTConfirmationViewSpecification.AddressInformation addressInformation1 = new DTConfirmationViewSpecification.AddressInformation();
        addressInformation1.setRequestedIndicator("false");

        //<Address>
        DTConfirmationViewSpecification.AddressInformation.Address address1 = new DTConfirmationViewSpecification.AddressInformation.Address();
        address1.setRequestedIndicator("false");

        //<FormattedAddress>
        DTConfirmationViewSpecification.AddressInformation.Address.FormattedAddress formattedAddress = new DTConfirmationViewSpecification.AddressInformation.Address.FormattedAddress();
        formattedAddress.setRequestedIndicator("false");
        address1.setFormattedAddress(formattedAddress);
        //</FormattedAddress>
        //</Address>

        addressInformation1.setAddress(address1);
        //<//AddressInformation>
        confirmationViewSpecification.setAddressInformation(addressInformation1);

        //<CommunicationData>
        DTConfirmationViewSpecification.CommunicationData communicationData = new DTConfirmationViewSpecification.CommunicationData();
        communicationData.setRequestedIndicator("false");

        confirmationViewSpecification.setCommunicationData(communicationData);
        //</CommunicationData>

        //<Role>
        DTConfirmationViewSpecification.Role role1 = new DTConfirmationViewSpecification.Role();
        role1.setRequestedIndicator("true");

        confirmationViewSpecification.setRole(role1);
        //</Role>

        //<BankDetails>
        DTConfirmationViewSpecification.BankDetails bankDetails = new DTConfirmationViewSpecification.BankDetails();
        bankDetails.setRequestedIndicator("false");

        confirmationViewSpecification.setBankDetails(bankDetails);
        //</BankDetails>

        //<PaymentCardDetails>
        DTConfirmationViewSpecification.PaymentCardDetails paymentCardDetails = new DTConfirmationViewSpecification.PaymentCardDetails();
        paymentCardDetails.setRequestedIndicator("false");

        confirmationViewSpecification.setPaymentCardDetails(paymentCardDetails);
        //</PaymentCardDetails>

        //<IndustrySector>
        DTConfirmationViewSpecification.IndustrySector industrySector = new DTConfirmationViewSpecification.IndustrySector();
        industrySector.setRequestedIndicator("false");

        confirmationViewSpecification.setIndustrySector(industrySector);
        //</IndustrySector>

        //<Identification>
        DTConfirmationViewSpecification.Identification identification1 = new DTConfirmationViewSpecification.Identification();
        identification1.setRequestedIndicator("true");

        confirmationViewSpecification.setIdentification(identification1);
        //</Identification>

        //<TaxNumber>
        DTConfirmationViewSpecification.TaxNumber taxNumber = new DTConfirmationViewSpecification.TaxNumber();
        taxNumber.setRequestedIndicator("false");

        confirmationViewSpecification.setTaxNumber(taxNumber);
        //</TaxNumber>

        //<GeneralProductTaxExemption>
        DTConfirmationViewSpecification.GeneralProductTaxExemption generalProductTaxExemption = new DTConfirmationViewSpecification.GeneralProductTaxExemption();
        generalProductTaxExemption.setRequestedIndicator("false");

        confirmationViewSpecification.setGeneralProductTaxExemption(generalProductTaxExemption);
        //</GeneralProductTaxExemption>

        //<TextCollection>
        DTConfirmationViewSpecification.TextCollection textCollection = new DTConfirmationViewSpecification.TextCollection();
        textCollection.setRequestedIndicator("false");

        confirmationViewSpecification.setTextCollection(textCollection);
        //</TextCollection>

        //<CreditRating>
        DTConfirmationViewSpecification.CreditRating creditRating = new DTConfirmationViewSpecification.CreditRating();
        creditRating.setRequestedIndicator("false");

        confirmationViewSpecification.setCreditRating(creditRating);
        //</CreditRating>

        //<CreditStanding>
        DTConfirmationViewSpecification.CreditStanding creditStanding = new DTConfirmationViewSpecification.CreditStanding();
        creditStanding.setRequestedIndicator("false");

        confirmationViewSpecification.setCreditStanding(creditStanding);
        //</CreditStanding>

        //<Occupation>
        DTConfirmationViewSpecification.Occupation occupation = new DTConfirmationViewSpecification.Occupation();
        occupation.setRequestedIndicator("false");

        confirmationViewSpecification.setOccupation(occupation);
        //</Occupation>

        //<FinancialStatement>
        DTConfirmationViewSpecification.FinancialStatement financialStatement = new DTConfirmationViewSpecification.FinancialStatement();
        financialStatement.setRequestedIndicator("false");

        confirmationViewSpecification.setFinancialStatement(financialStatement);
        //</FinancialStatement>

        //<Alias>
        DTConfirmationViewSpecification.Alias alias = new DTConfirmationViewSpecification.Alias();
        alias.setRequestedIndicator("false");

        confirmationViewSpecification.setAlias(alias);
        //</Alias>

        //<TaxCompliance>
        DTConfirmationViewSpecification.TaxCompliance taxCompliance = new DTConfirmationViewSpecification.TaxCompliance();
        taxCompliance.setRequestedIndicator("false");

        confirmationViewSpecification.setTaxCompliance(taxCompliance);
        //</TaxCompliance>

        //<BankRegulations>
        DTConfirmationViewSpecification.BankRegulations bankRegulations = new DTConfirmationViewSpecification.BankRegulations();
        bankRegulations.setRequestedIndicator("false");

        confirmationViewSpecification.setBankRegulations(bankRegulations);
        //</BankRegulations>

        create.setConfirmationViewSpecification(confirmationViewSpecification);
        //</ConfirmationViewSpecification>

        //[</Create>]
        request.setCreate(create);

        //Realizamos la petición
        Object response = getWebServiceTemplate().marshalSendAndReceive(
                creaBpProperties.getUrl(),
                request,
                getrequestCallback());

        //Obtenemos la informacion del Response
        DTCreaBusinessPartnerResp businessPartnerResp = null;

        if (response instanceof JAXBElement){
            JAXBElement<?> jaxbElement = (JAXBElement<?>) response;
            Object value = jaxbElement.getValue();

            if (value instanceof DTCreaBusinessPartnerResp){
                businessPartnerResp = (DTCreaBusinessPartnerResp) value;
            }else {
                throw new ClassCastException("El valor no es una instancia de DTCreaBusinessPartnerResp");
            }
        }else {
            throw new ClassCastException("response no es una instancia de JAXBElement");
        }
        return businessPartnerResp;
    }

    /**
     * Descipcion: Metodo para agregar la información Check_lists de la peticion
     * @parm idNumber Numero de documento del cliente
     * @return DTCheckListsReq Retorna un objeto de tipo DTCheckListsReq
     * */
    public DTCheckListsReq dtCheckListsReq(DatosBpDto datosBpDto){
        //Check_lists
        DTCheckListsReq dtCheckListsReq = new DTCheckListsReq();
        dtCheckListsReq.setIdType(getTipoIdentidicacion(datosBpDto.getTipoIdentificador()));
        System.out.println("Tipo Identificador: " + datosBpDto.getTipoIdentificador() + " -> " + getTipoIdentidicacion(datosBpDto.getTipoIdentificador()));
        System.out.println("Identificador: " + datosBpDto.getIdentificador());
        System.out.println("Pimer nombre: " + datosBpDto.getPrimerNombre());
        System.out.println("Segundo nombre: " + datosBpDto.getSegundoNombre());
        System.out.println("Primer apellido: " + datosBpDto.getPrimerApellido());
        System.out.println("Segundo apellido: " + datosBpDto.getSegundoApellido());
        System.out.println("Genero: " + datosBpDto.getGenero() + " -> " + getTipoGenero(datosBpDto));
        dtCheckListsReq.setIdNumber(datosBpDto.getIdentificador()); // Ejemplo -> "0122334455699"
        dtCheckListsReq.setIdNatio("HN");

        return dtCheckListsReq;
    }

    /**
     * Descipcion: Metodo para agregar la información Check_lists de la peticion
     * @parm idNumber Numero de documento del cliente
     * @return DTCheckListsReq Retorna un objeto de tipo DTCheckListsReq
     * */
    public DTCheckListsReq dtCheckListsReq(String identificador){
        //Check_lists
        DTCheckListsReq dtCheckListsReq = new DTCheckListsReq();
        dtCheckListsReq.setIdType("HN01");
        dtCheckListsReq.setIdNumber(identificador); // Ejemplo -> "0122334455699"
        dtCheckListsReq.setIdNatio("HN");

        return dtCheckListsReq;
    }

    /**
     * Descripcion: Metodo que mapea el tipo de identificacion que se recibe de JTELLER.
     * @parm tipoIdTemp Tipo de indentificacion temporal. Este valor viene de JTELLER.
     * @return String Retorna una cadena que contiene el tipo de identificacion que espera SAP.
     * */
    public String getTipoIdentidicacion(String tipoIdTemp){
        String tipoIdentificacion = "PAS";

        if (tipoIdTemp.equalsIgnoreCase("GOV")){
            tipoIdentificacion = "HN01";
        } else if (tipoIdTemp.equalsIgnoreCase("RES")) {
            tipoIdentificacion = "HN03";
        }
        return tipoIdentificacion;
    }

    /**
     * Descripcion: Metodo que arma el nombre completo del beneficio que viene de JTELLER.
     * @param datosBpDto Variable de tipo DatosBpDto, clase que contiene la informacion del beneficiario.
     * @return String Retrona un String con el nombre completo del beneficiario.
     * */
    public String getNombreBeneficiario(DatosBpDto datosBpDto){
        String nombreCompleto = "";
        if (!datosBpDto.getPrimerNombre().isEmpty()){
            nombreCompleto = datosBpDto.getPrimerNombre();
        }

        if (!datosBpDto.getSegundoNombre().isEmpty()){
            nombreCompleto = nombreCompleto + " " +datosBpDto.getSegundoNombre();
        }

        if (!datosBpDto.getPrimerApellido().isEmpty()){
            nombreCompleto = nombreCompleto + " " + datosBpDto.getPrimerApellido();
        }

        if (!datosBpDto.getSegundoApellido().isEmpty()){
            nombreCompleto = nombreCompleto + " " + datosBpDto.getSegundoApellido();
        }

        return nombreCompleto;
    }

    /**
     * @description Método que mapea el tipo de género que se recibe de JTELLER para transformarlo
     * al tipo de género que espera SAP.
     * @param datosBpDto Variable de tipo DatosBpDto, clase que contiene la información del beneficiario.
     * @return String Retorna un String con el tipo de género que espera SAP.
     * */
    public String getTipoGenero(DatosBpDto datosBpDto){
        String tipoGenero = "";
        if (!datosBpDto.getGenero().isEmpty() && datosBpDto.getGenero().equalsIgnoreCase("M")){
            tipoGenero = "1"; // 1 = Masculino
        } else if (!datosBpDto.getGenero().isEmpty() && datosBpDto.getGenero().equalsIgnoreCase("F")) {
            tipoGenero = "2"; // 2 = Femenino
        }
        return tipoGenero;
    }

    /**
     * @description: Método que valida el código ISO3 que viene de JTELLER para hacer el mapeo a ISO2.
     * @param nacionalidad Variable que contiene el código de nacionaliad en formato ISO3.
     * @return String Retorna un String con el valor ISO2 del código país.
     * */
    public String getNacionalidad(String nacionalidad){
        String iso2  = "";
        if (!nacionalidad.isEmpty()){
            iso2 = codigoPaisIso.getDictionary().get(nacionalidad.trim());
        }
        return iso2;
    }

}
