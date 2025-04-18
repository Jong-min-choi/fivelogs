package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardSummaryDto {
    Long id;
    String title;
    String content;
    Long views;
    String[] hashtags;
    LocalDateTime created;
    LocalDateTime updated;

    public static BoardSummaryDto from(Board board){
        return BoardSummaryDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .views(board.getViews())
                .hashtags(board.getHashtags().split(","))
                .created(board.getCreatedDate())
                .updated(board.getUpdatedDate())
                .build();
    }

}
