package com.fiveguys.fivelogbackend.domain.blog.comment.controller;

import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.service.CommentService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 단건 조회", description = "댓글 ID로 댓글을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        Comment comment = commentService.getComment(id);
        return ResponseEntity.ok(comment);
    }

    // 댓글 추가
    @PostMapping
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다")
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequestDto dto) {
        Comment savedComment = commentService.createComment(dto);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    //댓글 수정

    @Operation(summary = "댓글 수정", description = "댓글 ID로 댓글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Comment> editComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto dto
    ) {
        Comment updated = commentService.editComment(id, dto);
        return ResponseEntity.ok(updated);
    }

    // 댓글 삭제

    @Operation(summary = "댓글 삭제", description = "댓글 ID로 댓글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
//    public String deleteComment(@PathVariable Long id) {
//        commentService.deleteById(id);
//        return "댓글을 삭제했습니다";
//        }

}

