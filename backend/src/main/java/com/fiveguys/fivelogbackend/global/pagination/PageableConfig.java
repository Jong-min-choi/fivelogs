package com.fiveguys.fivelogbackend.global.pagination;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizePageable() {
        return resolver -> resolver.setOneIndexedParameters(true); // 👈 page=1부터 시작
    }
}
