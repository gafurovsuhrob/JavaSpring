package com.seros.java_spring_first.JavaSpring.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на изменение пароля пользователя")
public class UpdatePasswordRequest {

    @Schema(description = "Старыи? пароль")
    @NotEmpty(message = "Старый пароль не может быть пустым")
    private String oldPassword;

    @Schema(description = "Новыи? пароль")
    @NotEmpty(message = "Новый пароль не может быть пустым")
    private String newPassword;

    @Schema(description = "Подтверждение нового пароля")
    @NotEmpty(message = "Подтверждение пароля не может быть пустым")
    private String confirmPassword;
}
