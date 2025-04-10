package com.fiveguys.fivelogbackend.domain.user.user.entity;

import com.fiveguys.fivelogbackend.domain.image.image.entity.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 255, nullable = false)
    String password;
    @Column(length = 50, nullable = false)
    String email;
    @Column(length = 50, nullable = false)
    String nickname;
    @Column(length = 50, nullable = false)
    String introduce;
    @Column(name = "SNS_link")
    String SNSLink;
    @Column(length = 255)
    String refreshToken;

    @ManyToOne
    @JoinColumn(name = "image_id", nullable = false)
    Image profileImage;


}
