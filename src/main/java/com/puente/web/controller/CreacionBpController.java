package com.puente.web.controller;

import com.puente.client.WsdlBpClient;
import com.puente.service.dto.DatosBpDto;
import com.puente.web.exception.RequestException;
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
