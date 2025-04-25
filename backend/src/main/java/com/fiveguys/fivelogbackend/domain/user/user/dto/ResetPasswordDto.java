package com.fiveguys.fivelogbackend.domain.user.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ResetPasswordDto {
    String password;

    public ResetPasswordDto(String password) {
        this.password = password;
    }
}
