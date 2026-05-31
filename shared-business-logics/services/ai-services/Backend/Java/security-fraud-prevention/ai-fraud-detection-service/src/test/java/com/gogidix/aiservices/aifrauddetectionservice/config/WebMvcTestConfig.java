package com.gogidix.aiservices.aifrauddetectionservice.config;

import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.security.jwt.JwtAuthenticationFilter;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.security.jwt.JwtTokenProvider;
import com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.TenantContext;
import com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Test configuration for WebMvcTest slice tests.
 * Provides mock beans for security and tenant context components.
 */
@TestConfiguration
@Profile("default")  // Apply to non-test profile (WebMvcTest uses default profile)
@EnableWebSecurity
@Import(TestSecurityConfig.class)  // Import the test security config
public class WebMvcTestConfig {

    /**
     * Mock JWT token provider for WebMvcTest.
     */
    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider() {
            @Override
            public String generateToken(String username, java.util.List<String> roles, String tenantId) {
                return "test-jwt-token";
            }

            @Override
            public boolean validateToken(String token) {
                return true;
            }
        };
    }

    /**
     * Mock JWT authentication filter for WebMvcTest - skips actual authentication.
     */
    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationFilter(jwtTokenProvider) {
            @Override
            protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                            jakarta.servlet.http.HttpServletResponse response,
                                            jakarta.servlet.FilterChain filterChain) {
                try {
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    // Ignore filter errors in tests
                }
            }
        };
    }

    /**
     * Mock tenant context request filter for WebMvcTest.
     */
    @Bean
    @Primary
    public OncePerRequestFilter tenantContextRequestFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                            jakarta.servlet.http.HttpServletResponse response,
                                            jakarta.servlet.FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {
                try {
                    // Set up test tenant context for controller tests
                    TenantContext testContext = TenantContext.builder()
                            .tenantId("test-tenant")
                            .userId("test-user")
                            .correlationId("test-correlation")
                            .roles(Collections.singleton("ROLE_ADMIN"))
                            .build();
                    TenantContextHolder.setContext(testContext);
                    filterChain.doFilter(request, response);
                } finally {
                    TenantContextHolder.clearContext();
                }
            }

            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                // Skip filter for all requests in WebMvcTest
                return true;
            }
        };
    }

    /**
     * Disable security for WebMvcTest.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/**");
    }
}
