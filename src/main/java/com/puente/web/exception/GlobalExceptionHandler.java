package com.puente.web.exception;

import com.puente.service.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.puente.service.dto.ResponseGetRemittanceDataDto;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @description Metodo que centraliza los mensajes de error cuando el cuerpo del Request no tenga la estructura
     *              correcta.
     * @param ex Recibe un objeto de tipo RuntimeException.
     * @return ErrorDto Retorna un objeto de tipo ErrorDto, que contiene el detalle del error.
     * */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorDto> runtimeExceptionHandler(RuntimeException ex){
        ErrorDto errorDto = ErrorDto.builder().type("Error").code("400").message(ex.getMessage()).build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseGetRemittanceDataDto> handleValidationException(MethodArgumentNotValidException e) {
        String fieldName = Objects.requireNonNull(e.getBindingResult().getFieldError()).getField();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        // eval Not Null field
        if(this.evalError(allErrors, "NotNull")) {
            ResponseGetRemittanceDataDto errorResponse = new ResponseGetRemittanceDataDto();
            errorResponse.setMessageCode("E-P002");
            errorResponse.setMessage("Falta la etiqueta [" + fieldName + "] dentro del cuerpo de la petición");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        // eval Not Empty field
        if(this.evalError(allErrors, "NotEmpty")) {
            ResponseGetRemittanceDataDto errorResponse = new ResponseGetRemittanceDataDto();
            errorResponse.setMessageCode("E-P003");
            errorResponse.setMessage("El contenido de la etiqueta [" + fieldName + "] no puede ser vacío.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        ResponseGetRemittanceDataDto errorResponse = new ResponseGetRemittanceDataDto();
        errorResponse.setMessageCode("E-P001");
        errorResponse.setMessage("Debe revisar el cuerpo de la petición.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseGetRemittanceDataDto> handleUnrecognizedPropertyException(UnrecognizedPropertyException e) {
        String fieldName = e.getPropertyName();
        ResponseGetRemittanceDataDto errorResponse = new ResponseGetRemittanceDataDto();
        errorResponse.setMessageCode("E-P000");
        if(fieldName.trim().length() == 0) {
            errorResponse.setMessage("No puedes enviar propiedades vacias");
        } else {
            errorResponse.setMessage("propiedad [" + e.getPropertyName() + "] no permitida");
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }

    public boolean evalError(
        List<ObjectError> allErrors,
        String errorCode
    ) {
        return allErrors.stream()
            .flatMap(error -> Arrays.stream(Objects.requireNonNull(error.getCodes())))
            .anyMatch(code -> code.contains(errorCode));
    }

}
