package com.fiveguys.fivelogbackend.domain.user.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SNSLinkRequestDto {
    private String githubLink;
    private String instagramLink;
    private String twitterLink;
}