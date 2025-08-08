package com.seros.java_spring_first.JavaSpring.dto;

import com.seros.java_spring_first.JavaSpring.model.AuthProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class UserRequest2 {
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
    private String password;

    @Schema(description = "Почтовый адрес пользователя", example = "0oV4M@example.com")
    private String email;

    @Schema(description = "Телефон пользователя", example = "123456789")
    private String phone;

    @Schema(description = "Дата рождения пользователя", example = "2000-01-01")
    private String dateOfBirth;

    @Schema(description = "URL фотографии пользователя", example = "https://example.com/photo.jpg")
    private String photoUrl;

//    @Schema(description = "Идентификатор провайдера", example = "google Id")
//    private String authProviderId;
//
//    @Schema(description = "Провайдер", example = "GOOGLE")
//    @NotNull(message = "AuthProvider cannot be empty")
//    private AuthProvider authProvider;
//
//    @Schema(description = "Роли пользователя", example = "1,2,3")
//    @Size(min = 1, message = "Необходимо выбрать роль")
//    @NotEmpty(message = "Roles cannot be empty")
//    private Set<@Valid Long> roles;
}
