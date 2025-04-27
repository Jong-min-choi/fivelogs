package com.fiveguys.fivelogbackend.domain.user.user.dto;

import com.fiveguys.fivelogbackend.domain.user.user.entity.SNSLinks;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyPageDto {
    String introduce;
    String nickname;
    String email;
    String blogTitle;
    String githubLink;
    String instagramLink;
    String twitterLink;

    public static MyPageDto from (User user,String blogTitle){
        SNSLinks snsLinks = user.getSnsLink();

        return MyPageDto.builder()
                .blogTitle(blogTitle)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())  
                .githubLink(snsLinks != null ? user.getSnsLink().getGithubLink() : null)
                .instagramLink(snsLinks != null ?user.getSnsLink().getInstagramLink() : null)
                .twitterLink(snsLinks != null ? user.getSnsLink().getTwitterLink() : null)
                .profileImageUrl(profileImageUrl)
                .build();


    }
}
