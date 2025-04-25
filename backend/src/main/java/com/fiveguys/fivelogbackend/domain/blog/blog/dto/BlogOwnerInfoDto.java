package com.fiveguys.fivelogbackend.domain.blog.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

public class BlogOwnerInfoDto {

    Long ownerId;

    public BlogOwnerInfoDto(Long ownerId) {
        this.ownerId = ownerId;
    }
}
