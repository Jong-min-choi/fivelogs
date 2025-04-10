package com.fiveguys.fivelogbackend.domain.blog.board.dto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class BoardRequestDto {
    private String title;
    private String content;
    private BoardStatus status;
}