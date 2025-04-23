package com.fiveguys.fivelogbackend.domain.blog.follow.service;

import com.fiveguys.fivelogbackend.domain.blog.follow.entity.Follow;
import com.fiveguys.fivelogbackend.domain.blog.follow.repository.FollowRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 팔로워 팔로잉 조회
    public void follow(Long loginUserId, Long targetId) {
        User me = userRepository.findById(loginUserId).orElseThrow(() -> new RuntimeException("로그인을 해주십시오"));
        User target = userRepository.findById(targetId).orElseThrow(() -> new RuntimeException(""));

        // 팔로우 저장 팔로잉이
        Follow follow = new Follow(me, target);
        followRepository.save(follow);
    }

    // 언팔
    public void unfollow(Long loginUserId, Long targetId) {
        Follow follow = followRepository.findByFollowerIdAndFollowingId(loginUserId, targetId)
                .orElseThrow(() -> new RuntimeException("팔로우 관계가 존재하지 않습니다."));
        followRepository.delete(follow);
    }

    // 이건 내가 상대 프로필 들어갔을때 팔로우했냐 안했냐 뜨게하기위해
    public boolean followButton(Long loginUserId, Long targetId) {
        return followRepository.findByFollowerIdAndFollowingId(loginUserId,targetId).isPresent();
    }

    // 팔로워 목록을 보기위해
    public List<User> followerList(Long loginUserId) {
        return followRepository.findByFollowerId(loginUserId)
                .stream().map(Follow::getFollower).toList();
    }

    // 팔로잉 목록을 보기위해
    public List<User> followingList(Long loginUserId) {
        return followRepository.findByFollowingId(loginUserId)
                .stream().map(Follow::getFollowing).toList();
    }
}
