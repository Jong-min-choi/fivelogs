package com.fiveguys.fivelogbackend.domain.user.user.controller;


import com.fiveguys.fivelogbackend.domain.user.user.dto.AddUserDto;
import com.fiveguys.fivelogbackend.domain.user.user.dto.TestDto;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "user 관련 API")
@Slf4j
public class UserController {
    private final UserService userService;
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody AddUserDto addUserDto){
        log.info("adduserDto {}", addUserDto);
        String result = userService.addUser(addUserDto);

        return ResponseEntity.ok(result);
    }
}
