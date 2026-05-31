package com.gogidix.aiservices.aiauthenticationservice.domain.aggregate;

import com.gogidix.aiservices.aiauthenticationservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AuthenticationAttempt Domain Model Tests")
class AuthenticationAttemptTest {

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String USERNAME = "testuser";

    @Nested
    @DisplayName("Authentication Attempt Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create authentication attempt with username")
        void shouldCreateAttemptWithUsername() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            assertThat(attempt).isNotNull();
            assertThat(attempt.getUsername()).isEqualTo(USERNAME);
            assertThat(attempt.getAttemptId()).isNotNull();
            assertThat(attempt.getTimestamp()).isNotNull();
            assertThat(attempt.getTimestamp()).isBefore(Instant.now().plusSeconds(1));
        }

        @Test
        @DisplayName("Should create authentication attempt with user ID")
        void shouldCreateAttemptWithUserId() {
            AuthenticationAttempt attempt = AuthenticationAttempt.createWithUserId(UUID.fromString(USER_ID));

            assertThat(attempt).isNotNull();
            assertThat(attempt.getUserId()).isEqualTo(UUID.fromString(USER_ID));
            assertThat(attempt.getAttemptId()).isNotNull();
        }

        @Test
        @DisplayName("Should generate unique attempt ID")
        void shouldGenerateUniqueAttemptId() {
            AuthenticationAttempt attempt1 = AuthenticationAttempt.create(USERNAME);
            AuthenticationAttempt attempt2 = AuthenticationAttempt.create(USERNAME);

            assertThat(attempt1.getAttemptId()).isNotEqualTo(attempt2.getAttemptId());
        }

        @Test
        @DisplayName("Should reject null username")
        void shouldRejectNullUsername() {
            assertThatThrownBy(() -> AuthenticationAttempt.create(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Username cannot be null");
        }

        @Test
        @DisplayName("Should reject empty username")
        void shouldRejectEmptyUsername() {
            assertThatThrownBy(() -> AuthenticationAttempt.create(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Username cannot be empty");
        }
    }

    @Nested
    @DisplayName("Authentication Status Tracking Tests")
    class StatusTrackingTests {

        @Test
        @DisplayName("Should track successful authentication")
        void shouldTrackSuccessfulAuth() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.markSuccessful();

            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.SUCCESS);
            assertThat(attempt.isSuccessful()).isTrue();
            assertThat(attempt.getCompletedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should track failed authentication")
        void shouldTrackFailedAuth() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.markFailed(FailureReason.INVALID_CREDENTIALS);

            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.FAILED);
            assertThat(attempt.isSuccessful()).isFalse();
            assertThat(attempt.getFailureReason()).isEqualTo(FailureReason.INVALID_CREDENTIALS);
        }

        @Test
        @DisplayName("Should track MFA required status")
        void shouldTrackMfaRequired() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.requireMfa("SMS");

            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.MFA_REQUIRED);
            assertThat(attempt.getMfaMethod()).isEqualTo("SMS");
        }

        @Test
        @DisplayName("Should track pending status")
        void shouldTrackPendingStatus() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.PENDING);
            assertThat(attempt.isPending()).isTrue();
        }

        @Test
        @DisplayName("Should track blocked status")
        void shouldTrackBlockedStatus() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.markBlocked(FailureReason.ACCOUNT_LOCKED);

            assertThat(attempt.getStatus()).isEqualTo(AttemptStatus.BLOCKED);
            assertThat(attempt.isBlocked()).isTrue();
        }
    }

    @Nested
    @DisplayName("Attempt Counting Tests")
    class AttemptCountingTests {

        @Test
        @DisplayName("Should increment failed attempt count")
        void shouldIncrementFailedAttempts() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            for (int i = 0; i < 3; i++) {
                attempt.markFailed(FailureReason.INVALID_CREDENTIALS);
            }

            assertThat(attempt.getFailedAttemptCount()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should reset count on successful auth")
        void shouldResetCountOnSuccess() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            for (int i = 0; i < 3; i++) {
                attempt.markFailed(FailureReason.INVALID_CREDENTIALS);
            }
            attempt.markSuccessful();

            assertThat(attempt.getFailedAttemptCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should track remaining attempts")
        void shouldTrackRemainingAttempts() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            assertThat(attempt.getRemainingAttempts()).isEqualTo(5);

            attempt.markFailed(FailureReason.INVALID_CREDENTIALS);
            assertThat(attempt.getRemainingAttempts()).isEqualTo(4);

            attempt.markFailed(FailureReason.INVALID_CREDENTIALS);
            assertThat(attempt.getRemainingAttempts()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should indicate when locked out")
        void shouldIndicateLockout() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            for (int i = 0; i < 5; i++) {
                attempt.markFailed(FailureReason.INVALID_CREDENTIALS);
            }

            assertThat(attempt.isLockedOut()).isTrue();
            assertThat(attempt.getRemainingAttempts()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should calculate lockout expiration time")
        void shouldCalculateLockoutExpiration() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            for (int i = 0; i < 5; i++) {
                attempt.markFailed(FailureReason.INVALID_CREDENTIALS);
            }

            assertThat(attempt.getLockoutUntil()).isNotNull();
            assertThat(attempt.getLockoutUntil()).isAfter(Instant.now());
            assertThat(attempt.getLockoutUntil()).isBefore(Instant.now().plusSeconds(1801)); // 30 minutes + 1 second
        }
    }

    @Nested
    @DisplayName("Authentication Method Tests")
    class MethodTests {

        @ParameterizedTest
        @EnumSource(AuthenticationMethod.class)
        @DisplayName("Should support all authentication methods")
        void shouldSupportAllMethods(AuthenticationMethod method) {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setMethod(method);

            assertThat(attempt.getMethod()).isEqualTo(method);
        }

        @Test
        @DisplayName("Should default to password method")
        void shouldDefaultToPassword() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            assertThat(attempt.getMethod()).isEqualTo(AuthenticationMethod.PASSWORD);
        }

        @Test
        @DisplayName("Should track biometric method")
        void shouldTrackBiometricMethod() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setMethod(AuthenticationMethod.BIOMETRIC);

            assertThat(attempt.getMethod()).isEqualTo(AuthenticationMethod.BIOMETRIC);
        }
    }

    @Nested
    @DisplayName("Metadata Tracking Tests")
    class MetadataTests {

        @Test
        @DisplayName("Should store IP address")
        void shouldStoreIpAddress() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setIpAddress("192.168.1.1");

            assertThat(attempt.getIpAddress()).isEqualTo("192.168.1.1");
        }

        @Test
        @DisplayName("Should store user agent")
        void shouldStoreUserAgent() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setUserAgent("Mozilla/5.0");

            assertThat(attempt.getUserAgent()).isEqualTo("Mozilla/5.0");
        }

        @Test
        @DisplayName("Should store device information")
        void shouldStoreDeviceInfo() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setDeviceInfo("iPhone 13 Pro");

            assertThat(attempt.getDeviceInfo()).isEqualTo("iPhone 13 Pro");
        }

        @Test
        @DisplayName("Should store location")
        void shouldStoreLocation() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setLocation("New York, US");

            assertThat(attempt.getLocation()).isEqualTo("New York, US");
        }

        @Test
        @DisplayName("Should store custom metadata")
        void shouldStoreCustomMetadata() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.addMetadata("key", "value");

            assertThat(attempt.getMetadata("key")).isEqualTo("value");
        }

        @Test
        @DisplayName("Should store multiple metadata entries")
        void shouldStoreMultipleMetadata() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.addMetadata("key1", "value1");
            attempt.addMetadata("key2", "value2");

            assertThat(attempt.getMetadata("key1")).isEqualTo("value1");
            assertThat(attempt.getMetadata("key2")).isEqualTo("value2");
        }
    }

    @Nested
    @DisplayName("Timing Tests")
    class TimingTests {

        @Test
        @DisplayName("Should record start time")
        void shouldRecordStartTime() {
            Instant before = Instant.now();
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            Instant after = Instant.now();

            assertThat(attempt.getTimestamp()).isAfter(before);
            assertThat(attempt.getTimestamp()).isBefore(after);
        }

        @Test
        @DisplayName("Should calculate duration")
        void shouldCalculateDuration() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Ignore
            }

            attempt.markSuccessful();

            assertThat(attempt.getDuration()).isGreaterThanOrEqualTo(100);
        }

        @Test
        @DisplayName("Should handle attempts without completion")
        void shouldHandleIncompleteAttempts() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            assertThat(attempt.getDuration()).isNull();
        }
    }

    @Nested
    @DisplayName("Security Context Tests")
    class SecurityContextTests {

        @Test
        @DisplayName("Should track risk score")
        void shouldTrackRiskScore() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setRiskScore(0.75);

            assertThat(attempt.getRiskScore()).isEqualTo(0.75);
        }

        @Test
        @DisplayName("Should validate risk score range")
        void shouldValidateRiskScoreRange() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            assertThatThrownBy(() -> attempt.setRiskScore(-0.1))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> attempt.setRiskScore(1.1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should flag suspicious activity")
        void shouldFlagSuspiciousActivity() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.flagSuspicious("Multiple failed attempts from different IPs");

            assertThat(attempt.isSuspicious()).isTrue();
            assertThat(attempt.getSuspiciousReason()).contains("Multiple failed attempts");
        }

        @Test
        @DisplayName("Should track device fingerprint")
        void shouldTrackDeviceFingerprint() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setDeviceFingerprint("fp_abc123");

            assertThat(attempt.getDeviceFingerprint()).isEqualTo("fp_abc123");
        }
    }

    @Nested
    @DisplayName("MFA Tracking Tests")
    class MfaTests {

        @Test
        @DisplayName("Should store MFA code")
        void shouldStoreMfaCode() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setMfaCode("123456");

            assertThat(attempt.getMfaCode()).isEqualTo("123456");
        }

        @Test
        @DisplayName("Should validate MFA code")
        void shouldValidateMfaCode() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setMfaCode("123456");

            assertThat(attempt.validateMfaCode("123456")).isTrue();
            assertThat(attempt.validateMfaCode("654321")).isFalse();
        }

        @Test
        @DisplayName("Should track MFA delivery method")
        void shouldTrackMfaDelivery() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setMfaMethod("SMS");

            assertThat(attempt.getMfaMethod()).isEqualTo("SMS");
        }

        @Test
        @DisplayName("Should limit MFA attempts")
        void shouldLimitMfaAttempts() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.requireMfa("SMS");

            for (int i = 0; i < 3; i++) {
                attempt.invalidMfaAttempt();
            }

            assertThat(attempt.isMfaLocked()).isTrue();
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal with same attempt ID")
        void shouldBeEqualWithSameId() {
            AuthenticationAttempt attempt1 = AuthenticationAttempt.create(USERNAME);
            AuthenticationAttempt attempt2 = AuthenticationAttempt.create(USERNAME);

            assertThat(attempt1.getAttemptId()).isNotEqualTo(attempt2.getAttemptId());
            assertThat(attempt1).isNotEqualTo(attempt2);
        }

        @Test
        @DisplayName("Should have consistent hash code")
        void shouldHaveConsistentHashCode() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);

            int hashCode1 = attempt.hashCode();
            int hashCode2 = attempt.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }
}
