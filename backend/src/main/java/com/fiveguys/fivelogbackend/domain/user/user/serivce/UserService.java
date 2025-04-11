package com.fiveguys.fivelogbackend.domain.user.user.serivce;

import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.user.role.entity.Role;
import com.fiveguys.fivelogbackend.domain.user.role.entity.RoleType;
import com.fiveguys.fivelogbackend.domain.user.role.repository.RoleRepository;
import com.fiveguys.fivelogbackend.domain.user.user.dto.CreateUserDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    @Transactional
    public User createUser(CreateUserDto createUserDto){
        if(userRepository.findByEmail(createUserDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("email already exist");
        }
        User user = CreateUserDto.from(createUserDto);
        User savedUser = userRepository.save(user);
        log.info("user {}", user);
        return savedUser;
    }
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id))
            throw new IllegalArgumentException("이미 탈퇴 된 ID입니다 ");
        userRepository.deleteById(id);
    }
}
