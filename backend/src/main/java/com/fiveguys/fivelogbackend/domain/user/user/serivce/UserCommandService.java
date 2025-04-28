package com.fiveguys.fivelogbackend.domain.user.user.serivce;


import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import com.fiveguys.fivelogbackend.domain.blog.comment.service.CommentService;
import com.fiveguys.fivelogbackend.domain.image.config.ImageProperties;
import com.fiveguys.fivelogbackend.domain.image.entity.Image;
import com.fiveguys.fivelogbackend.domain.image.service.ImageService;
import com.fiveguys.fivelogbackend.domain.user.follow.repository.FollowRepository;
import com.fiveguys.fivelogbackend.domain.user.role.entity.RoleType;
import com.fiveguys.fivelogbackend.domain.user.role.service.RoleService;
import com.fiveguys.fivelogbackend.domain.user.user.dto.*;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.entity.UserStatus;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.access.InvalidInvocationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCommandService {
    private final UserService userService;
    private final RoleService roleService;
    private final BlogService blogService;
    private final BoardService boardService;
    private final FollowRepository followRepository;
    private final EmailService emailService;
    private final ImageService imageService;

    /*
    두 가지 방법
    1. user와 result를 동시에 리턴 ,< 별로임
    2.
     */
    @Transactional
    public User joinAndCreateBlog(String email,String password, String nickname, String provider){
        Optional<User> opUser = userService.findByEmail(email);
        if(!opUser.isPresent()){
            User user = userService.join (email, password,  nickname, provider);
            userService.modify(user, nickname);
            //권한 주기
            roleService.createUserRole(user, RoleType.USER);
            //블로그 가입
            blogService.createBlog(user);
            return user;
        }
        User user = opUser.get();
        userService.modify(user, nickname);
        return user;
    }
    //
    /*
    private String nickname; v
    private String myIntroduce; v
    private Long totalBoard;
    private Long totalView;
    private Long totalCommand;
    private Long totalFollower;
    private Long totalFollowing;
     */
    @Transactional(readOnly = true)
    public BlogOwnerDto getBlogOwnerDto(String nickname){
        User owner = userService.findByNicknameWithProfileImage(nickname).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 nickname 입니다."));

        Long blogBoardCount = boardService.getBlogBoardCount(nickname);
        Long viewCount = boardService.getAllBoardView(nickname);

        long followCount = followRepository.countByFollowId(owner.getId());
        long followedCount = followRepository.countByFollowedId(owner.getId());

        return BlogOwnerDto.builder()
                .id(owner.getId())
                .nickname(owner.getNickname())
                .introduce(owner.getIntroduce())
                .boardCount(blogBoardCount)
                .viewCount(viewCount)
                .followingCount(followCount)
                .followerCount(followedCount)
                .profileImageUrl(imageService.getImageProfileUrl(owner.getProfileImage()))
                .build();
    }

    @Transactional(readOnly = true)
    public MyPageDto getMyPageDto(Long userId){
        User user = userService.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 user Id 입니다."));
        Blog blog = blogService.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다."));


        String imageUrl = imageService.getImageProfileUrl(user.getProfileImage());
        return MyPageDto.from(user, blog.getTitle(), imageUrl);
    }


    // 임의의 비밀번호 설정
    @Transactional
    public ResetPasswordDto resetPassword(String email, String code){
        boolean result = emailService.verifyCode(email, code);
        if(!result) throw new IllegalArgumentException ("email 혹은 code가 잘못되었습니다.");
        String resetPassword = emailService.generateRandomCode();
        String newPassword = userService.changePassword(email, resetPassword);
        return new ResetPasswordDto(newPassword);
    }


    @Transactional(readOnly = true)
    public MeUserResponseDto getMeUserResponseDto(Long userId){
        User user = userService.findByIdWithProfileImage(userId);


        return MeUserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImageUrl(imageService.getImageProfileUrl(user.getProfileImage()))
                .build();
    }


    @Transactional
    public UserStatus changeUserStatus(Long userId, UserStatus userStatus){
        List<RoleType> userRoleType = roleService.getUserRoleType(userId);
        for (RoleType roleType : userRoleType) {
            if(roleType == RoleType.ADMIN) throw new AccessDeniedException("관리자는 정지할 수 없습니다."); // 403 에러 발생

        }
        User user = userService.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId 입니다."));
        user.setUserStatus(userStatus);

        return userStatus;
    }

}
