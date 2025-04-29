package com.fiveguys.fivelogbackend.domain.user.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntroduceUpdateRequest {
    @NotBlank(message = "자기소개는 필수입니다.")
    private String introduce;
}