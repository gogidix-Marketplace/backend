package com.gogidix.shared.infrastructure.services.security.mfa.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MfaDevice domain model.
 */
@DisplayName("MfaDevice Domain Model Tests")
class MfaDeviceTest {

    @Test
    @DisplayName("Should create MfaDevice with default values")
    void shouldCreateMfaDeviceWithDefaults() {
        MfaDevice device = new MfaDevice();

        assertNull(device.getId());
        assertNull(device.getTenantId());
        assertNull(device.getUserId());
        assertNull(device.getDeviceId());
        assertNull(device.getMfaType());
        assertNull(device.getStatus());
        assertNull(device.getSecretKey());
        assertNull(device.getPhoneNumber());
        assertNull(device.getEmail());
        assertNull(device.getIsPrimary());
        assertNull(device.getIsBackup());
        assertNull(device.getLastUsedAt());
        assertNull(device.getCreatedAt());
        assertNull(device.getUpdatedAt());
        assertNull(device.getFailedAttempts());
        assertNull(device.getLockedUntil());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        MfaDevice device = new MfaDevice();
        TenantId tenantId = TenantId.of("tenant-123");

        device.setId("device-123");
        device.setTenantId(tenantId);
        device.setUserId("user-123");
        device.setDeviceId("dev-456");
        device.setMfaType(MfaDevice.MfaType.TOTP);
        device.setStatus(MfaDevice.DeviceStatus.ACTIVE);
        device.setSecretKey("secret-key");
        device.setPhoneNumber("+1234567890");
        device.setEmail("test@example.com");
        device.setIsPrimary(true);
        device.setIsBackup(false);
        device.setLastUsedAt(LocalDateTime.now());
        device.setCreatedAt(LocalDateTime.now());
        device.setUpdatedAt(LocalDateTime.now());
        device.setFailedAttempts(0);
        device.setLockedUntil(null);

        assertEquals("device-123", device.getId());
        assertEquals(tenantId, device.getTenantId());
        assertEquals("user-123", device.getUserId());
        assertEquals("dev-456", device.getDeviceId());
        assertEquals(MfaDevice.MfaType.TOTP, device.getMfaType());
        assertEquals(MfaDevice.DeviceStatus.ACTIVE, device.getStatus());
        assertEquals("secret-key", device.getSecretKey());
        assertEquals("+1234567890", device.getPhoneNumber());
        assertEquals("test@example.com", device.getEmail());
        assertTrue(device.getIsPrimary());
        assertFalse(device.getIsBackup());
        assertNotNull(device.getLastUsedAt());
        assertNotNull(device.getCreatedAt());
        assertNotNull(device.getUpdatedAt());
        assertEquals(0, device.getFailedAttempts());
        assertNull(device.getLockedUntil());
    }

    @Test
    @DisplayName("Should mark device as used correctly")
    void shouldMarkDeviceAsUsed() {
        MfaDevice device = new MfaDevice();
        device.setFailedAttempts(3);

        device.markAsUsed();

        assertNotNull(device.getLastUsedAt());
        assertEquals(0, device.getFailedAttempts());
    }

    @Test
    @DisplayName("Should record failed attempt correctly")
    void shouldRecordFailedAttempt() {
        MfaDevice device = new MfaDevice();
        device.setStatus(MfaDevice.DeviceStatus.ACTIVE);

        device.recordFailedAttempt();
        assertEquals(1, device.getFailedAttempts());
        assertEquals(MfaDevice.DeviceStatus.ACTIVE, device.getStatus());

        device.recordFailedAttempt();
        assertEquals(2, device.getFailedAttempts());

        device.recordFailedAttempt();
        device.recordFailedAttempt();
        device.recordFailedAttempt();
        assertEquals(5, device.getFailedAttempts());
        assertEquals(MfaDevice.DeviceStatus.LOCKED, device.getStatus());
        assertNotNull(device.getLockedUntil());
    }

    @Test
    @DisplayName("Should revoke device correctly")
    void shouldRevokeDevice() {
        MfaDevice device = new MfaDevice();
        device.setStatus(MfaDevice.DeviceStatus.ACTIVE);

        device.revoke();

        assertEquals(MfaDevice.DeviceStatus.REVOKED, device.getStatus());
        assertNotNull(device.getUpdatedAt());
    }

    @Test
    @DisplayName("Should activate device correctly")
    void shouldActivateDevice() {
        MfaDevice device = new MfaDevice();
        device.setStatus(MfaDevice.DeviceStatus.PENDING_VERIFICATION);

        device.activate();

        assertEquals(MfaDevice.DeviceStatus.ACTIVE, device.getStatus());
        assertNotNull(device.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle all MfaType enum values")
    void shouldHandleAllMfaTypeEnums() {
        assertEquals(6, MfaDevice.MfaType.values().length);
        assertEquals(MfaDevice.MfaType.TOTP, MfaDevice.MfaType.valueOf("TOTP"));
        assertEquals(MfaDevice.MfaType.SMS, MfaDevice.MfaType.valueOf("SMS"));
        assertEquals(MfaDevice.MfaType.EMAIL, MfaDevice.MfaType.valueOf("EMAIL"));
        assertEquals(MfaDevice.MfaType.BACKUP_CODE, MfaDevice.MfaType.valueOf("BACKUP_CODE"));
        assertEquals(MfaDevice.MfaType.HARDWARE_TOKEN, MfaDevice.MfaType.valueOf("HARDWARE_TOKEN"));
        assertEquals(MfaDevice.MfaType.BIOMETRIC, MfaDevice.MfaType.valueOf("BIOMETRIC"));
    }

    @Test
    @DisplayName("Should handle all DeviceStatus enum values")
    void shouldHandleAllDeviceStatusEnums() {
        assertEquals(5, MfaDevice.DeviceStatus.values().length);
        assertEquals(MfaDevice.DeviceStatus.ACTIVE, MfaDevice.DeviceStatus.valueOf("ACTIVE"));
        assertEquals(MfaDevice.DeviceStatus.INACTIVE, MfaDevice.DeviceStatus.valueOf("INACTIVE"));
        assertEquals(MfaDevice.DeviceStatus.REVOKED, MfaDevice.DeviceStatus.valueOf("REVOKED"));
        assertEquals(MfaDevice.DeviceStatus.LOCKED, MfaDevice.DeviceStatus.valueOf("LOCKED"));
        assertEquals(MfaDevice.DeviceStatus.PENDING_VERIFICATION, MfaDevice.DeviceStatus.valueOf("PENDING_VERIFICATION"));
    }

    @Test
    @DisplayName("Should lock device after 5 failed attempts when starting from null")
    void shouldLockDeviceAfterFiveFailedAttemptsFromNull() {
        MfaDevice device = new MfaDevice();
        device.setStatus(MfaDevice.DeviceStatus.ACTIVE);
        device.setFailedAttempts(null);

        for (int i = 0; i < 5; i++) {
            device.recordFailedAttempt();
        }

        assertEquals(5, device.getFailedAttempts());
        assertEquals(MfaDevice.DeviceStatus.LOCKED, device.getStatus());
        assertNotNull(device.getLockedUntil());
    }
}
