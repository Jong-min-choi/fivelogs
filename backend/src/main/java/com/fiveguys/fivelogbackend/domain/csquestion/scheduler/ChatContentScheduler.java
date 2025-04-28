package com.fiveguys.fivelogbackend.domain.csquestion.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fiveguys.fivelogbackend.domain.csquestion.dto.ChatCompletionResponseDto;
import com.fiveguys.fivelogbackend.domain.csquestion.dto.ChatContent;
import com.fiveguys.fivelogbackend.domain.csquestion.service.CsQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatContentScheduler {

    private final CsQuestionService csQuestionService;

    // 매일 자정마다 실행 (필요시 cron 변경)
    @Scheduled(cron = "0 35 20 * * *", zone = "Asia/Seoul")
    //30초 마다 실행
//    @Scheduled(cron = "*/30 * * * * *")
    public void generateChatContents() throws JsonProcessingException {
        int retries = 0;
        List<ChatContent> chatContent = new ArrayList<>();
        log.info("Scheduled 실행: chatgpt api를 통한 문제 생성");
        while(chatContent.isEmpty() && retries < 3){
            try {
                ChatCompletionResponseDto chatCompletion = csQuestionService.getChatCompletion();
                chatContent = csQuestionService.getChatContent(chatCompletion);
            } catch (JsonProcessingException e){
                log.error("JSON 처리 중 오류 발생: {}", e.getMessage());
                retries++;
                continue;
            }
            retries++;
        }

        if(chatContent.isEmpty()) {
            log.error("문제 생성 실패 - 최대 시도 횟수 초과");
            return;
        }

        // 문제 저장
        csQuestionService.saveChatContents(chatContent); //문제 저장.......
        log.info("문제 생성 및 저장 완료: {}", chatContent);
    }

}
