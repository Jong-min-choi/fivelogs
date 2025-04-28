package com.fiveguys.fivelogbackend.domain.blog.blog.service;

import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogUpdateRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardDetailDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardHashtagDto;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardSummaryDto;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.repository.TaggingRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    // 검색기능 서비스 페이징해서 할거임
    public Page<Board> searchBoards(String searchContent, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCaseOrUser_NicknameContainingIgnoreCase(searchContent, searchContent, pageable);
        // 제목,작성자 두개로 검색할거라 searchContent가 두개임!
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
