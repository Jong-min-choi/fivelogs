package com.fiveguys.fivelogbackend.domain.user.attendance.controller;

import com.fiveguys.fivelogbackend.domain.csquestion.service.CsQuestionService;
import com.fiveguys.fivelogbackend.domain.user.attendance.dto.AttendanceDateDto;
import com.fiveguys.fivelogbackend.domain.user.attendance.entity.Attendance;
import com.fiveguys.fivelogbackend.domain.user.attendance.repository.AttendanceRepository;
import com.fiveguys.fivelogbackend.domain.user.attendance.service.AttendanceService;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.response.ApiResponse;
import com.fiveguys.fivelogbackend.global.rq.Rq;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@Slf4j
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    private final Rq rq;
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> processQuizCheckin() throws BadRequestException {
        //요청 받았으니 이제 출석체크 확인하기
        log.info("request test attendance");

        User actor = rq.getActor();
        boolean isCheckedIn = attendanceService.hasUserCheckedInToday(actor.getId());
        if(isCheckedIn) {
            throw new BadRequestException("이미 출석한 사용자입니다.");
        }
        // 출석을 해야함
        attendanceService.save(actor.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "출석 체크 완료"));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<AttendanceDateDto>> getAttendanceDateList() throws BadRequestException {
        User actor = rq.getActor();
        AttendanceDateDto attendanceDateDto = attendanceService.getThisMonthAttendancesByUser(actor.getId());

        return ResponseEntity.ok(ApiResponse.success(attendanceDateDto,"이번달 출석 데이터"));
    }
}
