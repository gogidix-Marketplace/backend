package com.gogidix.aiservices.aiauthenticationservice.domain.model;

import com.gogidix.aiservices.aiauthenticationservice.domain.aggregate.AuthenticationAttempt;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AuthenticationResult Domain Model Tests")
class AuthenticationResultTest {

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String SESSION_ID = "session-123";

    @Nested
    @DisplayName("Authentication Result Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create successful authentication result")
        void shouldCreateSuccessfulResult() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result).isNotNull();
            assertThat(result.isAuthenticated()).isTrue();
            assertThat(result.getUserId()).isEqualTo(UUID.fromString(USER_ID));
            assertThat(result.getSessionId()).isEqualTo(SESSION_ID);
            assertThat(result.getStatus()).isEqualTo(AuthenticationStatus.AUTHENTICATED);
            assertThat(result.getFailureReason()).isEmpty();
        }

        @Test
        @DisplayName("Should create failed authentication result with reason")
        void shouldCreateFailedResult() {
            AuthenticationResult result = AuthenticationResult.failed(
                AuthenticationStatus.INVALID_CREDENTIALS,
                "Invalid username or password"
            );

            assertThat(result).isNotNull();
            assertThat(result.isAuthenticated()).isFalse();
            assertThat(result.getStatus()).isEqualTo(AuthenticationStatus.INVALID_CREDENTIALS);
            assertThat(result.getFailureReason()).contains("Invalid username or password");
        }

        @Test
        @DisplayName("Should create MFA required result")
        void shouldCreateMfaRequiredResult() {
            AuthenticationResult result = AuthenticationResult.mfaRequired(USER_ID, "SMS");

            assertThat(result).isNotNull();
            assertThat(result.isAuthenticated()).isFalse();
            assertThat(result.getStatus()).isEqualTo(AuthenticationStatus.MFA_REQUIRED);
            assertThat(result.getMfaMethod()).isEqualTo("SMS");
        }

        @Test
        @DisplayName("Should create account locked result")
        void shouldCreateAccountLockedResult() {
            AuthenticationResult result = AuthenticationResult.accountLocked(
                USER_ID,
                Instant.now().plusSeconds(1800)
            );

            assertThat(result).isNotNull();
            assertThat(result.isAuthenticated()).isFalse();
            assertThat(result.getStatus()).isEqualTo(AuthenticationStatus.ACCOUNT_LOCKED);
            assertThat(result.getLockUntil()).isNotNull();
            assertThat(result.getLockUntil()).isAfter(Instant.now());
        }
    }

    @Nested
    @DisplayName("Token Management Tests")
    class TokenTests {

        @Test
        @DisplayName("Should generate access token on success")
        void shouldGenerateAccessToken() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result.getAccessToken()).isNotNull();
            assertThat(result.getAccessToken()).isNotEmpty();
        }

        @Test
        @DisplayName("Should generate refresh token on success")
        void shouldGenerateRefreshToken() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result.getRefreshToken()).isNotNull();
            assertThat(result.getRefreshToken()).isNotEmpty();
            assertThat(result.getRefreshToken()).isNotEqualTo(result.getAccessToken());
        }

        @Test
        @DisplayName("Should set correct token expiration")
        void shouldSetTokenExpiration() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result.getExpiresIn()).isEqualTo(3600); // 1 hour in seconds
        }

        @Test
        @DisplayName("Should verify access token format")
        void shouldVerifyAccessTokenFormat() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            // JWT tokens have 3 parts separated by dots
            String[] parts = result.getAccessToken().split("\\.");
            assertThat(parts).hasSize(3);
        }
    }

    @Nested
    @DisplayName("User Role Tests")
    class UserRoleTests {

        @Test
        @DisplayName("Should assign default user role")
        void shouldAssignDefaultRole() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result.getRoles()).containsExactly("USER");
        }

        @Test
        @DisplayName("Should assign multiple roles")
        void shouldAssignMultipleRoles() {
            Set<String> roles = Set.of("USER", "ADMIN", "MODERATOR");
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID, roles);

            assertThat(result.getRoles()).containsExactlyInAnyOrder("USER", "ADMIN", "MODERATOR");
        }

        @Test
        @DisplayName("Should reject empty roles")
        void shouldRejectEmptyRoles() {
            assertThatThrownBy(() -> AuthenticationResult.success(USER_ID, SESSION_ID, Set.of()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Roles cannot be empty");
        }

        @Test
        @DisplayName("Should reject null roles")
        void shouldRejectNullRoles() {
            assertThatThrownBy(() -> AuthenticationResult.success(USER_ID, SESSION_ID, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Roles cannot be null");
        }
    }

    @Nested
    @DisplayName("Authentication Attempt Tracking Tests")
    class AttemptTrackingTests {

        @Test
        @DisplayName("Should track failed attempts")
        void shouldTrackFailedAttempts() {
            AuthenticationResult result = AuthenticationResult.failed(
                AuthenticationStatus.INVALID_CREDENTIALS,
                "Invalid credentials"
            );

            assertThat(result.getRemainingAttempts()).isEqualTo(4); // Default 5 attempts
        }

        @Test
        @DisplayName("Should decrement remaining attempts")
        void shouldDecrementRemainingAttempts() {
            AuthenticationResult result = AuthenticationResult.failed(
                AuthenticationStatus.INVALID_CREDENTIALS,
                "Invalid credentials"
            );

            AuthenticationResult result2 = AuthenticationResult.withAttempts(
                AuthenticationStatus.INVALID_CREDENTIALS,
                "Invalid credentials",
                3
            );

            assertThat(result.getRemainingAttempts()).isEqualTo(4);
            assertThat(result2.getRemainingAttempts()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should lock account after max attempts")
        void shouldLockAfterMaxAttempts() {
            AuthenticationResult result = AuthenticationResult.withAttempts(
                AuthenticationStatus.ACCOUNT_LOCKED,
                "Account locked due to too many failed attempts",
                0
            );

            assertThat(result.getStatus()).isEqualTo(AuthenticationStatus.ACCOUNT_LOCKED);
            assertThat(result.getRemainingAttempts()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should reset attempts on successful auth")
        void shouldResetAttemptsOnSuccess() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result.getRemainingAttempts()).isEqualTo(5); // Reset to max
        }
    }

    @Nested
    @DisplayName("Biometric Authentication Tests")
    class BiometricTests {

        @Test
        @DisplayName("Should create biometric authentication result")
        void shouldCreateBiometricResult() {
            AuthenticationResult result = AuthenticationResult.biometricSuccess(
                USER_ID,
                SESSION_ID,
                BiometricType.FACE_RECOGNITION
            );

            assertThat(result.isAuthenticated()).isTrue();
            assertThat(result.getBiometricType()).isEqualTo(BiometricType.FACE_RECOGNITION);
            assertThat(result.getAuthenticationMethod()).isEqualTo("BIOMETRIC");
        }

        @Test
        @DisplayName("Should support multiple biometric types")
        void shouldSupportMultipleBiometricTypes() {
            AuthenticationResult faceResult = AuthenticationResult.biometricSuccess(
                USER_ID, SESSION_ID, BiometricType.FACE_RECOGNITION
            );
            AuthenticationResult fingerprintResult = AuthenticationResult.biometricSuccess(
                USER_ID, SESSION_ID, BiometricType.FINGERPRINT
            );
            AuthenticationResult voiceResult = AuthenticationResult.biometricSuccess(
                USER_ID, SESSION_ID, BiometricType.VOICE_RECOGNITION
            );

            assertThat(faceResult.getBiometricType()).isEqualTo(BiometricType.FACE_RECOGNITION);
            assertThat(fingerprintResult.getBiometricType()).isEqualTo(BiometricType.FINGERPRINT);
            assertThat(voiceResult.getBiometricType()).isEqualTo(BiometricType.VOICE_RECOGNITION);
        }

        @Test
        @DisplayName("Should calculate biometric confidence score")
        void shouldCalculateBiometricConfidence() {
            AuthenticationResult result = AuthenticationResult.biometricSuccess(
                USER_ID,
                SESSION_ID,
                BiometricType.FACE_RECOGNITION,
                0.95
            );

            assertThat(result.getConfidenceScore()).isEqualTo(0.95);
            assertThat(result.getConfidenceScore()).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("Should reject low confidence biometric auth")
        void shouldRejectLowConfidence() {
            assertThatThrownBy(() -> AuthenticationResult.biometricSuccess(
                USER_ID,
                SESSION_ID,
                BiometricType.FACE_RECOGNITION,
                0.5
            )).isInstanceOf(AuthenticationException.class)
              .hasMessageContaining("Biometric confidence too low");
        }
    }

    @Nested
    @DisplayName("Risk-Based Authentication Tests")
    class RiskBasedTests {

        @Test
        @DisplayName("Should calculate risk score for authentication")
        void shouldCalculateRiskScore() {
            AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                USER_ID,
                SESSION_ID,
                0.3
            );

            assertThat(result.getRiskScore()).isEqualTo(0.3);
        }

        @Test
        @DisplayName("Should allow low risk authentication")
        void shouldAllowLowRisk() {
            AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                USER_ID, SESSION_ID, 0.2
            );

            assertThat(result.isAuthenticated()).isTrue();
            assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.LOW);
        }

        @Test
        @DisplayName("Should require MFA for medium risk")
        void shouldRequireMfaForMediumRisk() {
            AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                USER_ID, SESSION_ID, 0.5
            );

            assertThat(result.getStatus()).isEqualTo(AuthenticationStatus.MFA_REQUIRED);
            assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.MEDIUM);
        }

        @Test
        @DisplayName("Should block high risk authentication")
        void shouldBlockHighRisk() {
            AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                USER_ID, SESSION_ID, 0.9
            );

            assertThat(result.isAuthenticated()).isFalse();
            assertThat(result.getStatus()).isEqualTo(AuthenticationStatus.HIGH_RISK_BLOCKED);
            assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.HIGH);
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 0.1, 0.25, 0.33})
        @DisplayName("Should classify low risk correctly")
        void shouldClassifyLowRisk(double score) {
            AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                USER_ID, SESSION_ID, score
            );

            assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.LOW);
        }

        @ParameterizedTest
        @ValueSource(doubles = {0.34, 0.5, 0.66, 0.75})
        @DisplayName("Should classify medium risk correctly")
        void shouldClassifyMediumRisk(double score) {
            AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                USER_ID, SESSION_ID, score
            );

            assertThat(result.getRiskLevel()).isEqualTo(RiskLevel.MEDIUM);
        }
    }

    @Nested
    @DisplayName("Session Management Tests")
    class SessionTests {

        @Test
        @DisplayName("Should create session on successful auth")
        void shouldCreateSession() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result.getSessionId()).isEqualTo(SESSION_ID);
            assertThat(result.getSessionCreatedAt()).isNotNull();
            assertThat(result.getSessionCreatedAt()).isBefore(Instant.now().plusSeconds(1));
        }

        @Test
        @DisplayName("Should track session expiration")
        void shouldTrackSessionExpiration() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result.getSessionExpiresAt()).isNotNull();
            assertThat(result.getSessionExpiresAt()).isAfter(Instant.now().plusSeconds(3599));
        }

        @Test
        @DisplayName("Should validate session status")
        void shouldValidateSession() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result.isSessionValid()).isTrue();
        }

        @Test
        @DisplayName("Should invalidate expired session")
        void shouldInvalidateExpiredSession() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            // Simulate expired session
            AuthenticationResult expiredResult = AuthenticationResult.withSessionExpiry(
                result, Instant.now().minusSeconds(60)
            );

            assertThat(expiredResult.isSessionValid()).isFalse();
        }
    }

    @Nested
    @DisplayName("Device Fingerprinting Tests")
    class DeviceTests {

        @Test
        @DisplayName("Should track device fingerprint")
        void shouldTrackDeviceFingerprint() {
            String deviceFingerprint = "fp_abc123";
            AuthenticationResult result = AuthenticationResult.withDeviceTracking(
                USER_ID, SESSION_ID, deviceFingerprint
            );

            assertThat(result.getDeviceFingerprint()).isEqualTo(deviceFingerprint);
        }

        @Test
        @DisplayName("Should flag new device")
        void shouldFlagNewDevice() {
            String deviceFingerprint = "fp_new_device";
            AuthenticationResult result = AuthenticationResult.withDeviceTracking(
                USER_ID, SESSION_ID, deviceFingerprint
            );

            assertThat(result.isNewDevice()).isTrue();
        }

        @Test
        @DisplayName("Should recognize known device")
        void shouldRecognizeKnownDevice() {
            String deviceFingerprint = "fp_known_device";
            AuthenticationResult result = AuthenticationResult.withKnownDevice(
                USER_ID, SESSION_ID, deviceFingerprint
            );

            assertThat(result.isNewDevice()).isFalse();
            assertThat(result.getDeviceFingerprint()).isEqualTo(deviceFingerprint);
        }
    }

    @Nested
    @DisplayName("IP and Location Tests")
    class LocationTests {

        @Test
        @DisplayName("Should track IP address")
        void shouldTrackIpAddress() {
            String ipAddress = "192.168.1.1";
            AuthenticationResult result = AuthenticationResult.withIpTracking(
                USER_ID, SESSION_ID, ipAddress
            );

            assertThat(result.getIpAddress()).isEqualTo(ipAddress);
        }

        @Test
        @DisplayName("Should track location")
        void shouldTrackLocation() {
            String location = "New York, US";
            AuthenticationResult result = AuthenticationResult.withLocationTracking(
                USER_ID, SESSION_ID, location
            );

            assertThat(result.getLocation()).isEqualTo(location);
        }

        @Test
        @DisplayName("Should flag unusual location")
        void shouldFlagUnusualLocation() {
            String unusualLocation = "Unknown Location";
            AuthenticationResult result = AuthenticationResult.withUnusualLocation(
                USER_ID, SESSION_ID, unusualLocation
            );

            assertThat(result.isUnusualLocation()).isTrue();
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal with same session ID")
        void shouldBeEqualWithSameSessionId() {
            AuthenticationResult result1 = AuthenticationResult.success(USER_ID, SESSION_ID);
            AuthenticationResult result2 = AuthenticationResult.success(USER_ID, SESSION_ID);

            assertThat(result1.getSessionId()).isEqualTo(result2.getSessionId());
        }

        @Test
        @DisplayName("Should have consistent hash code")
        void shouldHaveConsistentHashCode() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, SESSION_ID);

            int hashCode1 = result.hashCode();
            int hashCode2 = result.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
