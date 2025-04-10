package com.fiveguys.fivelogbackend.domain.blog.board.dto;


import com.fiveguys.fivelogbackend.domain.blog.board.entity.BoardStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBoardResponseDto {
    private Long boardId;
    private String blogTitle;
}
