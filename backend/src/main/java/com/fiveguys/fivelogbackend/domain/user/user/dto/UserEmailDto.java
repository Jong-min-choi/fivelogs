package com.fiveguys.fivelogbackend.domain.user.user.dto;

import lombok.Getter;

@Getter
public class UserEmailDto {

    String email;

    public UserEmailDto(String email) {
        this.email = email;
    }
}
