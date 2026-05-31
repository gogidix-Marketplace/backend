package com.gogidix.aiservices.aiauthenticationservice.application.service;

import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.AuthenticationRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.MfaVerificationRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.request.TokenRefreshRequest;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.response.AuthenticationResponse;
import com.gogidix.aiservices.aiauthenticationservice.application.dto.response.TokenValidationResponse;
import com.gogidix.aiservices.aiauthenticationservice.domain.aggregate.AuthenticationAttempt;
import com.gogidix.aiservices.aiauthenticationservice.domain.model.*;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.AuthenticationRepository;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.EventPublisherPort;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.PasswordEncoderPort;
import com.gogidix.aiservices.aiauthenticationservice.domain.policy.AccountLockoutPolicy;
import com.gogidix.aiservices.aiauthenticationservice.domain.policy.MfaPolicy;
import com.gogidix.aiservices.aiauthenticationservice.domain.policy.RiskAssessmentPolicy;
import com.gogidix.aiservices.aiauthenticationservice.domain.port.out.TokenPolicy;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AccountLockedException;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AuthenticationFailedException;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.InvalidTokenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Authentication Service Application Tests")
class AuthenticationServiceTest {

    @Mock
    private AuthenticationRepository authenticationRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private TokenPolicy tokenPolicy;

    @Mock
    private MfaPolicy mfaPolicy;

    @Mock
    private AccountLockoutPolicy lockoutPolicy;

    @Mock
    private RiskAssessmentPolicy riskPolicy;

    @Mock
    private EventPublisherPort eventPublisher;

    @InjectMocks
    private AuthenticationService authenticationService;

    private static final String USERNAME = "testuser";
    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String PASSWORD = "SecureP@ssw0rd123";
    private static final String ENCODED_PASSWORD = "$2a$10$encodedPasswordHere";
    private static final String SESSION_ID = "session-123";

    @Nested
    @DisplayName("Authentication Tests")
    class AuthenticationTests {

        @Test
        @DisplayName("Should authenticate user with valid credentials")
        void shouldAuthenticateValidCredentials() {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .build();

            when(authenticationRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(createUserRecord()));
            when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
            when(tokenPolicy.generateAccessToken(eq(USER_ID), any())).thenReturn("access-token");
            when(tokenPolicy.generateRefreshToken(USER_ID)).thenReturn("refresh-token");
            when(tokenPolicy.getAccessTokenExpiration()).thenReturn(3600);

            AuthenticationResponse response = authenticationService.authenticate(request);

            assertThat(response).isNotNull();
            assertThat(response.isAuthenticated()).isTrue();
            assertThat(response.getAccessToken()).isEqualTo("access-token");
            assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
            verify(eventPublisher).publish(eq("authentication.success"), any());
        }

        @Test
        @DisplayName("Should reject authentication with invalid credentials")
        void shouldRejectInvalidCredentials() {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password("WrongPassword123!")
                    .build();

            when(authenticationRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(createUserRecord()));
            when(passwordEncoder.matches("WrongPassword123!", ENCODED_PASSWORD)).thenReturn(false);

            assertThatThrownBy(() -> authenticationService.authenticate(request))
                    .isInstanceOf(AuthenticationFailedException.class)
                    .hasMessageContaining("Invalid credentials");

            verify(eventPublisher).publish(eq("authentication.failed"), any());
        }

        @Test
        @DisplayName("Should reject authentication for non-existent user")
        void shouldRejectNonExistentUser() {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username("nonexistent")
                    .password(PASSWORD)
                    .build();

            when(authenticationRepository.findByUsername("nonexistent"))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> authenticationService.authenticate(request))
                    .isInstanceOf(AuthenticationFailedException.class);

            verify(eventPublisher).publish(eq("authentication.failed"), any());
        }

