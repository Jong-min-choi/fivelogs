

package com.fiveguys.fivelogbackend.domain.blog.board.service;

import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.*;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.HashtagUtil;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.entity.Hashtag;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.entity.Tagging;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.repository.HashTagRepository;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.repository.TaggingRepository;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.service.HashTagService;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.service.TaggingService;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserService userService;
    private final BlogRepository blogRepository;
    private final HashTagService hashTagService;
    private final TaggingService taggingService;

    //    @Transactional
    public Board createBoard(CreateBoardRequestDto boardDto, Long id) {
        User user = userService.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다."));
        Blog blog = blogRepository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 blog id 입니다."));
        //hashtags 관련 로직이 이제 들어가야 함
        //ㅗㅁ
        List<String> hashtags = boardDto.getHashtags();
        List<Hashtag> savedHashTagList = hashTagService.createHashtags(hashtags);


        // hashtags name이 없다면 새로운 hashTags 이름을 생성
        // name 이 있다면 그 hash 태그를 가져오기
        // 가져온 것을

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .views(0L)
                .status(boardDto.getStatus())
                .user(user)
                .blog(blog)
                .build();
        Board savedBoard= boardRepository.save(board);

        taggingService.saveAllTaggingList(savedBoard, savedHashTagList);

        return savedBoard;
    }

    public CreateBoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return null;
    }

    public Page<Board> getBoardsAllWithUser(Pageable pageable) {
        return boardRepository.findAllWithUser(pageable);
    }
    public Page<Board> getBoardsAllWithNickname(String nickname,Pageable pageable) {
        return boardRepository.findAllWithUserByNickname(nickname, pageable);
    }

    // 앞에 자동으로 #붙이고 중복제거 GPT가 만들어줬어요ㅎ

//    public Page<Board> searchBoardsByHashtag(String tagName, Pageable pageable) {
//        String searchTagName = tagName.startsWith("#") ? tagName.trim() : "#" + tagName.trim();
//        return boardRepository.findByHashtagsContainingIgnoreCase(searchTagName, pageable);
//    }
    //dto로 바꿔야함
    public BoardPageResponseDto getBoardMainPageResponseDtoList (Page<Board> pageBoards){
        PageDto unitPageInit = PageUt.get10unitPageDto(pageBoards.getNumber(), pageBoards.getTotalPages());

        List<Board> boards = pageBoards.getContent();
        List<Long> boardIds = boards.stream().map(Board::getId).toList();

        // boardId -> [해시태그들] 맵
        Map<Long, List<String>> hashtagMap = taggingService.getHashtagsGroupedByBoardIds(boardIds);


        List<BoardSummaryDto> boardSummaryDtoList = boards.stream()
                .map(board -> {
                    List<String> hashtags = hashtagMap.getOrDefault(board.getId(), List.of());
                    return BoardSummaryDto.from(board, hashtags);
                })
                .toList();


        return new BoardPageResponseDto(boardSummaryDtoList, unitPageInit);
    }

    @Transactional
    public BoardDetailDto getBlogDetailDto( Long boardId){
        log.info("boardId {}", boardId);
        Board board = boardRepository.findWithUserById (boardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 boardId 입니다."));
        // board id를 갖고 있는 모든 taggins를 추출하고
        // taggings 의 tag id를 통해 모든 tag 찾기?
        List<String> hashtagNameList = taggingService.findAllHashtagNameByBoardId(board.getId());

        return BoardDetailDto.from(board, hashtagNameList);
    }

    @Transactional
    public SideBoardInfoDto sideBoardInfoDto(Long boardId, String nickname){
        Optional<Board> opPreBoard = boardRepository.findFirstByIdLessThanAndUser_NicknameOrderByIdDesc(boardId, nickname);
        SimpleBoardDto preBoardDto = null;
        if(opPreBoard.isPresent()){
            preBoardDto = SimpleBoardDto.from(opPreBoard.get());
        }


        Optional<Board> opNextBoard = boardRepository.findFirstByIdGreaterThanAndUser_NicknameOrderByIdAsc(boardId, nickname);
        SimpleBoardDto nextBoardDto = null;
        if(opNextBoard.isPresent()) {
            log.info("opNextBoard {}", opNextBoard);
            nextBoardDto = SimpleBoardDto.from(opNextBoard.get());
        }
        return new SideBoardInfoDto(preBoardDto, nextBoardDto);
    }

    public Long getBlogBoardCount(String nickname){
        return boardRepository.countByUser_Nickname(nickname);
    }

    public Long getAllBoardView(String nickname){
        return boardRepository.countView(nickname);
    }

    public Map<String, List<BoardSummaryDto>> getBoardMainPageByHashtags(List<Board> boardList) {
        List<Long> boardIds = boardList.stream()
                .map(Board::getId)
                .collect(Collectors.toList());

        // 1. boardId - hashtagName 매핑
        Map<Long, List<String>> hashtagsGroupedByBoardIds = taggingService.getHashtagsGroupedByBoardIds(boardIds);


        // 2. Board → BoardSummaryDto 변환
        List<BoardSummaryDto> boardSummaryList = boardList.stream()
                .map(board -> {
                    List<String> hashtags = hashtagsGroupedByBoardIds.getOrDefault(board.getId(), Collections.emptyList());
                    return BoardSummaryDto.from(board, hashtags);
                })
                .toList();

        // 3. hashtagName → BoardSummaryDto 리스트 매핑
        Map<String, List<BoardSummaryDto>> hashtagToBoardMap = new HashMap<>();
        for (BoardSummaryDto dto : boardSummaryList) {
            for (String hashtag : dto.getHashtags()) {
                hashtagToBoardMap
                        .computeIfAbsent(hashtag, key -> new ArrayList<>())
                        .add(dto);
            }
        }

        return hashtagToBoardMap;
    }


}
