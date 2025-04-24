package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardHashtagDto {

    private Long boardId;
    private String hashtagName;
}
