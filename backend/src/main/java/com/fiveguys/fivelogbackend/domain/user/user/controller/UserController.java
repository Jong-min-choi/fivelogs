package com.fiveguys.fivelogbackend.domain.user.user.controller;

import com.fiveguys.fivelogbackend.domain.user.user.dto.CreateUserDto;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserCommandService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "인증 관련 API")
public class UserController {

    private final UserService userService;
    private final UserCommandService userCommandService;


    public ResponseEntity<String> createUser(CreateUserDto createUserDto){
        String message = userCommandService.createUser(createUserDto);
        return ResponseEntity.ok(message);
    }
    //유저삭제
    @DeleteMapping("/user/delete")
    public ResponseEntity<Void> deleteUser() {
//        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
}
