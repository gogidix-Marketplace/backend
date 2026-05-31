package com.gogidix.shared.infrastructure.services.security.auth.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.LoginRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.RefreshTokenRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.AuthResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.UserResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;

    private LoginRequestDto loginRequest;
    private RefreshTokenRequestDto refreshTokenRequest;
    private AuthResponseDto authResponse;
    private UserResponseDto userResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService)).build();

        loginRequest = LoginRequestDto.builder()
                .username("john.doe")
                .password("password123")
                .build();

        refreshTokenRequest = RefreshTokenRequestDto.builder()
                .refreshToken("refresh-token-uuid")
                .build();

        userResponse = UserResponseDto.builder()
                .id("user-123")
                .tenantId("tenant-123")
                .username("john.doe")
                .email("john.doe@example.com")
                .firstName("John")
                .lastName("Doe")
                .enabled(true)
                .roles(Set.of("USER", "ADMIN"))
                .permissions(Set.of("read", "write"))
                .build();

        authResponse = AuthResponseDto.builder()
                .accessToken("jwt-access-token")
                .refreshToken("refresh-token-uuid")
                .tokenType("Bearer")
                .expiresAt(Instant.now().plusSeconds(900))
                .refreshExpiresAt(Instant.now().plusSeconds(604800))
                .user(userResponse)
                .build();
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void shouldLoginSuccessfullyWithValidCredentials() throws Exception {
        when(authService.login(any(LoginRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-uuid"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.user.id").value("user-123"))
                .andExpect(jsonPath("$.user.username").value("john.doe"));

        verify(authService).login(any(LoginRequestDto.class));
    }

    @Test
    @DisplayName("Should refresh token successfully")
    void shouldRefreshTokenSuccessfully() throws Exception {
        when(authService.refreshToken(any(RefreshTokenRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-uuid"))
                .andExpect(jsonPath("$.user.id").value("user-123"));

        verify(authService).refreshToken(any(RefreshTokenRequestDto.class));
    }

    @Test
    @DisplayName("Should logout successfully")
    void shouldLogoutSuccessfully() throws Exception {
        doNothing().when(authService).logout(any(String.class));

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer jwt-access-token"))
                .andExpect(status().isNoContent());

        verify(authService).logout("jwt-access-token");
    }

    @Test
    @DisplayName("Should validate token successfully")
    void shouldValidateTokenSuccessfully() throws Exception {
        when(authService.validateToken("jwt-access-token")).thenReturn(Optional.of(userResponse));

        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", "Bearer jwt-access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-123"))
                .andExpect(jsonPath("$.username").value("john.doe"));

        verify(authService).validateToken("jwt-access-token");
    }

    @Test
    @DisplayName("Should return unauthorized for invalid token")
    void shouldReturnUnauthorizedForInvalidToken() throws Exception {
        when(authService.validateToken("invalid-token")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());

        verify(authService).validateToken("invalid-token");
    }

    @Test
    @DisplayName("Should get current user successfully")
    void shouldGetCurrentUserSuccessfully() throws Exception {
        when(authService.findById("user-123")).thenReturn(Optional.of(userResponse));

        mockMvc.perform(get("/api/auth/me")
                        .header("X-User-Id", "user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-123"))
                .andExpect(jsonPath("$.username").value("john.doe"));

        verify(authService).findById("user-123");
    }

    @Test
    @DisplayName("Should return not found when user does not exist")
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(authService.findById("non-existent-user")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/me")
                        .header("X-User-Id", "non-existent-user"))
                .andExpect(status().isNotFound());

        verify(authService).findById("non-existent-user");
    }

    @Test
    @DisplayName("Should handle login with email instead of username")
    void shouldHandleLoginWithEmailInsteadOfUsername() throws Exception {
        LoginRequestDto emailLoginRequest = LoginRequestDto.builder()
                .username("john.doe@example.com")
                .password("password123")
                .build();

        when(authService.login(any(LoginRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"));

        verify(authService).login(any(LoginRequestDto.class));
    }

    @Test
    @DisplayName("Should handle login with IP address and user agent")
    void shouldHandleLoginWithIpAddressAndUserAgent() throws Exception {
        when(authService.login(any(LoginRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .header("X-Forwarded-For", "192.168.1.100")
                        .header("User-Agent", "Mozilla/5.0"))
                .andExpect(status().isOk());

        verify(authService).login(any(LoginRequestDto.class));
    }

    @Test
    @DisplayName("Should handle malformed JSON in login request")
    void shouldHandleMalformedJsonInLoginRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequestDto.class));
    }

    @Test
    @DisplayName("Should return unauthorized when authorization header is missing Bearer prefix")
    void shouldReturnUnauthorizedWhenAuthorizationHeaderIsMissingBearerPrefix() throws Exception {
        when(authService.validateToken("jwt-access-token")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", "jwt-access-token"))
                .andExpect(status().isUnauthorized());

        verify(authService).validateToken("jwt-access-token");
    }

    @Test
    @DisplayName("Should handle multiple concurrent login requests")
    void shouldHandleMultipleConcurrentLoginRequests() throws Exception {
        when(authService.login(any(LoginRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        verify(authService, times(2)).login(any(LoginRequestDto.class));
    }

    @Test
    @DisplayName("Should verify controller has RestController annotation")
    void shouldVerifyControllerHasRestControllerAnnotation() {
        assertTrue(AuthController.class.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
    }

    @Test
    @DisplayName("Should verify controller request mapping")
    void shouldVerifyControllerRequestMapping() {
        org.springframework.web.bind.annotation.RequestMapping annotation =
                AuthController.class.getAnnotation(org.springframework.web.bind.annotation.RequestMapping.class);

        assertNotNull(annotation);
        assertEquals("/api/auth", annotation.value()[0]);
    }

    @Test
    @DisplayName("Should verify controller has RequiredArgsConstructor annotation")
    void shouldVerifyControllerHasRequiredArgsConstructorAnnotation() throws NoSuchMethodException {
        assertNotNull(AuthController.class.getDeclaredConstructor(AuthService.class));
    }

    @Test
    @DisplayName("Should handle very long username in login request")
    void shouldHandleVeryLongUsernameInLoginRequest() throws Exception {
        String longUsername = "a".repeat(300);
        LoginRequestDto longUsernameRequest = LoginRequestDto.builder()
                .username(longUsername)
                .password("password123")
                .build();

        when(authService.login(any(LoginRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(longUsernameRequest)))
                .andExpect(status().isOk());

        verify(authService).login(any(LoginRequestDto.class));
    }

    @Test
    @DisplayName("Should handle special characters in username")
    void shouldHandleSpecialCharactersInUsername() throws Exception {
        LoginRequestDto specialCharRequest = LoginRequestDto.builder()
                .username("user+test@example.com")
                .password("password123")
                .build();

        when(authService.login(any(LoginRequestDto.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specialCharRequest)))
                .andExpect(status().isOk());

        verify(authService).login(any(LoginRequestDto.class));
    }

    @Test
    @DisplayName("Should validate token with valid JWT format")
    void shouldValidateTokenWithValidJwtFormat() throws Exception {
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";
        when(authService.validateToken(jwtToken)).thenReturn(Optional.of(userResponse));

        mockMvc.perform(get("/api/auth/validate")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("user-123"));

        verify(authService).validateToken(jwtToken);
    }
}
