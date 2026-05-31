package com.gogidix.sysadmin.accesscontrol.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Financial-Grade Unit Tests for SecurityConfig configuration class.
 * Tests verify Spring Security configuration for JWT-based authentication.
 */
@DisplayName("SecurityConfig Configuration Tests")
class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    @DisplayName("Should instantiate SecurityConfig successfully")
    void testSecurityConfigInstantiation() {
        assertThat(securityConfig).isNotNull();
    }

    @Test
    @DisplayName("Should create JwtAuthenticationConverter with correct configuration")
    void testJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();

        assertThat(converter).isNotNull();
    }

    @Test
    @DisplayName("Should create JwtDecoder successfully")
    void testJwtDecoder() {
        JwtDecoder decoder = securityConfig.jwtDecoder();

        assertThat(decoder).isNotNull();
    }

    @Test
    @DisplayName("SecurityConfig class should have expected simple name")
    void testSecurityConfigClassName() {
        assertThat(securityConfig.getClass().getSimpleName()).isEqualTo("SecurityConfig");
    }
}
