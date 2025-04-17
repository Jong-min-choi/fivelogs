package com.fiveguys.fivelogbackend.domain.blog.comment.controller;

import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.service.CommentService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    //댓글 조회
    @Operation(summary = "댓글 단건 조회", description = "댓글 ID로 댓글을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentDto(id));
    }

    // 댓글 추가
    @PostMapping
    @Operation(summary = "댓글 작성", description = "댓글 ID로 댓글을 작성합니다")
    @ApiResponse(responseCode = "201", description = "댓글이 성공적으로 작성됨")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto) {
        CommentResponseDto savedComment = commentService.createComment(dto);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    //댓글 수정
    @Operation(summary = "댓글 수정", description = "댓글 ID로 댓글을 수정합니다.")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "201", description = "댓글이 수정되었습니다")
    public ResponseEntity<CommentResponseDto> editComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto dto
    ) {
        CommentResponseDto updatedComment = commentService.editComment(id, dto);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "댓글 ID로 댓글을 삭제합니다.")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "201", description = "댓글이 성공적으로 삭제되었습니다")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    //댓글 페이징
    @GetMapping("/board/{boardId}/page")
    @Operation(summary = "댓글 목록 조회 (페이징)", description = "게시글 ID 기준으로 페이지별 댓글을 가져옵니다.")
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByBoard(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<CommentResponseDto> comments = commentService.getCommentsByBoard(boardId, page, size);
        return ResponseEntity.ok(comments);
    }
}

