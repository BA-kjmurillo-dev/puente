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
import com.puente.service.dto.comparacionNombreDto;

@RestController
@RequestMapping("/test")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    private final UtilService utilService;
    private final ConsultaService consultaService;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl05Service wsdl05Service;
    private final Wsdl07Service wsdl07Service;
    private final ValoresGlobalesRemesasRepository valoresGlobalesRemesasRepository;

    @Autowired
    public TestController(
            UtilService utilService,
            ConsultaService consultaService,
            Wsdl03Service wsdl03Service,
            Wsdl04Service wsdl04Service,
            Wsdl05Service wsdl05Service,
            Wsdl07Service wsdl07Service,
            ValoresGlobalesRemesasRepository valoresGlobalesRemesasRepository, ConsultaController consultaController

    ) {
        this.utilService = utilService;
        this.consultaService = consultaService;
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
            return ResponseEntity.status(400).body("Error: "+remesa);
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

    //Jaccard
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

            double similarity = this.utilService.calculateJaccardSimilarity(nombres.nombre1, nombres.nombre2,n_GRAM_SIZE)*100;

            boolean canCharge = similarity >= THRESHOLD;
            if (canCharge) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + canCharge);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + canCharge );
            }

        }
    }
    //JaroWinkler
    @PostMapping("/diferenciaj2")
    public ResponseEntity<String> defirenciaj2(@RequestBody Nombres nombres) {
        if (nombres == null){
            log.error("Nombres no puede ser null");
            throw new IllegalArgumentException("Nombres no puede ser null");
        }else {
            ValoresGlobalesRemesasEntity ValoresGlobalesRemesas2 = valoresGlobalesRemesasRepository.findByCodigoAndItem("remesadora", "000016");
            double THRESHOLD = Double.parseDouble(ValoresGlobalesRemesas2.getValor());

            double similarity = this.utilService.jaroWinklerSimilarity(nombres.nombre1, nombres.nombre2) * 100;
            boolean canCharge = similarity >= THRESHOLD;
            if (canCharge) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + canCharge);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + canCharge);
            }
        }
    }
    //needlemanWunsch
    @PostMapping("/diferenciaj3")
    public ResponseEntity<String> defirenciaj3(@RequestBody Nombres nombres) {
        if (nombres == null){
            log.error("Nombres no puede ser null");
            throw new IllegalArgumentException("Nombres no puede ser null");
        }else {
            ValoresGlobalesRemesasEntity ValoresGlobalesRemesas2 = valoresGlobalesRemesasRepository.findByCodigoAndItem("remesadora", "000016");
            double THRESHOLD = Double.parseDouble(ValoresGlobalesRemesas2.getValor());
            double similarity = this.utilService.needlemanWunsch(nombres.nombre1, nombres.nombre2,1,-1,-1)*100;
            boolean canCharge = similarity >= THRESHOLD;
            if (canCharge) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + canCharge);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + canCharge);
            }
        }
    }
    //Levenshtein
    @PostMapping("/diferenciaj4")
    public ResponseEntity<String> defirenciaj4(@RequestBody Nombres nombres) {
        if (nombres == null){
            log.error("Nombres no puede ser null");
            throw new IllegalArgumentException("Nombres no puede ser null");
        }else {
            ValoresGlobalesRemesasEntity ValoresGlobalesRemesas2 = valoresGlobalesRemesasRepository.findByCodigoAndItem("remesadora", "000016");
            double THRESHOLD = Double.parseDouble(ValoresGlobalesRemesas2.getValor());
            double similarity = this.utilService.calculaNombreSpearados(nombres.nombre1, nombres.nombre2);
            boolean canCharge = similarity >= THRESHOLD;
            if (canCharge) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + canCharge);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + canCharge);
            }
        }
    }

    @PostMapping("/diferenciaj5")
    public ResponseEntity<String> defirenciaj5(@RequestBody Nombres nombres) {
        if (nombres == null){
            log.error("Nombres no puede ser null");
            throw new IllegalArgumentException("Nombres no puede ser null");
        }else {
            ValoresGlobalesRemesasEntity ValoresGlobalesRemesas2 = valoresGlobalesRemesasRepository.findByCodigoAndItem("remesadora", "000016");
            double THRESHOLD = Double.parseDouble(ValoresGlobalesRemesas2.getValor());
            double similarity1 = this.utilService.calculaNombreSpearados(nombres.nombre1, nombres.nombre2);
            double similarity2 = this.utilService.jaroWinklerSimilarity(nombres.nombre1, nombres.nombre2) ;
            double jw_weight=0.7;
            double similarity = ((jw_weight*similarity2)+((1-jw_weight)*(similarity1)))*100;
            boolean canCharge = similarity >= THRESHOLD;
            if (canCharge) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + canCharge);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + canCharge);
            }
        }
    }

    @PostMapping("/diferenciaj6")
    public ResponseEntity<String> defirenciaj6(@RequestBody comparacionNombreDto nombres) {

        if (nombres == null){
            log.error("Nombres no puede ser null");
            throw new IllegalArgumentException("Nombres no puede ser null");
        }else {
            ValoresGlobalesRemesasEntity ValoresGlobalesRemesas2 = valoresGlobalesRemesasRepository.findByCodigoAndItem("remesadora", "000016");
            double THRESHOLD = Double.parseDouble(ValoresGlobalesRemesas2.getValor());
            double similarity = this.utilService.calcularSimilaridad(nombres)*100;
            boolean canCharge = similarity >= THRESHOLD;
            if (canCharge) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + canCharge);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + canCharge);
            }
        }
    }
}
