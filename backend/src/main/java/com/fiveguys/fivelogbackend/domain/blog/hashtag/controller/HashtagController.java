package com.fiveguys.fivelogbackend.domain.blog.hashtag.controller;

import com.fiveguys.fivelogbackend.domain.blog.hashtag.dto.HashtagCountDto;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.service.HashTagService;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.service.TaggingService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
public class HashtagController {

    private final TaggingService taggingService;

    @GetMapping("/{nickname}")
    public ResponseEntity<ApiResponse<List<HashtagCountDto>>>  getHashtagListByNickname(@PathVariable("nickname") String nickname){
        List<HashtagCountDto> hashtagCountDtoList = taggingService.getHashtagCountDtoList(nickname);

        return ResponseEntity.ok (ApiResponse.success(hashtagCountDtoList,"hashTag 리스트 만들기 성공"));
    }

}
