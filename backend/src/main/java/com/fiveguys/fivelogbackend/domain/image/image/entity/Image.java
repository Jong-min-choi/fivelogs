package com.fiveguys.fivelogbackend.domain.image.image.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long Id;
    @Column(length = 255, nullable = false)
    public String originalName;
    @Column(length = 255, nullable = false)
    public String serverImageName;
    @Column(length = 255, nullable = false)
    public String path;
}
