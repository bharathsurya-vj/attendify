package com.attendify.repository;
import com.attendify.domain.entity.User;
import com.attendify.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    long countByRoleAndActiveTrue(Role role);
    List<User> findByRoleOrderByFullNameAsc(Role role);
}
