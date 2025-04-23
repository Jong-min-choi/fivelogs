package com.fiveguys.fivelogbackend.domain.user.follow.repository;

import com.fiveguys.fivelogbackend.domain.user.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 팔로워 아이디와 팔로잉 아이디를 찾음
    Optional<Follow> findByFollowerIdAndFollowedId(Long loginUserId, Long targetId);



    // 파라미터가 팔로우하고있는 사람들을 조회
    public List<Follow> findByFollowerId(Long blogUserId);
    // 파라미터가 팔로우하고있는 사람들을 조회
    public List<Follow> findByFollowedId(Long blogUserId);

}
