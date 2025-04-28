package com.fiveguys.fivelogbackend.domain.user.user.dto;

import com.fiveguys.fivelogbackend.domain.user.user.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserStatusRequest {
    private Long userId;
    private UserStatus userStatus;
}
