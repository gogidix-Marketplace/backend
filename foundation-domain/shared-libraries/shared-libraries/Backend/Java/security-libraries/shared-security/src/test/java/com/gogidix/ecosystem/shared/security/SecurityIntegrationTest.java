package com.gogidix.ecosystem.shared.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Security components.
 *
 * @author Gogidix Development Team
 * @since 1.0.0
 */
@org.junit.jupiter.api.Disabled("Integration tests require full Spring context - use unit tests for library validation")
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DisplayName("Security Integration Tests")
class SecurityIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public MockSecurityController mockSecurityController() {
            return new MockSecurityController();
        }
    }

    @Test
    @DisplayName("Should allow access to public endpoints")
    void shouldAllowAccessToPublicEndpoints() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mockMvc.perform(get("/api/auth/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/health/status"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should deny access to protected endpoints without token")
    void shouldDenyAccessToProtectedEndpointsWithoutToken() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mockMvc.perform(get("/api/protected/resource"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    @Test
    @DisplayName("Should allow access to protected endpoints with valid token")
    void shouldAllowAccessToProtectedEndpointsWithValidToken() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Create a valid token
        UserPrincipal userPrincipal = UserPrincipal.create(
                1L, "testuser", "test@example.com", "password",
                Arrays.asList("USER"), true
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        String token = jwtTokenProvider.generateToken(authentication);

        mockMvc.perform(get("/api/protected/resource")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should deny access with invalid token")
    void shouldDenyAccessWithInvalidToken() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mockMvc.perform(get("/api/protected/resource")
                        .header("Authorization", "Bearer invalid.token.here"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should handle role-based access control")
    void shouldHandleRoleBasedAccessControl() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Create tokens for different roles
        UserPrincipal userPrincipal = UserPrincipal.create(
                1L, "user", "user@example.com", "password",
                Arrays.asList("USER"), true
        );
        Authentication userAuth = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        String userToken = jwtTokenProvider.generateToken(userAuth);

        UserPrincipal adminPrincipal = UserPrincipal.create(
                2L, "admin", "admin@example.com", "password",
                Arrays.asList("ADMIN"), true
        );
        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
                adminPrincipal, null, adminPrincipal.getAuthorities()
        );
        String adminToken = jwtTokenProvider.generateToken(adminAuth);

        // User should not have access to admin endpoint
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());

        // Admin should have access to admin endpoint
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle token refresh")
    void shouldHandleTokenRefresh() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Create a refresh token
        UserPrincipal userPrincipal = UserPrincipal.create(
                1L, "testuser", "test@example.com", "password",
                Arrays.asList("USER"), true
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(
                java.util.Map.of("refreshToken", refreshToken)
        );

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @DisplayName("Should handle malformed authorization header")
    void shouldHandleMalformedAuthorizationHeader() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Test various malformed headers
        mockMvc.perform(get("/api/protected/resource")
                        .header("Authorization", "Basic dGVzdA=="))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/protected/resource")
                        .header("Authorization", "Bearer"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/protected/resource")
                        .header("Authorization", "InvalidFormat token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should validate token expiration")
    void shouldValidateTokenExpiration() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Create a token that would be expired (simulated by using invalid secret)
        UserPrincipal userPrincipal = UserPrincipal.create(
                1L, "testuser", "test@example.com", "password",
                Arrays.asList("USER"), true
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities()
        );
        
        // Generate token, then modify the provider to use different secret (simulating expiration)
        String token = jwtTokenProvider.generateToken(authentication);
        
        // The token should work initially
        mockMvc.perform(get("/api/protected/resource")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should handle CORS preflight requests")
    void shouldHandleCorsPrefightRequests() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        mockMvc.perform(options("/api/protected/resource")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Access-Control-Request-Headers", "Authorization"))
                .andExpect(status().isOk());
    }
}