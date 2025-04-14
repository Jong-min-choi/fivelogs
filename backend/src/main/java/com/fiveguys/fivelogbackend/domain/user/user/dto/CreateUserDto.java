package com.fiveguys.fivelogbackend.domain.user.user.dto;


import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import lombok.*;

@Getter
@Setter
@ToString

public class CreateUserDto {

    private String email;
    private String password;
    private String nickname;
    private String introduce;
    private String snsLink;

    public static User from(CreateUserDto createUserDto){

        return User.builder()
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .nickname(createUserDto.getNickname())
                .SNSLink(createUserDto.getSnsLink())
                .introduce(createUserDto.getIntroduce())
                .build();
    }

}
