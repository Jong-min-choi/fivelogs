package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.BoardStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateBoardRequestDto {
    private String title;
    private String content;
    private BoardStatus status;
}