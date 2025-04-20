package com.fiveguys.fivelogbackend.domain.blog.blog.service;

import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardDetailDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final BoardRepository boardRepository;

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

    // 검색기능 서비스 페이징해서 할거임
    public Page<Board> searchBoards(String searchContent, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCaseOrUser_NicknameContainingIgnoreCase(searchContent, searchContent, pageable);
        // 제목,작성자 두개로 검색할거라 searchContent가 두개임!
    }




}
