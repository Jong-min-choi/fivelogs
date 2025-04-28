package com.fiveguys.fivelogbackend.domain.blog.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 응답 DTO")
public class CommentResponseDto {
    @Schema(description = "댓글 ID", example = "1")
    private Long id;
    private String comment;

    @Schema(description = "댓글 작성자 닉네임")
    private String nickname;

    private int likeCount;
    private int dislikeCount;
    //소프트 딜리트
    private boolean deleted;
    //좋아요를 눌렀는지에 대한 여부

    @Schema(description = "작성 날짜", example = "2025-04-14T10:30:00")
    private LocalDateTime createdDate;

    @Schema(description = "수정 날짜", example = "2025-04-14T10:30:00")
    private LocalDateTime updatedDate;

    @Schema(description = "대댓글 목록")
    private List<CommentResponseDto> replies;

    @JsonInclude(JsonInclude.Include.ALWAYS)
    @Schema(description = "현재 로그인한 사용자가 좋아요/싫어요를 눌렀는지 여부")
    private Boolean likedByMe;  // true = 좋아요, false = 싫어요, null = 반응 없음

    public static CommentResponseDto fromEntity(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.id = comment.getId();
        dto.comment = comment.getComment();
        dto.setNickname(comment.getUser().getNickname());
        dto.createdDate = comment.getCreatedDate();
        dto.comment = comment.isDeleted() ? "삭제된 댓글입니다." : comment.getComment();
        dto.likeCount = comment.getLikeCount();
        dto.dislikeCount = comment.getDislikeCount();
        dto.deleted = comment.isDeleted();;

        //수정할때 나오게
        if (!comment.getUpdatedDate().isEqual(comment.getCreatedDate())) {
            dto.updatedDate = comment.getUpdatedDate();
        } else {
            dto.updatedDate = null;
        }

        dto.replies = null;

        return dto;
    }

    // 대댓글 조회용 팩토리 메서드 (필요 시 사용)
    public static CommentResponseDto fromEntityWithReplies(Comment comment) {
        CommentResponseDto dto = fromEntity(comment);

        if (comment.getChildren() != null && !comment.getChildren().isEmpty()) {
            dto.replies = comment.getChildren().stream()
                    .map(CommentResponseDto::fromEntity)
                    .toList();
        } else {
            dto.replies = List.of();
        }

        return dto;
    }

    //좋아요 유지
    public static CommentResponseDto fromEntityWithReaction(Comment comment, Boolean liked) {
        CommentResponseDto dto = new CommentResponseDto();

        dto.id = comment.getId();
        dto.comment = comment.isDeleted() ? "삭제된 댓글입니다." : comment.getComment();
        dto.nickname = comment.getUser().getNickname();
        dto.likeCount = comment.getLikeCount();
        dto.dislikeCount = comment.getDislikeCount();
        dto.deleted = comment.isDeleted();
        dto.likedByMe = liked; // 여기에서만 가능
        dto.createdDate = comment.getCreatedDate();

        if (!comment.getUpdatedDate().isEqual(comment.getCreatedDate())) {
            dto.updatedDate = comment.getUpdatedDate();
        } else {
            dto.updatedDate = null;
        }

        dto.replies = null;

        return dto;
    }
}
