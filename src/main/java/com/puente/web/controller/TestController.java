package com.puente.web.controller;

import com.puente.client.SrvBasa002Client;
import com.puente.client.SrvBasa003Client;

import com.puente.client.WsdlBpClient;
import com.puente.persistence.entity.ParametroRemesadoraEntity;
import com.puente.persistence.repository.ParametroRemesadoraRepository;
import com.puente.service.dto.*;
import com.soap.wsdl.ServicioSrvBasa002.EjecutarSrvBasa002Response;
import com.soap.wsdl.ServicioSrvBasa003.EjecutarSrvBasa003Response;
import com.soap.wsdl.ServicioSrvBasa010.EjecutarSrvBasa010Response;
import com.soap.wsdl.service03.SDTServicioVentanillaIn;
import com.soap.wsdl.service03.SDTServicioVentanillaInItemRemesa;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
import com.soap.wsdl.serviceBP.DTCreaBusinessPartnerResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.puente.service.*;
import com.puente.service.dto.ComparacionNombreDto;


import java.util.List;

@RestController
@Profile("dev")
@RequestMapping("/test")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    private final ConsultaController consultaController;
    private final UtilService utilService;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl05Service wsdl05Service;
    private final Wsdl07Service wsdl07Service;
    private final WsdlBpClient wsdlBpClient;
    private final ParametroRemesadoraRepository parametroRemesadoraRepository;

    private final SrvBasa002Client srvBasa002Client;
    private final SrvBasa003Client srvBasa003Client;
    private final SrvBasa010Service srvBasa010Service;
    @Autowired
    public TestController(
            ConsultaController consultaController,
            UtilService utilService,
            Wsdl03Service wsdl03Service,
            Wsdl04Service wsdl04Service,
            Wsdl05Service wsdl05Service,
            Wsdl07Service wsdl07Service,
            ParametroRemesadoraRepository parametroRemesadoraRepository,
            WsdlBpClient wsdlBpClient,
            SrvBasa002Client srvBasa002Client,
            SrvBasa003Client srvBasa003Client,
            SrvBasa010Service srvBasa010Service

    ) {
        this.consultaController = consultaController;
        this.utilService = utilService;
        this.wsdl03Service = wsdl03Service;
        this.wsdl04Service = wsdl04Service;
        this.wsdl05Service = wsdl05Service;
        this.wsdl07Service = wsdl07Service;
        this.parametroRemesadoraRepository = parametroRemesadoraRepository;
        this.wsdlBpClient = wsdlBpClient;
        this.srvBasa002Client = srvBasa002Client;
        this.srvBasa003Client = srvBasa003Client;
        this.srvBasa010Service = srvBasa010Service;
    }

    @PostMapping("/remittanceAlgorithm")
    public ResponseEntity<String> consulaRenesadora(@RequestBody String remesa) {
        if (remesa == null){
            log.error("Remesa no puede ser null");
            throw new IllegalArgumentException("Remesa no puede ser null");
        }
        remesa = remesa.replace("\"", "");
        remesa = remesa.replaceAll("\\s+", "");

        RemittanceAlgorithmDto respuesta = utilService.consultaRemesadora(remesa);
        if (respuesta.getMessage().equals("true")) {
            return ResponseEntity.ok("Remesa: "+remesa+" Remesadora: "+respuesta.getMrecod());
        } else if (respuesta.getMrecod().equals("000006")) {
            return ResponseEntity.status(400).body("Remesa: "+remesa + " Redireccion a SIREMU");
        } else {
            return ResponseEntity.status(400).body("Error: "+remesa);
        }
    }

    @GetMapping("/wsdl03")
    public ResponseEntity<Wsdl03Dto> wsdl03Test() {
        SDTServicioVentanillaInItemRemesa sdtServicioVentanillaInItemRemesa = new SDTServicioVentanillaInItemRemesa();
        sdtServicioVentanillaInItemRemesa.setIdentificadorRemesa("12020137470");
        sdtServicioVentanillaInItemRemesa.setCodigoBanco("2000");
        sdtServicioVentanillaInItemRemesa.setCodigoRemesadora("000016");
        SDTServicioVentanillaIn request03 = new SDTServicioVentanillaIn();
        request03.setCanal("0002");
        request03.setItemRemesa(sdtServicioVentanillaInItemRemesa);
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

    @PostMapping("/consulta")
    public ResponseEntity<ResponseGetRemittanceDataDto> testConsulta(
        @RequestBody String strData
    ) {
        String[] data = strData.trim().split(";");
        RequestGetRemittanceDataDto requestData = new RequestGetRemittanceDataDto();
        requestData.setCanal(data[0]);
        requestData.setIdentificadorRemesa(data[1]);
        requestData.setIdentificacion(data[2]);
        return this.consultaController.validateRemittance(requestData);
    }

    @GetMapping("/wsdlbp/{idNumber}")
    public ResponseEntity<DTCreaBusinessPartnerResp> wsdlBpTest(@PathVariable String idNumber){
        return ResponseEntity.ok(wsdlBpClient.getResponse(idNumber));
    }

    public static class DefirenciajRequest {
        public ComparacionNombreDto nombres;
        public String mrecod;
    }

    @PostMapping("/compararNombre")
    public ResponseEntity<String> compararNombre(@RequestBody DefirenciajRequest defirenciajRequest) {
        ComparacionNombreDto nombres = defirenciajRequest.nombres;
        String mrecod = defirenciajRequest.mrecod;
        if (nombres == null){
            log.error("Nombres no puede ser null");
            throw new IllegalArgumentException("Nombres no puede ser null");
        }else {
            boolean similarity = this.utilService.compararNombre(nombres,mrecod);
            if (similarity) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + similarity);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + similarity);
            }
        }
    }

    @PostMapping("parametros")
    public ResponseEntity<List<ParametroRemesadoraEntity>> getParametros() {
        List<ParametroRemesadoraEntity> list = parametroRemesadoraRepository.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
    @PostMapping("/basa010")
    public ResponseEntity<EjecutarSrvBasa010Response> basa010Test() {

        String agencia = "asd";
        EjecutarSrvBasa010Response ejecutarSrvBasa010Response = srvBasa010Service.getInfoAgencia(agencia);
        log.info(ejecutarSrvBasa010Response.getRespuestaSrvBasa010().getCodigoMensaje());
        return ResponseEntity.ok(ejecutarSrvBasa010Response);
    }

    @PostMapping("/basa002")
    public ResponseEntity<EjecutarSrvBasa002Response> basa020Test() {
        EjecutarSrvBasa002Response ejecutarSrvBasa020Response = srvBasa002Client.getResponse002(1000478287,"1"); //ejecutarSrvBasa020Client
        return ResponseEntity.ok(ejecutarSrvBasa020Response);
    }

    @PostMapping("/basa003")
    public ResponseEntity<EjecutarSrvBasa003Response> basa030Test() {
        String cuenta = "120520077401";
        EjecutarSrvBasa003Response ejecutarSrvBasa030Response = srvBasa003Client.getResponse003(cuenta); //ejecutarSrvBasa030Client
        return ResponseEntity.ok(ejecutarSrvBasa030Response);
    }

    /**
     * @description Metodo Post que se utiliza para el llamado del servicio SOAP para la creacion del BP.
     * @param datosBpDto Recibe un objeto de tipo DatosBpDto, el cual contiene la información mínima para la creacion
     *                   de un BP.
     * @return DTCreaBusinessPartnerResp Retorna un objeto de tipo DTCreaBusinessPartnerResp, el cual contiene la
     *                                   información de la creación del BP.
     * */
    @PostMapping("/wsdlbp")
    public ResponseEntity<DTCreaBusinessPartnerResp> wsdlBpTestCreacion(@RequestBody DatosBpDto datosBpDto){
        return ResponseEntity.ok(wsdlBpClient.getResponseCreateBp(datosBpDto));
    }


}
