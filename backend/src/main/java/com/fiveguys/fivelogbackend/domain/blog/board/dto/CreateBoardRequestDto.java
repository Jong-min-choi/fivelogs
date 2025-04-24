package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.BoardStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateBoardRequestDto {
    private String title;
    private String content;
    private BoardStatus status;
    private List<String> hashtags;
}