package com.fiveguys.fivelogbackend.domain.user.user.serivce;

import com.fiveguys.fivelogbackend.domain.user.user.dto.JoinUserDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Transactional
    public User createUser(JoinUserDto joinUserDto){
        if(userRepository.findByEmail(joinUserDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("email already exist");
        }
        User user = JoinUserDto.from(joinUserDto);
        User savedUser = userRepository.save(user);
        log.info("user {}", user);
        return savedUser;
    }
    public Optional<User> findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }

    public User join(String email, String password, String nickname, String provider) {
        userRepository
                .findByEmail(email)
                .ifPresent(member -> {
                    throw new RuntimeException("해당 email은 이미 사용중입니다.");
                });
        if(StringUtils.hasText(password)) password = passwordEncoder.encode(password);
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


    public User modifyOrJoin(String email, String nickname, String provider) {
        Optional<User> opMember = findByEmail(email);

        if (opMember.isPresent()) {
            User user = opMember.get();

            modify(user, nickname);
            return user;
        }

        return join (email, "", nickname, provider);
    }
}
