package com.fiveguys.fivelogbackend.domain.blog.blog.controller;

import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogUpdateRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardPageResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import com.fiveguys.fivelogbackend.domain.user.user.dto.BlogOwnerDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blogs")
@Slf4j
public class BlogController {
    private final BlogService blogService;
    private final BoardService boardService;
    private final UserService userService;



    @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 페이지 단위로 조회합니다.") // 스웨거
    @GetMapping("/users")
    public ResponseEntity<Page<Board>> getBoards(@PageableDefault(page = 1, size = 10) Pageable pageable) {
        Page<Board> boards = boardService.getBoardsAllWithUser(pageable);
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

    @GetMapping("/{nickname}")
    public ResponseEntity<ApiResponse<BoardPageResponseDto>> getBlogMainInfo(@PageableDefault(size=10, sort = "createdDate",direction = Sort.Direction.DESC) Pageable pageable,
                                                                           @PathVariable(name = "nickname") String nickname){
        Page<Board> pagedBoards = boardService.getBoardsAllWithNickname(nickname,pageable);

        BoardPageResponseDto pageBoardDto =
                boardService.getBoardMainPageResponseDtoList(pagedBoards);


        log.info("BoardMainPageResponseDto : {}", pageBoardDto.getBoardDtoList().size());
        return ResponseEntity.ok(ApiResponse.success(pageBoardDto, "게시판 페이징 성공"));
    }

    @PutMapping("/{userNickname}")
    public ResponseEntity<ApiResponse<BlogResponseDto>> updateBlog(
            @PathVariable String userNickname,
            @RequestBody @Valid BlogUpdateRequestDto dto) {

        BlogResponseDto updatedBlog = blogService.updateBlog(userNickname, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedBlog, "블로그 수정을 성공"));
    }
}
