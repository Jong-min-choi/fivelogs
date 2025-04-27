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
    String githubUrl;
    String instagramUrl;
    String twitterUrl;
    String profileImageUrl;

    public static MyPageDto from (User user,String blogTitle, String profileImageUrl){
        SNSLinks snsLinks = user.getSNSLink();

        return MyPageDto.builder()
                .blogTitle(blogTitle)
                .email(user.getEmail())
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .githubUrl(snsLinks != null ? user.getSNSLink().getGithubLink() : null)
                .instagramUrl(snsLinks != null ?user.getSNSLink().getInstagramLink() : null)
                .twitterUrl(snsLinks != null ? user.getSNSLink().getTwitterLink() : null)
                .profileImageUrl(profileImageUrl)
                .build();


    }
}
