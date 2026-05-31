package com.gogidix.infrastructure.config.infrastructure.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Auditor aware implementation for capturing the current user.
 */
@Component
public class CurrentUserAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // In a real application, this would extract from security context
        // For now, return a default value
        return Optional.ofNullable(getCurrentUserFromContext());
    }

    private String getCurrentUserFromContext() {
        // Extract from Spring Security context or request header
        // This is a simplified implementation
        return "system";
    }
}
