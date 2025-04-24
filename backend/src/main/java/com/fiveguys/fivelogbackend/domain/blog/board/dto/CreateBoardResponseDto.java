package com.fiveguys.fivelogbackend.domain.blog.board.dto;


import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.BoardStatus;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBoardResponseDto {
    private Long boardId;
    private String blogTitle;
    private String nickname;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private BoardStatus status;
    private List<String> hashtags;
    private boolean deleted;

    public CreateBoardResponseDto(Long boardId, String blogTitle) {
        this.boardId = boardId;
        this.blogTitle = blogTitle;
    }

    public static CreateBoardResponseDto fromEntity(Board board) {
        CreateBoardResponseDto dto = new CreateBoardResponseDto();
        dto.boardId = board.getId();
        dto.setNickname(board.getUser().getNickname());
        dto.createdDate = board.getCreatedDate();
        dto.blogTitle = board.getTitle();
        dto.status = board.getStatus();
        dto.deleted = board.isDeleted();
        //수정할때 나오게
        if (!board.getUpdatedDate().isEqual(board.getCreatedDate())) {
            dto.updatedDate = board.getUpdatedDate();
        } else {
            dto.updatedDate = null;
        }

        // ✅ 해시태그 리스트 추가
        dto.hashtags = board.getTaggings().stream()
                .map(tagging -> tagging.getHashtag().getName())
                .distinct()
                .collect(Collectors.toList());

        return dto;
    }
}