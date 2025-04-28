package com.fiveguys.fivelogbackend.domain.user.follow.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FollowDto {

    Long id;
    String email;
    String nickname;
    String introduce;
    String profileImageUrl;
}
