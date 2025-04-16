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
import java.util.Optional;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //댓글 조회
    @Transactional
    public Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public CommentResponseDto getCommentDto(Long id) {
        return CommentResponseDto.fromEntity(getComment(id));
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
    public CommentResponseDto editComment(Long id, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        comment.setComment(dto.getComment());
        comment.setUpdatedDate(LocalDateTime.now());

        return CommentResponseDto.fromEntity(comment);
    }

    //댓글 삭제
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }


    //댓글 페이징
    public Page<CommentResponseDto> getCommentsByBoard(Long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Comment> commentPage = commentRepository.findByBoardId(boardId, pageable);

        return commentPage.map(CommentResponseDto::fromEntity);
    }
}