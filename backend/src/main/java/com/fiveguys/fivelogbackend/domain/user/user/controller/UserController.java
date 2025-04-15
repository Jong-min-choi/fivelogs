package com.fiveguys.fivelogbackend.domain.user.user.controller;

import com.fiveguys.fivelogbackend.domain.user.user.dto.JoinUserDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserCommandService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "인증 관련 API")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserCommandService userCommandService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody @Valid JoinUserDto joinUserDto){
        User join = userService.join(joinUserDto.getEmail(), joinUserDto.getPassword(), joinUserDto.getNickname(), null);
        ApiResponse<User> joinSuccess = ApiResponse.success( join,"join success");
        return ResponseEntity.ok(joinSuccess);
    }
    //유저삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser() {
//        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
}
