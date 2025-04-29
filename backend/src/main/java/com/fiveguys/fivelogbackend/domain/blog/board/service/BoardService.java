

package com.fiveguys.fivelogbackend.domain.blog.board.service;

import com.fiveguys.fivelogbackend.domain.blog.blog.entity.Blog;
import com.fiveguys.fivelogbackend.domain.blog.blog.repository.BlogRepository;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.*;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.entity.Hashtag;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.service.HashTagService;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.service.TaggingService;
import com.fiveguys.fivelogbackend.domain.image.entity.Image;
import com.fiveguys.fivelogbackend.domain.image.service.ImageService;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import com.fiveguys.fivelogbackend.global.pagination.PageDto;
import com.fiveguys.fivelogbackend.global.pagination.PageUt;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
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
    private final TrendingBoardService trendingBoardService;
    private final ImageService imageService;
    private final Rq rq;

    @Transactional
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
    @Transactional

    public CreateBoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return null;
    }
    @Transactional // 모든 게시판 조회 용
    public Page<Board> getBoardsAllWithUser(Pageable pageable) {
        return boardRepository.findAllWithUser(pageable);
    }

    @Transactional
    public Page<Board> getPublicBoardsAllWithUser(Pageable pageable){
        return boardRepository.findAllPublicWithUser(pageable);

    }

    @Transactional // 전체 조회, 블로그 주인 용
    public Page<Board> getBoardsAllWithNickname(String nickname,Pageable pageable) {
        return boardRepository.findAllWithUserByNickname(nickname, pageable);
    }
    @Transactional // 전체 조회, 블로그 주인 용
    public Page<Board> getPublicBoardsAllWithNickname(String nickname,Pageable pageable) {
        return boardRepository.findAllPublicWithUserByNickname(nickname, pageable);
    }


    // 앞에 자동으로 #붙이고 중복제거 GPT가 만들어줬어요ㅎ

//    public Page<Board> searchBoardsByHashtag(String tagName, Pageable pageable) {
//        String searchTagName = tagName.startsWith("#") ? tagName.trim() : "#" + tagName.trim();
//        return boardRepository.findByHashtagsContainingIgnoreCase(searchTagName, pageable);
//    }
    //dto로 바꿔야함
    @Transactional
    public BoardPageResponseDto getBoardMainPageResponseDtoList (Page<Board> pageBoards){
        PageDto unitPageInit = PageUt.get10unitPageDto(pageBoards.getNumber(), pageBoards.getTotalPages());

        List<Board> boards = pageBoards.getContent();
        List<BoardSummaryDto> boardSummaryDtoList = getBoardSummaryDtos(boards);


        return new BoardPageResponseDto(boardSummaryDtoList, unitPageInit);
    }

    public List<BoardSummaryDto> getBoardSummaryDtos(List<Board> boards) {
        List<Long> boardIds = boards.stream().map(Board::getId).toList();

        // boardId -> [해시태그들] 맵
        Map<Long, List<String>> hashtagMap = taggingService.getHashtagsGroupedByBoardIds(boardIds);


        List<BoardSummaryDto> boardSummaryDtoList = boards.stream()
                .map(board -> {
                    List<String> hashtags = hashtagMap.getOrDefault(board.getId(), List.of());
                    return BoardSummaryDto.from(board, hashtags);
                })
                .toList();
        return boardSummaryDtoList;
    }

    @Transactional
    public BoardDetailDto getBlogDetailDto( Long boardId){
        log.info("boardId {}", boardId);
        Board board = boardRepository.findWithUserAndProfileImageById(boardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 boardId 입니다."));
        // board id를 갖고 있는 모든 taggins를 추출하고
        // taggings 의 tag id를 통해 모든 tag 찾기?
        List<String> hashtagNameList = taggingService.findAllHashtagNameByBoardId(board.getId());

        Image profileImage = board.getUser().getProfileImage();
        String imageProfileUrl = imageService.getImageProfileUrl(profileImage);

        return BoardDetailDto.from(board, hashtagNameList, imageProfileUrl);
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
    @Transactional(readOnly = true)

    public Long getBlogBoardCount(String nickname){
        return boardRepository.countByUser_Nickname(nickname);
    }
    @Transactional(readOnly = true)

    public Long getAllBoardView(String nickname){
        return boardRepository.countView(nickname);
    }

    @Transactional
    public void increaseViewCount(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 boardId 입니다."));
        board.setViews(board.getViews() + 1);
        Board savedBoard = boardRepository.save(board);
        trendingBoardService.increaseViewCount(savedBoard.getId());
    }

    @Transactional(readOnly = true)
    public List<BoardSummaryDto> getTrendingBoards(){
        List<Long> boardIds = trendingBoardService.getTopTrendingBoards(10);
        List<Board> boards = boardRepository.findAllById(boardIds);
        Map<Long, Board> boardMap = boards.stream()
                .collect(Collectors.toMap(Board::getId, Function.identity()));

        List<Board> sortedBoards = boardIds.stream()
                .map(boardMap::get)
                .filter(Objects::nonNull) // 혹시 모를 null 방지
                .toList();

        return getBoardSummaryDtos(sortedBoards);
    }
    /*

       List<Long> boardIds = boards.stream().map(Board::getId).toList();

        // boardId -> [해시태그들] 맵
        Map<Long, List<String>> hashtagMap = taggingService.getHashtagsGroupedByBoardIds(boardIds);


        List<BoardSummaryDto> boardSummaryDtoList = boards.stream()
                .map(board -> {
                    List<String> hashtags = hashtagMap.getOrDefault(board.getId(), List.of());
                    return BoardSummaryDto.from(board, hashtags);
                })
                .toList();

     */

    //게시물 수정
    @Transactional
    public CreateBoardResponseDto editBoard(Long boardId, CreateBoardRequestDto dto) {
        User user = rq.getActor();

        Board board = boardRepository.findByIdAndUserId(boardId, user.getId())
                .orElseThrow(() -> new RuntimeException("해당 유저의 게시물을 찾을 수 없습니다."));

        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setStatus(dto.getStatus());
        taggingService.updateHashtags(board, dto.getHashtags());

        return CreateBoardResponseDto.fromEntity(board);
    }



    //게시물 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        User user = rq.getActor();

        Board board = boardRepository.findByIdAndUserId(boardId, user.getId())
                .orElseThrow(() -> new RuntimeException("해당 댓글이 존재하지 않거나 삭제 권한이 없습니다."));

//        //전체다 삭제하고 싶을떄
        boardRepository.delete(board);

        // 소프트 딜리트 처리
//        board.setTitle("삭제된 게시물입니다.");
//        board.setContent("");
//        board.setStatus(BoardStatus.PRIVATE); // 비공개 처리
    }

//    public List<BoardSearchResponseDto> searchBoardsByTitle(String keyword, int page, int size) {
//        Page<Board> boards = boardRepository.searchByTitle(keyword, PageRequest.of(page, size));
//
//        return boards.stream()
//                .map(BoardSearchResponseDto::fromEntity)
//                .toList();
//    }

//    @Transactional(readOnly = true)
//    public Page<BoardSearchResponseDto> searchByKeyword(String keyword, Pageable pageable) {
//        Page<Board> boardPage = boardRepository.searchByTitleOrHashtag(keyword, pageable);
//
//        return boardPage.map(BoardSearchResponseDto::fromEntity);
//    }

    @Transactional(readOnly = true)
    public Page<Board> searchByTitleOrHashtagPaging(String title, Set<String> hashtags, Long lastId, Pageable pageable) {
        return boardRepository.searchByTitleOrHashtagPaging(title, hashtags, lastId, pageable);
    }


}
