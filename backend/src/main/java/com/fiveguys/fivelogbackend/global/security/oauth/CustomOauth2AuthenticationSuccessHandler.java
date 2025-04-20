package com.fiveguys.fivelogbackend.global.security.oauth;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.app.AppConfig;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;
    private final Rq rq;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
// rq.getActor() 시큐리티에서 로그인된 회원정보 가지고 오기
        User actor = userService.findById(rq.getActor().getId()).get();
        // 토큰 발급
        rq.makeAuthCookie (actor);
        logger.info("state test:");

        String redirectUrl = request.getParameter("state");
        // 프론트 주소로 redirect
        response.sendRedirect(redirectUrl);    }
}
