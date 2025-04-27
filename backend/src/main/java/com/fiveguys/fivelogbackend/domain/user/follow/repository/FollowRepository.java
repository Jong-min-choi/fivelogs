package com.fiveguys.fivelogbackend.domain.user.follow.repository;

import com.fiveguys.fivelogbackend.domain.user.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 팔로워 아이디와 팔로잉 아이디를 찾음
    Optional<Follow> findByFollowIdAndFollowedId(Long loginUserId, Long targetId);
    boolean existsByFollowIdAndFollowedId(Long loginUserId, Long targetId);


    // 파라미터가 팔로우하고있는 사람들을 조회
    @Query("SELECT f FROM Follow f " +
            "JOIN FETCH f.follow fol " +
            "LEFT JOIN FETCH fol.profileImage " +
            "WHERE fol.nickname = :nickname")
    List<Follow> findByFollowNicknameWithImage(@Param("nickname") String nickname);

    @Query("SELECT f FROM Follow f " +
            "JOIN FETCH f.followed fol " +
            "LEFT JOIN FETCH fol.profileImage " +
            "WHERE fol.nickname = :nickname")
    List<Follow> findByFollowedNicknameWithImage(@Param("nickname") String nickname);


    // 내가 몇 명을 팔로우하고 있는가? (팔로잉 수)
    public long countByFollowId(Long blogUserId);

    // 나를 몇 명이 팔로우하고 있는가? (팔로워 수)
    public long countByFollowedId(Long blogUserId);

}
