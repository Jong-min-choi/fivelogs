package com.fiveguys.fivelogbackend.domain.user.user.Controller;

import com.fiveguys.fivelogbackend.domain.user.user.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //유저삭제

    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.noContent().build();
}
