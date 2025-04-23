package com.fiveguys.fivelogbackend.domain.user.follow.controller;

import com.fiveguys.fivelogbackend.domain.user.follow.service.FollowService;
import com.fiveguys.fivelogbackend.domain.user.user.dto.MeUserResponseDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final Rq rq;

    @Operation(summary = "팔로우", description = "유저가 다른 유저를 팔로우합니다.")
    @PostMapping("/follow/{followId}")
    public ResponseEntity<Void> follow(@PathVariable("followId") Long followId) {
        User user = rq.getActor();
        followService.follow(user.getId(),followId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "언팔로우", description = "유저가 팔로우한 유저를 언팔로우합니다.")
    @PostMapping("/unfollow/{followId}")
    public ResponseEntity<Void> unfollow(@PathVariable("followId") Long followId) {
        User user = rq.getActor();
        followService.unfollow(user.getId(),followId);
        return ResponseEntity.ok().build();
    }

    // 상대 프로필을 봤을때 팔로우했는지 안했는지 띄워주기
    @Operation(summary = "팔로우 상태 확인", description = "유저가 다른 유저를 팔로우했는지 확인.")
    @GetMapping("/followStatus/{followId}")
    public ResponseEntity<Boolean> followStatus(@PathVariable("followId") Long followId) {
        User user = rq.getActor();
        boolean followStatus = followService.followStatus(user.getId(), followId);
        return ResponseEntity.ok(followStatus);
    }

    // 내 팔로워 목록 보기
    @Operation(summary = "팔로워 목록 보기", description = "누가 나를 팔로우 했는지 목록을 확인")
    @GetMapping("/followerList/{blogUserId}")
    public ResponseEntity<List<MeUserResponseDto>> blogFollowedList(@PathVariable("blogUserId") Long blogUserId) {
        return ResponseEntity.ok(followService.blogFollowedList(blogUserId));
    }

    // 내 팔로잉 목록 보기
    @Operation(summary = "팔로잉 목록 보기", description = "내가 누구를 팔로우 했는지 목록을 확인")
    @GetMapping("/followingList/{blogUserId}")
    public ResponseEntity<List<MeUserResponseDto>> blogFollowingList(@PathVariable("blogUserId") Long blogUserId) {
        return ResponseEntity.ok(followService.blogFollowingList(blogUserId));
    }
}
