package com.fiveguys.fivelogbackend.domain.user.follow.controller;

import com.fiveguys.fivelogbackend.domain.user.follow.dto.FollowDto;
import com.fiveguys.fivelogbackend.domain.user.follow.dto.FollowStatusDto;
import com.fiveguys.fivelogbackend.domain.user.follow.service.FollowService;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
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
    @DeleteMapping("/unfollow/{followId}")
    public ResponseEntity<Void> unfollow(@PathVariable("followId") Long followId) {
        User user = rq.getActor();
        followService.unfollow(user.getId(),followId);
        return ResponseEntity.ok().build();
    }

    // 상대 프로필을 봤을때 팔로우했는지 안했는지 띄워주기
    @Operation(summary = "팔로우 상태 확인", description = "유저가 다른 유저를 팔로우했는지 확인.")
    @GetMapping("/followStatus/nickname/{followNickname}")
    public ResponseEntity<ApiResponse<FollowStatusDto>> followStatusByNickname(@PathVariable("followNickname") String followNickname) {
        User user = rq.getActor();
        boolean followStatus = followService.followStatusByNickname(user.getId(), followNickname);
        FollowStatusDto followStatusDto = new FollowStatusDto(followStatus);
        return ResponseEntity.ok(ApiResponse.success(followStatusDto, "status 반환 성공"));
    }
    @Operation(summary = "팔로우 상태 확인", description = "유저가 다른 유저를 팔로우했는지 확인.")
    @GetMapping("/followStatus/{followId}")
    public ResponseEntity<ApiResponse<FollowStatusDto>> followStatus(@PathVariable("followId") Long followId) {
        User user = rq.getActor();
        boolean followStatus = followService.followStatus(user.getId(), followId);
        FollowStatusDto followStatusDto = new FollowStatusDto(followStatus);
        return ResponseEntity.ok(ApiResponse.success(followStatusDto, "status 반환 성공"));
    }




    // 내 팔로워 목록 보기
    @Operation(summary = "팔로워 목록 보기", description = "누가 나를 팔로우 했는지 목록을 확인")
    @GetMapping("/followerList/{nickname}")
    public ResponseEntity<ApiResponse<List<FollowDto>>> getFollowedList(@PathVariable("nickname") String nickname) {
        return ResponseEntity.ok(ApiResponse.success(followService.blogFollowedList(nickname), "팔로워 조회 성공"));
    }

    // 내 팔로잉 목록 보기
    @Operation(summary = "팔로잉 목록 보기", description = "내가 누구를 팔로우 했는지 목록을 확인")
    @GetMapping("/followingList/{nickname}")
    public ResponseEntity<ApiResponse<List<FollowDto>>> blogFollowingList(@PathVariable("nickname") String nickname) {
        return ResponseEntity.ok(ApiResponse.success( followService.blogFollowingList(nickname), "팔로잉 조회 성공"));
    }
}
