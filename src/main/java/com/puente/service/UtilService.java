package com.puente.service;

import com.puente.client.SrvBasa010Client;
import com.puente.persistence.entity.MessageCodesEntity;
import com.puente.persistence.entity.ParametroRemesadoraEntity;
import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.service.dto.RemittanceAlgorithmDto;
import com.puente.service.dto.ResponseDto;
import com.puente.service.dto.ComparacionNombreDto;
import com.puente.service.dto.CredencialesDto;
import com.puente.web.config.MyProperties;
import com.puente.service.dto.ResponseGetRemittanceDataDto;
import com.soap.wsdl.ServicioSrvBasa003.EjecutarSrvBasa003Response;
import com.soap.wsdl.ServicioSrvBasa010.EjecutarSrvBasa010Response;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.Common;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.Common.Person;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.Common.Person.Name;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.Identification;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.AddressInformation;
import com.soap.wsdl.serviceBP.DTDataBusinessPartnerResp.AddressInformation.AddressUsage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

@Service
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UtilService {
    private static final String VAR_ERROR = "Error";
    private static final String VAR_BUS_MSG = "000006";
    private static final String VAR_BUS_BTS = "000016";

    private static final String VAR_FALSE1 = "false";
    private static final String VAR_FALSE2 = "false";
    private static final String VAR_FALSE3 = "false";
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(UtilService.class);
    @Autowired
    private MessageCodesService messageCodesService;
    private ComparacionNombreDto comparacionNombreDto;
    @Autowired
    private ParametroRemesadoraService parametroRemesadoraService;
    @Autowired
    private MyProperties myProperties;
    @Autowired
    ValoresGlobalesRemesasService valoresGlobalesRemesasService;
    @Autowired
    private static SrvBasa002Service srvBasa002Service;
    @Autowired
    SrvBasa003Service srvBasa003Service;
    @Autowired
    private static SrvBasa010Service srvBasa010Service;
    @Autowired
    private SrvBasa010Client srvBasa010Client;

    public Boolean isResponseSuccess(ResponseDto servicesResponse) {
        return servicesResponse.getCode().equals("000000");
    }

    public Boolean existBp(DTCreaBusinessPartnerResp bpInfo) {
        DTDataBusinessPartnerResp businessPartnerResp = bpInfo.getConsultation().getBusinessPartner();
        String internalID = businessPartnerResp.getInternalID();
        String givenName = Optional.of(businessPartnerResp)
            .map(DTDataBusinessPartnerResp::getCommon)
            .map(Common::getPerson)
            .map(Person::getName)
            .map(Name::getGivenName)
            .orElse(null);
        return internalID != null && givenName != null;
    }

    public Person getBpBeneficiaryInfo(DTCreaBusinessPartnerResp bpInfo) {
        DTDataBusinessPartnerResp businessPartnerResp = bpInfo.getConsultation().getBusinessPartner();
        return Optional.of(businessPartnerResp)
            .map(DTDataBusinessPartnerResp::getCommon)
            .map(Common::getPerson)
            .orElse(null);
    }

    public Identification getBpIdentificationInfo(DTCreaBusinessPartnerResp bpInfo) {
        DTDataBusinessPartnerResp businessPartnerResp = bpInfo.getConsultation().getBusinessPartner();
        return businessPartnerResp.getIdentification().stream().findFirst().orElse(null);
    }

    public AddressUsage getBpAddressInfo(DTCreaBusinessPartnerResp bpInfo) {
        DTDataBusinessPartnerResp businessPartnerResp = bpInfo.getConsultation().getBusinessPartner();
        AddressInformation addressInformation = businessPartnerResp.getAddressInformation().stream().findFirst().orElse(null);
        return addressInformation != null ? addressInformation.getAddressUsage().stream().findFirst().orElse(null) : null;
    }

    public String getFullName(Person beneficiaryInfo) {
        Name names = beneficiaryInfo.getName();
        ArrayList<String> namesList = new ArrayList<>();
        if(names.getGivenName() != null && !names.getGivenName().trim().isEmpty()) {
            namesList.add(names.getGivenName());
        }
        if(names.getMiddleName() != null && !names.getMiddleName().trim().isEmpty()) {
            namesList.add(names.getMiddleName());
        }
        if(names.getFamilyName() != null && !names.getFamilyName().trim().isEmpty()) {
            namesList.add(names.getFamilyName());
        }
        if(names.getAdditionalFamilyName() != null && !names.getAdditionalFamilyName().trim().isEmpty()) {
            namesList.add(names.getAdditionalFamilyName());
        }
        return String.join(" ", namesList);
    }

    public String getPaymentType(
            String paymentMethod
    ) {
        Map<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("01", "CASH");
        keyValueMap.put("02", "DEPOSIT_ACCOUNT");
        return keyValueMap.get(paymentMethod) == null ? "CASH" : keyValueMap.get(paymentMethod);
    }

    public XMLGregorianCalendar getXmlFormattedDate(String date) throws DatatypeConfigurationException {
        try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        GregorianCalendar gregorianCalendar = GregorianCalendar.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (Exception e) {
            return null;
        }
    }

    // Consultar mensaje de de la base de datos si no se le envia el type lo devuelve como Error
    public ResponseGetRemittanceDataDto getCustomMessageCode(String code) {
        return this.getCustomMessageCode(code, VAR_ERROR);
    }

    // Consultar mensaje de de la base de datos con type personalizado
    public ResponseGetRemittanceDataDto getCustomMessageCode(String code, String type) {
        ResponseGetRemittanceDataDto messageCode = new ResponseGetRemittanceDataDto();
        messageCode.setCode(code);
        return this.getFormattedMessageCode(messageCode, type);
    }

    public ResponseGetRemittanceDataDto getWsdlMessageCode(ResponseDto responseWsdl) {
        ResponseGetRemittanceDataDto messageCode = new ResponseGetRemittanceDataDto();
        messageCode.setCode(responseWsdl.getCode());
        messageCode.setMessage(responseWsdl.getMessage());
        return this.getFormattedMessageCode(messageCode, VAR_ERROR);
    }

    public ResponseGetRemittanceDataDto getWsdlMessageCode(ResponseDto responseWsdl, String type) {
        ResponseGetRemittanceDataDto messageCode = new ResponseGetRemittanceDataDto();
        messageCode.setCode(responseWsdl.getCode());
        messageCode.setMessage(responseWsdl.getMessage());
        messageCode.setType(type);
        return this.getFormattedMessageCode(messageCode, VAR_ERROR);
    }

    public ResponseGetRemittanceDataDto getFormattedMessageCode(
            ResponseGetRemittanceDataDto servicesResponse,
            String type
    ) {
        // Se busca por el campo  MESSAGE_CODE code en la tabla de MESSAGE_CODES
        MessageCodesEntity messageCode = messageCodesService.get(servicesResponse.getCode());
        ResponseGetRemittanceDataDto formattedResponse = new ResponseGetRemittanceDataDto();
        if (messageCode == null || messageCode.getMessage() == null) {
            formattedResponse.setType(type);
            formattedResponse.setMessage(servicesResponse.getMessage());
            formattedResponse.setCode(servicesResponse.getCode());
        } else {
            formattedResponse.setType(type);
            formattedResponse.setMessage(messageCode.getMessage());
            formattedResponse.setCode(messageCode.getCode());
        }
        // Technical message
        this.setTechnicalMessageLog(messageCode);
        return formattedResponse;
    }

    public void getCustomTechnicalMessage(String code) {
        MessageCodesEntity messageCode = messageCodesService.get(code);
        this.setTechnicalMessageLog(messageCode);
    }

    public void setTechnicalMessageLog(MessageCodesEntity messageCode) {
        if(messageCode != null) {
            if(messageCode.getTechnicalMessage() != null) {
                log.error(messageCode.getTechnicalMessage());
            } else if(messageCode.getMessage() != null) {
                log.error(messageCode.getMessage());
            }
            // Agregar registro en la tabla de LOG
        }
    }

    public ResponseGetRemittanceDataDto getExceptionMessageCode(Exception e) {
        ResponseGetRemittanceDataDto responseDto = new ResponseGetRemittanceDataDto();
        responseDto.setCode("Exception");
        responseDto.setMessage(e.getMessage());
        return this.getFormattedMessageCode(responseDto, VAR_ERROR);
    }


    public RemittanceAlgorithmDto consultaRemesadora(String remesa) {
        int cantidad = 0;
        String rem = "";
        RemittanceAlgorithmDto resp = new RemittanceAlgorithmDto();
        ParametroRemesadoraEntity parametro;
        List<ParametroRemesadoraEntity> parametros = parametroRemesadoraService.getAll();
        if (remesa == null) {
            throw new IllegalArgumentException("Remesa no puede ser null");
        } else {
            cantidad = remesa.trim().length();
            parametro = getParametrosMrecod(parametros, myProperties.getNumeroMg());
            rem = busMg(cantidad, remesa, parametro);
            //MoneyGram aun no se implementa
            if (rem.isEmpty()) {
                parametro = getParametrosMrecod(parametros, myProperties.getNumeroVigo());
                rem = busVigo(cantidad, remesa, parametro);
                if (rem.isEmpty()) {
                    parametro = getParametrosMrecod(parametros, myProperties.getNumeroUni());
                    rem = busUni(cantidad, remesa, parametro);
                    if (rem.isEmpty()) {
                        parametro = getParametrosMrecod(parametros, myProperties.getNumeroRia());
                        rem = busRia(cantidad, remesa, parametro);
                        if (rem.isEmpty()) {
                            parametro = getParametrosMrecod(parametros, myProperties.getNumeroBts());
                            rem = busBTS(cantidad, remesa, parametro);
                        }
                    }
                }
            }
        }
        if (rem.isEmpty()) {
            resp.setMessage(VAR_FALSE1);
            resp.setMrecod(VAR_FALSE2);
        }else{
            resp.setMessage(VAR_FALSE3);
            if(!rem.equals(VAR_BUS_MSG)) {
                resp.setMessage("true");
            }
            resp.setMrecod(rem);
            resp.setDatosExtras("true");
        }

        return resp;
    }

    public RemittanceAlgorithmDto validaRemBusBTS(RemittanceAlgorithmDto resp, String rem){
        if (rem.isEmpty()) {
            resp.setMessage(VAR_FALSE1);
            resp.setMrecod(VAR_FALSE2);
        }else{
            resp.setMessage("true");
            resp.setMrecod(rem);
        }
        return resp;
    }


    private ParametroRemesadoraEntity getParametrosMrecod(List<ParametroRemesadoraEntity> parametros, String mrecod) {
        ParametroRemesadoraEntity lista = new ParametroRemesadoraEntity();
        for (ParametroRemesadoraEntity parametro : parametros) {
            if (parametro.getMrecod().equals(mrecod)) {
                lista.setMrecod(parametro.getMrecod());
                lista.setCorval(parametro.getCorval());
                lista.setLongMax(parametro.getLongMax());
                lista.setLongMin(parametro.getLongMin());
                lista.setInicio(parametro.getInicio());
                lista.setTipoDato(parametro.getTipoDato());
            }
        }
        return lista;
    }

    private String busMg(int cantidad, String argumento, ParametroRemesadoraEntity parametro) {
        int mrelma = Integer.parseInt(parametro.getLongMax());
        String mretda = parametro.getTipoDato();

        if (cantidad == mrelma) {
            if (mretda.equals("N")) {
                if (validaNumerico(argumento, cantidad, parametro.getLongMin())) {
                    return VAR_BUS_MSG;
                }
            } else {
                return VAR_BUS_MSG;
            }
        }
        return "";
    }

    private String busVigo(int cantidad, String argumento, ParametroRemesadoraEntity parametro) {
        int mrelma = Integer.parseInt(parametro.getLongMax());
        String mretda = parametro.getTipoDato();
        String mreini = parametro.getInicio();


        if (cantidad == mrelma && ((mreini + "72").equals(argumento.substring(0, 3)) || (mreini + "73").equals(argumento.substring(0, 3)))) {
                if (mretda.equals("N")) {
                    if (validaNumerico(argumento, cantidad, parametro.getLongMin())) {
                        return "000004";
                    }
                } else {
                    return "000004";
                }
            }

        return "";
    }

    private String busUni(int cantidad, String argumento, ParametroRemesadoraEntity parametro) {
        int mrelma = Integer.parseInt(parametro.getLongMax());
        String mretda = parametro.getTipoDato();
        String mreini = parametro.getInicio();


        if (cantidad == mrelma && mreini.equals(String.valueOf(argumento.charAt(0)))) {
                if (mretda.equals("N")) {
                    if (validaNumerico(argumento, cantidad, parametro.getLongMin())) {
                        return "000007";
                    }
                } else {
                    return "000007";
                }
            }

        return "";
    }

    private String busRia(int cantidad, String argumento, ParametroRemesadoraEntity parametro) {
        int mrelma = Integer.parseInt(parametro.getLongMax());
        String mretda = parametro.getTipoDato();
        String mreini = parametro.getInicio();

        List<ValoresGlobalesRemesasEntity> lista = valoresGlobalesRemesasService.getListByItem("000018NC");
        String numCorrelativo1 = lista.get(0).getValor();
        String numCorrelativo2 = lista.get(1).getValor();


        if ((cantidad == mrelma) && ((mreini + numCorrelativo1).equals(argumento.substring(0, 2)) || (mreini + numCorrelativo2).equals(argumento.substring(0, 2))) && mretda.equals("N") && validaNumerico(argumento, cantidad, parametro.getLongMin())) {
                        return "000018";
                    }


        return "";
    }

    private String busBTS(int cantidad, String argumento, ParametroRemesadoraEntity parametro) {
        int mrelma = Integer.parseInt(parametro.getLongMax());


        if (cantidad == mrelma && validaNumerico(argumento, cantidad, parametro.getLongMin())) {
                boolean banBts = pVerBTS(argumento);
                if (banBts) {
                    return VAR_BUS_BTS;
                }
            }

        return "";
    }

    private boolean validaNumerico(String argumento, int cantidad, String minimo) {
        boolean ban = true;
        if (argumento.length() == Integer.parseInt(minimo)) {
            for (int i = 0; i < cantidad; i++) {
                char c = argumento.charAt(i);
                if (c < '0' || c > '9') {
                    ban = false;
                }
            }
        }
        return ban;
    }

    public boolean pVerBTS(String remCod) {
        String mrEcod = VAR_BUS_BTS;
        if (remCod.length() != 11 && remCod.length() != 10) {
            return false;
        }

        if (remCod.length() == 11) {
            try {
                int tot = 0;
                int[] factors = {2, 1, 2, 1, 2, 1, 2};
                for (int i = 0; i < 7; i++) {
                    int digit = Character.getNumericValue(remCod.charAt(3 + i));
                    int product = digit * factors[i];
                    tot += (product > 9) ? product / 10 + product % 10 : product;
                }
                int checkDigit = Character.getNumericValue(remCod.charAt(10));
                int remainder = (tot % 10 == 0) ? 0 : 10 - tot % 10;
                if (remainder != checkDigit) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return remCod.length() == 10 || VAR_BUS_BTS.equals(mrEcod);
    }

    //Correcion de nombre
    public static String normalize(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase().replaceAll("\\s+", " ").trim();
    }

    //Jaccard
    public static double calculateJaccardSimilarity(String s1, String s2, int n) {
        Set<String> nGrams1 = generateNGrams(s1, n);
        Set<String> nGrams2 = generateNGrams(s2, n);

        Set<String> intersection = new HashSet<>(nGrams1);
        intersection.retainAll(nGrams2);

        Set<String> union = new HashSet<>(nGrams1);
        union.addAll(nGrams2);

        return (double) intersection.size() / union.size();
    }

    //Jaccard
    private static Set<String> generateNGrams(String s, int n) {
        Set<String> nGrams = new HashSet<>();
        for (int i = 0; i <= s.length() - n; i++) {
            nGrams.add(s.substring(i, i + n));
        }
        return nGrams;
    }

    //JaroWinkler
    public static double jaroWinklerSimilarity(String s1, String s2) {
        if ((s1 == null || s1.isEmpty()) && (s2 == null || s2.isEmpty())) return 1.0;

        if ((s1 == null || s1.isEmpty()) || (s2 == null || s2.isEmpty())) return 0.0;
        s1 = normalize(s1);
        s2 = normalize(s2);
        int s1_len = s1.length();
        int s2_len = s2.length();
        int match_distance = Integer.max(s1_len, s2_len) / 2 - 1;

        boolean[] s1_matches = new boolean[s1_len];
        boolean[] s2_matches = new boolean[s2_len];

        int matches = 0;
        for (int i = 0; i < s1_len; i++) {
            int start = Integer.max(0, i - match_distance);
            int end = Integer.min(i + match_distance + 1, s2_len);

            for (int j = start; j < end; j++) {
                if (s2_matches[j]) continue;
                if (s1.charAt(i) != s2.charAt(j)) continue;
                s1_matches[i] = true;
                s2_matches[j] = true;
                matches++;
                break;
            }
        }

        if (matches == 0) return 0.0;

        double t = 0;
        int k = 0;
        for (int i = 0; i < s1_len; i++) {
            if (!s1_matches[i]) continue;
            while (!s2_matches[k]) k++;
            if (s1.charAt(i) != s2.charAt(k)) t++;
            k++;
        }

        double m = matches;
        double jaro = ((m / s1_len) + (m / s2_len) + ((m - t / 2) / m)) / 3;
        double p = 0.1;
        int l = 0;

        while (l < Integer.min(s1_len, s2_len) && s1.charAt(l) == s2.charAt(l) && l < 4) l++;

        return jaro + l * p * (1 - jaro);
    }

    //needlemanWunsch
    public static double needlemanWunsch(String s1, String s2, int MATCH_SCORE, int MISMATCH_PENALTY, int GAP_PENALTY) {
        int len1 = s1.length();
        int len2 = s2.length();

        int[][] scoreMatrix = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            scoreMatrix[i][0] = i * GAP_PENALTY;
        }

        for (int j = 0; j <= len2; j++) {
            scoreMatrix[0][j] = j * GAP_PENALTY;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int match = scoreMatrix[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? MATCH_SCORE : MISMATCH_PENALTY);
                int delete = scoreMatrix[i - 1][j] + GAP_PENALTY;
                int insert = scoreMatrix[i][j - 1] + GAP_PENALTY;
                scoreMatrix[i][j] = Math.max(match, Math.max(delete, insert));
            }
        }

        int maxScore = Math.max(len1, len2) * MATCH_SCORE;
        return (double) scoreMatrix[len1][len2] / maxScore;
    }

    public boolean compararNombre(ComparacionNombreDto comparacionNombreDto,String mrecod) {
        ValoresGlobalesRemesasEntity valoresGlobalesRemesas = valoresGlobalesRemesasService.findByCodeAndItem("ComparacionNombres", "algoritmo");
        boolean canCharge = false;
        if (valoresGlobalesRemesas.getValor().equals("1")) {
            canCharge = calcularSimilaridad(comparacionNombreDto,mrecod);
        } else if (valoresGlobalesRemesas.getValor().equals("2")) {
            canCharge = compararNombreSiremu(comparacionNombreDto,mrecod);
        }else {
            canCharge = true;
        }

        return canCharge;
    }

    public boolean calcularSimilaridad(ComparacionNombreDto comparacionNombreDto,String mrecod) {

        ValoresGlobalesRemesasEntity valoresGlobalesRemesas2 = valoresGlobalesRemesasService.findByCodeAndItem(mrecod, "remesadora");
        double umbral = Double.parseDouble(valoresGlobalesRemesas2.getValor());
        boolean canCharge = false;
        double similitudTotal = 0;

        double similitudBp1 = levenshtein(comparacionNombreDto.getBeneficiarioBp().getPrimerNombreBp(), comparacionNombreDto.getBeneficiarioSireon().getPrimerNombreSireon());
        double similutudS1 = jaroWinklerSimilarity(comparacionNombreDto.getBeneficiarioBp().getPrimerNombreBp(), comparacionNombreDto.getBeneficiarioSireon().getPrimerNombreSireon());

        double similitudBp2 = levenshtein(comparacionNombreDto.getBeneficiarioBp().getSegundoNombreBp(), comparacionNombreDto.getBeneficiarioSireon().getSegundoNombreSireon());
        double similutudS2 = jaroWinklerSimilarity(comparacionNombreDto.getBeneficiarioBp().getSegundoNombreBp(), comparacionNombreDto.getBeneficiarioSireon().getSegundoNombreSireon());

        double similitudBp3 = levenshtein(comparacionNombreDto.getBeneficiarioBp().getPrimerApellidoBp(), comparacionNombreDto.getBeneficiarioSireon().getPrimerApellidoSireon());
        double similutudS3 = jaroWinklerSimilarity(comparacionNombreDto.getBeneficiarioBp().getPrimerApellidoBp(), comparacionNombreDto.getBeneficiarioSireon().getPrimerApellidoSireon());

        double similitudBp4 = levenshtein(comparacionNombreDto.getBeneficiarioBp().getSegundoApellidoBp(), comparacionNombreDto.getBeneficiarioSireon().getSegundoApellidoSireon());
        double similutudS4 = jaroWinklerSimilarity(comparacionNombreDto.getBeneficiarioBp().getSegundoApellidoBp(), comparacionNombreDto.getBeneficiarioSireon().getSegundoApellidoSireon());


        similitudTotal = (( similitudBp1 + similutudS1 + similitudBp2 + similutudS2 + similitudBp3 + similutudS3 + similitudBp4 + similutudS4)*100) / 8;

        if (similitudTotal >= umbral) {
            canCharge= true;
        }

        return canCharge;
    }

    //Levenshtein
    public static double calculaNombreSpearados(String name1, String name2) {
        String[] partes1 = normalize(name1).split(" ");
        String[] partse2 = normalize(name2).split(" ");

        double total = 0;
        int resultaComparaciones = Math.min(partes1.length, partse2.length);

        for (int i = 0; i < resultaComparaciones; i++) {
            total += levenshtein(partes1[i], partse2[i]);
        }

        return total / resultaComparaciones;
    }

    //Levenshtein
    public static int computeLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + cost
                    );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    //Levenshtein
    public static double levenshtein(String s1, String s2) {

        if ((s1 == null || s1.isEmpty()) && (s2 == null || s2.isEmpty())) return 1.0;
        if ((s1 == null || s1.isEmpty()) || (s2 == null || s2.isEmpty())) return 0.0;
        int maxLen = Math.max(s1.length(), s2.length());

        s1 = normalize(s1);
        s2 = normalize(s2);
        return (1.0 - ((double) computeLevenshteinDistance(s1, s2) / maxLen));
    }

    public boolean compararNombreSiremu(ComparacionNombreDto comparacionNombreDto, String mrecod) {

        //Normalizar los nomrebres
        String pnBp = (comparacionNombreDto.getBeneficiarioBp().getPrimerNombreBp()).trim();
        String pnS = (comparacionNombreDto.getBeneficiarioSireon().getPrimerNombreSireon()).trim();
        String snBp = (comparacionNombreDto.getBeneficiarioBp().getSegundoNombreBp()).trim();
        String snS = (comparacionNombreDto.getBeneficiarioSireon().getSegundoNombreSireon()).trim();
        String paBp = (comparacionNombreDto.getBeneficiarioBp().getPrimerApellidoBp()).trim();
        String paS = (comparacionNombreDto.getBeneficiarioSireon().getPrimerApellidoSireon()).trim();
        String saBp = (comparacionNombreDto.getBeneficiarioBp().getSegundoApellidoBp()).trim();
        String saS = (comparacionNombreDto.getBeneficiarioSireon().getSegundoApellidoSireon()).trim();

        ComparacionNombreDto nombreNormalizado = new ComparacionNombreDto();
        ComparacionNombreDto.beneficiarioBp beneficiarioBp = new ComparacionNombreDto.beneficiarioBp();
        ComparacionNombreDto.beneficiarioSireon beneficiarioSireon = new ComparacionNombreDto.beneficiarioSireon();

        beneficiarioBp.setPrimerNombreBp(pnBp);
        beneficiarioBp.setSegundoNombreBp(snBp);
        beneficiarioBp.setPrimerApellidoBp(paBp);
        beneficiarioBp.setSegundoApellidoBp(saBp);

        beneficiarioSireon.setPrimerNombreSireon(pnS);
        beneficiarioSireon.setSegundoNombreSireon(snS);
        beneficiarioSireon.setPrimerApellidoSireon(paS);
        beneficiarioSireon.setSegundoApellidoSireon(saS);

        nombreNormalizado.setBeneficiarioBp(beneficiarioBp);
        nombreNormalizado.setBeneficiarioSireon(beneficiarioSireon);

        String nombreBen = (pnBp + " " + snBp + " " + paBp + " " + saBp).trim();
        String nombreBeneficiario = (pnS + " " + snS + " " + paS + " " + saS).trim();
        ValoresGlobalesRemesasEntity valoresGlobalesRemesas2 = valoresGlobalesRemesasService.findByCodeAndItem(mrecod, "remesadora");
        Double umbral = Double.parseDouble(valoresGlobalesRemesas2.getValor());
        if (!nombreBen.equals(nombreBeneficiario)) {
            if (!nombreBen.trim().isEmpty() && !nombreBeneficiario.trim().isEmpty()) {
                boolean valNom = validaNombreBp(beneficiarioBp, beneficiarioSireon,umbral);
                if (!valNom) {
                    valNom = validaNombreSireon(beneficiarioSireon, beneficiarioBp,umbral);
                    return valNom;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }

        return true;
    }

    private static boolean validaNombreBp(ComparacionNombreDto.beneficiarioBp beneficiarioBp, ComparacionNombreDto.beneficiarioSireon beneficiarioSireon,Double umbral) {
        int porcentaje = 0;
        String[] nombresBp = {beneficiarioBp.getPrimerNombreBp(), beneficiarioBp.getSegundoNombreBp(), beneficiarioBp.getPrimerApellidoBp(), beneficiarioBp.getSegundoApellidoBp()};
        String[] nombreSireon = {beneficiarioSireon.getPrimerNombreSireon(), beneficiarioSireon.getSegundoNombreSireon(), beneficiarioSireon.getPrimerApellidoSireon(),
                beneficiarioSireon.getSegundoApellidoSireon()};
        for(String nombreBp : nombresBp){

            porcentaje += calcularPorcentaje(nombreBp, nombreSireon);
        }
        return porcentaje >= umbral;
    }

    private static boolean validaNombreSireon(ComparacionNombreDto.beneficiarioSireon beneficiarioSireon, ComparacionNombreDto.beneficiarioBp beneficiarioBp,Double umbral) {
        int porcentaje = 0;
        String[] nombresBp = {beneficiarioBp.getPrimerNombreBp(), beneficiarioBp.getSegundoNombreBp(), beneficiarioBp.getPrimerApellidoBp(), beneficiarioBp.getSegundoApellidoBp()};
        String[] nombresSireon = {beneficiarioSireon.getPrimerNombreSireon(), beneficiarioSireon.getSegundoNombreSireon(), beneficiarioSireon.getPrimerApellidoSireon(),
                beneficiarioSireon.getSegundoApellidoSireon()};
        for(String nombreSireon : nombresSireon){
            porcentaje += calcularPorcentaje(nombreSireon, nombresBp);
        }
        return porcentaje >= umbral;

    }

    private static int calcularPorcentaje(String nombre, String[] nombres) {
        for (String n : nombres) {
            if (nombre.equals(n) && !nombre.equals("X") && !nombre.isEmpty()) {
                return 25;
            }
        }
        return 0;
    }

    public boolean validaAgenciaSucursal(String cuenta)
    {
        EjecutarSrvBasa003Response ejecutarSrvBasa003Response = srvBasa003Service.getInfoCuenta(cuenta);
        if (!ejecutarSrvBasa003Response.getRespuestaSrvBasa003().getCodigoMensaje().equals("00")) {
            return false;
        }
        String agencia = ejecutarSrvBasa003Response.getRespuestaSrvBasa003().getColeccionCuenta().get(0).getCodigoSucursal();
        EjecutarSrvBasa010Response ejecutarSrvBasa010Response = srvBasa010Client.getResponse010(agencia,"*");
        if (ejecutarSrvBasa010Response.getRespuestaSrvBasa010().getCodigoMensaje().isEmpty() &&
                (ejecutarSrvBasa010Response.getRespuestaSrvBasa010().getCodigoMensaje() == null)
        ) {
            return false;
        }
        return ejecutarSrvBasa010Response.getRespuestaSrvBasa010().getCodigoMensaje().equals("00");
    }

    public CredencialesDto getCredenciales(String codCanal) {
        CredencialesDto credencialesDto = new CredencialesDto();
        credencialesDto.setToken("");
        switch (codCanal) {
            case "0002":
                credencialesDto.setUser(myProperties.getServicesUserJteller());
                credencialesDto.setPassword(myProperties.getServicesPasswordJteller());
                break;
            case "0003":
                credencialesDto.setUser(myProperties.getServicesUserOcb());
                credencialesDto.setPassword(myProperties.getServicesPasswordOcb());
                break;
            case "0004":
                credencialesDto.setUser(myProperties.getServicesUserAbas());
                credencialesDto.setPassword(myProperties.getServicesPasswordAbas());
                break;
            case "0005":
                credencialesDto.setUser(myProperties.getServicesUserAbi());
                credencialesDto.setPassword(myProperties.getServicesPasswordAbi());
                break;
            case "0006":
                credencialesDto.setUser(myProperties.getServicesUserDilo());
                credencialesDto.setPassword(myProperties.getServicesPasswordDilo());
                break;
            default:
                break;
        }
        return credencialesDto;
    }
}
