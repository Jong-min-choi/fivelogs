package com.fiveguys.fivelogbackend.domain.user.user.serivce;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;

    public void sendVerificationEmail(String email) {
        String code = generateRandomCode();
        storeCodeInRedis(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드입니다.");
        message.setText("인증 코드: " + code);
        mailSender.send(message);
    }

    private void storeCodeInRedis(String email, String code) {
        redisTemplate.opsForValue().set(email, code, Duration.ofMinutes(5));
    }

    public boolean verifyCode(String email, String code) {
        String savedCode = redisTemplate.opsForValue().get(email);
        return code.equals(savedCode);
    }

    private String generateRandomCode() {
        return String.valueOf((int)(Math.random() * 899999) + 100000); // 6자리 숫자
    }
}