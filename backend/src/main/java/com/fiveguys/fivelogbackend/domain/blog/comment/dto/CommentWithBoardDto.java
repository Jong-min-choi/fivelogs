package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentWithBoardDto {
    private String boardTitle; // 게시글 제목
    private String commentContent; // 댓글 내용
}