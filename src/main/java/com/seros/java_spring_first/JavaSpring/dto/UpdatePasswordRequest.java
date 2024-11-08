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
@Schema(description = "������ �� ��������� ������ ������������")
public class UpdatePasswordRequest {

    @Schema(description = "Old password")
    @NotEmpty(message = "Old password cannot be empty")
    private String oldPassword;

    @Schema(description = "New password")
    @NotEmpty(message = "New password cannot be empty")
    private String newPassword;

    @Schema(description = "Confirm new password")
    @NotEmpty(message = "Confirm new password cannot be empty")
    private String confirmPassword;
}
