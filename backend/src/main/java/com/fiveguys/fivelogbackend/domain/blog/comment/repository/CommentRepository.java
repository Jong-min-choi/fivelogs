package com.fiveguys.fivelogbackend.domain.blog.comment.repository;

import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentWithBoardDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBoardId(Long boardId);
    //댓글을 보이게
    List<Comment> findByBoardIdAndParentIsNull(Long boardId);
    //댓글을 수정하고 싶을때
    Optional<Comment> findByIdAndUserId(Long id, Long userId);
    //페이징
    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId AND c.parent IS NULL")
    Page<Comment> findParentCommentsByBoardId(@Param("boardId") Long boardId, Pageable pageable);

    List<Comment> findByParentId(Long parentId);

//    //검색기능
//    @Query("""
//    SELECT new com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentWithBoardDto(
//        c.board.title,
//        c.comment
//    )
//    FROM Comment c
//    WHERE LOWER(c.comment) LIKE LOWER(CONCAT('%', :keyword, '%'))
//    AND c.deleted = false
//    ORDER BY c.board.title DESC
//""")
//    Page<CommentWithBoardDto> searchCommentsWithBoardTitle(@Param("keyword") String keyword, Pageable pageable);
}