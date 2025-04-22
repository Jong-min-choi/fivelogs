package com.fiveguys.fivelogbackend.domain.blog.follow.controller;

import com.fiveguys.fivelogbackend.domain.blog.follow.service.FollowService;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
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

    @Operation(summary = "팔로우", description = "유저가 다른 유저를 팔로우합니다.")
    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@PathVariable Long loginUserId, @PathVariable Long targetId) {
        followService.follow(loginUserId, targetId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "언팔로우", description = "유저가 팔로우한 유저를 언팔로우합니다.")
    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@PathVariable Long loginUserId, @PathVariable Long targetId) {
        followService.unfollow(loginUserId, targetId);
        return ResponseEntity.ok().build();
    }

    // 상대 프로필을 봤을때 팔로우했는지 안했는지 띄워주기
    @Operation(summary = "팔로우 상태 확인", description = "유저가 다른 유저를 팔로우했는지 확인.")
    @GetMapping("/isFollow")
    public ResponseEntity<Boolean> isFollow(@PathVariable Long loginUserId, @PathVariable Long targetId) {
        boolean isFollow = followService.followButton(loginUserId, targetId);
        return ResponseEntity.ok(isFollow);
    }

    // 내 팔로워 목록 보기
    @Operation(summary = "팔로워 목록 보기", description = "누가 나를 팔로우 했는지 목록을 확인")
    @GetMapping("/followerList")
    public ResponseEntity<List<User>> followerList(@PathVariable Long loginUserId) {
        return ResponseEntity.ok(followService.followerList(loginUserId));
    }

    // 내 팔로잉 목록 보기
    @Operation(summary = "팔로잉 목록 보기", description = "내가 누구를 팔로우 했는지 목록을 확인")
    @GetMapping("/followingList")
    public ResponseEntity<List<User>> followingList(@PathVariable Long loginUserId) {
        return ResponseEntity.ok(followService.followingList(loginUserId));
    }
}
