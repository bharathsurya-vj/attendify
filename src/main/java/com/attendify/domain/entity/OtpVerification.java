package com.attendify.domain.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name = "otp_verifications")
public class OtpVerification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "user_id") private User user;
    @Column(name = "login_attempt_id", unique = true) private String loginAttemptId;
    @Column(name = "otp_hash") private String otpHash;
    @Column(name = "expires_at") private Instant expiresAt;
    @Column(name = "failed_attempt_count") private int failedAttemptCount = 0;
    @Column(name = "consumed_at") private Instant consumedAt;
    private boolean invalidated = false;
    @Column(name = "last_sent_at") private Instant lastSentAt = Instant.now();
    @Column(name = "created_at") private Instant createdAt = Instant.now();
    public boolean isUsable(Instant now) { return !invalidated && consumedAt == null && failedAttemptCount < 5 && now.isBefore(expiresAt); }
    public User getUser() { return user; } public void setUser(User u) { this.user = u; }
    public String getLoginAttemptId() { return loginAttemptId; } public void setLoginAttemptId(String s) { this.loginAttemptId = s; }
    public String getOtpHash() { return otpHash; } public void setOtpHash(String s) { this.otpHash = s; }
    public Instant getExpiresAt() { return expiresAt; } public void setExpiresAt(Instant i) { this.expiresAt = i; }
    public int getFailedAttemptCount() { return failedAttemptCount; } public void setFailedAttemptCount(int c) { this.failedAttemptCount = c; }
    public void setConsumedAt(Instant i) { this.consumedAt = i; }
    public void setInvalidated(boolean b) { this.invalidated = b; }
}
