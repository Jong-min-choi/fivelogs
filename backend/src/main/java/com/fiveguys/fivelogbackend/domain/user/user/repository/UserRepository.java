package com.fiveguys.fivelogbackend.domain.user.user.repository;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);


    // 닉네임 키워드로 User 검색
    @Query("""
        SELECT u FROM User u
        WHERE LOWER(u.nickname) LIKE LOWER(CONCAT('%', :nickname, '%'))
    """)
    List<User> searchUsersByNickname(@Param("nickname") String nickname);

}
