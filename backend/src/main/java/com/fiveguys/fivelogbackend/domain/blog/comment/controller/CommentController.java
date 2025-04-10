package com.fiveguys.fivelogbackend.domain.blog.comment.controller;

import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class CommentController {
    private final CommentService commentService;

}

