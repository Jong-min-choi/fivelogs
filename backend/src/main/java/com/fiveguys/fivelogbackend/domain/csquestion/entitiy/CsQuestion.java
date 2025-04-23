package com.fiveguys.fivelogbackend.domain.csquestion.entitiy;


import com.fiveguys.fivelogbackend.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "cs_questions")
public class CsQuestion extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    String options;

    @Column(nullable = false)
    String answer;



}
