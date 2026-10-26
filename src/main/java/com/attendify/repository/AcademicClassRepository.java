package com.attendify.repository;
import com.attendify.domain.entity.AcademicClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AcademicClassRepository extends JpaRepository<AcademicClass, Long> {
    List<AcademicClass> findByActiveTrueOrderByClassNameAsc();
}
