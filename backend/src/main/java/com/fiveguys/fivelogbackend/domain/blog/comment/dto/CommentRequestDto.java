package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 작성 DTO")
public class CommentRequestDto {

    @Schema(description = "게시글 ID", example = "1")
    private Long boardId;

    @Schema(description = "댓글 내용", example = "좋은 글이에요")
    private String comment;

    @Schema(description = "부모 댓글 ID (대댓글일 경우)", example = "null", nullable = true)
    private Long parentId;
}