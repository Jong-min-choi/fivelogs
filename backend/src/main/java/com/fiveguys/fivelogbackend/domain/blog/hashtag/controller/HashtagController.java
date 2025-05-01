package com.fiveguys.fivelogbackend.domain.blog.hashtag.controller;

import com.fiveguys.fivelogbackend.domain.blog.hashtag.dto.HashtagCountDto;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.service.HashTagService;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.service.TaggingService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/hashtags")
@RequiredArgsConstructor
@Slf4j
public class HashtagController {

    private final TaggingService taggingService;
    private final Rq rq;
    @GetMapping("/{nickname}")
    public ResponseEntity<ApiResponse<List<HashtagCountDto>>>  getHashtagListByNickname(@PathVariable("nickname") String nickname){

        List<HashtagCountDto> hashtagCountDtoList;

        if(!Objects.isNull(rq.getActor()) && rq.getActor().getNickname().equals(nickname)){
            hashtagCountDtoList = taggingService.getHashtagCountDtoList(nickname);
        } else{
            hashtagCountDtoList = taggingService.getHashtagCountDtoListByPublicBoard(nickname);
        }


        return ResponseEntity.ok (ApiResponse.success(hashtagCountDtoList,"hashTag 리스트 만들기 성공"));
    }

}
