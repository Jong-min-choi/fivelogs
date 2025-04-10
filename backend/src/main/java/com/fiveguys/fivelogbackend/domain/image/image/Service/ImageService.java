package com.fiveguys.fivelogbackend.domain.image.image.Service;

import com.fiveguys.fivelogbackend.domain.image.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;


    @Transactional



}
