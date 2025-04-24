package com.fiveguys.fivelogbackend.domain.blog.comment.service;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;

import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.LikeResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.LikeComment;
import com.fiveguys.fivelogbackend.domain.blog.comment.repository.CommentRepository;

import com.fiveguys.fivelogbackend.domain.blog.comment.repository.LikeCommentRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;

import com.fiveguys.fivelogbackend.global.rq.Rq;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        User user = rq.getActor();
        List<Comment> topLevelComments = commentRepository.findByBoardIdAndParentIsNull(boardId);
        return topLevelComments.stream()
                .map(comment -> {
                    Boolean likedByMe = likeCommentRepository.findByCommentAndUser(comment, user)
                            .map(LikeComment::isLiked)
                            .orElse(null);
                    return CommentResponseDto.fromEntityWithReaction(comment, likedByMe);
                })
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
    public LikeResponseDto reactToComment(Long commentId, boolean isLike) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        User user = rq.getActor();
        Optional<LikeComment> existing = likeCommentRepository.findByCommentAndUser(comment, user);
        Boolean finalLikedByMe = null;

        if (existing.isPresent()) {
            LikeComment like = existing.get();
            boolean currentIsLike = like.isLiked();

            if (currentIsLike == isLike) {
                // 같은 반응을 다시 누르면 취소
                likeCommentRepository.delete(like);
                if (currentIsLike) {
                    comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
                } else {
                    comment.setDislikeCount(Math.max(0, comment.getDislikeCount() - 1));
                }
            } else {
                // 다른 반응으로 변경
                like.setLiked(isLike);
                if (isLike) {
                    // 싫어요 -> 좋아요로 변경
                    comment.setDislikeCount(Math.max(0, comment.getDislikeCount() - 1));
                    comment.setLikeCount(comment.getLikeCount() + 1);
                } else {
                    // 좋아요 -> 싫어요로 변경
                    comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
                    comment.setDislikeCount(comment.getDislikeCount() + 1);
                }
                finalLikedByMe = isLike;
                likeCommentRepository.save(like);
            }
        } else {
            // 새로운 반응
            LikeComment newReaction = LikeComment.builder()
                    .comment(comment)
                    .user(user)
                    .liked(isLike)
                    .build();
            likeCommentRepository.save(newReaction);

            if (isLike) {
                comment.setLikeCount(comment.getLikeCount() + 1);
            } else {
                comment.setDislikeCount(comment.getDislikeCount() + 1);
            }
            finalLikedByMe = isLike;
        }

        commentRepository.save(comment);

        return LikeResponseDto.builder()
                .likeCount(comment.getLikeCount())
                .dislikeCount(comment.getDislikeCount())
                .likedByMe(finalLikedByMe)
                .build();
    }

    //댓글 페이징
    public Page<CommentResponseDto> getCommentsByBoard(Long boardId, int page, int size, String sort) {
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        // 허용된 정렬 필드만 제한
        List<String> allowedSortFields = List.of("createdDate", "likeCount", "dislikeCount");
        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException("지원하지 않는 정렬 필드입니다: " + sortBy);
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
        Page<Comment> parentComments = commentRepository.findParentCommentsByBoardId(boardId, pageable);

        return parentComments.map(comment -> {
            // 현재 로그인 유저가 좋아요/싫어요 눌렀는지 확인
            Boolean likedByMe = likeCommentRepository.findByCommentAndUser(comment, rq.getActor())
                    .map(LikeComment::isLiked)
                    .orElse(null);

            // 좋아요 상태 포함하여 DTO 생성
            return CommentResponseDto.fromEntityWithReaction(comment, likedByMe);
        });
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getRepliesByParent(Long parentId) {
        List<Comment> replies = commentRepository.findByParentId(parentId);
        return replies.stream()
                .map(reply -> {
                    // 로그인 유저의 반응 정보 포함
                    Boolean likedByMe = likeCommentRepository.findByCommentAndUser(reply, rq.getActor())
                            .map(LikeComment::isLiked)
                            .orElse(null);
                    return CommentResponseDto.fromEntityWithReaction(reply, likedByMe);
                })
                .toList();
    }


    // 좋아요/싫어요 수 조절 유틸
    private void adjustCount(Comment comment, boolean isLike, int delta) {
        if (isLike) comment.setLikeCount(comment.getLikeCount() + delta);
        else comment.setDislikeCount(comment.getDislikeCount() + delta);
    }


}