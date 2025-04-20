package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleBoardDto {
    private Long id;
    private String title;

    public static SimpleBoardDto from (Board board){
        return SimpleBoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .build();
    }
}
