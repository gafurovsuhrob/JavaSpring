package com.seros.java_spring_first.JavaSpring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на создание пользователя")
public class UserRequest {
    @Schema(description = "ФИО пользователя", example = "John Wick")
    @Size(min = 4, max = 60, message = "Имя пользователя должно содержать от 4 до 60 символов")
    @NotBlank(message = "ФИО пользователя не может быть пустыми")
    private String name;

    @Schema(description = "Имя пользователя", example = "John")
    @Size(min = 4, max = 50, message = "Имя пользователя должно содержать от 4 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Schema(description = "Пароль пользователя", example = "password")
    @Size(min = 8, max = 32, message = "Пароль должен содержать от 8 до 32 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @Schema(description = "Роли пользователя", example = "1,2,3")
    @Size(min = 1, message = "Необходимо выбрать роль")
    @NotEmpty(message = "Roles cannot be empty")
    private Set<@Valid Long> roles;
}
