package com.attendify.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
@Configuration
public class SecurityConfig {
    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean public SecurityContextRepository securityContextRepository() { return new HttpSessionSecurityContextRepository(); }
    @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(a -> a.requestMatchers("/login", "/verify-otp", "/css/**", "/error").permitAll().anyRequest().authenticated())
            .formLogin(f -> f.disable()).exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> res.sendRedirect("/login")))
            .logout(l -> l.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true"));
        return http.build();
    }
}
