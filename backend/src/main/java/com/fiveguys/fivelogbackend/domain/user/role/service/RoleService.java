package com.fiveguys.fivelogbackend.domain.user.role.service;

import com.fiveguys.fivelogbackend.domain.user.role.entity.Role;
import com.fiveguys.fivelogbackend.domain.user.role.entity.RoleType;
import com.fiveguys.fivelogbackend.domain.user.role.repository.RoleRepository;
import com.fiveguys.fivelogbackend.domain.user.user.dto.AdminUserPageResponseDto;
import com.fiveguys.fivelogbackend.domain.user.user.dto.AdminUserResponseDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.pagination.PageDto;
import com.fiveguys.fivelogbackend.global.pagination.PageUt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void createUserRole(User user, RoleType roleType){
        Role role = Role.builder()
                .user(user)
                .name(roleType)
                .build();
        roleRepository.save(role);
    }

    public List<String> getRoleNames(Long userId){
        return roleRepository.findRoleNamesByUserId(userId);
    }

    public boolean existByName(RoleType roleType){
        return roleRepository.existsByName(roleType);
    }

    @Transactional(readOnly = true)
    public AdminUserPageResponseDto getPagedUserInfo (Pageable pageable){
        Page<AdminUserResponseDto> pagedUsers = roleRepository.findAdminUsers(pageable);
        PageDto unitPageDto = PageUt.get10unitPageDto(pagedUsers.getNumber(), pagedUsers.getTotalPages());
        return new AdminUserPageResponseDto(pagedUsers.getContent(), unitPageDto);
    }

    public List<RoleType> getUserRoleType(Long userId){
        return roleRepository.findRoleTypesByUserId(userId);
    }

}
