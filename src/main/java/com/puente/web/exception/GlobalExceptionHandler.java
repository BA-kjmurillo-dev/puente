package com.puente.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.puente.service.dto.ResponseGetRemittanceDataDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseGetRemittanceDataDto> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .findFirst()
            .orElse("Validation error");

        ResponseGetRemittanceDataDto errorResponse = new ResponseGetRemittanceDataDto();
        errorResponse.setMessageCode("Exception");
        errorResponse.setMessage(errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
