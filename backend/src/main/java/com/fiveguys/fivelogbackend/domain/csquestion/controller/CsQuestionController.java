package com.fiveguys.fivelogbackend.domain.csquestion.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fiveguys.fivelogbackend.domain.csquestion.dto.ChatCompletionResponseDto;
import com.fiveguys.fivelogbackend.domain.csquestion.dto.ChatContent;
import com.fiveguys.fivelogbackend.domain.csquestion.service.CsQuestionService;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CsQuestionController {
    private final CsQuestionService csQuestionService;

    private static final int MAX_RETRIES = 3; // 최대 재요청 횟수

    @PostMapping("/chat/completion/content")
    public ResponseEntity<ApiResponse<?>> getChatCompletionContent(){
        int retries = 0;
        List<ChatContent> chatContent = new ArrayList<>();
        while(chatContent.isEmpty() && retries < MAX_RETRIES){
            try {
                ChatCompletionResponseDto chatCompletion = csQuestionService.getChatCompletion();
                chatContent = csQuestionService.getChatContent (chatCompletion);
            }catch (JsonProcessingException e){
                log.error("JSON 처리 중 오류 발생: {}", e.getMessage());
                retries++;
                continue; // 예외가 발생하면 재시도
            }
            retries++;
        }
        log.info("chatContents : {} ", chatContent);
        if(chatContent.size() != MAX_RETRIES ) {
            String errorMessage = String.format("문제를 생성을 위해 %d 시도 했지만 실패했습니다.", MAX_RETRIES);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.fail(errorMessage));
        }
        return ResponseEntity.ok(ApiResponse.success(chatContent, "문제 만들기 성공"));
    }
}
