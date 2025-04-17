package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
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
//    private String nickname;

    @Schema(description = "작성 날짜", example = "2025-04-14T10:30:00")
    private LocalDateTime createdDate;

    @Schema(description = "수정 날짜", example = "2025-04-14T10:30:00")
    private LocalDateTime updatedDate;

    public static CommentResponseDto fromEntity(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.id = comment.getId();
        dto.comment = comment.getComment();
//        dto.nickname = comment.getUser().getNickname();
        dto.createdDate = comment.getCreatedDate();

        //수정할때 나오게
        if (!comment.getUpdatedDate().isEqual(comment.getCreatedDate())) {
            dto.updatedDate = comment.getUpdatedDate();
        } else {
            dto.updatedDate = null;
        }
        return dto;
    }
}