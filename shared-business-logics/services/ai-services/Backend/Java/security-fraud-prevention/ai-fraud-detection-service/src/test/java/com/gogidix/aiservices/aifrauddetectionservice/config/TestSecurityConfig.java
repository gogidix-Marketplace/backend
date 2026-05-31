package com.gogidix.aiservices.aifrauddetectionservice.config;

import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.security.jwt.JwtAuthenticationFilter;
import com.gogidix.aiservices.aifrauddetectionservice.infrastructure.security.jwt.JwtTokenProvider;
import com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.TenantContext;
import com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Test security configuration.
 * Disables security for tests and provides mock beans.
 */
@TestConfiguration
@Profile("test")
@EnableWebSecurity
@ComponentScan(
        basePackages = "com.gogidix.aiservices.aifrauddetectionservice",
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = ".*infrastructure\\.security\\..*"
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.TenantContextRequestFilter.class}
                )
        }
)
public class TestSecurityConfig {

    /**
     * Mock tenant context request filter that sets up test context.
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
                    // Set up test tenant context
                    TenantContext testContext = TenantContext.builder()
                            .tenantId("test-tenant")
                            .userId("test-user")
                            .correlationId("test-correlation-" + System.nanoTime())
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
                String path = request.getRequestURI();
                return path.contains("/health") ||
                        path.contains("/readiness") ||
                        path.contains("/liveness") ||
                        path.startsWith("/actuator");
            }
        };
    }

    /**
     * Provides a mock JWT token provider for tests.
     */
    @Bean
    @Primary
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider() {
            @Override
            public String generateToken(String username, java.util.List<String> roles, String tenantId) {
                return "test-jwt-token-" + username;
            }

            @Override
            public boolean validateToken(String token) {
                return token != null && token.startsWith("test-jwt-token-");
            }
        };
    }

    /**
     * Provides a mock JWT authentication filter for tests.
     */
    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationFilter(jwtTokenProvider) {
            @Override
            protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                            jakarta.servlet.http.HttpServletResponse response,
                                            jakarta.servlet.FilterChain filterChain) {
                // Skip authentication in tests
                try {
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    // Ignore filter errors in tests
                }
            }
        };
    }

    /**
     * Disables security for tests.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/**");
    }

    /**
     * Provides a security filter chain that permits all requests.
     */
    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .headers(headers -> headers
                    .frameOptions(frameOptions -> frameOptions.sameOrigin())
                    .xssProtection(xss -> xss.disable())
                    .httpStrictTransportSecurity(hsts -> hsts.disable()));
        return http.build();
    }

    /**
     * Provides a mock user details service for tests.
     */
    @Bean
    @Primary
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
    public org.springframework.security.core.userdetails.UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("test-user")
                .password("test-password")
                .roles("USER", "ADMIN")
                .build());
        return manager;
    }

    /**
     * Provides a password encoder for tests.
     */
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
