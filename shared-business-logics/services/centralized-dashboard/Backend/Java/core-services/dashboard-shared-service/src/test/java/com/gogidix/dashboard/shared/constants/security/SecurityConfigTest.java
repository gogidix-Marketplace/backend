package com.gogidix.dashboard.shared.constants.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Test
    @DisplayName("jwtDecoder returns non-null JwtDecoder")
    void jwtDecoder_returnsNonNull() {
        SecurityConfig config = new SecurityConfig();
        JwtDecoder decoder = config.jwtDecoder();
        assertNotNull(decoder);
    }

    @Test
    @DisplayName("jwtAuthenticationConverter returns non-null converter")
    void jwtAuthenticationConverter_returnsNonNull() {
        SecurityConfig config = new SecurityConfig();
        JwtAuthenticationConverter converter = config.jwtAuthenticationConverter();
        assertNotNull(converter);
    }
}
