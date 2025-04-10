package com.fiveguys.fivelogbackend.domain.user.user.serivce;

import com.fiveguys.fivelogbackend.domain.user.role.entity.Role;
import com.fiveguys.fivelogbackend.domain.user.role.entity.RoleType;
import com.fiveguys.fivelogbackend.domain.user.role.repository.RoleRepository;
import com.fiveguys.fivelogbackend.domain.user.user.dto.AddUserDto;
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
    private final RoleRepository roleRepository;

    @Transactional
    public String addUser(AddUserDto addUserDto){
        if(userRepository.findByEmail(addUserDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("email already exist");
        }
        User user = AddUserDto.from(addUserDto);
        User savedUser = userRepository.save(user);
        log.info("user {}", user);
//        roleRepository
        //블로그 등록까지 해야함 이건 나중에??..

        return "User registered successfully";
    }
}
