package com.fiveguys.fivelogbackend.domain.user.user.dto;


import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import lombok.*;

@Getter
@Setter
@ToString

public class JoinUserDto {

    private String email;
    private String password;
    private String nickname;

    public static User from(JoinUserDto joinUserDto){

        return User.builder()
                .email(joinUserDto.getEmail())
                .password(joinUserDto.getPassword())
                .nickname(joinUserDto.getNickname())
                .build();
    }

}
