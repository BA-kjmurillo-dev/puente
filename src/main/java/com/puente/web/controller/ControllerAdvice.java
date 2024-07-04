package com.puente.web.controller;

import com.puente.service.dto.ErrorDto;
import com.puente.web.exception.RequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    /**
     * @description Metodo que centraliza los mensajes de error cuando el cuerpo del Request no tenga la estructura
     *              correcta.
     * @param ex Recibe un objeto de tipo RuntimeException.
     * @return ErrorDto Retorna un objeto de tipo ErrorDto, que contiene el detalle del error.
     * */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorDto> runtimeExceptionHandler(RuntimeException ex){
        ErrorDto errorDto = ErrorDto.builder().type("Error").code("400").message("Debe revisar el cuerpo de la petición.").build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * @description Metodo que centraliza los mensajes de error para los request que se reciben incompletos.
     * @param ex Recibe un objeto de tipo RequestException, este contiene el codigo y mensaje de error.
     * @return ErrorDto Retorna un objeto de tipo ErrorDto que contiene la descripcion detallada del mesaje de error.
     * */
    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDto> requestExceptionHandler(RequestException ex){
        ErrorDto errorDto = ErrorDto.builder().type("Error").code(ex.getCode()).message(ex.getMessage()).build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
