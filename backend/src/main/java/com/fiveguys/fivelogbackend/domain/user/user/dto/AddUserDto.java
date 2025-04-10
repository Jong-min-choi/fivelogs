package com.fiveguys.fivelogbackend.domain.user.user.dto;


import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import lombok.*;

@Getter
@Setter
@ToString

public class AddUserDto {

    private String email;
    private String password;
    private String nickname;
    private String introduce;
    private String snsLink;

    public static User from(AddUserDto addUserDto){

        return User.builder()
                .email(addUserDto.getEmail())
                .password(addUserDto.getPassword())
                .nickname(addUserDto.getNickname())
                .SNSLink(addUserDto.getSnsLink())
                .introduce(addUserDto.getIntroduce())
                .build();


    }

}
