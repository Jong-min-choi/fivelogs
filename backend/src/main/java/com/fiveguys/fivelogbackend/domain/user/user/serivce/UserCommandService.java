package com.fiveguys.fivelogbackend.domain.user.user.serivce;


import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import com.fiveguys.fivelogbackend.domain.blog.comment.service.CommentService;
import com.fiveguys.fivelogbackend.domain.user.follow.repository.FollowRepository;
import com.fiveguys.fivelogbackend.domain.user.role.service.RoleService;
import com.fiveguys.fivelogbackend.domain.user.user.dto.BlogOwnerDto;
import com.fiveguys.fivelogbackend.domain.user.user.dto.MyPageDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final UserService userService;
    private final RoleService roleService;
    private final BlogService blogService;
    private final BoardService boardService;
    private final FollowRepository followRepository;

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
            roleService.createUserRole(user);
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
        User owner = userService.findByNickname(nickname).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 nickname 입니다."));

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
                .build();
    }

    @Transactional(readOnly = true)
    public MyPageDto getMyPageDto(Long userId){
        User user = userService.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 user Id 입니다."));
        Blog blog = blogService.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다."));

        return MyPageDto.from(user, blog.getTitle());
    }


}
