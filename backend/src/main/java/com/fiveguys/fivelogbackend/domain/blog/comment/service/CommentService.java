package com.fiveguys.fivelogbackend.domain.blog.comment.service;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;

import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.repository.CommentRepository;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //댓글 조회
    @Transactional
    public Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getCommentDto(Long id) {
        return toDto(getComment(id));
    }

    //댓글 쓰기

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto dto) {
//        try {
//
//            // 게시글을 조회
//            Board board = boardRepository.findById(dto.getBoardId())
//                    .orElseThrow(() -> {
//                        String errorMessage = "게시글을 찾을 수 없습니다. Board ID: " + dto.getBoardId();
//                        // 로그에 출력
//                        System.out.println(errorMessage);
//                        return new RuntimeException(errorMessage);
//                    });

//            logger.info("b{} u{} ", dto.getBoardId(), dto.getUserId() );
            Board board = boardRepository.findById(dto.getBoardId())
                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            Comment comment = new Comment();
            comment.setComment(dto.getComment());
            comment.setUser(user);
            comment.setBoard(board);
            comment.setCreatedDate(LocalDateTime.now());
            comment.setUpdatedDate(LocalDateTime.now());

            return toDto(commentRepository.save(comment));
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("댓글을 작성하는 중 오류가 발생");
//        }
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto editComment(Long id, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        comment.setComment(dto.getComment());
        comment.setUpdatedDate(LocalDateTime.now());

        return toDto(commentRepository.save(comment));
    }

    //댓글 삭제
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public CommentResponseDto toDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setComment(comment.getComment());
        dto.setNickname(comment.getNickname());
        dto.setCreatedDate(comment.getCreatedDate());
        dto.setUpdatedDate(comment.getUpdatedDate());
        return dto;


    }
}

