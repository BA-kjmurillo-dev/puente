package com.puente.service;

import lombok.NoArgsConstructor;
import lombok.ToString;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@ToString
@NoArgsConstructor
public class ConsultaRemesadoraService {
    //private static final Logger log = LoggerFactory.getLogger(ConsultaRemesadoraService.class);
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
}
