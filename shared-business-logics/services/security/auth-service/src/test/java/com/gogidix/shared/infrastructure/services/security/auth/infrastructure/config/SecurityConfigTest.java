package com.gogidix.shared.infrastructure.services.security.auth.infrastructure.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SecurityConfig.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpSecurity httpSecurity;

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig(jwtAuthenticationFilter);
    }

    @Test
    @DisplayName("Should create SecurityConfig with JwtAuthenticationFilter")
    void shouldCreateSecurityConfigWithJwtAuthenticationFilter() {
        // When
        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter);

        // Then
        assertNotNull(config);
    }

    @Test
    @DisplayName("Should have securityFilterChain bean method")
    void shouldHaveSecurityFilterChainBeanMethod() throws Exception {
        // Then
        assertNotNull(SecurityConfig.class.getMethod("securityFilterChain", HttpSecurity.class));
    }

    @Test
    @DisplayName("Should have passwordEncoder bean method")
    void shouldHavePasswordEncoderBeanMethod() throws Exception {
        // Then
        assertNotNull(SecurityConfig.class.getMethod("passwordEncoder"));
    }

    @Test
    @DisplayName("Should create passwordEncoder bean")
    void shouldCreatePasswordEncoderBean() {
        // When
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Then
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder);
    }

    @Test
    @DisplayName("Should encode and verify password with passwordEncoder")
    void shouldEncodeAndVerifyPasswordWithPasswordEncoder() {
        // Given
        String rawPassword = "testPassword123";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Then
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    @DisplayName("Should not match wrong password with encoded password")
    void shouldNotMatchWrongPasswordWithEncodedPassword() {
        // Given
        String rawPassword = "testPassword123";
        String wrongPassword = "wrongPassword";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Then
        assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword));
    }

    @Test
    @DisplayName("Should produce different hash for same password")
    void shouldProduceDifferentHashForSamePassword() {
        // Given
        String rawPassword = "testPassword123";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When
        String encoded1 = passwordEncoder.encode(rawPassword);
        String encoded2 = passwordEncoder.encode(rawPassword);

        // Then
        assertNotEquals(encoded1, encoded2, "BCrypt should produce different hashes for same password due to salt");
    }

    @Test
    @DisplayName("Should verify class has Configuration annotation")
    void shouldVerifyClassHasConfigurationAnnotation() {
        // Then
        assertTrue(securityConfig.getClass().isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    @DisplayName("Should verify class has EnableWebSecurity annotation")
    void shouldVerifyClassHasEnableWebSecurityAnnotation() {
        // Then
        assertTrue(securityConfig.getClass().isAnnotationPresent(org.springframework.security.config.annotation.web.configuration.EnableWebSecurity.class));
    }

    @Test
    @DisplayName("Should verify class has EnableMethodSecurity annotation")
    void shouldVerifyClassHasEnableMethodSecurityAnnotation() {
        // Then
        assertTrue(securityConfig.getClass().isAnnotationPresent(org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity.class));
    }

    @Test
    @DisplayName("Should throw exception for null password encoding")
    void shouldHandleNullPasswordForEncoding() {
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.encode(null));
    }

    @Test
    @DisplayName("Should throw exception for null password matching")
    void shouldReturnFalseForNullPasswordMatching() {
        String encodedPassword = "$2a$10$abcdef1234567890";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.matches(null, encodedPassword));
    }

    @Test
    @DisplayName("Should verify securityFilterChain returns SecurityFilterChain")
    void shouldVerifySecurityFilterChainReturnsSecurityFilterChain() throws Exception {
        // Then
        assertEquals(SecurityFilterChain.class,
                SecurityConfig.class.getMethod("securityFilterChain", HttpSecurity.class).getReturnType());
    }

    @Test
    @DisplayName("Should verify passwordEncoder returns PasswordEncoder")
    void shouldVerifyPasswordEncoderReturnsPasswordEncoder() throws Exception {
        // Then
        assertEquals(PasswordEncoder.class,
                SecurityConfig.class.getMethod("passwordEncoder").getReturnType());
    }

    @Test
    @DisplayName("Should create multiple instances of passwordEncoder")
    void shouldCreateMultipleInstancesOfPasswordEncoder() {
        // When
        PasswordEncoder encoder1 = securityConfig.passwordEncoder();
        PasswordEncoder encoder2 = securityConfig.passwordEncoder();

        // Then
        assertNotNull(encoder1);
        assertNotNull(encoder2);
        // These should be different instances since it's a @Bean method
        assertNotSame(encoder1, encoder2);
    }

    @Test
    @DisplayName("Should verify passwordEncoder strength")
    void shouldVerifyPasswordEncoderStrength() {
        // Given
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When
        String encodedPassword = passwordEncoder.encode("test");

        // Then - BCrypt hashes start with $2a$, $2b$, or $2y$
        assertTrue(encodedPassword.startsWith("$2"));
        assertEquals(60, encodedPassword.length(), "BCrypt hash should be 60 characters long");
    }

    @Test
    @DisplayName("Should handle empty password")
    void shouldHandleEmptyPassword() {
        // Given
        String emptyPassword = "";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When
        String encodedPassword = passwordEncoder.encode(emptyPassword);

        // Then
        assertNotNull(encodedPassword);
        assertTrue(passwordEncoder.matches("", encodedPassword));
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        // Given
        String specialPassword = "P@ssw0rd!#$%^&*()_+-=[]{}|;':\",./<>?";
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // When
        String encodedPassword = passwordEncoder.encode(specialPassword);

        // Then
        assertTrue(passwordEncoder.matches(specialPassword, encodedPassword));
    }
}
