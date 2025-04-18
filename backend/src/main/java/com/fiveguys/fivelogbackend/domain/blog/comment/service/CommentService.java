package com.fiveguys.fivelogbackend.domain.blog.comment.service;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;

import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.LikeComment;
import com.fiveguys.fivelogbackend.domain.blog.comment.repository.CommentRepository;

import com.fiveguys.fivelogbackend.domain.blog.comment.repository.LikeCommentRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;

import com.fiveguys.fivelogbackend.global.rq.Rq;
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

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final Rq rq;

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


    //댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByBoard(Long boardId) {
        List<Comment> topLevelComments = commentRepository.findByBoardIdAndParentIsNull(boardId);
        return topLevelComments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    //댓글 쓰기

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto dto) {
        // 게시글 조회
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 현재 로그인한 유저
        User user = rq.getActor();

        // 댓글 생성
        Comment comment = new Comment();
        comment.setComment(dto.getComment());
        comment.setUser(user);
        comment.setBoard(board);
        comment.setCreatedDate(LocalDateTime.now());
        comment.setUpdatedDate(LocalDateTime.now());

        if (dto.getParentId() != null) {
            Comment parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));
            comment.setParent(parent);
        }

        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.fromEntity(savedComment);
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto editComment(Long commentId, CommentRequestDto dto) {
        User user = rq.getActor();

        Comment comment = commentRepository.findByIdAndUserId(commentId, user.getId())
                .orElseThrow(() -> new RuntimeException("해당 유저의 댓글을 찾을 수 없습니다."));

        // 삭제하면 수정 불가능하게
        if (comment.isDeleted()) {
            throw new RuntimeException("삭제된 댓글은 수정할 수 없습니다.");
        }


        comment.setComment(dto.getComment());
        comment.setUpdatedDate(LocalDateTime.now());

        return CommentResponseDto.fromEntity(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        User user = rq.getActor();

        Comment comment = commentRepository.findByIdAndUserId(commentId, user.getId())
                .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않거나 삭제 권한이 없습니다."));


        //전체다 삭제하고 싶을떄
        //commentRepository.delete(comment);

        comment.setDeleted(true);
        comment.setComment("삭제된 댓글입니다.");
    }

    //댓글 좋아요
    @Transactional
    public void reactToComment(Long commentId, boolean isLike) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        User user = rq.getActor();

        Optional<LikeComment> existing = likeCommentRepository.findByCommentAndUser(comment, user);

        if (existing.isPresent()) {
            LikeComment like = existing.get();
            if (like.isLiked() == isLike) {
                likeCommentRepository.delete(like);
                adjustCount(comment, isLike, -1);
            } else {
                like.setLiked(isLike);
                adjustCount(comment, !isLike, -1);
                adjustCount(comment, isLike, 1);
            }
        } else {
            likeCommentRepository.save(LikeComment.builder()
                    .comment(comment)
                    .user(user)
                    .liked(isLike)
                    .build());

            adjustCount(comment, isLike, 1);
        }
    }


    //댓글 페이징
    public Page<CommentResponseDto> getCommentsByBoard(Long boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Comment> commentPage = commentRepository.findByBoardId(boardId, pageable);

        return commentPage.map(CommentResponseDto::fromEntity);
    }

    // 좋아요/싫어요 수 조절 유틸
    private void adjustCount(Comment comment, boolean isLike, int delta) {
        if (isLike) comment.setLikeCount(comment.getLikeCount() + delta);
        else comment.setDislikeCount(comment.getDislikeCount() + delta);
    }

}