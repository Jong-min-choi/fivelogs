package com.fiveguys.fivelogbackend.domain.user.user.dto;


import com.fiveguys.fivelogbackend.domain.user.role.entity.RoleType;
import com.fiveguys.fivelogbackend.domain.user.user.entity.SNSLinks;
import com.fiveguys.fivelogbackend.domain.user.user.entity.UserStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminUserResponseDto {
    Long id;
    String email;
    String nickname;
    SNSLinks snsLinks;
    String provider;
    UserStatus userStatus;
    RoleType roleType;
}
