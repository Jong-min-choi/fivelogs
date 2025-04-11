package com.fiveguys.fivelogbackend.domain.blog.blog.service;

import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createBlog(User user) {
        int count = 0;
        String blogTitle = user.getEmail().split("@")[0] + ".log";

        if (blogRepository.existsByTitle(blogTitle)) {
            blogTitle = blogTitle.replace(".log", count + ".log");
        }
        Blog blog = Blog.builder()
                .title(blogTitle)
                .user(user)
                .build();
        blogRepository.save(blog);
    }

    // 다른 사람 블로그를 찾을떄?


    // 회원탈퇴시 블로그 삭제
    @Transactional
    public void deleteBlog(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        blogRepository.delete(user);
    }
}
