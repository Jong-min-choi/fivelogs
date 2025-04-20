package com.fiveguys.fivelogbackend.domain.image.service;


import com.fiveguys.fivelogbackend.domain.image.dto.ImageRequestDto;
import com.fiveguys.fivelogbackend.domain.image.dto.ImageResponseDto;
import com.fiveguys.fivelogbackend.domain.image.entity.Image;
import com.fiveguys.fivelogbackend.domain.image.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
            String uploadDir = "/Users/yugwanglyun/project/uploadDir";
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
}