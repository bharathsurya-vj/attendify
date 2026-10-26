package com.attendify.repository;
import com.attendify.domain.entity.OtpVerification;
import com.attendify.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByUserAndLoginAttemptId(User user, String loginAttemptId);
}
