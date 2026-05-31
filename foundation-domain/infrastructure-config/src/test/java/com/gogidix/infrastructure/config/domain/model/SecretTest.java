package com.gogidix.infrastructure.config.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Secret domain model.
 */
@DisplayName("Secret Tests")
class SecretTest {

    @Test
    @DisplayName("Should create valid secret")
    void shouldCreateValidSecret() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .description("External API key")
                .encryptedValue("encrypted-value")
                .iv("iv-value")
                .secretType(Secret.SecretType.API_KEY)
                .isActive(true)
                .build();

        assertNotNull(secret);
        assertEquals("tenant-1", secret.getTenantId());
        assertEquals("api-key", secret.getSecretKey());
        assertEquals("API Key", secret.getName());
        assertEquals(Secret.SecretType.API_KEY, secret.getSecretType());
    }

    @Test
    @DisplayName("Should check if secret needs rotation")
    void shouldCheckIfSecretNeedsRotation() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);

        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .rotationIntervalDays(30)
                .nextRotationAt(past)
                .build();

        assertTrue(secret.needsRotation());
    }

    @Test
    @DisplayName("Should not need rotation when interval is null")
    void shouldNotNeedRotationWhenIntervalIsNull() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .rotationIntervalDays(null)
                .build();

        assertFalse(secret.needsRotation());
    }

    @Test
    @DisplayName("Should not need rotation when next rotation is in future")
    void shouldNotNeedRotationWhenNextRotationIsInFuture() {
        LocalDateTime future = LocalDateTime.now().plusDays(10);

        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .rotationIntervalDays(30)
                .nextRotationAt(future)
                .build();

        assertFalse(secret.needsRotation());
    }

    @Test
    @DisplayName("Should check if secret is expired")
    void shouldCheckIfSecretIsExpired() {
        LocalDateTime past = LocalDateTime.now().minusDays(1);

        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .expiresAt(past)
                .build();

        assertTrue(secret.isExpired());
    }

    @Test
    @DisplayName("Should not be expired when expiration is null")
    void shouldNotBeExpiredWhenExpirationIsNull() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .expiresAt(null)
                .build();

        assertFalse(secret.isExpired());
    }

    @Test
    @DisplayName("Should not be expired when expiration is in future")
    void shouldNotBeExpiredWhenExpirationIsInFuture() {
        LocalDateTime future = LocalDateTime.now().plusDays(30);

        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .expiresAt(future)
                .build();

        assertFalse(secret.isExpired());
    }

    @Test
    @DisplayName("Should check if secret is valid")
    void shouldCheckIfSecretIsValid() {
        LocalDateTime future = LocalDateTime.now().plusDays(30);

        Secret activeSecret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .isActive(true)
                .expiresAt(future)
                .build();

        Secret inactiveSecret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key-2")
                .name("API Key 2")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .isActive(false)
                .expiresAt(future)
                .build();

        Secret expiredSecret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key-3")
                .name("API Key 3")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .isActive(true)
                .expiresAt(LocalDateTime.now().minusDays(1))
                .build();

        assertTrue(activeSecret.isValid());
        assertFalse(inactiveSecret.isValid());
        assertFalse(expiredSecret.isValid());
    }

    @Test
    @DisplayName("Should check user access when ACL is empty")
    void shouldCheckUserAccessWhenACLEmpty() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .accessControlList(null)
                .build();

        assertTrue(secret.hasAccess("user1"));
    }

    @Test
    @DisplayName("Should check user access when ACL is set")
    void shouldCheckUserAccessWhenACLIsSet() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .accessControlList(Set.of("admin", "devops"))
                .build();

        assertTrue(secret.hasAccess("admin"));
        assertTrue(secret.hasAccess("devops"));
        assertFalse(secret.hasAccess("user1"));
    }

    @Test
    @DisplayName("Should record secret access")
    void shouldRecordSecretAccess() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .accessCount(5)
                .build();

        secret.recordAccess("user1");

        assertEquals("user1", secret.getLastAccessedBy());
        assertNotNull(secret.getLastAccessedAt());
        assertEquals(6, secret.getAccessCount());
    }

    @Test
    @DisplayName("Should update rotation schedule")
    void shouldUpdateRotationSchedule() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .rotationIntervalDays(30)
                .build();

        secret.updateRotationSchedule();

        assertNotNull(secret.getLastRotatedAt());
        assertNotNull(secret.getNextRotationAt());
        assertTrue(secret.getNextRotationAt().isAfter(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should not update rotation schedule when interval is null")
    void shouldNotUpdateRotationScheduleWhenIntervalIsNull() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .rotationIntervalDays(null)
                .build();

        secret.updateRotationSchedule();

        assertNull(secret.getNextRotationAt());
    }

    @Test
    @DisplayName("Should not update rotation schedule when interval is zero")
    void shouldNotUpdateRotationScheduleWhenIntervalIsZero() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .rotationIntervalDays(0)
                .build();

        secret.updateRotationSchedule();

        assertNull(secret.getNextRotationAt());
    }

    @Test
    @DisplayName("Should increment version")
    void shouldIncrementVersion() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .version(1)
                .build();

        secret.incrementVersion();
        assertEquals(2, secret.getVersion());

        secret.incrementVersion();
        assertEquals(3, secret.getVersion());
    }

    @Test
    @DisplayName("Should initialize version to null and increment to 1")
    void shouldInitializeVersionToNullAndIncrementToOne() {
        Secret secret = Secret.builder()
                .tenantId("tenant-1")
                .secretKey("api-key")
                .name("API Key")
                .encryptedValue("encrypted-value")
                .secretType(Secret.SecretType.API_KEY)
                .version(null)
                .build();

        secret.incrementVersion();
        assertEquals(1, secret.getVersion());
    }

    @Test
    @DisplayName("Should support all secret types")
    void shouldSupportAllSecretTypes() {
        Secret.SecretType[] types = Secret.SecretType.values();

        assertEquals(13, types.length);
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.API_KEY));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.DATABASE_PASSWORD));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.OAUTH_TOKEN));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.CERTIFICATE));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.SSH_KEY));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.ENCRYPTION_KEY));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.WEBHOOK_SECRET));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.JWT_SECRET));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.THIRD_PARTY_KEY));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.GENERIC_PASSWORD));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.SERVICE_ACCOUNT_KEY));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.ACCESS_TOKEN));
        assertTrue(java.util.Arrays.asList(types).contains(Secret.SecretType.REFRESH_TOKEN));
    }
}
