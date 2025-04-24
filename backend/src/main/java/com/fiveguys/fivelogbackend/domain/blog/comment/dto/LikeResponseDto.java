package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponseDto {
    private int likeCount;
    private int dislikeCount;
    private Boolean likedByMe;
}