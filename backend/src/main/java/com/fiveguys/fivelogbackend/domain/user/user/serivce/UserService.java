package com.fiveguys.fivelogbackend.domain.user.user.serivce;

import com.fiveguys.fivelogbackend.domain.image.config.ImageProperties;
import com.fiveguys.fivelogbackend.domain.image.service.ImageService;
import com.fiveguys.fivelogbackend.domain.user.user.dto.*;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.user.user.entity.SNSLinks;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.entity.UserStatus;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jmx.access.InvalidInvocationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.pagination.sync.PaginatedResponsesIterator;

import java.time.LocalDateTime;
import java.util.List;
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
        if (StringUtils.hasText(password)) password = passwordEncoder.encode(password);
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입한 email 입니다.");
        }
        int count = 1;
        if (userRepository.existsByNickname(nickname)) {
            while (userRepository.existsByNickname(nickname + "_" + count)) {
                count++;
            }
        }
        User user = User.builder()
                .email(email)
                .password(password)
                .introduce("안녕하세요 " + nickname + "입니다.")
                .snsLink(null)
                .nickname(nickname)
                .provider(provider)
                .refreshToken(UUID.randomUUID().toString())
                .userStatus(UserStatus.NORMAL)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id, String password) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent())
            throw new IllegalArgumentException("존재하지 않는 ID 입니다.");
        User user = userOptional.get();

        if(user.getProvider().equals(password)){
            userRepository.deleteById(id);
            return;
        }
        if(!StringUtils.hasText(password)){
            throw new IllegalArgumentException("비밀번호를 입력하세요.");
        }
        String encodedPassword = user.getPassword();
        if(passwordEncoder.matches(password, encodedPassword)){
            userRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }
    public Optional<User> findByNicknameWithProfileImage(String nickname){
        return userRepository.findByNicknameWithProfileImage(nickname);
    }

    public Optional<User> findById(long authorId) {
        return userRepository.findById(authorId);
    }
    @Transactional(readOnly = true)
    public User findByIdWithProfileImage(Long id) {
        return userRepository.findByIdWithProfileImage(id)
                .orElseThrow(() -> new InvalidInvocationException("존재하지 않는 userId 입니다."));
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
    public User modifyOrJoin(String email, String password, String nickname, String provider) {
        Optional<User> opUser = findByEmail(email);

        if (opUser.isPresent()) {
            User user = opUser.get();

            modify(user, nickname);
            return user;
        }

        return join(email, password, nickname, provider);
    }

    public String login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(user.getUserStatus() == UserStatus.BANNED) {
                throw new AccessDeniedException("정지된 회원입니다."); // 403 에러 발생
            }
            if (passwordEncoder.matches(password, user.getPassword())) {
                return rq.makeAuthCookie(user);
            }
        }
        throw new BadCredentialsException("Invalid email or password");
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean nicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public Long countUsers() {
        return userRepository.count();
    }


    //SNSLink 추가/수정
    @Transactional
    public SNSLinkResponseDto updateSNSLink(SNSLinkRequestDto dto) {
        User user = userRepository.findById(rq.getActor().getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//        System.out.println("유저 닉네임: " + user.getNickname());
        SNSLinks snsLinks = user.getSnsLink();
//        System.out.println("snsLinks is null? " + (snsLinks == null));

        if (snsLinks == null) {
            // 링크가 없으면 새로 생성
            snsLinks = SNSLinks.builder()
                    .githubLink(dto.getGithubLink())
                    .instagramLink(dto.getInstagramLink())
                    .twitterLink(dto.getTwitterLink())
                    .build();
            user.setSnsLink(snsLinks);
        } else {
            // 링크가 있으면 업데이트
            snsLinks.setGithubLink(dto.getGithubLink());
            snsLinks.setInstagramLink(dto.getInstagramLink());
            snsLinks.setTwitterLink(dto.getTwitterLink());
        }

        userRepository.save(user);
        return SNSLinkResponseDto.fromEntity(user.getSnsLink());
    }


    @Transactional
    public String changePassword(String email, String newPassword){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email입니다."));
        String encode = passwordEncoder.encode(newPassword);
        user.setPassword(encode);
        return newPassword;
    }

    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        if (!StringUtils.hasText(currentPassword) || !StringUtils.hasText(newPassword)) {
            throw new IllegalArgumentException("비밀번호는 필수 입력입니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 email입니다."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 새 비밀번호가 같습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }
    @Transactional(readOnly = true)
    public List<User> findAllByProfileImageId(Long imageId){
        return userRepository.findAllByProfileImageId(imageId);
    }

    @Transactional
    public String updateIntroduce(Long userId, String introduce){
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId 입니다."));
        if(StringUtils.hasText(introduce)) user.setIntroduce(introduce);
        else throw new NullPointerException("introduce가 비어있습니다.");
        return introduce;
    }

}
