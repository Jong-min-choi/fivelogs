package com.fiveguys.fivelogbackend.domain.blog.board.controller;

import com.fiveguys.fivelogbackend.domain.blog.board.dto.*;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import com.fiveguys.fivelogbackend.domain.blog.board.service.TrendingBoardService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시글 관련 API")
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final Rq rq;

    //user는 있고,
    @PostMapping
    @Operation(summary = "게시글 작성")
    public ResponseEntity<ApiResponse<CreateBoardResponseDto>> createBoard(
            @RequestBody CreateBoardRequestDto createBoardRequestDto){
        Board board = boardService.createBoard(createBoardRequestDto, rq.getActor().getId());
        //여기서 boardid와 블로그 주소를 반환하자
        CreateBoardResponseDto responseDto = new CreateBoardResponseDto(board.getId(), board.getTitle());
        //성공하면 board id와
        return ResponseEntity.ok(ApiResponse.success(responseDto, "게시글 작성 성공"));
    }

    // 게시물 수정
    @PutMapping("/{boardId}/edit")
    @Operation(summary = "게시글 수정", description = "게시글 ID로 게시글을 수정합니다.")
    public ResponseEntity<ApiResponse<CreateBoardResponseDto>> editBoard(
            @PathVariable("boardId") Long boardId,
            @RequestBody CreateBoardRequestDto dto) {
        CreateBoardResponseDto updatedBoard = boardService.editBoard(boardId, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedBoard, "게시물이 성공적으로 수정되었습니다."));
    }


    // 게시물 삭제
    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시물 삭제", description = "게시물 ID를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok(ApiResponse.success(null, "게시물이 성공적으로 삭제되었습니다."));
    }


    @GetMapping
    @Operation(summary = "홈 페이지 게시글 조회")
    public ResponseEntity<ApiResponse<BoardPageResponseDto>> getBoards(@PageableDefault(size=12, sort = "createdDate",direction = Sort.Direction.DESC) Pageable pageable){
        Page<Board> pagedBoards = boardService.getBoardsAllWithUser(pageable);

//        log.info("pagedBoards {}", pagedBoards.getContent().size());
        for (Board board : pagedBoards) {
            log.info("pagedBoards {}", board.getTitle());
        }
        BoardPageResponseDto pageBoardDto =
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

    @PostMapping("/{boardId}/views")
    public ResponseEntity<ApiResponse<Void>> increaseViewCount(@PathVariable(name = "boardId") Long boardId){
        boardService.increaseViewCount(boardId);

        return ResponseEntity.ok(ApiResponse.success(null, "조회수 증가 성공"));
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<BoardSummaryDto>>> getTrendingBoards(){
        List<BoardSummaryDto> trendingBoards = boardService.getTrendingBoards();
        return ResponseEntity.ok().body(ApiResponse.success(trendingBoards, "트랜딩 게시판 조회 성공"));
    }

//    @GetMapping("/search")
//    @Operation(summary = "게시글 제목 검색", description = "제목에 keyword가 포함된 게시글 검색 (공개글만)")
//    public ResponseEntity<ApiResponse<List<BoardSearchResponseDto>>> searchBoardsByTitle(
//            @RequestParam("keyword") String keyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        List<BoardSearchResponseDto> result = boardService.searchBoardsByTitle(keyword, page, size);
//        return ResponseEntity.ok(ApiResponse.success(result, "게시물 제목으로 검색합니다."));
//    }
    @Operation(summary = "게시글 제목 검색", description = "제목에 keyword가 포함된 게시글 검색 (공개글만)")
    @GetMapping("/search")
    public ResponseEntity<Page<BoardSearchResponseDto>> searchBoards(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        return ResponseEntity.ok(boardService.searchByKeyword(keyword, pageable));
    }


}