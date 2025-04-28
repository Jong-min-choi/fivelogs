package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardSummaryDto {
    Long id;
    String title;
    String content;
    Long views;
    List<String> hashtags;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    LocalDateTime updated;
    String nickname;

    String githubLink;
    String instagramLink;
    String twitterLink;

    public static BoardSummaryDto from(Board board, List<String> hashtagNames){
        return BoardSummaryDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .views(board.getViews())
                .nickname(board.getUser().getNickname())
                .githubLink(board.getUser().getSnsLink().getGithubLink())
                .instagramLink(board.getUser().getSnsLink().getInstagramLink())
                .twitterLink(board.getUser().getSnsLink().getTwitterLink())
                .hashtags(hashtagNames)
                .created(board.getCreatedDate())
                .updated(board.getUpdatedDate())
                .build();
    }

}
