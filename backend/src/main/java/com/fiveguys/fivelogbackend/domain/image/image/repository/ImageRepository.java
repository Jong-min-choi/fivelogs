package com.fiveguys.fivelogbackend.domain.image.image.repository;

import com.fiveguys.fivelogbackend.domain.image.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image,Long> {
}
