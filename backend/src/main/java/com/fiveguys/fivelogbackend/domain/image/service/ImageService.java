package com.fiveguys.fivelogbackend.domain.image.service;


import com.fiveguys.fivelogbackend.domain.image.entity.Image;
import com.fiveguys.fivelogbackend.domain.image.repository.ImageRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    public final ImageRepository imageRepository;

    // 이미지 저장
    public Image saveImage(Image image) {
        return imageRepository.save(image);
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
    public String deleteImage(Long id) {
        imageRepository.deleteById(id); // ID 기준으로 삭제
        return "이미지가 삭제되었습니다.";
    }
}

