package com.puente.web.controller;

import com.puente.client.WsdlBpClient;
import com.puente.service.dto.DatosBpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;

@RestController
@RequestMapping("/crear")
public class CreacionBpController {
    private final WsdlBpClient wsdlBpClient;

    @Autowired
    public CreacionBpController(WsdlBpClient wsdlBpClient) {
        this.wsdlBpClient = wsdlBpClient;
    }

    /**
     * @description Metodo Post que se utiliza para el llamado del servicio SOAP para la creacion del BP.
     * @param datosBpDto Recibe un objeto de tipo DatosBpDto, el cual contiene la información mínima para la creacion
     *                   de un BP.
     * @return DTCreaBusinessPartnerResp Retorna un objeto de tipo DTCreaBusinessPartnerResp, el cual contiene la
     *                                   información de la creación del BP.
     * */
    @PostMapping("/add")
    public ResponseEntity<?> wsdlBpTestCreacion(@RequestBody DatosBpDto datosBpDto){
        return ResponseEntity.ok(wsdlBpClient.getResponseCreateBp(datosBpDto));
    }
}
