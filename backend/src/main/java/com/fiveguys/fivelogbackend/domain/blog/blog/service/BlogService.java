package com.fiveguys.fivelogbackend.domain.blog.blog.service;

import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    // 회원가입하면 블로그가 생성됨
    @Transactional
    public void createBlog(User user) {
        String blogTitle = user.getEmail();
        Blog blog = Blog.builder()
                .title(blogTitle)
                .user(user)
                .build();
        blogRepository.save(blog);
    }
}
