package com.fiveguys.fivelogbackend.domain.user.user.serivce;


import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import com.fiveguys.fivelogbackend.domain.user.role.entity.Role;
import com.fiveguys.fivelogbackend.domain.user.role.entity.RoleType;
import com.fiveguys.fivelogbackend.domain.user.role.repository.RoleRepository;
import com.fiveguys.fivelogbackend.domain.user.role.service.RoleService;
import com.fiveguys.fivelogbackend.domain.user.user.dto.CreateUserDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final UserService userService;
    private final RoleService roleService;
    private final BlogService blogService;

    @Transactional
    public String createUser(CreateUserDto createUserDto){
        User user = userService.createUser(createUserDto);
        roleService.createUserRole();
        //블로그 가입
        blogService.createBlog(user);
        return "User registered successfully";
    }
}
