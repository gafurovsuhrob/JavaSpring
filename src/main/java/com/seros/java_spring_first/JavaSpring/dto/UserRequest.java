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
@Schema(description = "������ �� �������� ������������")
public class UserRequest {
    @Schema(description = "��� ������������", example = "John Wick")
    @Size(min = 4, max = 60, message = "��� ������������ ������ ��������� �� 4 �� 60 ��������")
    @NotBlank(message = "��� ������������ �� ����� ���� �������")
    private String name;

    @Schema(description = "��� ������������", example = "John")
    @Size(min = 4, max = 50, message = "��� ������������ ������ ��������� �� 4 �� 50 ��������")
    @NotBlank(message = "��� ������������ �� ����� ���� �������")
    private String username;

    @Schema(description = "������ ������������", example = "password")
    @Size(min = 8, max = 32, message = "������ ������ ��������� �� 8 �� 32 ��������")
    @NotBlank(message = "������ �� ����� ���� ������")
    private String password;

    @Schema(description = "���� ������������", example = "1,2,3")
    @Size(min = 1, message = "���������� ������� ����")
    @NotEmpty(message = "Roles cannot be empty")
    private Set<@Valid Long> roles;
}
