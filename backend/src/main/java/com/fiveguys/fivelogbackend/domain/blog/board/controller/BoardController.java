package com.fiveguys.fivelogbackend.domain.blog.board.controller;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 관련 API")
public class BoardController {

    private final BoardService boardService;

//    @PostMapping
//    @Operation(summary = "게시글 작성")
//        return ResponseEntity.ok(boardService.createBoard(requestDto));
//    }

//    @Operation(summary = "게시글 열람")
//    }

    @GetMapping("/search-tag")
    @Operation(summary = "태그 기반 게시글 검색", description = "해시태그에 해당하는 게시글 검색")
    public ResponseEntity<Page<Board>> searchByTag(
            @RequestParam String tag,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<Board> boards = boardService.searchBoardsByHashtag(tag, pageable);
        return ResponseEntity.ok(boards);
    }
}