package com.fiveguys.fivelogbackend.domain.image.controller;

import com.fiveguys.fivelogbackend.domain.image.dto.ImageRequestDto;
import com.fiveguys.fivelogbackend.domain.image.dto.ImageResponseDto;
import com.fiveguys.fivelogbackend.domain.image.entity.Image;
import com.fiveguys.fivelogbackend.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    //  이미지 업로드
    @Operation(summary = "이미지 업로드", description = "json형태로 받기 + 이미지 파일 꼭 있어야 작동.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ImageResponseDto uploadImage(@RequestPart("file") MultipartFile file) {
        ImageRequestDto dto = new ImageRequestDto();
        dto.setFile(file);
        return imageService.saveImage(dto);
    }

    //  이미지 단건 조회
    @GetMapping("/{id}")
    @Operation(summary = "이미지 단건 조회", description = "ID로 이미지를 조회합니다.")
    public Optional<Image> getImage(@PathVariable("id") Long id) {
        return imageService.getImageById(id);
    }

    //  이미지 전체 조회
    @GetMapping
    @Operation(summary = "이미지 전체 조회", description = "이미지 전체조회 합니다.")
    public List<Image> getAllImages() {
        return imageService.getAllImages();
    }

    //  이미지 수정
    @PutMapping("/{id}")
    @Operation(summary = "이미지 수정", description = "ID로 이미지를 수정합니다.")
    public Image updateImage(@PathVariable("id") Long id, @RequestBody Image updatedImage) {
        return imageService.updateImage(id, updatedImage);
    }

    //  이미지 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "이미지 삭제", description = "ID로 이미지를 삭제합니다.")
    public String deleteImage(@PathVariable("id") Long id) {
        return imageService.deleteImage(id);
    }
}
