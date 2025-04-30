package com.fiveguys.fivelogbackend.domain.user.user.controller;

import com.fiveguys.fivelogbackend.domain.user.user.dto.*;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.EmailService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserCommandService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import com.fiveguys.fivelogbackend.global.security.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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


    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> deleteUser() {
        rq.deleteCookie("accessToken");
        rq.deleteCookie("refreshToken");
//        userService.deleteUser();
        return ResponseEntity.ok(ApiResponse.success(null, "로그아웃 성공"));
    }
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeUserResponseDto>> getMe() {
        User user = rq.getActor();
        log.info("user {}", user);
        MeUserResponseDto me = userCommandService.getMeUserResponseDto(user.getId());
        return ResponseEntity.ok(ApiResponse.success(me,"인가 성공"));
    }

    @GetMapping("/me/mypage")
    public ResponseEntity<ApiResponse<MyPageDto>> getMypage(){
        User user = rq.getActor();
        MyPageDto myPageDto = userCommandService.getMyPageDto(user.getId());
        return ResponseEntity.ok(ApiResponse.success(myPageDto, "myPageDto 생성 성공"));
    }

    @GetMapping("/nickname-exists")
    public ResponseEntity<?> checkEmail(@RequestParam String nickname) {
        boolean exists = userService.nicknameExists(nickname);
        return ResponseEntity.ok(ApiResponse.success((Map.of("exists", exists)), "중복 확인 성공"));
    }

    @GetMapping("/{nickname}/blog")
    public ResponseEntity<ApiResponse<BlogOwnerDto>> getBlogOwnerInfo(@PathVariable("nickname") String nickname){
        BlogOwnerDto blogOwnerDto = userCommandService.getBlogOwnerDto(nickname);

        return ResponseEntity.ok(ApiResponse.success(blogOwnerDto,"success get owner info"));
    }

    //snslink

    @PostMapping("/me/mypage/sns")
    @Operation(summary = "SNS 링크 추가/수정", description = "SNS 링크를 추가하거나 수정합니다.")
    public ResponseEntity<ApiResponse<SNSLinkResponseDto>> saveSNSLink(
            @RequestBody SNSLinkRequestDto dto) {
        SNSLinkResponseDto responseDto = userService.updateSNSLink(dto);
        return ResponseEntity.ok(ApiResponse.success(responseDto, "SNS 링크가 저장되었습니다"));
    }
    @PutMapping("/me/mypage/introduce")
    public ResponseEntity<ApiResponse<String>> changeIntroduce(@RequestBody @Valid IntroduceUpdateRequest introduceUpdateRequest){
        User actor = rq.getActor();
        String updatedIntroduce = userService.updateIntroduce(actor.getId(), introduceUpdateRequest.getIntroduce());
        return ResponseEntity.ok(ApiResponse.success(updatedIntroduce, "introduce 업데이트 성공"));
    }
    @GetMapping("/nickname/{nickname}/email")
    public ResponseEntity<ApiResponse<UserEmailDto>> getEmail(@PathVariable("nickname") String nickname ){
        User user = userService.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 nickname 입니다."));
        UserEmailDto userEmailDto = new UserEmailDto(user.getEmail());
        return ResponseEntity.ok(ApiResponse.success(userEmailDto, "이메일 얻기 성공"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<ResetPasswordDto>> resetPassword(@RequestBody Map<String, String> body){
        String email = body.get("email");
        String code = body.get("code");
        ResetPasswordDto resetPasswordDto = userCommandService.resetPassword(email, code);
        return ResponseEntity.ok(ApiResponse.success(resetPasswordDto, "비밀번호 초기화 성공"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody ChangePasswordDto changePasswordDto){
        String newPassword = changePasswordDto.getNewPassword();
        String confirmPassword = changePasswordDto.getConfirmPassword();
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }
        User actor = rq.getActor();
        String currentPassword = changePasswordDto.getCurrentPassword();
        userService.changePassword(actor.getEmail(), currentPassword,newPassword);
        return ResponseEntity.ok(ApiResponse.success(null,"비밀번호 변경 성공"));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> leaveUser(@RequestBody PasswordRequestDto passwordRequestDto){
        User actor = rq.getActor();
        userService.deleteUser(actor.getId(), passwordRequestDto.getPassword());
        return ResponseEntity.ok(ApiResponse.success(null, "회원탈퇴 성공"));
    }

}
