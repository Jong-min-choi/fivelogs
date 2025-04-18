package com.fiveguys.fivelogbackend.domain.user.user.serivce;

import com.fiveguys.fivelogbackend.domain.user.role.service.RoleService;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.jwt.JwtUt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {
    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;
    private RoleService roleService;

    @Value("${custom.accessToken.expirationSeconds}")
    private long accessTokenExpirationSeconds;

    String genAccessToken(User user) {
        long id = user.getId();
        String email = user.getEmail();
        return JwtUt.jwt.toString(
                jwtSecretKey,
                accessTokenExpirationSeconds,
                Map.of("id", id, "email", email )
        );
    }

    Map<String, Object> payload(String accessToken) {
        Map<String, Object> parsedPayload = JwtUt.jwt.payload(jwtSecretKey, accessToken);

        if (parsedPayload == null) return null;

        long id = (long) (Integer) parsedPayload.get("id");
        String email = (String) parsedPayload.get("email");

        return Map.of("id", id, "email", email);
    }
}
