package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SideBoardInfoDto {

    SimpleBoardDto prev;
    SimpleBoardDto next;

}
