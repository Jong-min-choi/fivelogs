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

    Long userId;
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
    String profileImageUrl;
    String myIntroduce;
    String githubLink;
    String instagramLink;
    String twitterLink;

    public static BoardDetailDto from (Board board, List<String> hashtagNameList, String viewPath){
        return BoardDetailDto.builder()
                .userId(board.getUser().getId())
                .boardId(board.getId())
                .blogTitle(board.getBlog().getTitle())
                .boardTitle(board.getTitle())
                .content(board.getContent())
                .views(board.getViews())
                .hashtags(hashtagNameList)
                .nickName(board.getUser().getNickname())
                .myIntroduce(board.getUser().getIntroduce())
                .profileImageUrl(viewPath)
                .createdDateTime(board.getCreatedDate())
                .updatedDateTime(board.getUpdatedDate())
                .githubLink(board.getUser().getSnsLink().getGithubLink())
                .instagramLink(board.getUser().getSnsLink().getInstagramLink())
                .twitterLink(board.getUser().getSnsLink().getTwitterLink())
                .build();
    }

}
