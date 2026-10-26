package com.attendify.repository;
import com.attendify.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAcademicClassIdAndActiveTrueOrderByRegistrationNumberAsc(Long classId);
    List<Student> findAllByOrderByRegistrationNumberAsc();
}
