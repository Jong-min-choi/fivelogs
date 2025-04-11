package com.fiveguys.fivelogbackend.domain.blog.blog.repository;

import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    void deleteByUser(User user); //    void delete(User user); << 안 됨

    void delete(Optional<User> user);
}
