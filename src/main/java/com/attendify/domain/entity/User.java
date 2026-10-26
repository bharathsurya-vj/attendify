package com.attendify.domain.entity;
import com.attendify.domain.enums.Role;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "full_name", nullable = false) private String fullName;
    @Column(nullable = false, unique = true) private String email;
    @Column(name = "password_hash", nullable = false) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Role role;
    @Column(nullable = false) private boolean active = true;
    @Column(name = "created_at") private Instant createdAt = Instant.now();
    @Column(name = "updated_at") private Instant updatedAt = Instant.now();
    @Column(name = "last_login_at") private Instant lastLoginAt;
    public Long getId() { return id; }
    public String getFullName() { return fullName; } public void setFullName(String s) { this.fullName = s; }
    public String getEmail() { return email; } public void setEmail(String s) { this.email = s; }
    public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String s) { this.passwordHash = s; }
    public Role getRole() { return role; } public void setRole(Role r) { this.role = r; }
    public boolean isActive() { return active; } public void setActive(boolean b) { this.active = b; }
    public void setLastLoginAt(Instant i) { this.lastLoginAt = i; }
}
