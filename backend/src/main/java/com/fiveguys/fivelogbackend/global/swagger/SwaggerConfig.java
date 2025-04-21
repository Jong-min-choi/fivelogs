package com.fiveguys.fivelogbackend.global.swagger;

import io.swagger.v3.oas.models.*;                  // Swagger의 OpenAPI 기본 모델들
import io.swagger.v3.oas.models.info.Info;           // API 문서의 기본 정보(제목, 버전 등)
import io.swagger.v3.oas.models.security.*;          // 인증/보안 설정 관련 클래스들
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 이 클래스가 Spring 설정 파일임을 나타냅니다.
public class SwaggerConfig {

    @Bean // Spring Bean으로 등록하여 Swagger 설정을 구성합니다.
    public OpenAPI openAPI() {
        return new OpenAPI()
                // 보안 요구사항 추가: Swagger UI에서 인증 헤더를 설정할 수 있도록 함
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))

                // 보안 스키마 정의: Bearer 토큰 방식(JWT) 사용
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", // 스키마 이름
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)    // HTTP 방식의 인증 사용
                                        .scheme("bearer")                  // Bearer 스킴 사용
                                        .bearerFormat("JWT")               // JWT 형식 명시
                        )
                )

                // 문서 기본 정보 설정
                .info(new Info()
                        .title("Five Log API")     // Swagger UI 상단 제목
                        .version("v1")             // API 버전 표시
                );
    }
}
