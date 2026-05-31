package com.gogidix.centralizeddashboard.realtime.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Test
    void jwtDecoder_returnsNonNull() {
        assertNotNull(new SecurityConfig().jwtDecoder());
    }

    @Test
    void jwtAuthenticationConverter_returnsNonNull() {
        assertNotNull(new SecurityConfig().jwtAuthenticationConverter());
    }
}
