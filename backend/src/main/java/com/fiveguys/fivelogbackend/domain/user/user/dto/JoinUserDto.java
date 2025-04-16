package com.fiveguys.fivelogbackend.domain.user.user.dto;


import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
public class JoinUserDto {

    private String email;
    @NotBlank(message = "패스워드를 입력하세요.")
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
