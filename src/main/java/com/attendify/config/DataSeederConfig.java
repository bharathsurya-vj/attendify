package com.attendify.config;
import com.attendify.domain.entity.*;
import com.attendify.domain.enums.Role;
import com.attendify.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class DataSeederConfig {
    @Value("${attendify.bootstrap.admin-email:}") private String adminEmail;
    @Value("${attendify.bootstrap.admin-password:}") private String adminPassword;
    @Bean public CommandLineRunner seed(UserRepository users, AcademicClassRepository classes, StudentRepository students, SubjectRepository subjects, PasswordEncoder encoder) {
        return args -> {
            if (users.count() == 0 && adminEmail != null && !adminEmail.isBlank()) {
                User admin = new User(); admin.setFullName("College Administrator"); admin.setEmail(adminEmail.trim().toLowerCase());
                admin.setPasswordHash(encoder.encode(adminPassword)); admin.setRole(Role.ADMIN); admin.setActive(true);
                admin = users.save(admin);
                AcademicClass c = new AcademicClass(); c.setClassName("B.Tech CSE"); c.setSectionName("A"); c.setAcademicYear("2026-2027"); c = classes.save(c);
                Subject s = new Subject(); s.setSubjectName("Data Structures"); s.setSubjectCode("CS301"); s.setAcademicClass(c); s.setTeacher(admin); subjects.save(s);
                String[][] stData = {{"Aarav Sharma","REG2026CS001"},{"Diya Nair","REG2026CS002"},{"Rohan Verma","REG2026CS003"}};
                for (String[] r : stData) { Student st = new Student(); st.setStudentName(r[0]); st.setRegistrationNumber(r[1]); st.setAcademicClass(c); students.save(st); }
            }
        };
    }
}
