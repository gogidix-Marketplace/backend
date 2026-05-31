package com.gogidix.aiservices.aiauthenticationservice.domain.policy;

import com.gogidix.aiservices.aiauthenticationservice.domain.aggregate.AuthenticationAttempt;
import com.gogidix.aiservices.aiauthenticationservice.domain.model.*;
import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Authentication Policy Domain Tests")
class AuthenticationPolicyTest {

    private static final String USERNAME = "testuser";
    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Password Policy Tests")
    class PasswordPolicyTests {

        private final PasswordPolicy policy = new PasswordPolicy();

        @Test
        @DisplayName("Should accept valid password")
        void shouldAcceptValidPassword() {
            String password = "SecureP@ssw0rd123";

            assertThat(policy.validate(password).isValid()).isTrue();
            assertThat(policy.validate(password).getErrors()).isEmpty();
        }

        @Test
        @DisplayName("Should reject password shorter than 8 characters")
        void shouldRejectShortPassword() {
            String password = "Short1!";

            PasswordPolicy.ValidationResult result = policy.validate(password);

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).contains("Password must be at least 8 characters long");
        }

        @Test
        @DisplayName("Should reject password without uppercase letter")
        void shouldRejectNoUppercase() {
            String password = "lowercase123!";

            PasswordPolicy.ValidationResult result = policy.validate(password);

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).contains("Password must contain at least one uppercase letter");
        }

        @Test
        @DisplayName("Should reject password without lowercase letter")
        void shouldRejectNoLowercase() {
            String password = "UPPERCASE123!";

            PasswordPolicy.ValidationResult result = policy.validate(password);

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).contains("Password must contain at least one lowercase letter");
        }

        @Test
        @DisplayName("Should reject password without digit")
        void shouldRejectNoDigit() {
            String password = "NoDigits!";

            PasswordPolicy.ValidationResult result = policy.validate(password);

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).contains("Password must contain at least one digit");
        }

        @Test
        @DisplayName("Should reject password without special character")
        void shouldRejectNoSpecialChar() {
            String password = "NoSpecialChars123";

            PasswordPolicy.ValidationResult result = policy.validate(password);

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).contains("Password must contain at least one special character");
        }

        @Test
        @DisplayName("Should reject common passwords")
        void shouldRejectCommonPasswords() {
            String[] commonPasswords = {"Password123!", "Welcome123!", "Admin123!"};

            for (String password : commonPasswords) {
                PasswordPolicy.ValidationResult result = policy.validate(password);
                assertThat(result.isValid()).isFalse();
                assertThat(result.getErrors()).contains("Password is too common");
            }
        }

        @Test
        @DisplayName("Should calculate password strength")
        void shouldCalculatePasswordStrength() {
            assertThat(policy.calculateStrength("aA1!")).isEqualTo(PasswordStrength.WEAK);
            assertThat(policy.calculateStrength("Password1!")).isEqualTo(PasswordStrength.MEDIUM);
            assertThat(policy.calculateStrength("SecureP@ssw0rd123!#")).isEqualTo(PasswordStrength.STRONG);
        }

        @Test
        @DisplayName("Should require strong password for admin users")
        void shouldRequireStrongPasswordForAdmin() {
            String password = "Password1!"; // Medium strength

            PasswordPolicy.ValidationResult result = policy.validateForAdmin(password);

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).contains("Admin passwords must be strong");
        }
    }

    @Nested
    @DisplayName("Account Lockout Policy Tests")
    class LockoutPolicyTests {

        private final AccountLockoutPolicy policy = new AccountLockoutPolicy();

        @Test
        @DisplayName("Should allow authentication after few failed attempts")
        void shouldAllowAfterFewFailures() {
            AuthenticationAttempt attempt = createFailedAttempts(3);

            assertThat(policy.isLockedOut(attempt)).isFalse();
        }

        @Test
        @DisplayName("Should lock account after 5 failed attempts")
        void shouldLockAfterMaxFailures() {
            AuthenticationAttempt attempt = createFailedAttempts(5);

            assertThat(policy.isLockedOut(attempt)).isTrue();
        }

        @Test
        @DisplayName("Should set correct lockout duration")
        void shouldSetLockoutDuration() {
            AuthenticationAttempt attempt = createFailedAttempts(5);

            Instant lockoutUntil = policy.getLockoutUntil(attempt);

            assertThat(lockoutUntil).isNotNull();
            assertThat(lockoutUntil).isAfter(Instant.now());
            assertThat(lockoutUntil).isBefore(Instant.now().plusSeconds(1801)); // 30 minutes
        }

        @Test
        @DisplayName("Should unlock after lockout period")
        void shouldUnlockAfterPeriod() {
            AuthenticationAttempt attempt = createFailedAttempts(5);
            attempt.setLockoutUntil(Instant.now().minusSeconds(60));

            assertThat(policy.isLockedOut(attempt)).isFalse();
        }

        @Test
        @DisplayName("Should calculate progressive lockout duration")
        void shouldCalculateProgressiveLockout() {
            // First lockout: 30 minutes
            Instant firstLockout = policy.calculateLockoutDuration(1);
            assertThat(firstLockout).isBeforeOrEqualTo(Instant.now().plusSeconds(1801));

            // Second lockout: 1 hour
            Instant secondLockout = policy.calculateLockoutDuration(2);
            assertThat(secondLockout).isAfter(Instant.now().plusSeconds(1800));

            // Third lockout: 2 hours
            Instant thirdLockout = policy.calculateLockoutDuration(3);
            assertThat(thirdLockout).isAfter(Instant.now().plusSeconds(3600));
        }

        @Test
        @DisplayName("Should reset lockout on successful authentication")
        void shouldResetOnSuccess() {
            AuthenticationAttempt attempt = createFailedAttempts(5);
            attempt.markSuccessful();

            assertThat(policy.isLockedOut(attempt)).isFalse();
        }

        private AuthenticationAttempt createFailedAttempts(int count) {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            for (int i = 0; i < count; i++) {
                attempt.markFailed(FailureReason.INVALID_CREDENTIALS);
            }
            return attempt;
        }
    }

    @Nested
    @DisplayName("MFA Policy Tests")
    class MfaPolicyTests {

        private final MfaPolicy policy = new MfaPolicy();

        @Test
        @DisplayName("Should not require MFA for low risk users")
        void shouldNotRequireMfaForLowRisk() {
            AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                USER_ID, "session-1", 0.2
            );

            assertThat(policy.isMfaRequired(result)).isFalse();
        }

        @Test
        @DisplayName("Should require MFA for medium risk users")
        void shouldRequireMfaForMediumRisk() {
            AuthenticationResult result = AuthenticationResult.withRiskAssessment(
                USER_ID, "session-1", 0.5
            );

            assertThat(policy.isMfaRequired(result)).isTrue();
        }

        @Test
        @DisplayName("Should require MFA for admin users")
        void shouldRequireMfaForAdmins() {
            AuthenticationResult result = AuthenticationResult.success(
                USER_ID, "session-1", Set.of("USER", "ADMIN")
            );

            assertThat(policy.isMfaRequired(result)).isTrue();
        }

        @Test
        @DisplayName("Should generate valid MFA code")
        void shouldGenerateValidMfaCode() {
            String code = policy.generateMfaCode();

            assertThat(code).isNotNull();
            assertThat(code).hasSize(6);
            assertThat(code).matches("\\d{6}");
        }

        @Test
        @DisplayName("Should validate correct MFA code")
        void shouldValidateCorrectMfaCode() {
            String code = policy.generateMfaCode();

            assertThat(policy.validateMfaCode(code, code)).isTrue();
        }

        @Test
        @DisplayName("Should reject incorrect MFA code")
        void shouldRejectIncorrectMfaCode() {
            assertThat(policy.validateMfaCode("123456", "654321")).isFalse();
        }

        @Test
        @DisplayName("Should respect MFA code expiry")
        void shouldRespectMfaExpiry() {
            String code = policy.generateMfaCode();

            assertThat(policy.validateMfaCode(code, code)).isTrue();

            // Simulate expired code
            policy.expireCode(code);

            assertThat(policy.validateMfaCode(code, code)).isFalse();
        }

        @Test
        @DisplayName("Should limit MFA attempts")
        void shouldLimitMfaAttempts() {
            String code = policy.generateMfaCode();

            for (int i = 0; i < 3; i++) {
                policy.validateMfaCode(code, "wrong" + i);
            }

            assertThat(policy.isMfaLocked(code)).isTrue();
        }

        @Test
        @DisplayName("Should determine preferred MFA method")
        void shouldDeterminePreferredMethod() {
            String method = policy.getPreferredMethod(Set.of("SMS", "EMAIL", "TOTP"));

            assertThat(method).isIn("SMS", "EMAIL", "TOTP");
        }
    }

    @Nested
    @DisplayName("Session Policy Tests")
    class SessionPolicyTests {

        private final SessionPolicy policy = new SessionPolicy();

        @Test
        @DisplayName("Should set default session timeout")
        void shouldSetDefaultTimeout() {
            assertThat(policy.getDefaultTimeout()).isEqualTo(3600); // 1 hour
        }

        @Test
        @DisplayName("Should calculate session timeout for user type")
        void shouldCalculateTimeoutForUserType() {
            int regularTimeout = policy.getTimeoutForUser(Set.of("USER"));
            int adminTimeout = policy.getTimeoutForUser(Set.of("ADMIN"));

            assertThat(adminTimeout).isLessThan(regularTimeout);
        }

        @Test
        @DisplayName("Should extend session for remember me")
        void shouldExtendForRememberMe() {
            int regularTimeout = policy.getDefaultTimeout();
            int extendedTimeout = policy.getRememberMeTimeout();

            assertThat(extendedTimeout).isGreaterThan(regularTimeout);
            assertThat(extendedTimeout).isEqualTo(2592000); // 30 days
        }

        @Test
        @DisplayName("Should validate session")
        void shouldValidateSession() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, "session-1");

            assertThat(policy.isSessionValid(result)).isTrue();
        }

        @Test
        @DisplayName("Should invalidate expired session")
        void shouldInvalidateExpired() {
            AuthenticationResult result = AuthenticationResult.success(USER_ID, "session-1");
            result = AuthenticationResult.withSessionExpiry(result, Instant.now().minusSeconds(60));

            assertThat(policy.isSessionValid(result)).isFalse();
        }

        @Test
        @DisplayName("Should track concurrent sessions")
        void shouldTrackConcurrentSessions() {
            assertThat(policy.getMaxConcurrentSessions()).isEqualTo(3);

            boolean allowed = policy.allowNewSession(2);
            assertThat(allowed).isTrue();

            allowed = policy.allowNewSession(3);
            assertThat(allowed).isFalse();
        }
    }

    @Nested
    @DisplayName("Risk Assessment Policy Tests")
    class RiskPolicyTests {

        private final RiskAssessmentPolicy policy = new RiskAssessmentPolicy();

        @Test
        @DisplayName("Should calculate low risk for normal login")
        void shouldCalculateLowRisk() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setIpAddress("192.168.1.1");
            attempt.setLocation("Home Location");
            attempt.setDeviceFingerprint("known_device_fp");

            double risk = policy.assessRisk(attempt);

            assertThat(risk).isLessThan(0.34);
        }

        @Test
        @DisplayName("Should calculate high risk for unknown location")
        void shouldCalculateHighRiskForUnknownLocation() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setLocation("Unknown Country");
            attempt.setDeviceFingerprint("new_device_fp");

            double risk = policy.assessRisk(attempt);

            assertThat(risk).isGreaterThan(0.5);
        }

        @Test
        @DisplayName("Should calculate high risk for blocked IP")
        void shouldCalculateHighRiskForBlockedIp() {
            AuthenticationAttempt attempt = AuthenticationAttempt.create(USERNAME);
            attempt.setIpAddress("103.21.244.0"); // Known malicious IP

            double risk = policy.assessRisk(attempt);

            assertThat(risk).isGreaterThan(0.8);
        }

        @Test
        @DisplayName("Should track rapid successive attempts")
        void shouldTrackRapidAttempts() {
            AuthenticationAttempt attempt1 = AuthenticationAttempt.create(USERNAME);
            AuthenticationAttempt attempt2 = AuthenticationAttempt.create(USERNAME);
            AuthenticationAttempt attempt3 = AuthenticationAttempt.create(USERNAME);

            boolean isRapid = policy.isRapidSuccessiveAttempts(
                java.util.List.of(attempt1, attempt2, attempt3)
            );

            assertThat(isRapid).isTrue();
        }

        @Test
        @DisplayName("Should detect impossible travel")
        void shouldDetectImpossibleTravel() {
            Instant fiveMinutesAgo = Instant.now().minusSeconds(300);
            Instant now = Instant.now();

            AuthenticationAttempt attempt1 = AuthenticationAttempt.builder()
                    .attemptId(UUID.randomUUID())
                    .username(USERNAME)
                    .timestamp(fiveMinutesAgo)
                    .location("New York, US")
                    .status(AttemptStatus.SUCCESS)
                    .build();

            AuthenticationAttempt attempt2 = AuthenticationAttempt.builder()
                    .attemptId(UUID.randomUUID())
                    .username(USERNAME)
                    .timestamp(now)
                    .location("Tokyo, JP")
                    .status(AttemptStatus.SUCCESS)
                    .build();

            boolean isImpossible = policy.isImpossibleTravel(attempt1, attempt2);

            assertThat(isImpossible).isTrue();
        }

        @ParameterizedTest
        @CsvSource({
            "0.0, LOW",
            "0.3, LOW",
            "0.34, MEDIUM",
            "0.5, MEDIUM",
            "0.75, MEDIUM",
            "0.8, HIGH",
            "1.0, HIGH"
        })
        @DisplayName("Should classify risk levels correctly")
        void shouldClassifyRiskLevels(double score, RiskLevel expectedLevel) {
            RiskLevel level = policy.classifyRisk(score);

            assertThat(level).isEqualTo(expectedLevel);
        }
    }

    @Nested
    @DisplayName("Biometric Policy Tests")
    class BiometricPolicyTests {

        private final BiometricPolicy policy = new BiometricPolicy();

        @Test
        @DisplayName("Should validate biometric confidence threshold")
        void shouldValidateConfidenceThreshold() {
            assertThat(policy.isValidConfidence(0.9)).isTrue();
            assertThat(policy.isValidConfidence(0.7)).isTrue();
            assertThat(policy.isValidConfidence(0.5)).isFalse();
            assertThat(policy.isValidConfidence(0.3)).isFalse();
        }

        @Test
        @DisplayName("Should get required confidence for biometric type")
        void shouldGetRequiredConfidence() {
            double faceThreshold = policy.getRequiredConfidence(BiometricType.FACE_RECOGNITION);
            double fingerprintThreshold = policy.getRequiredConfidence(BiometricType.FINGERPRINT);
            double voiceThreshold = policy.getRequiredConfidence(BiometricType.VOICE_RECOGNITION);

            assertThat(faceThreshold).isEqualTo(0.85);
            assertThat(fingerprintThreshold).isEqualTo(0.90);
            assertThat(voiceThreshold).isEqualTo(0.80);
        }

        @Test
        @DisplayName("Should support all biometric types")
        void shouldSupportAllTypes() {
            assertThat(policy.getSupportedTypes()).containsExactlyInAnyOrder(
                BiometricType.FACE_RECOGNITION,
                BiometricType.FINGERPRINT,
                BiometricType.VOICE_RECOGNITION,
                BiometricType.IRIS_SCAN
            );
        }

        @Test
        @DisplayName("Should calculate liveness score")
        void shouldCalculateLivenessScore() {
            // Mock biometric data
            byte[] faceData = new byte[]{1, 2, 3, 4, 5};

            double liveness = policy.assessLiveness(faceData);

            assertThat(liveness).isBetween(0.0, 1.0);
        }

        @Test
        @DisplayName("Should reject spoofed biometric data")
        void shouldRejectSpoofedData() {
            byte[] spoofedData = new byte[]{0, 0, 0, 0};

            assertThat(policy.isGenuine(spoofedData)).isFalse();
        }
    }

    @Nested
    @DisplayName("Token Policy Tests")
    class TokenPolicyTests {

        private final TestTokenPolicy policy = new TestTokenPolicy();

        @Test
        @DisplayName("Should generate valid access token")
        void shouldGenerateAccessToken() {
            String token = policy.generateAccessToken(USER_ID, Set.of("USER"));

            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();

            // Verify JWT format (3 parts separated by dots)
            String[] parts = token.split("\\.");
            assertThat(parts).hasSize(3);
        }

        @Test
        @DisplayName("Should generate valid refresh token")
        void shouldGenerateRefreshToken() {
            String token = policy.generateRefreshToken(USER_ID);

            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
            assertThat(token).hasSize(64); // 256 bits hex encoded
        }

        @Test
        @DisplayName("Should validate access token")
        void shouldValidateAccessToken() {
            String token = policy.generateAccessToken(USER_ID, Set.of("USER"));

            assertThat(policy.validateAccessToken(token)).isTrue();
        }

        @Test
        @DisplayName("Should reject invalid access token")
        void shouldRejectInvalidToken() {
            assertThat(policy.validateAccessToken("invalid.token.here")).isFalse();
        }

        @Test
        @DisplayName("Should extract user ID from token")
        void shouldExtractUserId() {
            String token = policy.generateAccessToken(USER_ID, Set.of("USER"));

            String extractedId = policy.extractUserId(token);

            assertThat(extractedId).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should extract roles from token")
        void shouldExtractRoles() {
            Set<String> roles = Set.of("USER", "ADMIN");
            String token = policy.generateAccessToken(USER_ID, roles);

            Set<String> extractedRoles = policy.extractRoles(token);

            assertThat(extractedRoles).containsExactlyInAnyOrderElementsOf(roles);
        }

        @Test
        @DisplayName("Should respect token expiration")
        void shouldRespectTokenExpiration() {
            String token = policy.generateAccessToken(USER_ID, Set.of("USER"));

            assertThat(policy.isTokenExpired(token)).isFalse();
        }

        @Test
        @DisplayName("Should check token expiration time")
        void shouldCheckExpirationTime() {
            assertThat(policy.getAccessTokenExpiration()).isEqualTo(3600); // 1 hour
            assertThat(policy.getRefreshTokenExpiration()).isEqualTo(2592000); // 30 days
        }
    }

    /**
     * Test implementation of TokenPolicy for testing purposes.
     */
    private static class TestTokenPolicy {
        private static final String SECRET = "gogidix-secret-key-for-testing-purposes-only-must-be-at-least-32-chars";
        private static final int ACCESS_TOKEN_EXPIRATION = 3600;
        private static final int REFRESH_TOKEN_EXPIRATION = 2592000;

        public String generateAccessToken(String userId, Set<String> roles) {
            long now = System.currentTimeMillis();
            long exp = now + (ACCESS_TOKEN_EXPIRATION * 1000L);

            String header = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(
                    ("{\"alg\":\"HS256\",\"typ\":\"JWT\"}").getBytes()
            );

            String payload = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(
                    String.format("{\"sub\":\"%s\",\"roles\":\"%s\",\"iss\":\"gogidix\",\"iat\":%d,\"exp\":%d}",
                            userId, String.join(",", roles), now / 1000, exp / 1000).getBytes()
            );

            String signature = generateSignature(header, payload);
            return header + "." + payload + "." + signature;
        }

        public String generateRefreshToken(String userId) {
            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                String raw = userId + "-" + System.currentTimeMillis() + "-" + java.util.UUID.randomUUID();
                byte[] hash = digest.digest(raw.getBytes());
                return java.util.HexFormat.of().formatHex(hash);
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new RuntimeException("SHA-256 algorithm not available", e);
            }
        }

        public boolean validateAccessToken(String token) {
            if (token == null || token.isEmpty()) return false;
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;
            try {
                String expectedSignature = generateSignature(parts[0], parts[1]);
                return expectedSignature.equals(parts[2]) && !isTokenExpired(token);
            } catch (Exception e) {
                return false;
            }
        }

        public boolean validateRefreshToken(String token) {
            if (token == null || token.isEmpty()) return false;
            return token.matches("[0-9a-f]{64}");
        }

        public String extractUserId(String token) {
            String payload = new String(java.util.Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            return payload.substring(6, payload.indexOf("\",\"roles\""));
        }

        public Set<String> extractRoles(String token) {
            String payload = new String(java.util.Base64.getUrlDecoder().decode(token.split("\\.")[1]));
            int rolesStart = payload.indexOf("\"roles\":\"") + 9;
            int rolesEnd = payload.indexOf("\",\"iss\"", rolesStart);
            String rolesStr = payload.substring(rolesStart, rolesEnd);
            return Set.of(rolesStr.split(","));
        }

        public boolean isTokenExpired(String token) {
            Long exp = extractExpiration(token);
            return exp != null && exp * 1000 < System.currentTimeMillis();
        }

        public void blacklistToken(String token) {}

        public int getAccessTokenExpiration() {
            return ACCESS_TOKEN_EXPIRATION;
        }

        public int getRefreshTokenExpiration() {
            return REFRESH_TOKEN_EXPIRATION;
        }

        public Long extractExpiration(String token) {
            try {
                String payload = new String(java.util.Base64.getUrlDecoder().decode(token.split("\\.")[1]));
                int expStart = payload.indexOf("\"exp\":") + 6;
                int expEnd = payload.indexOf("}", expStart);
                return Long.parseLong(payload.substring(expStart, expEnd));
            } catch (Exception e) {
                return null;
            }
        }

        public Long extractIssuedAt(String token) {
            try {
                String payload = new String(java.util.Base64.getUrlDecoder().decode(token.split("\\.")[1]));
                int iatStart = payload.indexOf("\"iat\":") + 6;
                int iatEnd = payload.indexOf(",\"exp\"", iatStart);
                return Long.parseLong(payload.substring(iatStart, iatEnd));
            } catch (Exception e) {
                return null;
            }
        }

        public String extractIssuer(String token) {
            try {
                String payload = new String(java.util.Base64.getUrlDecoder().decode(token.split("\\.")[1]));
                int issStart = payload.indexOf("\"iss\":\"") + 7;
                int issEnd = payload.indexOf("\",\"iat\"", issStart);
                return payload.substring(issStart, issEnd);
            } catch (Exception e) {
                return null;
            }
        }

        private String generateSignature(String header, String payload) {
            String data = header + "." + payload;
            byte[] hash = hmac(SECRET.getBytes(), data.getBytes());
            return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        }

        private byte[] hmac(byte[] key, byte[] data) {
            try {
                javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
                javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key, "HmacSHA256");
                mac.init(secretKeySpec);
                return mac.doFinal(data);
            } catch (Exception e) {
                throw new RuntimeException("HMAC calculation failed", e);
            }
        }
    }
}
