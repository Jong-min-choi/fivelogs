package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.BoardStatus;
import com.fiveguys.fivelogbackend.domain.user.user.entity.SNSLinks;
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
    BoardStatus boardStatus;

    public static BoardDetailDto from(Board board, List<String> hashtagNameList, String viewPath) {
        SNSLinks snsLink = board.getUser().getSnsLink();

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
                .boardStatus(board.getStatus())
                .githubLink(snsLink != null ? snsLink.getGithubLink() : null)
                .instagramLink(snsLink != null ? snsLink.getInstagramLink() : null)
                .twitterLink(snsLink != null ? snsLink.getTwitterLink() : null)
                .build();
    }

}
