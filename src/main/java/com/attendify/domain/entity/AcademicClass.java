package com.attendify.domain.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name = "classes")
public class AcademicClass {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "class_name") private String className;
    @Column(name = "section_name") private String sectionName;
    @Column(name = "academic_year") private String academicYear;
    private boolean active = true;
    @Column(name = "created_at") private Instant createdAt = Instant.now();
    public String getDisplayLabel() { return className + " - Sec " + sectionName + " (" + academicYear + ")"; }
    public Long getId() { return id; }
    public String getClassName() { return className; } public void setClassName(String s) { this.className = s; }
    public String getSectionName() { return sectionName; } public void setSectionName(String s) { this.sectionName = s; }
    public String getAcademicYear() { return academicYear; } public void setAcademicYear(String s) { this.academicYear = s; }
    public boolean isActive() { return active; } public void setActive(boolean b) { this.active = b; }
}
