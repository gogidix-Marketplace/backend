package com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response;

import com.gogidix.shared.infrastructure.services.security.mfa.domain.model.MfaDevice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MfaDeviceResponseDto.
 */
@DisplayName("MfaDeviceResponseDto Tests")
class MfaDeviceResponseDtoTest {

    @Test
    @DisplayName("Should create MfaDeviceResponseDto with all values")
    void shouldCreateDtoWithAllValues() {
        LocalDateTime now = LocalDateTime.now();

        MfaDeviceResponseDto dto = new MfaDeviceResponseDto(
                "id-123",
                "tenant-123",
                "user-123",
                "device-123",
                MfaDevice.MfaType.TOTP,
                MfaDevice.DeviceStatus.ACTIVE,
                null,
                "test@example.com",
                true,
                false,
                now,
                now,
                now
        );

        assertEquals("id-123", dto.id());
        assertEquals("tenant-123", dto.tenantId());
        assertEquals("user-123", dto.userId());
        assertEquals("device-123", dto.deviceId());
        assertEquals(MfaDevice.MfaType.TOTP, dto.mfaType());
        assertEquals(MfaDevice.DeviceStatus.ACTIVE, dto.status());
        assertNull(dto.phoneNumber());
        assertEquals("test@example.com", dto.email());
        assertTrue(dto.isPrimary());
        assertFalse(dto.isBackup());
        assertEquals(now, dto.lastUsedAt());
        assertEquals(now, dto.createdAt());
        assertEquals(now, dto.updatedAt());
    }

    @Test
    @DisplayName("Should create MfaDeviceResponseDto with null optional values")
    void shouldCreateDtoWithNullOptionalValues() {
        MfaDeviceResponseDto dto = new MfaDeviceResponseDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertNull(dto.id());
        assertNull(dto.tenantId());
        assertNull(dto.userId());
        assertNull(dto.deviceId());
        assertNull(dto.mfaType());
        assertNull(dto.status());
        assertNull(dto.phoneNumber());
        assertNull(dto.email());
        assertNull(dto.isPrimary());
        assertNull(dto.isBackup());
        assertNull(dto.lastUsedAt());
        assertNull(dto.createdAt());
        assertNull(dto.updatedAt());
    }

    @Test
    @DisplayName("Should create MfaDeviceResponseDto for SMS device")
    void shouldCreateDtoForSmsDevice() {
        MfaDeviceResponseDto dto = new MfaDeviceResponseDto(
                "id-123",
                "tenant-123",
                "user-123",
                "device-123",
                MfaDevice.MfaType.SMS,
                MfaDevice.DeviceStatus.ACTIVE,
                "+1234567890",
                null,
                true,
                false,
                null,
                null,
                null
        );

        assertEquals(MfaDevice.MfaType.SMS, dto.mfaType());
        assertEquals("+1234567890", dto.phoneNumber());
        assertNull(dto.email());
    }

    @Test
    @DisplayName("Should create MfaDeviceResponseDto for EMAIL device")
    void shouldCreateDtoForEmailDevice() {
        MfaDeviceResponseDto dto = new MfaDeviceResponseDto(
                "id-123",
                "tenant-123",
                "user-123",
                "device-123",
                MfaDevice.MfaType.EMAIL,
                MfaDevice.DeviceStatus.PENDING_VERIFICATION,
                null,
                "email@example.com",
                false,
                false,
                null,
                null,
                null
        );

        assertEquals(MfaDevice.MfaType.EMAIL, dto.mfaType());
        assertEquals("email@example.com", dto.email());
        assertNull(dto.phoneNumber());
        assertEquals(MfaDevice.DeviceStatus.PENDING_VERIFICATION, dto.status());
    }

    @Test
    @DisplayName("Should create MfaDeviceResponseDto for backup device")
    void shouldCreateDtoForBackupDevice() {
        MfaDeviceResponseDto dto = new MfaDeviceResponseDto(
                "id-123",
                "tenant-123",
                "user-123",
                "device-123",
                MfaDevice.MfaType.BACKUP_CODE,
                MfaDevice.DeviceStatus.ACTIVE,
                null,
                null,
                false,
                true,
                null,
                null,
                null
        );

        assertEquals(MfaDevice.MfaType.BACKUP_CODE, dto.mfaType());
        assertTrue(dto.isBackup());
        assertFalse(dto.isPrimary());
    }
}
