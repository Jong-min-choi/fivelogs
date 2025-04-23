package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 리액션 요청 DTO")
public class LikeRequestDto {
    @Schema(description = "좋아요 여부 (true: 좋아요, false: 싫어요)", example = "true")
    @JsonProperty("isLike")
    private boolean like;    // 필드명을 like로 변경

    public boolean isLike() {
        return like;
    }
}