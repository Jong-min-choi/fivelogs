package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 응답 DTO")
public class CommentResponseDto {
    @Schema(description = "댓글 ID", example = "1")
    private Long id;
    private String comment;

    @Schema(description = "작성 날짜", example = "2025-04-14T10:30:00")
    private LocalDateTime createdDate;

    @Schema(description = "수정 날짜", example = "2025-04-14T10:30:00")
    private LocalDateTime updatedDate;

    @Schema(description = "대댓글 목록")
    private List<CommentResponseDto> replies;

    public static CommentResponseDto fromEntity(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.id = comment.getId();
        dto.comment = comment.getComment();
        dto.createdDate = comment.getCreatedDate();
        dto.comment = comment.isDeleted() ? "삭제된 댓글입니다." : comment.getComment();

        //수정할때 나오게
        if (!comment.getUpdatedDate().isEqual(comment.getCreatedDate())) {
            dto.updatedDate = comment.getUpdatedDate();
        } else {
            dto.updatedDate = null;
        }

        if (comment.getChildren() != null && !comment.getChildren().isEmpty()) {
            dto.replies = comment.getChildren().stream()
                    .map(CommentResponseDto::fromEntity)
                    .collect(Collectors.toList());
        } else {
            dto.replies = List.of();
        }

        return dto;
    }
}