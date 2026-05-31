package com.gogidix.corporatecms.application.config;

import com.gogidix.corporatecms.application.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Provider for auditing information (created by, updated by).
 */
@Component
@RequiredArgsConstructor
public class AuditorProvider implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(auth -> auth.getPrincipal() instanceof UserDetailsImpl)
                .map(auth -> ((UserDetailsImpl) auth.getPrincipal()).getId());
    }
}
