package com.seros.java_spring_first.JavaSpring.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Schema(description = "Username cannot be empty")
    private String username;
    @Schema(description = "Password cannot be empty")
    private String password;
}
