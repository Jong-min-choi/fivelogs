package com.fiveguys.fivelogbackend.domain.blog.board.dto;


import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.global.pagination.PageDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardMainPageResponseDto {
    private List<BoardSummaryDto> boardDtoList;
    private PageDto pageDto;

}