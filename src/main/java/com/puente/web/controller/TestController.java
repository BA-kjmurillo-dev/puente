package com.puente.web.controller;

import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.persistence.repository.ValoresGlobalesRemesasRepository;
import com.puente.service.dto.*;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.puente.service.*;
import info.debatty.java.stringsimilarity.JaroWinkler;

@RestController
@RequestMapping("/test")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    private final ConsultaService consultaService;
    private final UtilServices consultaRemesadoraServices;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl05Service wsdl05Service;
    private final Wsdl07Service wsdl07Service;
    private final ValoresGlobalesRemesasRepository valoresGlobalesRemesasRepository;

    @Autowired
    public TestController(
            ConsultaService consultaService,
            UtilServices consultaRemesadoraServices,
            Wsdl03Service wsdl03Service,
            Wsdl04Service wsdl04Service,
            Wsdl05Service wsdl05Service,
            Wsdl07Service wsdl07Service, ValoresGlobalesRemesasRepository valoresGlobalesRemesasRepository

    ) {
        this.consultaService = consultaService;
        this.consultaRemesadoraServices = consultaRemesadoraServices;
        this.wsdl03Service = wsdl03Service;
        this.wsdl04Service = wsdl04Service;
        this.wsdl05Service = wsdl05Service;
        this.wsdl07Service = wsdl07Service;
        this.valoresGlobalesRemesasRepository = valoresGlobalesRemesasRepository;
    }

    @PostMapping("/remittanceAlgorithm")
    public ResponseEntity<String>ConsulaRenesadora(@RequestBody String remesa) {
        if (remesa == null){
            log.error("Remesa no puede ser null");
            throw new IllegalArgumentException("Remesa no puede ser null");
        }
        remesa = remesa.replaceAll("\"", "");
        remesa = remesa.replaceAll("\\s+", "");
        log.info("Constoller remesa:"+remesa);
        String respuesta = this.consultaService.ConsultaRemesadora(remesa);
        if (respuesta.equals("000004") ||
            respuesta.equals("000007") ||
            respuesta.equals("000016") ||
            respuesta.equals("000018")) {
            return ResponseEntity.ok("Remesa: "+remesa+" Remesadora: "+respuesta);
        } else if (respuesta.equals("000006")) {
            return ResponseEntity.ok("Remesa: "+remesa+" Remesadora: "+respuesta + " Redireccion a SIREMU");
        } else {
            return ResponseEntity.status(400).body("Error");
        }
    }

    @GetMapping("/wsdl03")
    public ResponseEntity<Wsdl03Dto> wsdl03Test() {
        RequestGetRemittanceDataDto request03 = new RequestGetRemittanceDataDto();
        request03.setCanal("0002");
        request03.setIdentificadorRemesa("202405230001");
        request03.setCodigoBanco("2000");
        request03.setCodigoRemesadora("000016");
        return ResponseEntity.ok(this.wsdl03Service.getRemittanceData(request03));
    }

    @GetMapping("/wsdl04")
    public ResponseEntity<Wsdl04Dto> wsdl04Test() {
        String canal = "0002";
        return ResponseEntity.ok(this.wsdl04Service.testSireonConection(canal));
    }

    @GetMapping("/wsdl05")
    public ResponseEntity<Wsdl05Dto> wsdl05Test() {
        String canal = "0002";
        return ResponseEntity.ok(this.wsdl05Service.getRemittersListByChannel(canal));
    }

    @GetMapping("/wsdl07")
    public ResponseEntity<Wsdl07Dto> wsdl07Test() {
        ServicesRequest007ItemSolicitud itemSolicitudRequest = new ServicesRequest007ItemSolicitud();
        itemSolicitudRequest.setCodigoBanco("2000");
        itemSolicitudRequest.setCanal("0002");
        itemSolicitudRequest.setIdentificadorRemesa("202405230001");
        return ResponseEntity.ok(this.wsdl07Service.getRemittanceByIdentifier(itemSolicitudRequest));
    }
    public static class Nombres {
        public String nombre1;
        public String nombre2;
    }

    @PostMapping("/diferenciaj")
    public ResponseEntity<String> defirenciaj(@RequestBody Nombres nombres) {

        if (nombres == null){
            log.error("Nombres no puede ser null");
            throw new IllegalArgumentException("Nombres no puede ser null");
        }else{
            ValoresGlobalesRemesasEntity ValoresGlobalesRemesas1 = valoresGlobalesRemesasRepository.findByCodigoAndItem("tam", "comp");
            ValoresGlobalesRemesasEntity ValoresGlobalesRemesas2 = valoresGlobalesRemesasRepository.findByCodigoAndItem("remesadora", "000016");
            int n_GRAM_SIZE = Integer.parseInt(ValoresGlobalesRemesas1.getValor());
            double THRESHOLD = Double.parseDouble(ValoresGlobalesRemesas2.getValor());

            double similarity = consultaRemesadoraServices.calculateJaccardSimilarity(nombres.nombre1, nombres.nombre2,n_GRAM_SIZE)*100;

            boolean canCharge = similarity >= THRESHOLD;
            if (canCharge) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + canCharge);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + canCharge);
            }

        }
    }

    @PostMapping("/diferenciaj2")
    public ResponseEntity<String> defirenciaj2(@RequestBody Nombres nombres) {
        if (nombres == null){
            log.error("Nombres no puede ser null");
            throw new IllegalArgumentException("Nombres no puede ser null");
        }else {
            ValoresGlobalesRemesasEntity ValoresGlobalesRemesas2 = valoresGlobalesRemesasRepository.findByCodigoAndItem("remesadora", "000016");
            double THRESHOLD = Double.parseDouble(ValoresGlobalesRemesas2.getValor());

            JaroWinkler jw = new JaroWinkler();
            double similarity = jw.similarity(nombres.nombre1, nombres.nombre2) * 100;
            boolean canCharge = similarity >= THRESHOLD;
            if (canCharge) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + canCharge);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + canCharge);
            }
        }
    }

    @PostMapping("/diferenciaj3")
    public ResponseEntity<String> defirenciaj3(@RequestBody Nombres nombres) {
        if (nombres == null){
            log.error("Nombres no puede ser null");
            throw new IllegalArgumentException("Nombres no puede ser null");
        }else {
            ValoresGlobalesRemesasEntity ValoresGlobalesRemesas2 = valoresGlobalesRemesasRepository.findByCodigoAndItem("remesadora", "000016");
            double THRESHOLD = Double.parseDouble(ValoresGlobalesRemesas2.getValor());
            double similarity = UtilServices.needlemanWunsch(nombres.nombre1, nombres.nombre2,1,-1,-1);
            boolean canCharge = similarity >= THRESHOLD;
            if (canCharge) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + canCharge);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + canCharge);
            }
        }
    }

    public static double calculateSimilarity(String s1, String s2) {
        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) {
            return 1.0;
        }
        return (1.0 - ((double) computeLevenshteinDistance(s1, s2) / maxLen)) * 100;
    }

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



}
