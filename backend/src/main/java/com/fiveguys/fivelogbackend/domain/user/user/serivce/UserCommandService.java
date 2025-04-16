package com.fiveguys.fivelogbackend.domain.user.user.serivce;


import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import com.fiveguys.fivelogbackend.domain.user.role.service.RoleService;
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


}
