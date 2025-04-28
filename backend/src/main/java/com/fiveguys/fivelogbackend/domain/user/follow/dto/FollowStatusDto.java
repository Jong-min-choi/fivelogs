package com.fiveguys.fivelogbackend.domain.user.follow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FollowStatusDto {
    boolean following;

    public FollowStatusDto(boolean isFollowing) {
        this.following = isFollowing;
    }
}
