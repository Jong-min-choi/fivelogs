package com.fiveguys.fivelogbackend.domain.blog.comment.controller;

import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.LikeRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.LikeResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.service.CommentService;
import com.fiveguys.fivelogbackend.global.pagination.PageDto;
import com.fiveguys.fivelogbackend.global.pagination.PageUt;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Slf4j
public class CommentController {
    private final CommentService commentService;
    private final Rq rq;

//    //유저가 쓴 댓글 조회
//    @GetMapping("/user/{userId}")
//    @Operation(summary = "유저 댓글 목록 조회", description = "유저 ID로 해당 유저의 댓글 목록을 조회합니다.")
//    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getCommentsByUser(
//            @PathVariable("userId") Long userId) {
//        List<CommentResponseDto> comments = commentService.getCommentsByUser(userId);
//        return ResponseEntity.ok(ApiResponse.success(comments, "유저의 댓글 목록을 성공적으로 불러왔습니다."));
//    }

    //블로그 속 댓글 조회
    @GetMapping("/boards/{boardId}")
    @Operation(summary = "게시글 댓글 조회", description = "게시글 ID로 댓글들을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getCommentsByBoard(
            @PathVariable("boardId") Long boardId
    ) {
        List<CommentResponseDto> comments = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(ApiResponse.success(comments, "게시글의 모든 댓글을 조회했습니다."));
    }

    // 댓글 작성
    @PostMapping("/boards/{boardId}")
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @PathVariable("boardId") Long boardId,
            @RequestBody CommentRequestDto dto) {
        dto.setBoardId(boardId);
        CommentResponseDto savedComment = commentService.createComment(dto);
        return new ResponseEntity<>(ApiResponse.success(savedComment, "댓글 작성을 성공하셨습니다")
                , HttpStatus.CREATED);
    }

    // 댓글 수정
    @PutMapping("/boards/{boardId}/{id}")
    @Operation(summary = "댓글 수정", description = "댓글 ID로 댓글을 수정합니다.")
    public ResponseEntity<ApiResponse<CommentResponseDto>> editComment(
            @PathVariable("id") Long commentId,
            @RequestBody CommentRequestDto dto) {
        CommentResponseDto updatedComment = commentService.editComment(commentId, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedComment, "댓글이 성공적으로 수정되었습니다."));
    }

    // 댓글 삭제
    @DeleteMapping("/boards/{boardId}/{id}")
    @Operation(summary = "댓글 삭제", description = "댓글 ID로 댓글을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable("id") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.success(null, "댓글이 성공적으로 삭제되었습니다."));
    }

    //댓글 좋아요 싫어요
    @PostMapping("/boards/{boardId}/{id}/reaction")
    public ResponseEntity<ApiResponse<LikeResponseDto>> reactToComment(
            @PathVariable("id") Long commentId,
            @RequestBody LikeRequestDto request) {
        LikeResponseDto response = commentService.reactToComment(commentId, request.isLike());
        return ResponseEntity.ok(ApiResponse.success(response, "리액션이 처리되었습니다."));
    }

    // 댓글 페이징
    @GetMapping("/boards/{boardId}/page")
    @Operation(summary = "댓글 목록 조회 (페이징)", description = "게시글 ID 기준으로 페이지별 댓글을 가져옵니다.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCommentsPageByBoard(
            @PathVariable("boardId") Long boardId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "createdDate,desc") String sort
    ) {
        Page<CommentResponseDto> commentPage = commentService.getCommentsByBoard(boardId, page, size, sort);


        int totalPage = commentPage.getTotalPages();
        PageDto pageDto = PageUt.get10unitPageDto(page - 1, totalPage);

        Map<String, Object> response = new HashMap<>();
        response.put("comments", commentPage.getContent());
        response.put("pageInfo", pageDto);

        return ResponseEntity.ok(ApiResponse.success(response, "페이지별 댓글을 조회했습니다."));
    }

    // 특정 댓글의 대댓글 목록 조회
    @GetMapping("/{parentId}/replies")
    @Operation(summary = "대댓글 조회", description = "특정 댓글의 대댓글(자식 댓글)을 조회합니다.")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getRepliesByParent(
            @PathVariable("parentId") Long parentId
    ) {
        List<CommentResponseDto> replies = commentService.getRepliesByParent(parentId);
        return ResponseEntity.ok(ApiResponse.success(replies, "대댓글을 조회했습니다."));
    }
}