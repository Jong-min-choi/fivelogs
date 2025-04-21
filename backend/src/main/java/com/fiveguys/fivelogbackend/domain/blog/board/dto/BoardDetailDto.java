package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BoardDetailDto {

    Long boardId;
    String blogTitle;
    String boardTitle;
    String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime createdDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime updatedDateTime;
    Long views;
    List<String> hashtags;
    String nickName;
    String profileImageLink;
    String myIntroduce;

    public static BoardDetailDto from (Board board){
        return BoardDetailDto.builder()
                .boardId(board.getId())
                .blogTitle(board.getBlog().getTitle())
                .boardTitle(board.getTitle())
                .content(board.getContent())
                .views(board.getViews())
                .hashtags(Arrays.stream(board.getHashtags().split(",")).toList())
                .nickName(board.getUser().getNickname())
                .myIntroduce(board.getUser().getIntroduce())
                .profileImageLink(null)
                .createdDateTime(board.getCreatedDate())
                .updatedDateTime(board.getUpdatedDate())
                .build();
    }

}