        @Test
        @DisplayName("Should lock account after max failed attempts")
        void shouldLockAccountAfterMaxAttempts() {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .build();

            when(authenticationRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(createUserRecordWithFailedAttempts(5)));
            when(lockoutPolicy.isLockedOut(any(AuthenticationAttempt.class))).thenReturn(true);

            assertThatThrownBy(() -> authenticationService.authenticate(request))
                    .isInstanceOf(AccountLockedException.class)
                    .hasMessageContaining("Account is locked");

            verify(eventPublisher).publish(eq("account.locked"), any());
        }

        @Test
        @DisplayName("Should require MFA for medium risk authentication")
        void shouldRequireMfaForMediumRisk() {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .build();

            when(authenticationRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(createUserRecord()));
            when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
            when(riskPolicy.assessRisk(any(AuthenticationAttempt.class))).thenReturn(0.5);
            when(mfaPolicy.isMfaRequired(any(AuthenticationResult.class))).thenReturn(true);
            when(mfaPolicy.generateMfaCode()).thenReturn("123456");

            AuthenticationResponse response = authenticationService.authenticate(request);

            assertThat(response.isAuthenticated()).isFalse();
            assertThat(response.isMfaRequired()).isTrue();
            assertThat(response.getMfaMethod()).isNotEmpty();
        }

        @Test
        @DisplayName("Should track authentication attempt")
        void shouldTrackAuthenticationAttempt() {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .ipAddress("192.168.1.1")
                    .userAgent("Mozilla/5.0")
                    .build();

            when(authenticationRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(createUserRecord()));
            when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
            when(tokenPolicy.generateAccessToken(eq(USER_ID), any())).thenReturn("access-token");
            when(tokenPolicy.generateRefreshToken(USER_ID)).thenReturn("refresh-token");
            when(tokenPolicy.getAccessTokenExpiration()).thenReturn(3600);

            authenticationService.authenticate(request);

            verify(authenticationRepository).saveAttempt(any(AuthenticationAttempt.class));
        }

        @Test
        @DisplayName("Should assess risk during authentication")
        void shouldAssessRisk() {
            AuthenticationRequest request = AuthenticationRequest.builder()
                    .username(USERNAME)
                    .password(PASSWORD)
                    .ipAddress("103.21.244.0") // Suspicious IP
                    .build();

            when(authenticationRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(createUserRecord()));
            when(passwordEncoder.matches(PASSWORD, ENCODED_PASSWORD)).thenReturn(true);
            when(riskPolicy.assessRisk(any(AuthenticationAttempt.class))).thenReturn(0.9);
            when(tokenPolicy.generateAccessToken(eq(USER_ID), any())).thenReturn("access-token");
            when(tokenPolicy.generateRefreshToken(USER_ID)).thenReturn("refresh-token");
            when(tokenPolicy.getAccessTokenExpiration()).thenReturn(3600);

            AuthenticationResponse response = authenticationService.authenticate(request);

            assertThat(response.getRiskScore()).isEqualTo(0.9);
            assertThat(response.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        }
    }

    @Nested
    @DisplayName("MFA Verification Tests")
    class MfaVerificationTests {

        @Test
        @DisplayName("Should verify correct MFA code")
        void shouldVerifyCorrectMfaCode() {
            MfaVerificationRequest request = MfaVerificationRequest.builder()
                    .username(USERNAME)
                    .mfaCode("123456")
                    .sessionId(SESSION_ID)
                    .build();

            when(authenticationRepository.findPendingMfaSession(SESSION_ID))
                    .thenReturn(Optional.of(createPendingMfaSession()));
            when(mfaPolicy.validateMfaCode("123456", "123456")).thenReturn(true);
            when(tokenPolicy.generateAccessToken(eq(USER_ID), any())).thenReturn("access-token");
            when(tokenPolicy.generateRefreshToken(USER_ID)).thenReturn("refresh-token");
            when(tokenPolicy.getAccessTokenExpiration()).thenReturn(3600);

            AuthenticationResponse response = authenticationService.verifyMfa(request);

            assertThat(response.isAuthenticated()).isTrue();
            assertThat(response.getAccessToken()).isEqualTo("access-token");
        }

