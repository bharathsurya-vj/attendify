package com.attendify.security;
import com.attendify.domain.entity.User;
import com.attendify.domain.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
public class AttendifyUserPrincipal implements UserDetails {
    private final Long id; private final String fullName; private final String email; private final String passwordHash; private final Role role; private final boolean active;
    public AttendifyUserPrincipal(User u) { this.id = u.getId(); this.fullName = u.getFullName(); this.email = u.getEmail(); this.passwordHash = u.getPasswordHash(); this.role = u.getRole(); this.active = u.isActive(); }
    public Long getId() { return id; } public String getFullName() { return fullName; } public String getEmail() { return email; } public Role getRole() { return role; } public boolean isAdmin() { return role == Role.ADMIN; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(new SimpleGrantedAuthority("ROLE_" + role.name())); }
    @Override public String getPassword() { return passwordHash; } @Override public String getUsername() { return email; } @Override public boolean isEnabled() { return active; }
}
