

package com.fiveguys.fivelogbackend.domain.blog.board.service;

import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.*;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.HashtagUtil;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.pagination.PageDto;
import com.fiveguys.fivelogbackend.global.pagination.PageUt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final BlogRepository blogRepository;
    //    @Transactional
    public Board createBoard(CreateBoardRequestDto dto, Long id) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다."));
        Blog blog = blogRepository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 blog id 입니다."));
        Board board = Board.builder()
                .title(dto.getTitle())
                .hashtags(HashtagUtil.joinHashtags(dto.getHashtags()))
                .content(dto.getContent())
                .views(0L)
                .status(dto.getStatus())
                .user(user)
                .blog(blog)
                .build();

        return boardRepository.save(board);
    }

    public CreateBoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return null;
    }

    public Page<Board> getBoards(Pageable pageable) {
        return boardRepository.findAllWithUser(pageable);
    }

    // 앞에 자동으로 #붙이고 중복제거 GPT가 만들어줬어요ㅎ

    public Page<Board> searchBoardsByHashtag(String tagName, Pageable pageable) {
        String searchTagName = tagName.startsWith("#") ? tagName.trim() : "#" + tagName.trim();
        return boardRepository.findByHashtagsContainingIgnoreCase(searchTagName, pageable);
    }
    //dto로 바꿔야함
    public BoardMainPageResponseDto getBoardMainPageResponseDtoList (Page<Board> pageBoards){
        PageDto unitPageInit = PageUt.get10unitPageDto(pageBoards.getNumber(), pageBoards.getTotalPages());
        List<BoardSummaryDto> boardSummaryDtoList = pageBoards.getContent()
                .stream()
                .map(BoardSummaryDto::from)
                .collect(Collectors.toList());
        return new BoardMainPageResponseDto(boardSummaryDtoList, unitPageInit);
    }

    @Transactional
    public BoardDetailDto getBlogDetailDto( Long boardId){
        log.info("boardId {}", boardId);
        Board board = boardRepository.findWithUserById (boardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 boardId 입니다."));
        return BoardDetailDto.from(board);
    }


}
