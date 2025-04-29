package com.fiveguys.fivelogbackend.domain.blog.blog.service;

import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogUpdateRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.repository.TaggingRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final BoardRepository boardRepository;
    private final TaggingRepository taggingRepository;
    private final Rq rq;

    @Transactional
    public void createBlog(User user) {
        String blogTitle = user.getNickname() + ".log";

        Blog blog = Blog.builder()
                .title(blogTitle)
                .user(user)
                .build();
        blogRepository.save(blog);
    }

    // 다른 사람 블로그를 찾을떄?
    public BlogResponseDto findBlog(Long userId) {
        Blog blog = blogRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException());
        return BlogResponseDto.fromEntity(blog);
    }

    public Optional<Blog> findByUserId(Long userId){
        return blogRepository.findByUserId(userId);
    }

    // 블로그 제목 수정
    @Transactional
    public BlogResponseDto updateBlog(String userNickname, BlogUpdateRequestDto dto) {
        User user = rq.getActor(); // 현재 로그인한 유저

        Blog blog = blogRepository.findByUserNickname(userNickname)
                .orElseThrow(() -> new RuntimeException("블로그를 찾을 수 없습니다."));

        // 블로그 주인인지 확인
        if (!blog.getUser().getNickname().equals(user.getNickname())) {
            throw new RuntimeException("블로그를 수정할 권한이 없습니다.");
        }

        blog.setTitle(dto.getTitle());

        return BlogResponseDto.builder()
                .title(blog.getTitle())
                .ownerNickname(blog.getUser().getNickname())
                .build();
    }
}
