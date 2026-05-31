package com.gogidix.management.executive.analytics.infrastructure.config;

import com.gogidix.management.executive.analytics.domain.model.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * User Details Service for Executive Analytics
 * Loads user-specific data for authentication
 */
@Service
public class AnalyticsUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user details for username: {}", username);

        // In production, this would load from a user repository
        // For now, we create a basic user with default authorities
        try {
            return User.builder()
                    .username(username)
                    .password("$2a$12$dummyPasswordHashForValidation") // Password not used for JWT
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        } catch (Exception e) {
            log.error("Error loading user: {}", e.getMessage(), e);
            throw new DomainException("Failed to load user details: " + e.getMessage());
        }
    }
}
