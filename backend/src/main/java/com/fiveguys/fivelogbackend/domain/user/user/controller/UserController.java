package com.fiveguys.fivelogbackend.domain.user.user.controller;

import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import com.fiveguys.fivelogbackend.domain.user.user.dto.*;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserCommandService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import com.fiveguys.fivelogbackend.global.security.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "user 관련 API")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserCommandService userCommandService;
    private final Rq rq;

    @Operation(
            summary = "회원가입",
            description = "이메일과 비밀번호 닉네임을 입력하여 회원가입합니다..",
            tags = {"User"}
    )
    @PostMapping("/join")//리플래시토큰과 쿠키 생성이 필요함
    public ResponseEntity<ApiResponse<User>> joinUser(@RequestBody @Valid JoinUserDto joinUserDto){
        User join = userCommandService.joinAndCreateBlog(joinUserDto.getEmail(), joinUserDto.getPassword(), joinUserDto.getNickname(), "");
        ApiResponse<User> joinSuccess = ApiResponse.success( join,"join success");
        return ResponseEntity.ok(joinSuccess);
    }
    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호를 입력하여 로그인을 합니다.",
            tags = {"User"}
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequestDto user){
        String token = userService.login(user.getEmail(), user.getPassword());
        return ResponseEntity.ok(ApiResponse.success(token, "login success"));
    }
    //유저삭제
    @DeleteMapping("/logout")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal SecurityUser securityUser) {
        rq.deleteCookie("accessToken");
        rq.deleteCookie("refreshToken");
//        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeUserResponseDto>> getMe() {
        User user = rq.getActor();

        MeUserResponseDto me = MeUserResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
        return ResponseEntity.ok(ApiResponse.success(me,"인가 성공"));
    }

    @GetMapping("/me/mypage")
    public ResponseEntity<ApiResponse<MyPageDto>> getMypage(){
        User user = rq.getActor();
        MyPageDto myPageDto = userCommandService.getMyPageDto(user.getId());
        return ResponseEntity.ok(ApiResponse.success(myPageDto, "myPageDto 생성 성공"));
    }



    @GetMapping("/{nickname}/blog")
    public ResponseEntity<ApiResponse<BlogOwnerDto>> getBlogOwnerInfo(@PathVariable("nickname") String nickname){
        BlogOwnerDto blogOwnerDto = userCommandService.getBlogOwnerDto(nickname);

        return ResponseEntity.ok(ApiResponse.success(blogOwnerDto,"success get owner info"));
    }


}
