package com.fiveguys.fivelogbackend.domain.blog.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
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
                .toList();
    }
}