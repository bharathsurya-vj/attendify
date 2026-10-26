package com.attendify.repository;
import com.attendify.domain.entity.AttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface AttendanceSessionRepository extends JpaRepository<AttendanceSession, Long> {
    Optional<AttendanceSession> findByAcademicClassIdAndSubjectIdAndAttendanceDate(Long classId, Long subjectId, LocalDate date);
    List<AttendanceSession> findTop10ByOrderByAttendanceDateDesc();
}
