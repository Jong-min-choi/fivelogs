package com.fiveguys.fivelogbackend.domain.blog.board.dto;


import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardSearchResponseDto {
    private Long id;
    private String title;

    public static BoardSearchResponseDto fromEntity(Board board) {
        return BoardSearchResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .build();
    }
}
