package com.fiveguys.fivelogbackend.domain.user.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MeUserResponseDto {
    String email;
    String nickname;
}
