package com.fiveguys.fivelogbackend.domain.user.role.repository;

import com.fiveguys.fivelogbackend.domain.user.role.entity.Role;
import com.fiveguys.fivelogbackend.domain.user.role.entity.RoleType;
import com.fiveguys.fivelogbackend.domain.user.user.dto.AdminUserResponseDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query("SELECT r.name FROM Role r WHERE r.user.id = :userId")
    List<String> findRoleNamesByUserId(@Param("userId") Long userId);

    boolean existsByName(RoleType roleType);

    @Query("SELECT new com.fiveguys.fivelogbackend.domain.user.user.dto.AdminUserResponseDto(u.id, u.email, u.nickname, u.snsLink, u.provider, u.userStatus, r.name) " +
            "FROM Role r JOIN r.user u "
           ) // 예시로 'ROLE_ADMIN'을 가진 사용자만 조회하는 경우
    Page<AdminUserResponseDto> findAdminUsers(Pageable pageable);

    @Query("SELECT r.name FROM Role r WHERE r.user.id = :userId")
    List<RoleType> findRoleTypesByUserId(@Param("userId") Long userId);

}
