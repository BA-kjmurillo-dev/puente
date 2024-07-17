package com.puente.web.interceptor;

import com.puente.persistence.entity.CamposRequeridosEntity;
import com.puente.persistence.entity.CamposRequeridosId;
import com.puente.service.CamposRequeridosService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puente.service.dto.DatosBpDto;
import com.puente.service.dto.ResponseDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Component
public class ValidacionBPInterceptor implements HandlerInterceptor {
    @Autowired
    private CamposRequeridosService camposRequeridosService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // crear una copia de la peticion que permita extraer el body
            CustomHttpServletRequestWrapper customRequest = new CustomHttpServletRequestWrapper(request);

            // Obtener el cuerpo de la petición
            DatosBpDto datosBpDto = this.optenerCuerpoDePeticion(customRequest);

            // Obtenemos la clase de nuestro objeto
            Class<?> miClase = DatosBpDto.class;

            // Obtenemos los nombres de los atributos de nuestra clase
            Field[] campos = miClase.getDeclaredFields();

            if(datosBpDto != null) {
                List<CamposRequeridosEntity> camposRequeridos = this.camposRequeridosService.getByServicio("crearBp");
                // Recorremos los atributos del objeto que viene de la petición para realizar las validaciones
                for(Field campo : campos){
                    //Habilitamos los atributos que sean privados para poder acceder a ellos
                    campo.setAccessible(true);

                    //Variable que servirá para guardar el contenido de cada atributo. Para ello haremos un Casteo.
                    String valorCampoCasteado = "";

                    //Obtenemos el valor del atributo
                    try {
                        //Obtenemos el nombre del atributo
                        String nombreCampo = campo.getName();
                        Optional<CamposRequeridosEntity> configuracionDeCampo = camposRequeridos.stream().filter(
                            campoRequerido -> campoRequerido.getNombre().equals(nombreCampo)
                        ).findFirst();

                        if(configuracionDeCampo.isPresent() && configuracionDeCampo.get().isEsRequerido()) {
                            if(campo.get(datosBpDto) instanceof String){
                                valorCampoCasteado = (String) campo.get(datosBpDto);
                            }
                            if(campo.get(datosBpDto) != null) {
                                if (valorCampoCasteado.isEmpty() || valorCampoCasteado.isBlank()){
                                    return enviarError(
                                        response,
                                        "E-P003",
                                        "El contenido de la etiqueta [" + nombreCampo + "] no puede ser vacío."
                                    );
                                }
                            } else {
                                return enviarError(
                                    response,
                                    "E-P002",
                                    "Falta la etiqueta [" + nombreCampo + "] dentro del cuerpo de la petición"
                                );
                            }
                        }
                    } catch(IllegalAccessException e) {
                        return enviarError(
                            response,
                            "E-P005",
                            "Error interno del servidor."
                        );
                    }
                }
            } else {
                return enviarError(
                    response,
                    "E-P001",
                    "El cuerpo de la petición no puede estar vacío."
                );
            }
            return true;
        } catch(UnrecognizedPropertyException e) {
            String fieldName = e.getPropertyName();
            String mensaje = fieldName.trim().isEmpty() ?
                "No puedes enviar propiedades vacías" :
                "Propiedad [" + fieldName + "] no permitida";
            return enviarError(
                response,
                "E-P000",
                mensaje
            );
        } catch (Exception e) {
            // Error si el request viene vacio
            return enviarError(
                response,
                "E-P005",
                "Error interno del servidor."
            );
        }
    }

    private DatosBpDto optenerCuerpoDePeticion(CustomHttpServletRequestWrapper request) throws IOException {
        String body = request.getBody();
        String requestBody = body.lines().reduce("",String::concat);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(requestBody, DatosBpDto.class);
    }

    private boolean enviarError(HttpServletResponse response, String codigo, String mensaje) throws IOException {
        ResponseDto errorResponse = new ResponseDto();
        errorResponse.setType("Error");
        errorResponse.setMessageCode(codigo);
        errorResponse.setMessage(mensaje);
        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        return false;
    }
}
