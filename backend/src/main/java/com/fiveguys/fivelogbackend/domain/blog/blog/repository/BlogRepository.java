package com.fiveguys.fivelogbackend.domain.blog.blog.repository;

import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    void deleteByUser(User user); //    void delete(User user); << 안 됨

//    void delete(Optional<User> user); 잘못된 문법

    public boolean existsByTitle(String title);

    public Optional<Blog> findByUserId(Long userId);

    public Optional<Blog> findByTitle(String userId);



}
