package com.gogidix.shared.infrastructure.services.security.auth.application.service;

import com.gogidix.shared.multitenancy.context.TenantContextHolder;
import com.gogidix.shared.multitenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.LoginRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.request.RefreshTokenRequestDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.AuthResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.dto.response.UserResponseDto;
import com.gogidix.shared.infrastructure.services.security.auth.application.mapper.UserMapper;
import com.gogidix.shared.infrastructure.services.security.auth.domain.exception.InvalidCredentialsException;
import com.gogidix.shared.infrastructure.services.security.auth.domain.exception.UserNotFoundException;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.AuthToken;
import com.gogidix.shared.infrastructure.services.security.auth.domain.model.User;
import com.gogidix.shared.infrastructure.services.security.auth.domain.port.out.TokenRepositoryPort;
import com.gogidix.shared.infrastructure.services.security.auth.domain.port.out.UserRepositoryPort;
import com.gogidix.shared.infrastructure.services.security.auth.infrastructure.config.JwtProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private TokenRepositoryPort tokenRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-123";
    private static final String USERNAME = "john.doe";
    private static final String EMAIL = "john.doe@example.com";
    private static final String PASSWORD = "rawPassword";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String ACCESS_TOKEN = "jwt-access-token";
    private static final String REFRESH_TOKEN = UUID.randomUUID().toString();

    private User testUser;
    private UserResponseDto testUserResponseDto;

    @BeforeEach
    void setUp() {
        // Set tenant context for all tests
        TenantContextHolder.setTenantId(TENANT_ID);

        testUser = User.builder()
                .id(USER_ID)
                .tenantId(TenantId.of(TENANT_ID))
                .username(USERNAME)
                .email(EMAIL)
                .password(ENCODED_PASSWORD)
                .firstName("John")
                .lastName("Doe")
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .roles(Set.of("USER", "ADMIN"))
                .permissions(Set.of("read", "write"))
                .build();

        testUserResponseDto = UserResponseDto.builder()
                .id(USER_ID)
                .tenantId(TENANT_ID)
                .username(USERNAME)
                .email(EMAIL)
                .firstName("John")
                .lastName("Doe")
                .enabled(true)
                .roles(Set.of("USER", "ADMIN"))
                .permissions(Set.of("read", "write"))
                .build();
    }

    @AfterEach
    void tearDown() {
        // Clear tenant context after each test
        TenantContextHolder.clear();
    }

    @Test
    @DisplayName("Should login successfully with valid credentials using username")
    void shouldLoginSuccessfullyWithValidCredentialsUsingUsername() {
        // Given
        LoginRequestDto request = LoginRequestDto.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .ipAddress("192.168.1.1")
                .userAgent("Mozilla/5.0")
                .build();

        when(userRepository.findByUsernameAndTenantId(USERNAME, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(jwtProvider.generateAccessToken(USER_ID, TENANT_ID, testUser.getRoles())).thenReturn(ACCESS_TOKEN);
        when(tokenRepository.save(any(AuthToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toResponseDto(testUser)).thenReturn(testUserResponseDto);

        // When
        AuthResponseDto response = authService.login(request);

        // Then
        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getExpiresAt());
        assertNotNull(response.getRefreshExpiresAt());
        assertNotNull(response.getUser());
        assertEquals(USER_ID, response.getUser().getId());

        verify(userRepository).findByUsernameAndTenantId(USERNAME, TENANT_ID);
        verify(passwordEncoder).matches(PASSWORD, ENCODED_PASSWORD);
        verify(jwtProvider).generateAccessToken(USER_ID, TENANT_ID, testUser.getRoles());
        verify(tokenRepository).save(any(AuthToken.class));
        verify(userRepository).save(any(User.class));
        verify(userMapper).toResponseDto(testUser);
    }

    @Test
    @DisplayName("Should login successfully with valid credentials using email")
    void shouldLoginSuccessfullyWithValidCredentialsUsingEmail() {
        // Given
        LoginRequestDto request = LoginRequestDto.builder()
                .username(EMAIL)
                .password(PASSWORD)
                .build();

        when(userRepository.findByUsernameAndTenantId(EMAIL, TENANT_ID)).thenReturn(Optional.empty());
        when(userRepository.findByEmailAndTenantId(EMAIL, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(jwtProvider.generateAccessToken(USER_ID, TENANT_ID, testUser.getRoles())).thenReturn(ACCESS_TOKEN);
        when(tokenRepository.save(any(AuthToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toResponseDto(testUser)).thenReturn(testUserResponseDto);

        // When
        AuthResponseDto response = authService.login(request);

        // Then
        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.getAccessToken());

        verify(userRepository).findByUsernameAndTenantId(EMAIL, TENANT_ID);
        verify(userRepository).findByEmailAndTenantId(EMAIL, TENANT_ID);
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when username not found")
    void shouldThrowInvalidCredentialsExceptionWhenUsernameNotFound() {
        // Given
        LoginRequestDto request = LoginRequestDto.builder()
                .username("unknown")
                .password(PASSWORD)
                .build();

        when(userRepository.findByUsernameAndTenantId("unknown", TENANT_ID)).thenReturn(Optional.empty());
        when(userRepository.findByEmailAndTenantId("unknown", TENANT_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when password is invalid")
    void shouldThrowInvalidCredentialsExceptionWhenPasswordIsInvalid() {
        // Given
        LoginRequestDto request = LoginRequestDto.builder()
                .username(USERNAME)
                .password("wrongPassword")
                .build();

        when(userRepository.findByUsernameAndTenantId(USERNAME, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", ENCODED_PASSWORD)).thenReturn(false);

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(passwordEncoder).matches("wrongPassword", ENCODED_PASSWORD);
        verify(jwtProvider, never()).generateAccessToken(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when account is disabled")
    void shouldThrowInvalidCredentialsExceptionWhenAccountIsDisabled() {
        // Given
        testUser.setEnabled(false);
        LoginRequestDto request = LoginRequestDto.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        when(userRepository.findByUsernameAndTenantId(USERNAME, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);

        // When & Then
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
        assertTrue(exception.getMessage().contains("disabled") || exception.getMessage().contains("Account"));

        verify(jwtProvider, never()).generateAccessToken(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("Should refresh token successfully")
    void shouldRefreshTokenSuccessfully() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken existingToken = AuthToken.builder()
                .id("token-123")
                .userId(USER_ID)
                .token(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .issuedAt(now.minusHours(1))
                .expiresAt(now.plusMinutes(15))
                .refreshExpiresAt(now.plusDays(6))
                .revoked(false)
                .build();

        String newAccessToken = "new-jwt-access-token";

        RefreshTokenRequestDto request = RefreshTokenRequestDto.builder()
                .refreshToken(REFRESH_TOKEN)
                .build();

        when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
        when(tokenRepository.findByRefreshToken(REFRESH_TOKEN)).thenReturn(Optional.of(existingToken));
        when(userRepository.findByIdAndTenantId(USER_ID, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(tokenRepository.save(any(AuthToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtProvider.generateAccessToken(USER_ID, TENANT_ID, testUser.getRoles())).thenReturn(newAccessToken);
        when(userMapper.toResponseDto(testUser)).thenReturn(testUserResponseDto);

        // When
        AuthResponseDto response = authService.refreshToken(request);

        // Then
        assertNotNull(response);
        assertEquals(newAccessToken, response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertNotEquals(REFRESH_TOKEN, response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());

        verify(tokenRepository).findByRefreshToken(REFRESH_TOKEN);
        verify(userRepository).findByIdAndTenantId(USER_ID, TENANT_ID);
        verify(jwtProvider).generateAccessToken(USER_ID, TENANT_ID, testUser.getRoles());
        verify(tokenRepository, times(2)).save(any(AuthToken.class)); // Once to revoke old, once to save new
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when refresh token not found")
    void shouldThrowInvalidCredentialsExceptionWhenRefreshTokenNotFound() {
        // Given
        RefreshTokenRequestDto request = RefreshTokenRequestDto.builder()
                .refreshToken("invalid-refresh-token")
                .build();

        when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
        when(tokenRepository.findByRefreshToken("invalid-refresh-token")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> authService.refreshToken(request));

        verify(userRepository, never()).findByIdAndTenantId(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when refresh token is expired")
    void shouldThrowInvalidCredentialsExceptionWhenRefreshTokenIsExpired() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken expiredToken = AuthToken.builder()
                .id("token-123")
                .userId(USER_ID)
                .refreshToken(REFRESH_TOKEN)
                .refreshExpiresAt(now.minusDays(1))
                .revoked(false)
                .build();

        RefreshTokenRequestDto request = RefreshTokenRequestDto.builder()
                .refreshToken(REFRESH_TOKEN)
                .build();

        when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
        when(tokenRepository.findByRefreshToken(REFRESH_TOKEN)).thenReturn(Optional.of(expiredToken));

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> authService.refreshToken(request));

        verify(jwtProvider, never()).generateAccessToken(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when refresh token is revoked")
    void shouldThrowInvalidCredentialsExceptionWhenRefreshTokenIsRevoked() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken revokedToken = AuthToken.builder()
                .id("token-123")
                .userId(USER_ID)
                .refreshToken(REFRESH_TOKEN)
                .refreshExpiresAt(now.plusDays(6))
                .revoked(true)
                .build();

        RefreshTokenRequestDto request = RefreshTokenRequestDto.builder()
                .refreshToken(REFRESH_TOKEN)
                .build();

        when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
        when(tokenRepository.findByRefreshToken(REFRESH_TOKEN)).thenReturn(Optional.of(revokedToken));

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> authService.refreshToken(request));

        verify(jwtProvider, never()).generateAccessToken(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found during refresh")
    void shouldThrowUserNotFoundExceptionWhenUserNotFoundDuringRefresh() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken validToken = AuthToken.builder()
                .id("token-123")
                .userId(USER_ID)
                .refreshToken(REFRESH_TOKEN)
                .refreshExpiresAt(now.plusDays(6))
                .revoked(false)
                .build();

        RefreshTokenRequestDto request = RefreshTokenRequestDto.builder()
                .refreshToken(REFRESH_TOKEN)
                .build();

        when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
        when(tokenRepository.findByRefreshToken(REFRESH_TOKEN)).thenReturn(Optional.of(validToken));
        when(userRepository.findByIdAndTenantId(USER_ID, TENANT_ID)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> authService.refreshToken(request));
    }

    @Test
    @DisplayName("Should logout successfully")
    void shouldLogoutSuccessfully() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId(USER_ID)
                .token(ACCESS_TOKEN)
                .revoked(false)
                .build();

        when(tokenRepository.findByToken(ACCESS_TOKEN)).thenReturn(Optional.of(authToken));
        when(tokenRepository.save(any(AuthToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        authService.logout(ACCESS_TOKEN);

        // Then
        verify(tokenRepository).findByToken(ACCESS_TOKEN);
        verify(tokenRepository).save(authToken);
        assertTrue(authToken.isRevoked());
    }

    @Test
    @DisplayName("Should logout silently when token not found")
    void shouldLogoutSilentlyWhenTokenNotFound() {
        // Given
        when(tokenRepository.findByToken(ACCESS_TOKEN)).thenReturn(Optional.empty());

        // When
        authService.logout(ACCESS_TOKEN);

        // Then
        verify(tokenRepository).findByToken(ACCESS_TOKEN);
        verify(tokenRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should validate token successfully")
    void shouldValidateTokenSuccessfully() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        AuthToken authToken = AuthToken.builder()
                .id("token-123")
                .userId(USER_ID)
                .token(ACCESS_TOKEN)
                .expiresAt(now.plusMinutes(15))
                .revoked(false)
                .build();

        when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
        when(jwtProvider.validateToken(ACCESS_TOKEN)).thenReturn(true);
        when(tokenRepository.findValidByToken(ACCESS_TOKEN)).thenReturn(Optional.of(authToken));
        when(userRepository.findByIdAndTenantId(USER_ID, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(userMapper.toResponseDto(testUser)).thenReturn(testUserResponseDto);

        // When
        Optional<UserResponseDto> result = authService.validateToken(ACCESS_TOKEN);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUserResponseDto, result.get());

        verify(jwtProvider).validateToken(ACCESS_TOKEN);
        verify(tokenRepository).findValidByToken(ACCESS_TOKEN);
        verify(userRepository).findByIdAndTenantId(USER_ID, TENANT_ID);
        verify(userMapper).toResponseDto(testUser);
    }

    @Test
    @DisplayName("Should return empty when validating invalid JWT")
    void shouldReturnEmptyWhenValidatingInvalidJWT() {
        // Given
        when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
        when(jwtProvider.validateToken(ACCESS_TOKEN)).thenReturn(false);

        // When
        Optional<UserResponseDto> result = authService.validateToken(ACCESS_TOKEN);

        // Then
        assertFalse(result.isPresent());
        verify(jwtProvider).validateToken(ACCESS_TOKEN);
        verify(tokenRepository, never()).findValidByToken(anyString());
    }

    @Test
    @DisplayName("Should return empty when validating token not found in repository")
    void shouldReturnEmptyWhenValidatingTokenNotFoundInRepository() {
        // Given
        when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
        when(jwtProvider.validateToken(ACCESS_TOKEN)).thenReturn(true);
        when(tokenRepository.findValidByToken(ACCESS_TOKEN)).thenReturn(Optional.empty());

        // When
        Optional<UserResponseDto> result = authService.validateToken(ACCESS_TOKEN);

        // Then
        assertFalse(result.isPresent());
        verify(jwtProvider).validateToken(ACCESS_TOKEN);
        verify(tokenRepository).findValidByToken(ACCESS_TOKEN);
        verify(userRepository, never()).findByIdAndTenantId(anyString(), anyString());
    }

    @Test
    @DisplayName("Should return empty when validating token with exception")
    void shouldReturnEmptyWhenValidatingTokenWithException() {
        // Given
        when(tenantContextHolder.getRequiredTenantId()).thenThrow(new RuntimeException("Tenant error"));

        // When
        Optional<UserResponseDto> result = authService.validateToken(ACCESS_TOKEN);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserByIdSuccessfully() {
        // Given
        when(userRepository.findByIdAndTenantId(USER_ID, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(userMapper.toResponseDto(testUser)).thenReturn(testUserResponseDto);

        // When
        Optional<UserResponseDto> result = authService.findById(USER_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUserResponseDto, result.get());
        verify(userRepository).findByIdAndTenantId(USER_ID, TENANT_ID);
    }

    @Test
    @DisplayName("Should return empty when user not found by id")
    void shouldReturnEmptyWhenUserNotFoundById() {
        // Given
        when(userRepository.findByIdAndTenantId(USER_ID, TENANT_ID)).thenReturn(Optional.empty());

        // When
        Optional<UserResponseDto> result = authService.findById(USER_ID);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findByIdAndTenantId(USER_ID, TENANT_ID);
    }

    @Test
    @DisplayName("Should find user by username successfully")
    void shouldFindUserByUsernameSuccessfully() {
        // Given
        when(userRepository.findByUsernameAndTenantId(USERNAME, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(userMapper.toResponseDto(testUser)).thenReturn(testUserResponseDto);

        // When
        Optional<UserResponseDto> result = authService.findByUsername(USERNAME);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUserResponseDto, result.get());
        verify(userRepository).findByUsernameAndTenantId(USERNAME, TENANT_ID);
    }

    @Test
    @DisplayName("Should return empty when user not found by username")
    void shouldReturnEmptyWhenUserNotFoundByUsername() {
        // Given
        when(userRepository.findByUsernameAndTenantId(USERNAME, TENANT_ID)).thenReturn(Optional.empty());

        // When
        Optional<UserResponseDto> result = authService.findByUsername(USERNAME);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void shouldFindUserByEmailSuccessfully() {
        // Given
        when(userRepository.findByEmailAndTenantId(EMAIL, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(userMapper.toResponseDto(testUser)).thenReturn(testUserResponseDto);

        // When
        Optional<UserResponseDto> result = authService.findByEmail(EMAIL);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testUserResponseDto, result.get());
        verify(userRepository).findByEmailAndTenantId(EMAIL, TENANT_ID);
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // Given
        when(userRepository.findByEmailAndTenantId(EMAIL, TENANT_ID)).thenReturn(Optional.empty());

        // When
        Optional<UserResponseDto> result = authService.findByEmail(EMAIL);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should set lastLoginAt during login")
    void shouldSetLastLoginAtDuringLogin() {
        // Given
        LoginRequestDto request = LoginRequestDto.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        when(userRepository.findByUsernameAndTenantId(USERNAME, TENANT_ID)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
        when(jwtProvider.generateAccessToken(USER_ID, TENANT_ID, testUser.getRoles())).thenReturn(ACCESS_TOKEN);
        when(tokenRepository.save(any(AuthToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(testUserResponseDto);

        // When
        authService.login(request);

        // Then
        assertNotNull(testUser.getLastLoginAt());
        verify(userRepository).save(testUser);
    }
}
