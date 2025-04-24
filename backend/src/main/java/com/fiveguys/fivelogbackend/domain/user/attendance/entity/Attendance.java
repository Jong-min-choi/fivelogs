package com.fiveguys.fivelogbackend.domain.user.attendance.entity;

import com.fiveguys.fivelogbackend.domain.csquestion.entitiy.CsQuestion;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@AllArgsConstructor
@Getter
@NoArgsConstructor
@SuperBuilder
@Builder
public class Attendance extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    User user;


}
