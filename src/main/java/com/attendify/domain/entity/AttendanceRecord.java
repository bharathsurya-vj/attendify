package com.attendify.domain.entity;
import com.attendify.domain.enums.AttendanceStatus;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name = "attendance_records")
public class AttendanceRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "attendance_session_id") private AttendanceSession attendanceSession;
    @ManyToOne(optional = false) @JoinColumn(name = "student_id") private Student student;
    @Enumerated(EnumType.STRING) @Column(name = "attendance_status") private AttendanceStatus attendanceStatus;
    @ManyToOne(optional = false) @JoinColumn(name = "marked_by") private User markedBy;
    @Column(name = "marked_at") private Instant markedAt = Instant.now();
    private String remarks;
    public Long getId() { return id; }
    public AttendanceSession getAttendanceSession() { return attendanceSession; } public void setAttendanceSession(AttendanceSession s) { this.attendanceSession = s; }
    public Student getStudent() { return student; } public void setStudent(Student s) { this.student = s; }
    public AttendanceStatus getAttendanceStatus() { return attendanceStatus; } public void setAttendanceStatus(AttendanceStatus s) { this.attendanceStatus = s; }
    public void setMarkedBy(User u) { this.markedBy = u; }
    public void setMarkedAt(Instant i) { this.markedAt = i; }
    public String getRemarks() { return remarks; } public void setRemarks(String s) { this.remarks = s; }
}
