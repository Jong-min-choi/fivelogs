package com.fiveguys.fivelogbackend.domain.user.user.dto;

import com.fiveguys.fivelogbackend.domain.user.user.entity.SNSLinks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SNSLinkResponseDto {
    private String githubLink;
    private String instagramLink;
    private String twitterLink;

    public static SNSLinkResponseDto fromEntity(SNSLinks snsLinks) {
        if (snsLinks == null) {
            return new SNSLinkResponseDto("", "", "");
        }

        return SNSLinkResponseDto.builder()
                .githubLink(snsLinks.getGithubLink())
                .instagramLink(snsLinks.getInstagramLink())
                .twitterLink(snsLinks.getTwitterLink())
                .build();
    }
}