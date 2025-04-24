package com.fiveguys.fivelogbackend.domain.user.attendance.service;

import com.fiveguys.fivelogbackend.domain.user.attendance.dto.AttendanceDateDto;
import com.fiveguys.fivelogbackend.domain.user.attendance.entity.Attendance;
import com.fiveguys.fivelogbackend.domain.user.attendance.repository.AttendanceRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserService userService;

    public boolean hasUserCheckedInToday(Long userId) {
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return attendanceRepository.existsByUserIdAndCreatedDateBetween (userId, startOfDay, endOfDay);
    }
    @Transactional
    public void save(Long userId){
        User user = userService.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 userId 입니다."));
        log.info("asdas??");
        Attendance attendance = Attendance.builder()
                .user(user)
                .build();
        attendanceRepository.save(attendance);
    }

    public AttendanceDateDto getThisMonthAttendancesByUser(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.withDayOfMonth(1).atStartOfDay(); // 2025-04-01T00:00
        LocalDateTime end = now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59); // 2025-04-30T23:59:59

        List<LocalDateTime> thisMonthData = attendanceRepository.findThisMonthData(userId, start, end);
        AttendanceDateDto attendanceDateDto = new AttendanceDateDto(thisMonthData);
        return attendanceDateDto;
    }

}
