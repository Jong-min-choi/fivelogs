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
<<<<<<< HEAD

    public Board toEntity() {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .status(this.status)
                .build();
    }
=======
    private String hashtags;
>>>>>>> 16a8d542087a276c301e689bceb2f3589b648306
}