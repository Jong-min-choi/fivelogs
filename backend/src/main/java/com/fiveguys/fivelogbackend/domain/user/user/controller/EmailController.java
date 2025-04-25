package com.fiveguys.fivelogbackend.domain.user.user.controller;

import com.fiveguys.fivelogbackend.domain.user.user.serivce.EmailService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
@Slf4j
public class EmailController {

    private final EmailService emailService;
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> sendCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if(userService.emailExists(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.fail("이미 사용 중인 이메일입니다."));
        }
        emailService.sendVerificationEmail(email);
        return ResponseEntity.ok(ApiResponse.success(null, "이메일 전송 성공"));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> body) {
        boolean result = emailService.verifyCode(body.get("email"), body.get("code"));
        if (result) {
            return ResponseEntity.ok(ApiResponse.success(null, "인증 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.success(null, "인증 실패"));
        }
    }


}