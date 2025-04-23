package com.fiveguys.fivelogbackend.domain.csquestion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CsQuestionConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
