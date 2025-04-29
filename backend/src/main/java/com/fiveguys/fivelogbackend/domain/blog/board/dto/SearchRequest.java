package com.fiveguys.fivelogbackend.domain.blog.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SearchRequest {
    private String title;
    private Set<String> hashtags;
}
