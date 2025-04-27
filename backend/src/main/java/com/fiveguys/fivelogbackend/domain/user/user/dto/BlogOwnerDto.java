package com.fiveguys.fivelogbackend.domain.user.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogOwnerDto {
    private Long id;
    private String nickname;
    private String introduce;
    private Long boardCount;
    private Long viewCount;
    private Long followingCount;
    private Long followerCount;
}
