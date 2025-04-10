package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentRequestDto {
    private Long id;


    @Schema(description = "닉네임", example = "스프링부트")
    private String nickname;

    @Schema(description = "댓글", example = "안녕하세요. 반갑습니다.")
    private String comment;//댓글 내용
    private String createdDate = LocalDateTime.now()
            .format(DateTimeFormatter
            .ofPattern("yyyy.MM.dd HH:mm"));//댓글 생성 날짜
    private String updatedDate = LocalDateTime.now()
            .format(DateTimeFormatter
            .ofPattern("yyyy.MM.dd HH:mm"));//댓글 수정 날짜


}


