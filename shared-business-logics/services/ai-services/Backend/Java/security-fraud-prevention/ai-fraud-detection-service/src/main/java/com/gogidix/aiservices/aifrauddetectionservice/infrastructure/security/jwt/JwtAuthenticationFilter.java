package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.security.jwt;

import com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.TenantContext;
import com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT Authentication Filter.
 * Extracts and validates JWT tokens from requests, sets up security context and tenant context.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Extract token from request
        String token = jwtTokenProvider.extractToken(request);

        // Extract tenant ID from header
        String tenantId = request.getHeader("X-Tenant-ID");
        if (tenantId != null) {
            TenantContextHolder.setTenantId(tenantId);
        }

        // Validate and process token if present
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);

            // Create authentication object
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Set authentication for user: {}", username);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clear tenant context after request
            TenantContextHolder.clearContext();
        }
    }
}
