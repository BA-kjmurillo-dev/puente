package com.puente.web.controller;

import com.puente.client.WsdlBpClient;
import com.puente.persistence.entity.ParametroRemesadoraEntity;
import com.puente.persistence.entity.ValoresGlobalesRemesasEntity;
import com.puente.persistence.repository.ParametroRemesadoraRepository;
import com.puente.persistence.repository.ValoresGlobalesRemesasRepository;
import com.puente.service.dto.*;
import com.puente.web.exception.RequestException;
import com.soap.wsdl.service03.SDTServicioVentanillaIn;
import com.soap.wsdl.service03.SDTServicioVentanillaInItemRemesa;
import com.soap.wsdl.service07.ServicesRequest007ItemSolicitud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.puente.service.*;
import com.puente.service.dto.ComparacionNombreDto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RestController
@Profile("dev")
@RequestMapping("/test")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(ConsultaController.class);
    private final ConsultaController consultaController;
    private final UtilService utilService;
    private final ConsultaService consultaService;
    private final Wsdl03Service wsdl03Service;
    private final Wsdl04Service wsdl04Service;
    private final Wsdl05Service wsdl05Service;
    private final Wsdl07Service wsdl07Service;
    private final ValoresGlobalesRemesasRepository valoresGlobalesRemesasRepository;
    private final WsdlBpClient wsdlBpClient;
    private final ParametroRemesadoraRepository parametroRemesadoraRepository;

    @Autowired
    public TestController(
            ConsultaController consultaController,
            UtilService utilService,
            ConsultaService consultaService,
            Wsdl03Service wsdl03Service,
            Wsdl04Service wsdl04Service,
            Wsdl05Service wsdl05Service,
            Wsdl07Service wsdl07Service,
            ValoresGlobalesRemesasRepository valoresGlobalesRemesasRepository,
            ParametroRemesadoraRepository parametroRemesadoraRepository,
            WsdlBpClient wsdlBpClient 

    ) {
        this.consultaController = consultaController;
        this.utilService = utilService;
        this.consultaService = consultaService;
        this.wsdl03Service = wsdl03Service;
        this.wsdl04Service = wsdl04Service;
        this.wsdl05Service = wsdl05Service;
        this.wsdl07Service = wsdl07Service;
        this.valoresGlobalesRemesasRepository = valoresGlobalesRemesasRepository;
        this.parametroRemesadoraRepository = parametroRemesadoraRepository;
        this.wsdlBpClient = wsdlBpClient;
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
        RemittanceAlgorithmDto respuesta = utilService.ConsultaRemesadora(remesa);
        if (respuesta.getMessage().equals("true")) {
            return ResponseEntity.ok("Remesa: "+remesa+" Remesadora: "+respuesta.getMrecod());
        } else if (respuesta.getMrecod().equals("000006")) {
            return ResponseEntity.ok("Remesa: "+remesa + " Redireccion a SIREMU");
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
    public ResponseEntity<?> wsdlBpTest(@PathVariable String idNumber){
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
            boolean similarity = this.utilService.CompararNombre(nombres,mrecod);
            if (similarity) {
                return ResponseEntity.ok("Similarity: " + similarity + " canCharge: " + similarity);
            }else {
                return ResponseEntity.status(400).body("Similarity: " + similarity + " canCharge: " + similarity);
            }
        }
    }

    @PostMapping("parametros")
    public ResponseEntity<List<ParametroRemesadoraEntity>> getParametros() {
        List<ParametroRemesadoraEntity> list = new ArrayList<ParametroRemesadoraEntity>();
        list = parametroRemesadoraRepository.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(list);
    }

    /**
     * @description Metodo Post que se utiliza para el llamado del servicio SOAP para la creacion del BP.
     * @param datosBpDto Recibe un objeto de tipo DatosBpDto, el cual contiene la información mínima para la creacion
     *                   de un BP.
     * @return DTCreaBusinessPartnerResp Retorna un objeto de tipo DTCreaBusinessPartnerResp, el cual contiene la
     *                                   información de la creación del BP.
     * */
    @PostMapping("/wsdlbp")
    public ResponseEntity<?> wsdlBpTestCreacion(@RequestBody DatosBpDto datosBpDto){
        //Obtenemos la clase de nuestro objeto
        Class<?> miClase = datosBpDto.getClass();

        //Obtenemos los nombres de los atributos de nuestra clase
        Field[] campos = miClase.getDeclaredFields();

        //Validamos que el cuerpo de la petición este completo
        if (datosBpDto != null){
            // Recorremos los atributos del objeto que viene de la petición para realizar las validaciones
            for (Field campo : campos){
                //Habilitamos los atributos que sean privados para poder acceder a ellos
                campo.setAccessible(true);

                //Variable que servirá para guardar el contenido de cada atributo. Para ello haremos un Casteo.
                String valorCampoCasteado = "";

                //Obtenemos el valor del atributo
                try {
                    //Obtenemos el nombre del atributo
                    String nombreCampo = campo.getName();

                    if (campo.get(datosBpDto) instanceof String){
                        valorCampoCasteado = (String) campo.get(datosBpDto);
                    }

                    if (campo.get(datosBpDto) != null){
                        if (valorCampoCasteado.isEmpty() || valorCampoCasteado.isBlank()){
                            throw new RequestException("E-P003", "El contenido de la etiqueta [" + nombreCampo + "] no puede ser vacío.");
                        }
                    } else{
                        throw new RequestException("E-P002", "Falta la etiqueta [" + nombreCampo + "] dentro del cuerpo de la petición");
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            throw new RequestException("E-P001", "El cuerpo de la petición no puede estar vacío.");
        }
        return ResponseEntity.ok(wsdlBpClient.getResponseCreateBp(datosBpDto));
    }
}
