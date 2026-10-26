package com.attendify.domain.entity;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
@Entity @Table(name = "attendance_sessions")
public class AttendanceSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "subject_id") private Subject subject;
    @ManyToOne(optional = false) @JoinColumn(name = "class_id") private AcademicClass academicClass;
    @Column(name = "attendance_date") private LocalDate attendanceDate;
    @ManyToOne(optional = false) @JoinColumn(name = "created_by") private User createdBy;
    private boolean finalized = true;
    @Column(name = "created_at") private Instant createdAt = Instant.now();
    @Column(name = "updated_at") private Instant updatedAt = Instant.now();
    public Long getId() { return id; }
    public Subject getSubject() { return subject; } public void setSubject(Subject s) { this.subject = s; }
    public AcademicClass getAcademicClass() { return academicClass; } public void setAcademicClass(AcademicClass c) { this.academicClass = c; }
    public LocalDate getAttendanceDate() { return attendanceDate; } public void setAttendanceDate(LocalDate d) { this.attendanceDate = d; }
    public User getCreatedBy() { return createdBy; } public void setCreatedBy(User u) { this.createdBy = u; }
}
