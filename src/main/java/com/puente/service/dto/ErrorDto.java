package com.puente.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    private String type;
    private String code;
    private String message;
}
