package com.fiveguys.fivelogbackend.domain.user.user.serivce;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final AuthTokenService authTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Rq rq;

    public Optional<User> findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }
    @Transactional
    public User join(String email, String password, String nickname, String provider) {
        userRepository
                .findByEmail(email)
                .ifPresent(member -> {
                    throw new RuntimeException("해당 email은 이미 사용중입니다.");
                });
        if(StringUtils.hasText(password)) password = passwordEncoder.encode(password);
        if (userRepository.existsByEmail(email)){
            throw new IllegalArgumentException("이미 가입한 email 입니다.");
        }
        int count = 1;
        if(userRepository.existsByNickname(nickname)){
            while(userRepository.existsByNickname(nickname + "_" + count)){
                count++;
            }
        }
        User user = User.builder()
                .email(email)
                .password(password)
                .introduce("안녕하세요 " + nickname + "입니다.")
                .SNSLink(null)
                .nickname(nickname)
                .provider(provider)
                .refreshToken(UUID.randomUUID().toString())
                .build();

        return userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new IllegalArgumentException("이미 탈퇴 된 ID입니다 ");
        userRepository.deleteById(id);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }

    public Optional<User> findById(long authorId) {
        return userRepository.findById(authorId);
    }

    public String genAccessToken(User user) {
        return authTokenService.genAccessToken(user);
    }

    public String genAuthToken(User user) {
        return user.getRefreshToken() + " " + genAccessToken(user);
    }

    public User getUserFromAccessToken(String accessToken) {
        Map<String, Object> payload = authTokenService.payload(accessToken);
        if (payload == null) return null;

        long id = (long) payload.get("id");
        String email = (String) payload.get("email");
        String nickname = (String) payload.get("nickname");

        User user = new User(id, email, nickname);

        return user;
    }

    public void modify(User user, @NotBlank String nickname) {
        user.setNickname(nickname);
    }

    @Transactional
    public User modifyOrJoin(String email,String password,String nickname, String provider) {
        Optional<User> opUser = findByEmail(email);

        if (opUser.isPresent()) {
            User user = opUser.get();

            modify(user, nickname);
            return user;
        }

        return join (email, password, nickname, provider);
    }

    public String login(String email, String password){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return rq.makeAuthCookie(user);
            }
        }
        throw new BadCredentialsException("Invalid email or password");
    }

    public Long countUsers(){
        return userRepository.count();
    }

}
