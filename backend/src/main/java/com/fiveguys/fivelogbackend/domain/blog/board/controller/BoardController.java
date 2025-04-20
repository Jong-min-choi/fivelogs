package com.fiveguys.fivelogbackend.domain.blog.board.controller;

import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.*;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import com.fiveguys.fivelogbackend.global.security.security.SecurityUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 관련 API")
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final Rq rq;

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
    //user는 있고,
    @PostMapping
    @Operation(summary = "게시글 작성")
    public ResponseEntity<ApiResponse<CreateBoardResponseDto>> createBoard(
            @RequestBody CreateBoardRequestDto createBoardRequestDto){
        log.info("요청이 오나? {}", createBoardRequestDto);
        Board board = boardService.createBoard(createBoardRequestDto, rq.getActor().getId());
        //여기서 boardid와 블로그 주소를 반환하자
        CreateBoardResponseDto responseDto = new CreateBoardResponseDto(board.getId(), board.getTitle());
        //성공하면 board id와

        return ResponseEntity.ok(ApiResponse.success(responseDto, "게시글 작성 성공"));
    }

    @GetMapping
    @Operation(summary = "게시글 조회")
    //4*4 로간다.
    public ResponseEntity<ApiResponse<BoardMainPageResponseDto>> getBoards(@PageableDefault(size=12, direction = Sort.Direction.DESC) Pageable pageable){
        Page<Board> pagedBoards = boardService.getBoards(pageable);

        log.info("pagedBoards {}", pagedBoards.getContent().size());
        for (Board board : pagedBoards) {
            log.info("pagedBoards {}", board.getTitle());
        }
        BoardMainPageResponseDto pageBoardDto =
                boardService.getBoardMainPageResponseDtoList(pagedBoards);
        log.info("BoardMainPageResponseDto : {}", pageBoardDto.getBoardDtoList().size());
        return ResponseEntity.ok(ApiResponse.success(pageBoardDto, "게시판 페이징 성공"));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardDetailDto>> getBlogWithBoards(
            @PathVariable("boardId")  Long boardId ){

        BoardDetailDto boardDetailDto = boardService.getBlogDetailDto(boardId);

        return ResponseEntity.ok().body(ApiResponse.success(boardDetailDto, "board 생성 성공"));
    }

    @GetMapping("/{boardId}/author/{nickname}")
    public ResponseEntity<ApiResponse<SideBoardInfoDto>> getSidesBoardInfo(@PathVariable("boardId") Long boardId,
                                                                           @PathVariable("nickname") String nickname){
        return ResponseEntity.ok(ApiResponse.success(boardService.sideBoardInfoDto(boardId, nickname), "전, 후 게시판 조회 성공"));
    }


}