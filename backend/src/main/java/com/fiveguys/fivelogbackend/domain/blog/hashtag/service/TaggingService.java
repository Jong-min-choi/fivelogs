package com.fiveguys.fivelogbackend.domain.blog.hashtag.service;

import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardHashtagDto;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.dto.HashtagCountDto;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.entity.Hashtag;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.entity.Tagging;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.repository.HashTagRepository;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.repository.TaggingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaggingService {
    private final TaggingRepository taggingRepository;
    private final HashTagRepository hashTagRepository;

    public List<Tagging> saveAllTaggingList(Board board, List<Hashtag> hastagList ){
        List<Tagging> taggingList = hastagList.stream()
                .map(hashtag -> new Tagging(board, hashtag))
                .toList();
        return taggingRepository.saveAll(taggingList);
    }

    public List<String> findAllHashtagNameByBoardId(Long boardId){
        return taggingRepository.findAllHashtagsByBoardId(boardId);
    }

    public Map<Long, List<String>> getHashtagsGroupedByBoardIds(List<Long> boardIds) {
        List<BoardHashtagDto> dtos = taggingRepository.findHashtagDtosByBoardIds(boardIds);

        return dtos.stream()
                .collect(Collectors.groupingBy(
                        BoardHashtagDto::getBoardId,
                        Collectors.mapping(BoardHashtagDto::getHashtagName, Collectors.toList())
                ));
    }

    @Transactional(readOnly = true)
    public List<HashtagCountDto> getHashtagCountDtoList(String nickname){
        return taggingRepository.findHashtagCountsByNickname(nickname);

    }

    @Transactional
    public void updateHashtags(Board board, List<String> hashtagNames) {
        Long boardId = board.getId();
        // 기존 태깅 전부 삭제
        taggingRepository.deleteAllByBoardId(boardId); // 👈 ID로 삭제!

        List<Tagging> newTaggings = hashtagNames.stream()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .distinct()
                .map(name -> {
                    Hashtag hashtag = hashTagRepository.findByName(name)
                            .orElseGet(() -> hashTagRepository.save(new Hashtag(name)));
                    return new Tagging(board, hashtag);
                }).toList();

        taggingRepository.saveAll(newTaggings);
    }
}
