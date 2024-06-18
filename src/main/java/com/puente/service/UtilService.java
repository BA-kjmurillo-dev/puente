package com.puente.service;

import com.puente.persistence.entity.MessageCodesEntity;
import com.puente.service.dto.ResponseDto;
import com.puente.service.dto.comparacionNombreDto;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class UtilService {
    @Autowired
    private MessageCodesService messageCodesService;
    private comparacionNombreDto comparacionNombreDto;
    private static int numerico = 0;

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

    //Correcion de nombre
    public static String normalize(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String respult = pattern.matcher(normalized).replaceAll("").toLowerCase().replaceAll("\\s+", " ").trim();
        return respult;
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
        if ((s1 == null || s1.isEmpty()) && (s2 == null || s2.isEmpty())) return 1.0;;
        if ((s1 == null || s1.isEmpty()) || (s2 == null || s2.isEmpty())) return 0.0;
        numerico ++;
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

    public static double calcularSimilaridad(comparacionNombreDto comparacionNombreDto) {

        double similitudTotal = 0;
        double similitudTotalL = 0;
        double similitudTotalJW = 0;

        double similitudBp1 = Levenshtein(comparacionNombreDto.getBeneficiarioBp().getPrimerNombreBp(), comparacionNombreDto.getBeneficiarioSireon().getPrimerNombreSireon());
        double similutudS1 = jaroWinklerSimilarity(comparacionNombreDto.getBeneficiarioBp().getPrimerNombreBp(), comparacionNombreDto.getBeneficiarioSireon().getPrimerNombreSireon());

        double similitudBp2 = Levenshtein(comparacionNombreDto.getBeneficiarioBp().getSegundoNombreBp(), comparacionNombreDto.getBeneficiarioSireon().getSegundoNombreSireon());
        double similutudS2 = jaroWinklerSimilarity(comparacionNombreDto.getBeneficiarioBp().getSegundoNombreBp(), comparacionNombreDto.getBeneficiarioSireon().getSegundoNombreSireon());

        double similitudBp3 = Levenshtein(comparacionNombreDto.getBeneficiarioBp().getPrimerApellidoBp(), comparacionNombreDto.getBeneficiarioSireon().getPrimerApellidoSireon());
        double similutudS3 = jaroWinklerSimilarity(comparacionNombreDto.getBeneficiarioBp().getPrimerApellidoBp(), comparacionNombreDto.getBeneficiarioSireon().getPrimerApellidoSireon());

        double similitudBp4 = Levenshtein(comparacionNombreDto.getBeneficiarioBp().getSegundoApellidoBp(), comparacionNombreDto.getBeneficiarioSireon().getSegundoApellidoSireon());
        double similutudS4 = jaroWinklerSimilarity(comparacionNombreDto.getBeneficiarioBp().getSegundoApellidoBp(), comparacionNombreDto.getBeneficiarioSireon().getSegundoApellidoSireon());



        similitudTotal = (similitudBp1 + similutudS1 + similitudBp2 + similutudS2 + similitudBp3 + similutudS3 + similitudBp4 + similutudS4) / 8;


        return similitudTotal;
    }

    //Levenshtein
    public static double calculaNombreSpearados(String name1, String name2) {
        String[] partes1 = normalize(name1).split(" ");
        String[] partse2 = normalize(name2).split(" ");

        double total = 0;
        int resultaComparaciones = Math.min(partes1.length, partse2.length);

        for (int i = 0; i < resultaComparaciones; i++) {
            total += Levenshtein(partes1[i], partse2[i]);
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
    public static double Levenshtein(String s1, String s2) {


        if ((s1 == null || s1.isEmpty()) && (s2 == null || s2.isEmpty())) return 1.0;
        if ((s1 == null || s1.isEmpty()) || (s2 == null || s2.isEmpty())) return 0.0;
        int maxLen = Math.max(s1.length(), s2.length());

        s1 = normalize(s1);
        s2 = normalize(s2);
        return (1.0 - ((double) computeLevenshteinDistance(s1, s2) / maxLen));
    }
}
