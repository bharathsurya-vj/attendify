package com.attendify.repository;
import com.attendify.domain.entity.AttendanceRecord;
import com.attendify.domain.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByAttendanceSessionId(Long sessionId);
    long countByAttendanceStatus(AttendanceStatus status);
}
