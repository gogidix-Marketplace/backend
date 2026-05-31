package com.gogidix.aiservices.aiauthenticationservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.AuthenticationRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.MfaVerificationRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.TokenRefreshRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.response.AuthenticationResponse;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.response.TokenValidationResponse;
import com.gogidix.aiservices.aiauthenticationservice.application.service.AuthenticationService;
import com.gogidix.aiservices.aiauthenticationservice.domain.model.RiskLevel;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AccountLockedException;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AuthenticationFailedException;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.InvalidTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@DisplayName("Authentication Controller Interface Tests")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "SecureP@ssw0rd123";
    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Authentication Endpoint Tests")
    class AuthenticationEndpointTests {

        @Test
        @DisplayName("POST /api/v1/auth/authenticate - Should authenticate successfully")
        void shouldAuthenticateSuccessfully() throws Exception {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .build();

            AuthenticationResponse response = AuthenticationResponse.builder()
                    .authenticated(true)
                    .accessToken("access-token")
                    .refreshToken("refresh-token")
                    .expiresIn(3600)
                    .userId(UUID.fromString(USER_ID))
                    .roles(Set.of("USER"))
                    .riskScore(0.1)
                    .riskLevel(RiskLevel.LOW)
                    .build();

            when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.authenticated").value(true))
                    .andExpect(jsonPath("$.accessToken").value("access-token"))
                    .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                    .andExpect(jsonPath("$.expiresIn").value(3600))
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.roles[0]").value("USER"))
                    .andExpect(jsonPath("$.riskLevel").value("LOW"));

            verify(authenticationService).authenticate(any(AuthenticationRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/auth/authenticate - Should return 401 for invalid credentials")
        void shouldReturn401ForInvalidCredentials() throws Exception {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password("WrongPassword123!")
                    .build();

            when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                    .thenThrow(new AuthenticationFailedException("Invalid credentials"));

            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").value("Invalid credentials"));
        }

        @Test
        @DisplayName("POST /api/v1/auth/authenticate - Should return 423 for locked account")
        void shouldReturn423ForLockedAccount() throws Exception {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .build();

            when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                    .thenThrow(new AccountLockedException("Account is locked"));

            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isLocked())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").value("Account is locked"));
        }

        @Test
        @DisplayName("POST /api/v1/auth/authenticate - Should return 202 for MFA required")
        void shouldReturn202ForMfaRequired() throws Exception {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .build();

            AuthenticationResponse response = AuthenticationResponse.builder()
                    .authenticated(false)
                    .mfaRequired(true)
                    .mfaMethod("SMS")
                    .sessionId("mfa-session-123")
                    .build();

            when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.authenticated").value(false))
                    .andExpect(jsonPath("$.mfaRequired").value(true))
                    .andExpect(jsonPath("$.mfaMethod").value("SMS"))
                    .andExpect(jsonPath("$.sessionId").value("mfa-session-123"));
        }

        @Test
        @DisplayName("POST /api/v1/auth/authenticate - Should reject missing username")
        void shouldRejectMissingUsername() throws Exception {
            String request = "{\"password\":\"" + PASSWORD + "\"}";

            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest());

            verify(authenticationService, never()).authenticate(any());
        }

        @Test
        @DisplayName("POST /api/v1/auth/authenticate - Should reject missing password")
        void shouldRejectMissingPassword() throws Exception {
            String request = "{\"username\":\"" + USERNAME + "\"}";

            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest());

            verify(authenticationService, never()).authenticate(any());
        }

        @Test
        @DisplayName("POST /api/v1/auth/authenticate - Should include IP and user agent")
        void shouldIncludeIpAndUserAgent() throws Exception {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .ipAddress("192.168.1.1")
                    .userAgent("Mozilla/5.0")
                    .build();

            AuthenticationResponse response = AuthenticationResponse.builder()
                    .authenticated(true)
                    .accessToken("access-token")
                    .refreshToken("refresh-token")
                    .expiresIn(3600)
                    .userId(UUID.fromString(USER_ID))
                    .roles(Set.of("USER"))
                    .build();

            when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .header("X-Forwarded-For", "192.168.1.1")
                            .header("User-Agent", "Mozilla/5.0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("MFA Verification Endpoint Tests")
    class MfaEndpointTests {

        @Test
        @DisplayName("POST /api/v1/auth/mfa/verify - Should verify MFA code")
        void shouldVerifyMfaCode() throws Exception {
            MfaVerificationRequest request = MfaVerificationRequest.builder()
                    .username(USERNAME)
                    .mfaCode("123456")
                    .sessionId("session-123")
                    .build();

            AuthenticationResponse response = AuthenticationResponse.builder()
                    .authenticated(true)
                    .accessToken("access-token")
                    .refreshToken("refresh-token")
                    .expiresIn(3600)
                    .userId(UUID.fromString(USER_ID))
                    .roles(Set.of("USER"))
                    .build();

            when(authenticationService.verifyMfa(any(MfaVerificationRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/auth/mfa/verify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.authenticated").value(true))
                    .andExpect(jsonPath("$.accessToken").value("access-token"));
        }

        @Test
        @DisplayName("POST /api/v1/auth/mfa/verify - Should reject invalid MFA code")
        void shouldRejectInvalidMfaCode() throws Exception {
            MfaVerificationRequest request = MfaVerificationRequest.builder()
                    .username(USERNAME)
                    .mfaCode("654321")
                    .sessionId("session-123")
                    .build();

            when(authenticationService.verifyMfa(any(MfaVerificationRequest.class)))
                    .thenThrow(new AuthenticationFailedException("Invalid MFA code"));

            mockMvc.perform(post("/api/v1/auth/mfa/verify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").value("Invalid MFA code"));
        }

        @Test
        @DisplayName("POST /api/v1/auth/mfa/verify - Should reject missing MFA code")
        void shouldRejectMissingMfaCode() throws Exception {
            String request = "{\"username\":\"" + USERNAME + "\",\"sessionId\":\"session-123\"}";

            mockMvc.perform(post("/api/v1/auth/mfa/verify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Token Validation Endpoint Tests")
    class TokenValidationTests {

        @Test
        @DisplayName("POST /api/v1/auth/validate - Should validate valid token")
        void shouldValidateValidToken() throws Exception {
            String token = "valid-access-token";

            TokenValidationResponse response = TokenValidationResponse.builder()
                    .valid(true)
                    .userId(UUID.fromString(USER_ID))
                    .roles(Set.of("USER"))
                    .expired(false)
                    .build();

            when(authenticationService.validateToken(token)).thenReturn(response);

            mockMvc.perform(post("/api/v1/auth/validate")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.valid").value(true))
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.roles[0]").value("USER"))
                    .andExpect(jsonPath("$.expired").value(false));
        }

        @Test
        @DisplayName("POST /api/v1/auth/validate - Should reject invalid token")
        void shouldRejectInvalidToken() throws Exception {
            String token = "invalid-token";

            TokenValidationResponse response = TokenValidationResponse.builder()
                    .valid(false)
                    .build();

            when(authenticationService.validateToken(token)).thenReturn(response);

            mockMvc.perform(post("/api/v1/auth/validate")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.valid").value(false));
        }

        @Test
        @DisplayName("POST /api/v1/auth/validate - Should require authorization header")
        void shouldRequireAuthorizationHeader() throws Exception {
            mockMvc.perform(post("/api/v1/auth/validate"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("POST /api/v1/auth/validate - Should handle malformed authorization header")
        void shouldHandleMalformedAuthHeader() throws Exception {
            mockMvc.perform(post("/api/v1/auth/validate")
                            .header("Authorization", "InvalidFormat token"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Token Refresh Endpoint Tests")
    class TokenRefreshTests {

        @Test
        @DisplayName("POST /api/v1/auth/refresh - Should refresh token")
        void shouldRefreshToken() throws Exception {
            TokenRefreshRequest request = TokenRefreshRequest.builder()
                    .refreshToken("valid-refresh-token")
                    .build();

            AuthenticationResponse response = AuthenticationResponse.builder()
                    .authenticated(true)
                    .accessToken("new-access-token")
                    .refreshToken("new-refresh-token")
                    .expiresIn(3600)
                    .userId(UUID.fromString(USER_ID))
                    .roles(Set.of("USER"))
                    .build();

            when(authenticationService.refreshToken(any(TokenRefreshRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/auth/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                    .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
        }

        @Test
        @DisplayName("POST /api/v1/auth/refresh - Should reject invalid refresh token")
        void shouldRejectInvalidRefreshToken() throws Exception {
            TokenRefreshRequest request = TokenRefreshRequest.builder()
                    .refreshToken("invalid-refresh-token")
                    .build();

            when(authenticationService.refreshToken(any(TokenRefreshRequest.class)))
                    .thenThrow(new InvalidTokenException("Invalid refresh token"));

            mockMvc.perform(post("/api/v1/auth/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").value("Invalid refresh token"));
        }

        @Test
        @DisplayName("POST /api/v1/auth/refresh - Should reject missing refresh token")
        void shouldRejectMissingRefreshToken() throws Exception {
            String request = "{}";

            mockMvc.perform(post("/api/v1/auth/refresh")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Logout Endpoint Tests")
    class LogoutTests {

        @Test
        @DisplayName("POST /api/v1/auth/logout - Should logout successfully")
        void shouldLogoutSuccessfully() throws Exception {
            String token = "valid-access-token";
            String sessionId = "session-123";

            doNothing().when(authenticationService).logout(eq(token), eq(sessionId));

            mockMvc.perform(post("/api/v1/auth/logout")
                            .header("Authorization", "Bearer " + token)
                            .param("sessionId", sessionId))
                    .andExpect(status().isNoContent());

            verify(authenticationService).logout(eq(token), eq(sessionId));
        }

        @Test
        @DisplayName("POST /api/v1/auth/logout - Should require authorization")
        void shouldRequireAuthorization() throws Exception {
            mockMvc.perform(post("/api/v1/auth/logout"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("User Profile Endpoint Tests")
    class UserProfileTests {

        @Test
        @DisplayName("GET /api/v1/auth/profile - Should get user profile")
        void shouldGetUserProfile() throws Exception {
            String token = "valid-access-token";

            when(authenticationService.extractUserIdFromToken(token))
                    .thenReturn(UUID.fromString(USER_ID));

            var userProfile = new AuthenticationService.UserProfile(
                    UUID.fromString(USER_ID),
                    USERNAME,
                    Set.of("USER"),
                    false,
                    Instant.now()
            );

            when(authenticationService.getUserProfile(UUID.fromString(USER_ID)))
                    .thenReturn(userProfile);

            mockMvc.perform(get("/api/v1/auth/profile")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.username").value(USERNAME))
                    .andExpect(jsonPath("$.roles[0]").value("USER"));
        }

        @Test
        @DisplayName("GET /api/v1/auth/profile - Should return 404 for non-existent user")
        void shouldReturn404ForNonExistentUser() throws Exception {
            String token = "valid-access-token";

            when(authenticationService.extractUserIdFromToken(token))
                    .thenReturn(UUID.fromString(USER_ID));

            when(authenticationService.getUserProfile(UUID.fromString(USER_ID)))
                    .thenThrow(new AuthenticationFailedException("User not found"));

            mockMvc.perform(get("/api/v1/auth/profile")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").value("User not found"));
        }
    }

    @Nested
    @DisplayName("Password Management Endpoint Tests")
    class PasswordManagementTests {

        @Test
        @DisplayName("PUT /api/v1/auth/password - Should update password")
        void shouldUpdatePassword() throws Exception {
            String token = "valid-access-token";

            when(authenticationService.extractUserIdFromToken(token))
                    .thenReturn(UUID.fromString(USER_ID));

            doNothing().when(authenticationService).updatePassword(
                    eq(USERNAME), eq("OldPass123!"), eq("NewPass456!")
            );

            String request = """
                    {
                        "username": "%s",
                        "oldPassword": "OldPass123!",
                        "newPassword": "NewPass456!"
                    }
                    """.formatted(USERNAME);

            mockMvc.perform(put("/api/v1/auth/password")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("PUT /api/v1/auth/password - Should reject wrong old password")
        void shouldRejectWrongOldPassword() throws Exception {
            String token = "valid-access-token";

            when(authenticationService.extractUserIdFromToken(token))
                    .thenReturn(UUID.fromString(USER_ID));

            doThrow(new AuthenticationFailedException("Current password is incorrect"))
                    .when(authenticationService).updatePassword(
                            eq(USERNAME), eq("WrongPass123!"), eq("NewPass456!")
                    );

            String request = """
                    {
                        "username": "%s",
                        "oldPassword": "WrongPass123!",
                        "newPassword": "NewPass456!"
                    }
                    """.formatted(USERNAME);

            mockMvc.perform(put("/api/v1/auth/password")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").value("Current password is incorrect"));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle malformed JSON")
        void shouldHandleMalformedJson() throws Exception {
            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return standard error format")
        void shouldReturnStandardErrorFormat() throws Exception {
            when(authenticationService.authenticate(any(AuthenticationRequest.class)))
                    .thenThrow(new RuntimeException("Unexpected error"));

            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .build();

            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.timestamp").exists());
        }

        @Test
        @DisplayName("Should handle unsupported media type")
        void shouldHandleUnsupportedMediaType() throws Exception {
            mockMvc.perform(post("/api/v1/auth/authenticate")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("some text"))
                    .andExpect(status().isUnsupportedMediaType());
        }
    }
}
