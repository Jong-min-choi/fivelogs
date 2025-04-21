package com.fiveguys.fivelogbackend.domain.image.service;


import com.fiveguys.fivelogbackend.domain.image.dto.ImageRequestDto;
import com.fiveguys.fivelogbackend.domain.image.dto.ImageResponseDto;
import com.fiveguys.fivelogbackend.domain.image.entity.Image;
import com.fiveguys.fivelogbackend.domain.image.repository.ImageRepository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    public final ImageRepository imageRepository;

    // 이미지 저장
    @Transactional
    public ImageResponseDto saveImage(ImageRequestDto dto) {
        MultipartFile file = dto.getFile();

        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        try {
            String originalName = file.getOriginalFilename();
            String serverFileName = UUID.randomUUID().toString() + "_" + originalName;
            String uploadDir = "/Users/yugwanglyun/team project/uploadDir";
            File destination = new File(uploadDir, serverFileName);
            file.transferTo(destination);

            Image image = new Image();
            image.originalName = originalName;
            image.serverImageName = serverFileName;
            image.path = uploadDir + "/" + serverFileName;

            Image saved = imageRepository.save(image);

            return ImageResponseDto.builder()
                    .id(saved.getId())
                    .originalName(saved.originalName)
                    .path(saved.path)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }

    // 이미지 하나 조회
    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    // 전체 이미지 리스트 조회
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    // 이미지 수정 (Update)
    public Image updateImage(Long id, Image updatedImage) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지가 없습니다."));

        // 업데이트 할 필드 지정
        image.originalName = updatedImage.originalName;
        image.serverImageName = updatedImage.serverImageName;
        image.path = updatedImage.path;

        return imageRepository.save(image);
    }

    // 이미지 삭제 (Delete)
    @Transactional
    public String deleteImage(Long id) {
        imageRepository.deleteById(id);
        return "이미지가 삭제되었습니다.";

    }

    // 이미지 보기
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> viewImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));

        File file = new File(image.getPath());
        if (!file.exists()) {
            throw new IllegalArgumentException("이미지 파일이 존재하지 않습니다.");
        }

        Resource resource = new FileSystemResource(file);

        // MIME 타입 자동 추론 (jpg, png 등)
        MediaType mediaType = MediaTypeFactory.getMediaType(file.getName())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}
