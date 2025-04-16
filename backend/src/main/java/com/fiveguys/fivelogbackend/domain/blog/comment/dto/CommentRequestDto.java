package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 작성 DTO")
public class CommentRequestDto {

    @Schema(description = "게시글 ID", example = "0")
    private Long boardId;
    @Schema(description = "작성자 유저 ID", example = "0")
    private Long userId;

    //    @Schema(description = "닉네임", example = "Five Guys")
//    private String nickname;
    @Schema(description = "댓글", example = "좋은 글이에요")
    private String comment;
}