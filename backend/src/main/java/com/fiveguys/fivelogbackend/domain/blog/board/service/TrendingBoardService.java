package com.fiveguys.fivelogbackend.domain.blog.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrendingBoardService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String TRENDING_BOARD_KEY = "trending:boards";

    public void increaseViewCount(Long boardId) {
        redisTemplate.opsForZSet().incrementScore(TRENDING_BOARD_KEY, boardId.toString(), 1);
    }

    public List<Long> getTopTrendingBoards(int limit) {

        Set<String> boardIds = redisTemplate.opsForZSet()
                .reverseRange(TRENDING_BOARD_KEY, 0, limit - 1);

        if (boardIds == null) return List.of();

        return boardIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());


    }
}