package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCountingResponseDto {
    Long boardId;
    Long count;
}
