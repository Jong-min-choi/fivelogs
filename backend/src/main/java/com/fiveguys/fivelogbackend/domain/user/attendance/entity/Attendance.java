package com.fiveguys.fivelogbackend.domain.user.attendance.entity;

import com.fiveguys.fivelogbackend.domain.csquestion.entitiy.CsQuestion;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cs_question_id")
    CsQuestion csQuestion;
    LocalDateTime attendanceDate;

    @PrePersist
    protected void onAttend() {
        this.attendanceDate = LocalDateTime.now();
    }

}
