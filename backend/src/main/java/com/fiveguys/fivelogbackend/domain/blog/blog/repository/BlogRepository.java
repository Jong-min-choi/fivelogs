package com.fiveguys.fivelogbackend.domain.blog.blog.repository;

import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

}
