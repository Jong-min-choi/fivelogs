package com.fiveguys.fivelogbackend.domain.user.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FollowStatusDto {
    @JsonProperty("isFollowing")
    boolean isFollowing;

    public FollowStatusDto(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }
}
