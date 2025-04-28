package com.fiveguys.fivelogbackend.domain.user.user.repository;

import com.fiveguys.fivelogbackend.domain.user.user.dto.AdminUserResponseDto;
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

    List<User> findAllByProfileImageId(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profileImage WHERE u.id = :id")
    Optional<User> findByIdWithProfileImage(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profileImage WHERE u.nickname = :nickname")
    Optional<User> findByNicknameWithProfileImage(@Param("nickname") String nickname);

 
    @Query("""
        SELECT u FROM User u
        WHERE LOWER(u.nickname) LIKE LOWER(CONCAT('%', :nickname, '%'))
    """)
    List<User> searchUsersByNickname(@Param("nickname") String nickname);



}
