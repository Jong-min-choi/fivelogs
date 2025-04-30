package com.fiveguys.fivelogbackend.domain.blog.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
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

    private static final String TRENDING_BOARD_KEY_PREFIX = "trending:boards:";

    private String getTodayKey() {
        LocalDate today = LocalDate.now(); // 오늘 날짜 (ex: 2025-04-30)
        return TRENDING_BOARD_KEY_PREFIX + today.toString(); // trending:boards:2025-04-30
    }

    /**
     * 조회수를 1 증가
     */
    public void increaseViewCount(Long boardId) {
        String key = getTodayKey();
        redisTemplate.opsForZSet().incrementScore(key, boardId.toString(), 1);
        // 하루짜리 TTL도 설정 (선택)
        redisTemplate.expire(key, Duration.ofDays(2)); // 이틀 뒤 삭제
    }

    /**
     * 오늘 기준 상위 인기 게시물 조회
     */
    public List<Long> getTopTrendingBoards(int limit) {
        String key = getTodayKey();

        Set<String> boardIds = redisTemplate.opsForZSet()
                .reverseRange(key, 0, limit - 1);

        if (boardIds == null) return List.of();

        return boardIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}