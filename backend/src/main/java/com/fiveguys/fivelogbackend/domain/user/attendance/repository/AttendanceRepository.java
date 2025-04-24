package com.fiveguys.fivelogbackend.domain.user.attendance.repository;

import com.fiveguys.fivelogbackend.domain.user.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByUserIdAndCreatedDateBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT a.createdDate FROM Attendance a WHERE a.user.id = :userId AND a.createdDate BETWEEN :start AND :end")
    List<LocalDateTime> findThisMonthData(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
