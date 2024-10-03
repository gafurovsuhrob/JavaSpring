package com.seros.java_spring_first.JavaSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private String details;
//    private int status;
//    private String timestamp;
}
