package com.fiveguys.fivelogbackend.domain.user.role.repository;

import com.fiveguys.fivelogbackend.domain.user.role.entity.Role;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
