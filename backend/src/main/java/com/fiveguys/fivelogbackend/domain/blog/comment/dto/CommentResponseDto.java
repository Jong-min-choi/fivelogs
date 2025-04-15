package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 응답 DTO")
public class CommentResponseDto {
    @Schema(description = "댓글 ID", example = "1")
    private Long id;
    private String comment;
    private String nickname;

    @Schema(description = "작성 날짜", example = "2025-04-14T10:30:00")
    private LocalDateTime createdDate;

    @Schema(description = "수정 날짜", example = "2025-04-14T10:30:00")
    private LocalDateTime updatedDate;

}