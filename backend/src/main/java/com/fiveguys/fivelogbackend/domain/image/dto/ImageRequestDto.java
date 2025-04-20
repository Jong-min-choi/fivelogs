package com.fiveguys.fivelogbackend.domain.image.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageRequestDto {
    private MultipartFile file;      // 실제 업로드된 이미지 파일

}