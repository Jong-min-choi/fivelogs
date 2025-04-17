package com.fiveguys.fivelogbackend.domain.blog.board.controller;

import com.fiveguys.fivelogbackend.domain.blog.board.dto.CreateBoardRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.CreateBoardResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import com.fiveguys.fivelogbackend.global.security.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    //user는 있고,
    @PostMapping("/blogs/{blogName}/boards/write")
    @Operation(summary = "게시글 작성")
    public ResponseEntity<CreateBoardResponseDto> writeBoard(@AuthenticationPrincipal SecurityUser securityUser,
                                                              @PathVariable("blogName") String blogName,
                                                              @RequestBody CreateBoardRequestDto createBoardRequestDto){
        Board board = boardService.createBoard(createBoardRequestDto);
        //여기서 boardid와 블로그 주소를 반환하자


        //성공하면 board id와

        return null;
    }

}