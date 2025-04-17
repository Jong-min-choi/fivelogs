package com.fiveguys.fivelogbackend.domain.blog.comment.repository;

import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long userId);
    List<Comment> findByBoardId(Long boardId);
    //댓글을 수정하고 싶을때
    Optional<Comment> findByIdAndUserId(Long id, Long userId);
    //특정 게시물에서 댓글을 삭제하고 싶을때
    Optional<Comment> findByIdAndUserIdAndBoardId(Long id, Long userId, Long boardId);
    //페이징
    Page<Comment> findByBoardId(Long boardId, Pageable pageable);
}