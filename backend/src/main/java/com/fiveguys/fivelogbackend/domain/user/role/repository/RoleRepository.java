package com.fiveguys.fivelogbackend.domain.user.role.repository;

import com.fiveguys.fivelogbackend.domain.user.role.entity.Role;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query("SELECT r.name FROM Role r WHERE r.user.id = :userId")
    List<String> findRoleNamesByUserId(@Param("userId") Long userId);
}
