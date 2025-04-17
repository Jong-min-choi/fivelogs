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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

//    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    //유저가 쓴 댓글 조회
//    @Transactional(readOnly = true)
//    public List<CommentResponseDto> getCommentsByUser(Long userId) {
//        // 유저 조회
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        // 유저의 모든 댓글 조회
//        List<Comment> comments = commentRepository.findByUserId(userId);
//
//        return comments.stream()
//                .map(CommentResponseDto::fromEntity)
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByBoard(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        return comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    //댓글 쓰기

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto dto) {
//            logger.info("b{} u{} ", dto.getBoardId(), dto.getUserId() );

        //게시글 조회
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        //사용자 조회
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 댓글 생성
        Comment comment = new Comment();
        comment.setComment(dto.getComment());
        comment.setUser(user);
        comment.setBoard(board);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setUpdatedDate(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.fromEntity(savedComment);
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto editComment(Long commentId, Long userId, CommentRequestDto dto) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new RuntimeException("해당 유저의 댓글을 찾을 수 없습니다."));

        comment.setComment(dto.getComment());
        comment.setUpdatedDate(LocalDateTime.now());

        return CommentResponseDto.fromEntity(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않거나 삭제 권한이 없습니다."));

        commentRepository.delete(comment);
    }

    //댓글 페이징
    public Page<CommentResponseDto> getCommentsByBoard(Long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Comment> commentPage = commentRepository.findByBoardId(boardId, pageable);

        return commentPage.map(CommentResponseDto::fromEntity);
    }
}