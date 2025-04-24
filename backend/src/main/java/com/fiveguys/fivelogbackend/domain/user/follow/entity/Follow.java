package com.fiveguys.fivelogbackend.domain.user.follow.entity;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "follows")
@Entity
public class Follow extends BaseEntity {

    // 팔로우 하는사람
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    // 팔로우 당한 사람
    @ManyToOne
    @JoinColumn(name = "followed_id")
    private User followed;
}
