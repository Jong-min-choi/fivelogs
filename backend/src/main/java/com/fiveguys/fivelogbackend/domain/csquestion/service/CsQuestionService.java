package com.fiveguys.fivelogbackend.domain.csquestion.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiveguys.fivelogbackend.domain.csquestion.config.ChatGptProperties;
import com.fiveguys.fivelogbackend.domain.csquestion.dto.ChatCompletionRequestDto;
import com.fiveguys.fivelogbackend.domain.csquestion.dto.ChatCompletionResponseDto;
import com.fiveguys.fivelogbackend.domain.csquestion.dto.ChatContent;
import com.fiveguys.fivelogbackend.domain.csquestion.dto.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsQuestionService {
    private final ChatGptProperties chatGptProperties;

    private final RestTemplate restTemplate;

    @Transactional
    public ChatCompletionResponseDto getChatCompletion() throws JsonProcessingException {
        String apiUrl = chatGptProperties.getApiChatUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatGptProperties.getApiKey());

        Message systemMessage = Message.builder()
                .content(chatGptProperties.getSystemMessage())
                .role("system")
                .build();
        Message userMessage = Message.builder()
                .role("user")
                .content(chatGptProperties.getUserMessage())
                .build();

        List<Message> messages = new ArrayList<Message>();
        messages.add(systemMessage);
        messages.add(userMessage);
        ChatCompletionRequestDto completionRequestDto = ChatCompletionRequestDto.builder()
                .model(chatGptProperties.getGptModel())
                .max_tokens(1500)
                .temperature(0.7f)
                .messages(messages)
                .build();

        HttpEntity<ChatCompletionRequestDto> chatEntity = new HttpEntity<>(completionRequestDto, headers);
        ObjectMapper mapper = new ObjectMapper();


        String body = restTemplate.exchange(apiUrl, HttpMethod.POST, chatEntity, String.class).getBody();
        log.info("json data body : {}", body);

        ChatCompletionResponseDto response = mapper.readValue(body, ChatCompletionResponseDto.class);
        log.info("json data response : {}", response);

        return response;
    }

    public List<ChatContent> getChatContent(ChatCompletionResponseDto chatCompletionResponseDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<ChatContent> chatContents = null;
        if(!chatCompletionResponseDto.getChoices().isEmpty()){
            String content = chatCompletionResponseDto.getChoices().get(0).getMessage().getContent();
            String cleanedContent = content.replaceAll("(?m)^```json|```$", "").trim();
            chatContents = mapper.readValue(cleanedContent, new TypeReference<List<ChatContent>>(){});
        }
        return chatContents;
    }

}
