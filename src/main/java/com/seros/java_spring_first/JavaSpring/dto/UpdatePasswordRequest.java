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

    @Schema(description = "������? ������")
    @NotEmpty(message = "������ ������ �� ����� ���� ������")
    private String oldPassword;

    @Schema(description = "�����? ������")
    @NotEmpty(message = "����� ������ �� ����� ���� ������")
    private String newPassword;

    @Schema(description = "������������� ������ ������")
    @NotEmpty(message = "������������� ������ �� ����� ���� ������")
    private String confirmPassword;
}
