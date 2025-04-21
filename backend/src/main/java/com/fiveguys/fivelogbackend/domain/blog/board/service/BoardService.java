package com.fiveguys.fivelogbackend.domain.blog.board.service;

import com.fiveguys.fivelogbackend.domain.blog.board.dto.CreateBoardRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.board.dto.CreateBoardResponseDto;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public CreateBoardResponseDto createBoard(CreateBoardRequestDto dto) {
        Board board = Board.builder()
                .title(dto.getTitle())
                .hashtags(dto.getHashtags())
                .content(dto.getContent())
                .status(dto.getStatus())
                .build();

        Board saved = boardRepository.save(board);
        return null;
    }

    public CreateBoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return null;
    }

    public Page<Board> getBoards(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // 앞에 자동으로 #붙이고 중복제거 GPT가 만들어줬어요ㅎ

        public static String cleanHashtags(String rawInput) {
            return Arrays.stream(rawInput.split("\\s+")) //   \\s	 공백, 탭, 줄바꿈 등 모든 공백 문자 +는 1개이상을 뜻함 공백이 여러개여도 1개로 취급!
                    .map(tag -> tag.startsWith("#") ? tag : "#" + tag)
                    .distinct()
                    .collect(Collectors.joining(" "));
        }


}