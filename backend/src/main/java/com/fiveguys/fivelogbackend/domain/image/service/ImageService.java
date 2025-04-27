package com.fiveguys.fivelogbackend.domain.image.service;


import com.fiveguys.fivelogbackend.domain.image.config.ImageProperties;
import com.fiveguys.fivelogbackend.domain.image.dto.ImageResponseDto;
import com.fiveguys.fivelogbackend.domain.image.entity.Image;
import com.fiveguys.fivelogbackend.domain.image.repository.ImageRepository;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserService userService;
    private final ImageProperties imageProperties;
    // 이미지 저장
    @Transactional
    public ImageResponseDto saveImage(MultipartFile file, Long userId) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        try {
            String originalName = file.getOriginalFilename();
            String serverFileName = UUID.randomUUID().toString() + "_" + originalName;

            // 저장 방식 결정 (로컬 or S3)

            String savedPath = null;
            if (imageProperties.isUseS3()) {
                // S3 저장 로직 (예시)
//                savedPath = s3Uploader.upload(file, "images"); // S3 업로더가 저장 후 URL 반환
            } else {
                // 로컬 저장 로직
                String uploadPath = imageProperties.getUploadPath();
                File destinationDir = new File(System.getProperty("user.dir"), uploadPath);

                if (!destinationDir.exists()) {
                    destinationDir.mkdirs();
                }

                File destination = new File(destinationDir, serverFileName);
                file.transferTo(destination);

                savedPath = uploadPath + File.separator + serverFileName;
            }

            Image image = new Image();
            image.originalName = originalName;
            image.serverImageName = serverFileName;
            image.path = savedPath;

            User user = userService.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다."));

            Image savedImage = imageRepository.save(image);
            Image profileImage = user.getProfileImage();
            File preFile = new File(System.getProperty("user.dir"), profileImage.getPath());
            deleteFile(preFile);

            user.setProfileImage(savedImage);

            return ImageResponseDto.builder()
                    .id(savedImage.getId())
                    .originalName(savedImage.originalName)
                    .path(getImageProfileUrl(savedImage.getId()))
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }

    private static void deleteFile(File preFile) {
        if(preFile.exists()){
            boolean deleted = preFile.delete();
            if (deleted) {
                log.info("파일 삭제 성공");
            } else {
                log.warn("파일 삭제 실패");
            }
        }else {
            log.warn("삭제하려는 파일이 존재하지 않습니다.");
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
    public String deleteImage(Long imageId, Long userId) {
        // 유저 조회 (actor)
        User user = userService.findByIdWithProfileImage(userId);

        // 이 이미지를 사용하는 모든 유저 찾기
        List<User> usersUsingImage = userService.findAllByProfileImageId(imageId);

        // 각 유저의 프로필 이미지 끊기
        for (User u : usersUsingImage) {
            u.setProfileImage(null);
        }
        // 이미지 파일 삭제
        File preFile = new File(System.getProperty("user.dir"), user.getProfileImage().getPath());
        deleteFile(preFile);
        // 이미지 삭제
        imageRepository.deleteById(imageId);

        return "이미지가 삭제되었습니다.";
    }


    // 로컬 이미지 보기
    @Transactional(readOnly = true)
    public ResponseEntity<Resource> viewImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));

        File file = new File(System.getProperty("user.dir"),image.getPath());
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

    public String getImageProfileUrl(Long imageId){
        return imageProperties.getViewUrl() + imageId;
    }

}
