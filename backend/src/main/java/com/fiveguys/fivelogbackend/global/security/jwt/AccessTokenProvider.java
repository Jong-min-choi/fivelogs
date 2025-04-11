package com.fiveguys.fivelogbackend.global.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
//생성, 검증, 디코딩
public class AccessTokenProvider {

    private final byte[] accessSecret;

    private final ConcurrentHashMap<String, Boolean> invalidTokens = new ConcurrentHashMap<>();

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public AccessTokenProvider(@Value("${custom.jwt}") String SECRET) {
        this.accessSecret= SECRET.getBytes(StandardCharsets.UTF_8);
    }
    public String provideToken(Long userId, String role, String email){
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("email", email)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(accessSecret));

    }
    public Long getSubject(String token) {
        try {
            DecodedJWT decodedJWT = getDecodedJWT(token);
            if (decodedJWT == null) return null;
            log.info("claims {}", decodedJWT.getClaims());
            return Long.parseLong(decodedJWT.getSubject());
        } catch (JWTVerificationException | NumberFormatException e) {
            return null;
        }
    }
    public DecodedJWT getDecodedJWT(String token) {
        if (invalidTokens.containsKey(token)) {
            throw new JWTVerificationException("Token is invalidated");
        }
        // 페이로드는 인코딩, 서명은 해싱
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(accessSecret)).build().verify(token);
        return decodedJWT;
    }
    public void invalidateToken(String token) {
        invalidTokens.put(token, true);
    }
}
