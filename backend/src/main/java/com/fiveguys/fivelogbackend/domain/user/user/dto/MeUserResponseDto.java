package com.fiveguys.fivelogbackend.domain.user.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MeUserResponseDto {
    Long id;
    String email;
    String nickname;

    String profileImageUrl;
}
