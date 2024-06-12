package com.puente.service;

import com.puente.persistence.entity.MessageCodesEntity;
import com.puente.service.dto.ResponseDto;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.HashSet;
import java.util.Set;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ToString
@NoArgsConstructor
public class UtilService {
    @Autowired
    private MessageCodesService messageCodesService;

    public Boolean isResponseSuccess(ResponseDto servicesResponse) {
        return servicesResponse.getMessageCode().equals("000000");
    }

    public ResponseDto getFormattedMessageCode(ResponseDto servicesResponse) {
        MessageCodesEntity messageCode = messageCodesService.get(servicesResponse.getMessageCode());
        ResponseDto formattedResponse = new ResponseDto();
        if(messageCode == null) {
            formattedResponse.setMessage(servicesResponse.getMessage());
            formattedResponse.setMessageCode(servicesResponse.getMessageCode());
        } else {
            formattedResponse.setMessage(messageCode.getMessage());
            formattedResponse.setMessageCode(messageCode.getCode());
        }
        return formattedResponse;
    }

    public ResponseDto getExceptionMessageCode(Exception e) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessageCode("03");
        responseDto.setMessage(e.getMessage());
        return this.getFormattedMessageCode(responseDto);
    }

    //private static final Logger log = LoggerFactory.getLogger(UtilServices.class);
    public String ConsultaRemesadora(String remesa){
        int cantidad = 0;
        int numerico = 0;
        String rem = "";

        if (remesa == null){
            throw new IllegalArgumentException("Remesa no puede ser null");
        }
        else {
            cantidad = remesa.trim().length();
            rem = busMg(cantidad, remesa);
            if (rem.isEmpty()) {
                rem = busVigo(cantidad, remesa);
                if (rem.isEmpty()) {
                    rem = busUni(cantidad, remesa);
                    if (rem.isEmpty()) {
                        rem = busRia(cantidad, remesa);
                        if (rem.isEmpty()) {
                            rem = busBTS(cantidad, remesa);
                            if (rem.isEmpty()) {
                              rem = "error";
                            }
                        }
                    }
                }
            }
        }
        return rem;
    }

    private String busMg(int cantidad, String argumento) {
        int MRELMA = 8;
        char MRETDA = 'N';

        if (cantidad == MRELMA) {
            if (MRETDA == 'N') {
                if (validaNumerico(argumento, cantidad)) {
                    return "000006";
                }
            } else {
                return "000006";
            }
        }
        return "";
    }

    private String busVigo(int cantidad, String argumento) {
        int MRELMA = 10;
        char MRETDA = 'N';
        String MREINI = "9";

        if (cantidad == MRELMA) {
            if ((MREINI + "72").equals(argumento.substring(0, 3)) || (MREINI + "73").equals(argumento.substring(0, 3))) {
                if (MRETDA == 'N') {
                    if (validaNumerico(argumento, cantidad)) {
                        return "000004";
                    }
                } else {
                    return "000004";
                }
            }
        }
        return "";
    }

    private String busUni(int cantidad, String argumento) {
        int MRELMA = 10;
        char MRETDA = 'N';
        String MREINI = "5";

        if (cantidad == MRELMA) {
            if (MREINI.equals(String.valueOf(argumento.charAt(0)))) {
                if (MRETDA == 'N') {
                    if (validaNumerico(argumento, cantidad)) {
                        return "000007";
                    }
                } else {
                    return "000007";
                }
            }
        }
        return "";
    }

    private String busRia(int cantidad, String argumento) {
        int MRELMA = 11;
        char MRETDA = 'N';
        String MREINI = "1";

        String numCorrelativo1 = "2";
        String numCorrelativo2 = "3";

        if (cantidad == MRELMA) {
            if ((MREINI + numCorrelativo1).equals(argumento.substring(0, 2)) || (MREINI + numCorrelativo2).equals(argumento.substring(0, 2))) {
                if (MRETDA == 'N') {
                    if (validaNumerico(argumento, cantidad)) {
                        return "000018";
                    }
                }
            }
        }
        return "";
    }

    private String busBTS(int cantidad, String argumento) {
        int MRELMA = 11;

        if (cantidad == MRELMA) {
            if (validaNumerico(argumento, cantidad)) {
                boolean banBts = PVerBTS(argumento);
                if (banBts) {
                    return "000016";
                }
            }
        }
        return "";
    }

    private boolean validaNumerico(String argumento, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            char c = argumento.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
    public boolean PVerBTS(String remCod) {
        String mrEcod = "000016";
        String mrEsts = "A";  // Dato estático
        if (!"A".equals(mrEsts)) {
            return false;
        }

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

        return remCod.length() == 10 || "000016".equals(mrEcod);
    }

    public static String normalize(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase().replaceAll("\\s+", " ").trim();
    }

    public double calculateJaccardSimilarity(String s1, String s2, int n) {
        Set<String> nGrams1 = generateNGrams(s1, n);
        Set<String> nGrams2 = generateNGrams(s2, n);

        Set<String> intersection = new HashSet<>(nGrams1);
        intersection.retainAll(nGrams2);

        Set<String> union = new HashSet<>(nGrams1);
        union.addAll(nGrams2);

        return (double) intersection.size() / union.size();
    }

    private Set<String> generateNGrams(String s, int n) {
        Set<String> nGrams = new HashSet<>();
        for (int i = 0; i <= s.length() - n; i++) {
            nGrams.add(s.substring(i, i + n));
        }
        return nGrams;
    }

    public static int needlemanWunsch(String s1, String s2, int match, int mismatch, int gap) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i * gap;
        }

        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j * gap;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int matchScore = dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? match : mismatch);
                int delete = dp[i - 1][j] + gap;
                int insert = dp[i][j - 1] + gap;
                dp[i][j] = Math.max(matchScore, Math.max(delete, insert));
            }
        }

        return dp[s1.length()][s2.length()];
    }

    public static void main(String[] args) {
        String s1 = "GATTACA";
        String s2 = "GCATGCU";
        int match = 1;
        int mismatch = -1;
        int gap = -1;

        int score = needlemanWunsch(s1, s2, match, mismatch, gap);
        System.out.println("La puntuación de alineación óptima es: " + score);
    }

}
