package com.fiveguys.fivelogbackend.domain.user.role.service;

import com.fiveguys.fivelogbackend.domain.user.role.entity.Role;
import com.fiveguys.fivelogbackend.domain.user.role.entity.RoleType;
import com.fiveguys.fivelogbackend.domain.user.role.repository.RoleRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void createUserRole(User user){
        Role role = Role.builder()
                .user(user)
                .name(RoleType.USER)
                .build();
        roleRepository.save(role);
    }

    public List<String> getRoleNames(Long userId){
        return roleRepository.findRoleNamesByUserId(userId);
    }


}
