package com.fiveguys.fivelogbackend.domain.blog.board.controller;

import com.fiveguys.fivelogbackend.domain.blog.board.dto.CreateBoardRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.CreateBoardResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 관련 API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "게시글 작성")
    public ResponseEntity<CreateBoardResponseDto> createBoard(@RequestBody CreateBoardRequestDto requestDto) {
        return ResponseEntity.ok(boardService.createBoard(requestDto));
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "게시글 열람")
    public ResponseEntity<CreateBoardResponseDto> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }
}