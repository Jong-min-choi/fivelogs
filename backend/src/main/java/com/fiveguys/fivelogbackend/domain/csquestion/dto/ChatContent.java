package com.fiveguys.fivelogbackend.domain.csquestion.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiveguys.fivelogbackend.domain.csquestion.entitiy.CsQuestion;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatContent {
    private String question;
    private List<String> options;
    private String answer;
}

