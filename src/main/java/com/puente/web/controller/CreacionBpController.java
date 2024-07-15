package com.puente.web.controller;

import com.puente.client.WsdlBpClient;
import com.puente.service.dto.DatosBpDto;
import com.puente.service.dto.ErrorDto;
import com.puente.web.exception.RequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;

@Tag(name = "Creación BP", description = "Este controlador permite crear un Business Partner.")
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
    @Operation(summary = "Crear un Business Partner.",
            description = "Método que se utiliza para crear un Business Partner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se creó el BP con éxito."),
            @ApiResponse(responseCode = "E-P002", description = "Falta un elemento dentro del cuerpo de la petición.", content = {@Content(schema = @Schema(implementation = ErrorDto.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "E-P003", description = "Uno o más elementos no deben estar vacíos dentro del cuerpo de la petición.", content = {@Content(schema = @Schema(implementation = ErrorDto.class), mediaType = "application/json")}),
    })
    @Parameters({
            @Parameter(name = "tipoIdentificador", description = "Tipo de identificador.", example = "GOV"),
            @Parameter(name = "identificador", description = "Identificador.", example = "0701198209280"),
            @Parameter(name = "primerNombre", description = "Primer nombre.", example = "Maria"),
            @Parameter(name = "segundoNombre", description = "Segundo nombre.", example = "Jose"),
            @Parameter(name = "primerApellido", description = "Primer apellido.", example = "Flores"),
            @Parameter(name = "segundoApellido", description = "Segundo apellido.", example = "Caceres"),
            @Parameter(name = "genero", description = "Género.", example = "F"),
            @Parameter(name = "nacionalidad", description = "Nacionalidad.", example = "HND"),
            @Parameter(name = "telefono", description = "Teléfono.", example = "32909090"),
            @Parameter(name = "fechaNacimiento", description = "Fecha de nacimiento.", example = "19900416"),
            @Parameter(name = "paisNacimiento", description = "País de nacimiento.", example = "HND"),
            @Parameter(name = "codigoPais", description = "Código de país.", example = "HND"),
            @Parameter(name = "direccion", description = "Dirección.", example = "San Pedro Sula"),
            @Parameter(name = "ciudadNacimiento", description = "Ciudad de nacimiento.", example = "SPS"),
            @Parameter(name = "fechaEmisionId", description = "Fecha de emisión de identificación.", example = "20080101"),
            @Parameter(name = "fechaVencimientoId", description = "Fecha de vencimiento de identificación.", example = "20300101"),

    })
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