        @Test
        @DisplayName("Should reject incorrect MFA code")
        void shouldRejectIncorrectMfaCode() {
            MfaVerificationRequest request = MfaVerificationRequest.builder()
                    .username(USERNAME)
                    .mfaCode("654321")
                    .sessionId(SESSION_ID)
                    .build();

            when(authenticationRepository.findPendingMfaSession(SESSION_ID))
                    .thenReturn(Optional.of(createPendingMfaSession()));
            when(mfaPolicy.validateMfaCode("123456", "654321")).thenReturn(false);

            assertThatThrownBy(() -> authenticationService.verifyMfa(request))
                    .isInstanceOf(AuthenticationFailedException.class)
                    .hasMessageContaining("Invalid MFA code");
        }

        @Test
        @DisplayName("Should lock after max MFA attempts")
        void shouldLockAfterMaxMfaAttempts() {
            MfaVerificationRequest request = MfaVerificationRequest.builder()
                    .username(USERNAME)
                    .mfaCode("654321")
                    .sessionId(SESSION_ID)
                    .build();

            when(authenticationRepository.findPendingMfaSession(SESSION_ID))
                    .thenReturn(Optional.of(createPendingMfaSession()));
            when(mfaPolicy.validateMfaCode(any(), any())).thenReturn(false);
            when(mfaPolicy.isMfaLocked(any())).thenReturn(true);

            assertThatThrownBy(() -> authenticationService.verifyMfa(request))
                    .isInstanceOf(AccountLockedException.class)
                    .hasMessageContaining("MFA locked");
        }

