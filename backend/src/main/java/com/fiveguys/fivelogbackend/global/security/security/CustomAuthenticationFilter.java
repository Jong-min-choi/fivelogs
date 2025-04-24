package com.fiveguys.fivelogbackend.global.security.security;


import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final Rq rq;

    record AuthTokens(String refreshToken, String accessToken) {
    }

    private AuthTokens getAuthTokensFromRequest() {
        String authorization = rq.getHeader("Authorization");
//        log.info("authorization {}", authorization);
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring("Bearer ".length());
            String[] tokenBits = token.split(" ", 2);

            if (tokenBits.length == 2)
                return new AuthTokens(tokenBits[0], tokenBits[1]);
        }

        String refreshToken = rq.getCookieValue("refreshToken");
        String accessToken = rq.getCookieValue("accessToken");

        if (refreshToken != null && accessToken != null)
            return new AuthTokens(refreshToken, accessToken);

        return null;
    }


    private void refreshAccessToken(User user) {
        String newAccessToken = userService.genAccessToken(user);
        rq.setHeader("Authorization", "Bearer " + user.getRefreshToken() + " " + newAccessToken);
        rq.setCookie("accessToken", newAccessToken);
    }

    private User refreshAccessTokenByRefreshToken(String refreshToken) {
        Optional<User> opMemberByRefreshToken = userService.findByRefreshToken(refreshToken);

        if (opMemberByRefreshToken.isEmpty()) {
            return null;
        }

        User user = opMemberByRefreshToken.get();

        refreshAccessToken(user);

        return user;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        if (!request.getRequestURI().startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }

        //  GET 요청은 모두 인증 없이 통과, 예외는 필요함 ex) mypage
        if ("GET".equalsIgnoreCase(method) && !request.getRequestURI().startsWith("/api/users") && !request.getRequestURI().startsWith("/api/attendance")) {
            filterChain.doFilter(request, response);
            return;
        }


        if (List.of("/api/users/login",  "/api/users/join").contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthTokens authTokens = getAuthTokensFromRequest();
        log.info("authTokens test {}", authTokens);
        if (authTokens == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = authTokens.refreshToken;
        String accessToken = authTokens.accessToken;


        User user = userService.getUserFromAccessToken(accessToken);
        if (user == null)
            user = refreshAccessTokenByRefreshToken(refreshToken);

        if (user != null)
            rq.setLogin(user);


        filterChain.doFilter(request, response);
    }
}