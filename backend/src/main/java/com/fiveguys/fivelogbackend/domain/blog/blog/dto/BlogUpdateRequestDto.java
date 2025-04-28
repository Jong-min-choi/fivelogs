package com.fiveguys.fivelogbackend.domain.blog.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BlogUpdateRequestDto {

    @NotBlank
    private String title; // 제목만 수정 가능
}