package com.fiveguys.fivelogbackend.domain.blog.blog.dto;

import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BlogResponseDto {
    private Long id;
    private String title;
    private Long userId;
    private List<Long> boardIds;

    public static BlogResponseDto fromEntity(Blog blog) {
        return BlogResponseDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .userId(blog.getId())
                .boardIds(
                        blog.getBoardList().stream()
                        .map(Board::getId)
                        .toList())
                .build();
    }
}
