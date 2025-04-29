package com.fiveguys.fivelogbackend.domain.blog.blog.controller;

import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.dto.BlogUpdateRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.blog.service.BlogService;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardPageResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blogs")
@Slf4j
public class BlogController {
    private final BlogService blogService;
    private final BoardService boardService;
    private final Rq rq;



    @GetMapping("/{nickname}")
    public ResponseEntity<ApiResponse<BoardPageResponseDto>> getBlogMainInfo(@PageableDefault(size=10,page = 0, sort = "createdDate",direction = Sort.Direction.DESC) Pageable pageable,
                                                                             @RequestParam(required = false) String tag,
                                                                           @PathVariable(name = "nickname") String nickname){
        Page<Board> pagedBoards;
        log.info("nickname {}", nickname);
        if(!Objects.isNull(rq.getActor()) && rq.getActor().getNickname().equals(nickname)){
            pagedBoards = boardService.getBoardsByNicknameAndOptionalTag(nickname,tag,pageable);
        } else {
            pagedBoards = boardService.getPublicBoardsAllWithNickname(nickname,tag,pageable);
        }

        BoardPageResponseDto pageBoardDto =
                boardService.getBoardMainPageResponseDtoList(pagedBoards);

        log.info("BoardMainPageResponseDto : {}", pageBoardDto.getBoardDtoList().size());
        return ResponseEntity.ok(ApiResponse.success(pageBoardDto, "게시판 페이징 성공"));
    }

    @PutMapping("/{userNickname}/title")
    public ResponseEntity<ApiResponse<BlogResponseDto>> updateBlog(
            @PathVariable String userNickname,
            @RequestBody @Valid BlogUpdateRequestDto dto) {

        BlogResponseDto updatedBlog = blogService.updateBlog(userNickname, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedBlog, "블로그 수정을 성공"));
    }
}
