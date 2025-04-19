package com.fiveguys.fivelogbackend.domain.blog.blog.controller;

import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardDetailDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardMainPageResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blogs")
@Slf4j
public class BlogController {
    private final BlogService blogService;
    private final BoardService boardService;

//     블로그 이메일 "@" 앞대가리로 조회 응답이니까 응답 dto 사용
    @GetMapping("/{userId}")
    public ResponseEntity<BlogResponseDto> findBlog(@PathVariable Long userId) {
        BlogResponseDto blog = blogService.findBlog(userId);
        return ResponseEntity.ok().build();
    }

    // 사용자 페이징
//    @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 페이지 단위로 조회합니다.")
//    @GetMapping("/users")
//    public  ResponseEntity<List<Board>> getBoards(
//            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
//            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
//        return ResponseEntity.ok(null);
//    }


    @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 페이지 단위로 조회합니다.") // 스웨거
    @GetMapping("/users")
    public ResponseEntity<Page<Board>> getBoards(@PageableDefault(page = 1, size = 10) Pageable pageable) {
        Page<Board> boards = boardService.getBoards(pageable);
        return ResponseEntity.ok(boards);
    }

    // 닉네임으로 조회?
    // 검색기능
    @Operation(summary = "게시물 검색", description = "제목, 닉네임으로 게시물 검색후 페이징")
    @GetMapping("search")
    public ResponseEntity<Page<Board>> searchBoards(
            @RequestParam String searchContent, // 검색 내용
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<Board> searchResult = blogService.searchBoards(searchContent, pageable);
        return ResponseEntity.ok(searchResult);
    }

//    @GetMapping("/{nickname}")
//    public ResponseEntity<ApiResponse<BoardMainPageResponseDto>> getBlogBoards(@PageableDefault(size=10, direction = Sort.Direction.DESC) Pageable pageable,
//                                                                               @PathVariable String nickname){
//        Page<Board> pagedBoards = boardService.getBoards(pageable);
//
//        log.info("pagedBoards {}", pagedBoards.getContent().size());
//        for (Board board : pagedBoards) {
//            log.info("pagedBoards {}", board.getTitle());
//        }
//        BoardMainPageResponseDto pageBoardDto =
//                boardService.getBoardMainPageResponseDtoList(pagedBoards);
//        log.info("BoardMainPageResponseDto : {}", pageBoardDto.getBoardDtoList().size());
//        return ResponseEntity.ok(ApiResponse.success(pageBoardDto, "게시판 페이징 성공"));
//    }



}
