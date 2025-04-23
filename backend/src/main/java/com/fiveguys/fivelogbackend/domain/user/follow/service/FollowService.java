package com.fiveguys.fivelogbackend.domain.user.follow.service;

import com.fiveguys.fivelogbackend.domain.user.follow.entity.Follow;
import com.fiveguys.fivelogbackend.domain.user.follow.repository.FollowRepository;
import com.fiveguys.fivelogbackend.domain.user.user.dto.MeUserResponseDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로워 팔로잉 조회
    public void follow(Long loginUserId, Long followId) {
        if (followRepository.findByFollowerIdAndFollowedId(loginUserId, followId).isPresent()) {
            throw new RuntimeException("이미 팔로우하셨습니다.");
        }
        if (loginUserId.equals(followId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }
            User me = userRepository.findById(loginUserId).orElseThrow(() -> new RuntimeException("로그인을 해주십시오"));
            User target = userRepository.findById(followId).orElseThrow(() -> new RuntimeException(""));
            // 팔로우 저장 팔로잉이
            Follow follow = new Follow(me, target);
            followRepository.save(follow);
        }

    // 언팔
    public void unfollow(Long loginUserId, Long followId) {
        Follow follow = followRepository.findByFollowerIdAndFollowedId(loginUserId, followId)
                .orElseThrow(() -> new RuntimeException("팔로우 관계가 존재하지 않습니다."));
        followRepository.delete(follow);
    }

    // 이건 내가 상대 프로필 들어갔을때 팔로우했냐 안했냐 뜨게하기위해
    public boolean followStatus(Long loginUserId, Long followId) {
        if (followRepository.findByFollowerIdAndFollowedId(loginUserId,followId).isPresent())
        return false; // 저장된게 있으면 false = 팔로우한 상태로 띄우기
        else {
            return true; // 저장된게 없으면 팔로우 할수있게 띄우기
        }
    }

    // 팔로워 목록을 보기위해
    @Transactional(readOnly = true)
    public List<MeUserResponseDto> blogFollowedList(Long blogUserId) {
        return followRepository.findByFollowedId(blogUserId)
                .stream()
                .map(follow -> {
                    User follower = follow.getFollower(); // 나를 팔로우한 사람
                    return MeUserResponseDto.builder()
                            .email(follower.getEmail())
                            .nickname(follower.getNickname())
                            .build();
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MeUserResponseDto> blogFollowingList(Long blogUserId) {
        return followRepository.findByFollowerId(blogUserId)
                .stream()
                .map(follow -> {
                    User followed = follow.getFollowed(); // 내가 팔로우한 사람
                    return MeUserResponseDto.builder()
                            .email(followed.getEmail())
                            .nickname(followed.getNickname())
                            .build();
                })
                .toList();
    }
}
