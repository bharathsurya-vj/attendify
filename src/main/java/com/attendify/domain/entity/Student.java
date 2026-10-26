package com.attendify.domain.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name = "students")
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "student_name") private String studentName;
    @Column(name = "registration_number", unique = true) private String registrationNumber;
    private String email;
    @ManyToOne(optional = false) @JoinColumn(name = "class_id") private AcademicClass academicClass;
    private boolean active = true;
    @Column(name = "created_at") private Instant createdAt = Instant.now();
    @Column(name = "updated_at") private Instant updatedAt = Instant.now();
    public Long getId() { return id; }
    public String getStudentName() { return studentName; } public void setStudentName(String s) { this.studentName = s; }
    public String getRegistrationNumber() { return registrationNumber; } public void setRegistrationNumber(String s) { this.registrationNumber = s; }
    public String getEmail() { return email; } public void setEmail(String s) { this.email = s; }
    public AcademicClass getAcademicClass() { return academicClass; } public void setAcademicClass(AcademicClass c) { this.academicClass = c; }
    public boolean isActive() { return active; } public void setActive(boolean b) { this.active = b; }
}
