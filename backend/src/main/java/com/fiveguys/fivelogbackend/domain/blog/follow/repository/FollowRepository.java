package com.fiveguys.fivelogbackend.domain.blog.follow.repository;

import com.fiveguys.fivelogbackend.domain.blog.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    // 나를 팔로우하고있는 사람들을 조회
    public List<Follow> findByFollowerId(Long followerId);
    // 내가 팔로우하고있는 사람들을 조회
    public List<Follow> findByFollowingId(Long followingId);

}
