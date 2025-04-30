package com.fiveguys.fivelogbackend.domain.csquestion.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ChatGptProperties {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.api-url-chat}")
    private String apiChatUrl;

    private String systemMessage = "당신은 컴퓨터공학 퀴즈 문제를 생성하는 AI입니다. 주제는 운영체제, 네트워크, 자료구조, 알고리즘, 데이터베이스 등으로 구성되며, 문제는 한국어로 작성합니다. 각 문제는 5지선다형 객관식이어야 하며, 정답은 번호 입니다. 정답 번호 범위는 0부터 4 입니다. 응답은 항상 JSON 형식으로 해주세요. JSON은 \"question\", \"options\", \"answer\" 세 개의 key를 포함해야 합니다.";

    private String userMessage = "컴퓨터 공학 전반에 걸쳐 랜덤으로 5지선다형 문제 3개 만들어줘. 정답 번호의 범위는 반드시 0부터 4까지여야 해. JSON 배열 형식으로 운영체제, 네트워크, 자료구조, 알고리즘, 데이터베이스 중 하나입니다.";

    private final String gptModel = "gpt-4.1-nano";
}
