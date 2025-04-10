package com.fiveguys.fivelogbackend.domain.blog.blog.service;

import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    // 회원가입하면 블로그가 생성됌
    public void createBlog(User user) {
        String blogTitle = user.getEmail().split("@")[0];

        Blog blog = Blog.builder()
                .title(blogTitle)
                .user(user)
                .build();

//        Blog blog = new Blog();
//        blog.setTitle(blogTitle);
//        blog.setUser(user);

        blogRepository.save(blog);
    }

    // 회원탈퇴시 블로그 삭제
    public void deleteBlog(User user) {
        blogRepository.delete(user);
    }
}
