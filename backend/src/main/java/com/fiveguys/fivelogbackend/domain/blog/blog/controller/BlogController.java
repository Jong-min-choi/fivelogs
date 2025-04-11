package com.fiveguys.fivelogbackend.domain.blog.blog.controller;

import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

//     블로그 이메일 "@" 앞대가리로 조회 응답이니까 응답 dto 사용
    @GetMapping("/{userId}")
    public ResponseEntity<BlogResponseDto> findBlog(@PathVariable Long userId) {
        BlogResponseDto blog = blogService.findBlog(userId);
        return ResponseEntity.ok().build();
    }

    // 회원탈퇴시 블로그 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long userId) {
        blogService.deleteBlog(userId);
        return ResponseEntity.ok().build();
    }

}
