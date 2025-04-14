package com.fiveguys.fivelogbackend.domain.blog.board.controller;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 관련 API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "게시글 작성")
        return ResponseEntity.ok(boardService.createBoard(requestDto));
    }

    @Operation(summary = "게시글 열람")
    }
}