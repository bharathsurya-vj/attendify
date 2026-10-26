package com.attendify.domain.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name = "subjects")
public class Subject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "subject_name") private String subjectName;
    @Column(name = "subject_code", unique = true) private String subjectCode;
    @ManyToOne(optional = false) @JoinColumn(name = "class_id") private AcademicClass academicClass;
    @ManyToOne(optional = false) @JoinColumn(name = "teacher_id") private User teacher;
    private boolean active = true;
    @Column(name = "created_at") private Instant createdAt = Instant.now();
    public Long getId() { return id; }
    public String getSubjectName() { return subjectName; } public void setSubjectName(String s) { this.subjectName = s; }
    public String getSubjectCode() { return subjectCode; } public void setSubjectCode(String s) { this.subjectCode = s; }
    public AcademicClass getAcademicClass() { return academicClass; } public void setAcademicClass(AcademicClass c) { this.academicClass = c; }
    public User getTeacher() { return teacher; } public void setTeacher(User u) { this.teacher = u; }
    public boolean isActive() { return active; } public void setActive(boolean b) { this.active = b; }
}
