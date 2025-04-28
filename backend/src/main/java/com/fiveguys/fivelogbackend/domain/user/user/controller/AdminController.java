package com.fiveguys.fivelogbackend.domain.user.user.controller;

import com.fiveguys.fivelogbackend.domain.user.role.service.RoleService;
import com.fiveguys.fivelogbackend.domain.user.user.dto.AdminUserPageResponseDto;
import com.fiveguys.fivelogbackend.domain.user.user.dto.ChangeUserStatusRequest;
import com.fiveguys.fivelogbackend.domain.user.user.entity.UserStatus;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserCommandService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import com.fiveguys.fivelogbackend.global.security.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserCommandService userCommandService;
    private final RoleService roleService;
    //회원 조회
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<AdminUserPageResponseDto>> getUsersInfo(@PageableDefault(size=30, sort = "id",direction = Sort.Direction.DESC) Pageable pageable) {
        AdminUserPageResponseDto pagedUserInfo = roleService.getPagedUserInfo(pageable);
        log.info("pagedUserInfo {}", pagedUserInfo);
        return ResponseEntity.ok(ApiResponse.success(pagedUserInfo, "회원 조회 성공"));
    }

    //회원 상태 변경
    @PostMapping("/users/change-status")
    public ResponseEntity<ApiResponse<UserStatus>> changeUserStatus(
            @RequestBody ChangeUserStatusRequest changeUserStatusRequest) {


        Long userId = changeUserStatusRequest.getUserId();
        UserStatus userStatus = changeUserStatusRequest.getUserStatus();
        UserStatus changedStatus = userCommandService.changeUserStatus(userId, userStatus);

        return ResponseEntity.ok(ApiResponse.success(changedStatus, "userStatus 변경 성공"));
    }


}
