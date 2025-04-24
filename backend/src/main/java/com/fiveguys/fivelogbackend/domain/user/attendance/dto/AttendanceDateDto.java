package com.fiveguys.fivelogbackend.domain.user.attendance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class AttendanceDateDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    List<LocalDateTime> attendanceDateList;

    public AttendanceDateDto(List<LocalDateTime> attendanceDateList) {
        this.attendanceDateList = attendanceDateList;
    }
}
