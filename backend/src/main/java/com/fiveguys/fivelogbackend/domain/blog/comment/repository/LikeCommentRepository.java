package com.fiveguys.fivelogbackend.domain.blog.comment.repository;

import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.LikeComment;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    Optional<LikeComment> findByCommentAndUser(Comment comment, User user);
    @Query("SELECT l FROM LikeComment l WHERE l.comment = :comment AND l.user.id = :userId")
    Optional<LikeComment> findByCommentAndUserId(@Param("comment") Comment comment, @Param("userId") Long userId);
}