        @Test
        @DisplayName("Should expire MFA session after timeout")
        void shouldExpireMfaSession() {
            MfaVerificationRequest request = MfaVerificationRequest.builder()
                    .username(USERNAME)
                    .mfaCode("123456")
                    .sessionId(SESSION_ID)
                    .build();

            when(authenticationRepository.findPendingMfaSession(SESSION_ID))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> authenticationService.verifyMfa(request))
                    .isInstanceOf(AuthenticationFailedException.class)
                    .hasMessageContaining("MFA session expired");
        }
    }

    @Nested
    @DisplayName("Token Refresh Tests")
    class TokenRefreshTests {

        @Test
        @DisplayName("Should refresh valid token")
        void shouldRefreshValidToken() {
            TokenRefreshRequest request = TokenRefreshRequest.builder()
                    .refreshToken("valid-refresh-token")
                    .build();

            when(tokenPolicy.validateRefreshToken("valid-refresh-token")).thenReturn(true);
            when(tokenPolicy.extractUserId("valid-refresh-token")).thenReturn(USER_ID);
            when(authenticationRepository.findById(UUID.fromString(USER_ID)))
                    .thenReturn(Optional.of(createUserRecord()));
            when(tokenPolicy.generateAccessToken(eq(USER_ID), any())).thenReturn("new-access-token");
            when(tokenPolicy.generateRefreshToken(USER_ID)).thenReturn("new-refresh-token");
            when(tokenPolicy.getAccessTokenExpiration()).thenReturn(3600);

            AuthenticationResponse response = authenticationService.refreshToken(request);

            assertThat(response.getAccessToken()).isEqualTo("new-access-token");
            assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");
        }

        @Test
        @DisplayName("Should reject invalid refresh token")
        void shouldRejectInvalidRefreshToken() {
            TokenRefreshRequest request = TokenRefreshRequest.builder()
                    .refreshToken("invalid-token")
                    .build();

            when(tokenPolicy.validateRefreshToken("invalid-token")).thenReturn(false);

            assertThatThrownBy(() -> authenticationService.refreshToken(request))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessageContaining("Invalid refresh token");
        }

        @Test
        @DisplayName("Should reject refresh token for locked account")
        void shouldRejectTokenForLockedAccount() {
            TokenRefreshRequest request = TokenRefreshRequest.builder()
                    .refreshToken("valid-refresh-token")
                    .build();

            when(tokenPolicy.validateRefreshToken("valid-refresh-token")).thenReturn(true);
            when(tokenPolicy.extractUserId("valid-refresh-token")).thenReturn(USER_ID);
            when(authenticationRepository.findById(UUID.fromString(USER_ID)))
                    .thenReturn(Optional.of(createLockedUserRecord()));

            assertThatThrownBy(() -> authenticationService.refreshToken(request))
                    .isInstanceOf(AccountLockedException.class);
        }
    }

    @Nested
    @DisplayName("Token Validation Tests")
    class TokenValidationTests {

        @Test
        @DisplayName("Should validate valid access token")
        void shouldValidateAccessToken() {
            String token = "valid-access-token";

            when(tokenPolicy.validateAccessToken(token)).thenReturn(true);
            when(tokenPolicy.extractUserId(token)).thenReturn(USER_ID);
            when(tokenPolicy.extractRoles(token)).thenReturn(Set.of("USER"));

            TokenValidationResponse response = authenticationService.validateToken(token);

            assertThat(response.isValid()).isTrue();
            assertThat(response.getUserId()).isEqualTo(USER_ID);
            assertThat(response.getRoles()).containsExactly("USER");
        }

        @Test
        @DisplayName("Should reject invalid access token")
        void shouldRejectInvalidToken() {
            String token = "invalid-token";

            when(tokenPolicy.validateAccessToken(token)).thenReturn(false);

            TokenValidationResponse response = authenticationService.validateToken(token);

            assertThat(response.isValid()).isFalse();
        }

        @Test
        @DisplayName("Should check token expiration")
        void shouldCheckTokenExpiration() {
            String token = "valid-access-token";

            when(tokenPolicy.validateAccessToken(token)).thenReturn(true);
            when(tokenPolicy.isTokenExpired(token)).thenReturn(false);
            when(tokenPolicy.extractUserId(token)).thenReturn(USER_ID);
            when(tokenPolicy.extractRoles(token)).thenReturn(Set.of("USER"));

            TokenValidationResponse response = authenticationService.validateToken(token);

            assertThat(response.isExpired()).isFalse();
        }

        @Test
        @DisplayName("Should indicate expired token")
        void shouldIndicateExpiredToken() {
            String token = "expired-token";

            when(tokenPolicy.validateAccessToken(token)).thenReturn(true);
            when(tokenPolicy.isTokenExpired(token)).thenReturn(true);

            TokenValidationResponse response = authenticationService.validateToken(token);

            assertThat(response.isExpired()).isTrue();
        }
    }

    @Nested
    @DisplayName("Logout Tests")
    class LogoutTests {

        @Test
        @DisplayName("Should logout user successfully")
        void shouldLogoutUser() {
            String token = "valid-access-token";
            String sessionId = "session-123";

            when(tokenPolicy.extractUserId(token)).thenReturn(USER_ID);
            when(authenticationRepository.findSession(sessionId))
                    .thenReturn(Optional.of(createActiveSession()));

            authenticationService.logout(token, sessionId);

            verify(authenticationRepository).invalidateSession(sessionId);
            verify(eventPublisher).publish(eq("authentication.logout"), any());
        }

        @Test
        @DisplayName("Should blacklist token on logout")
        void shouldBlacklistToken() {
            String token = "valid-access-token";
            String sessionId = "session-123";

            when(tokenPolicy.extractUserId(token)).thenReturn(USER_ID);
            when(authenticationRepository.findSession(sessionId))
                    .thenReturn(Optional.of(createActiveSession()));

            authenticationService.logout(token, sessionId);

            verify(authenticationRepository).blacklistToken(token);
        }

        @Test
        @DisplayName("Should handle logout for non-existent session")
        void shouldHandleNonExistentSession() {
            String token = "valid-access-token";
            String sessionId = "non-existent-session";

            when(tokenPolicy.extractUserId(token)).thenReturn(USER_ID);
            when(authenticationRepository.findSession(sessionId))
                    .thenReturn(Optional.empty());

            // Should not throw exception, just complete gracefully
            assertThatCode(() -> authenticationService.logout(token, sessionId))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("User Management Tests")
    class UserManagementTests {

        @Test
        @DisplayName("Should get user profile")
        void shouldGetUserProfile() {
            when(authenticationRepository.findById(UUID.fromString(USER_ID)))
                    .thenReturn(Optional.of(createUserRecord()));

            var profile = authenticationService.getUserProfile(UUID.fromString(USER_ID));

            assertThat(profile).isNotNull();
            assertThat(profile.userId()).isEqualTo(UUID.fromString(USER_ID));
            assertThat(profile.username()).isEqualTo(USERNAME);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowWhenUserNotFound() {
            UUID nonExistentId = UUID.randomUUID();

            when(authenticationRepository.findById(nonExistentId))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> authenticationService.getUserProfile(nonExistentId))
                    .isInstanceOf(AuthenticationFailedException.class)
                    .hasMessageContaining("User not found");
        }

        @Test
        @DisplayName("Should update user password")
        void shouldUpdatePassword() {
            String oldPassword = "OldP@ss123";
            String newPassword = "NewP@ss456";

            when(authenticationRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(createUserRecord()));
            when(passwordEncoder.matches(oldPassword, ENCODED_PASSWORD)).thenReturn(true);
            when(passwordEncoder.encode(newPassword)).thenReturn("$2a$10$newEncodedPassword");

            authenticationService.updatePassword(USERNAME, oldPassword, newPassword);

            verify(authenticationRepository).updatePassword(eq(USERNAME), any());
            verify(eventPublisher).publish(eq("password.changed"), any());
        }

        @Test
        @DisplayName("Should reject password update with wrong old password")
        void shouldRejectWrongOldPassword() {
            String oldPassword = "WrongP@ss123";
            String newPassword = "NewP@ss456";

            when(authenticationRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(createUserRecord()));
            when(passwordEncoder.matches(oldPassword, ENCODED_PASSWORD)).thenReturn(false);

            assertThatThrownBy(() ->
                    authenticationService.updatePassword(USERNAME, oldPassword, newPassword)
            ).isInstanceOf(AuthenticationFailedException.class)
             .hasMessageContaining("Current password is incorrect");
        }
    }

    // Helper methods

    private AuthenticationRepository.UserRecord createUserRecord() {
        return new AuthenticationRepository.UserRecord(
                UUID.fromString(USER_ID),
                USERNAME,
                ENCODED_PASSWORD,
                Set.of("USER"),
                false,
                null,
                0
        );
    }

    private AuthenticationRepository.UserRecord createUserRecordWithFailedAttempts(int attempts) {
        return new AuthenticationRepository.UserRecord(
                UUID.fromString(USER_ID),
                USERNAME,
                ENCODED_PASSWORD,
                Set.of("USER"),
                false,
                null,
                attempts
        );
    }

    private AuthenticationRepository.UserRecord createLockedUserRecord() {
        return new AuthenticationRepository.UserRecord(
                UUID.fromString(USER_ID),
                USERNAME,
                ENCODED_PASSWORD,
                Set.of("USER"),
                true,
                Instant.now().plusSeconds(1800),
                5
        );
    }

    private AuthenticationRepository.PendingMfaSession createPendingMfaSession() {
        return new AuthenticationRepository.PendingMfaSession(
                SESSION_ID,
                USERNAME,
                "123456",
                Instant.now().plusSeconds(300)
        );
    }

    private AuthenticationRepository.ActiveSession createActiveSession() {
        return new AuthenticationRepository.ActiveSession(
                SESSION_ID,
                UUID.fromString(USER_ID),
                Instant.now().plusSeconds(3600)
        );
    }
}
