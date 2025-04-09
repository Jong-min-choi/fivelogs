package com.fiveguys.fivelogbackend.domain.post.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
@Table(name = "blogs")
public class blog {
    @Id
    private Long id;
    @Column(nullable = false)
    private String title;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedBy
    private LocalDateTime updatedDate;



}
