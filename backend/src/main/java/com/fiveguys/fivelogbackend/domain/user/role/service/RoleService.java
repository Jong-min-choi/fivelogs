package com.fiveguys.fivelogbackend.domain.user.role.service;

import com.fiveguys.fivelogbackend.domain.user.role.entity.Role;
import com.fiveguys.fivelogbackend.domain.user.role.entity.RoleType;
import com.fiveguys.fivelogbackend.domain.user.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void createUserRole(){
        Role role = Role.builder()
                .name(RoleType.USER)
                .build();
        roleRepository.save(role);
    }


}
