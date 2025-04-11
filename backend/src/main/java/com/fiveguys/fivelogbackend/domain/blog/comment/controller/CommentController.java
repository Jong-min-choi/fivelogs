package com.fiveguys.fivelogbackend.domain.blog.comment.controller;

import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;


    // 댓글 추가
    public String createComment(@PathVariable Long boardId
            , Comment comment) {
//        commentService.firstComment(comment, boardId);
        return "댓글을 작성했습니다";
    }

    //댓글 수정

    public String editComment(@PathVariable Long boardId
            , Long id
            , Comment comment) {
//        commentService.editComment(comment);
        return "댓글을 수정했습니다";
    }

    public String deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return "댓글을 삭제했습니다";


    }
}

