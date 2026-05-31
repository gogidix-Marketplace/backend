package com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RegisterMfaDeviceRequestDto.
 */
@DisplayName("RegisterMfaDeviceRequestDto Tests")
class RegisterMfaDeviceRequestDtoTest {

    @Test
    @DisplayName("Should create RegisterMfaDeviceRequestDto with all values")
    void shouldCreateDtoWithAllValues() {
        RegisterMfaDeviceRequestDto dto = new RegisterMfaDeviceRequestDto(
                "user-123",
                RegisterMfaDeviceRequestDto.MfaType.TOTP,
                "+1234567890",
                "test@example.com",
                "My Device",
                true
        );

        assertEquals("user-123", dto.userId());
        assertEquals(RegisterMfaDeviceRequestDto.MfaType.TOTP, dto.mfaType());
        assertEquals("+1234567890", dto.phoneNumber());
        assertEquals("test@example.com", dto.email());
        assertEquals("My Device", dto.deviceName());
        assertTrue(dto.isPrimary());
    }

    @Test
    @DisplayName("Should create RegisterMfaDeviceRequestDto with null optional values")
    void shouldCreateDtoWithNullOptionalValues() {
        RegisterMfaDeviceRequestDto dto = new RegisterMfaDeviceRequestDto(
                "user-123",
                RegisterMfaDeviceRequestDto.MfaType.SMS,
                null,
                null,
                null,
                null
        );

        assertEquals("user-123", dto.userId());
        assertEquals(RegisterMfaDeviceRequestDto.MfaType.SMS, dto.mfaType());
        assertNull(dto.phoneNumber());
        assertNull(dto.email());
        assertNull(dto.deviceName());
        assertNull(dto.isPrimary());
    }

    @Test
    @DisplayName("Should have all MfaType enum values")
    void shouldHaveAllMfaTypeEnumValues() {
        assertEquals(6, RegisterMfaDeviceRequestDto.MfaType.values().length);
        assertEquals(RegisterMfaDeviceRequestDto.MfaType.TOTP, RegisterMfaDeviceRequestDto.MfaType.valueOf("TOTP"));
        assertEquals(RegisterMfaDeviceRequestDto.MfaType.SMS, RegisterMfaDeviceRequestDto.MfaType.valueOf("SMS"));
        assertEquals(RegisterMfaDeviceRequestDto.MfaType.EMAIL, RegisterMfaDeviceRequestDto.MfaType.valueOf("EMAIL"));
        assertEquals(RegisterMfaDeviceRequestDto.MfaType.BACKUP_CODE, RegisterMfaDeviceRequestDto.MfaType.valueOf("BACKUP_CODE"));
        assertEquals(RegisterMfaDeviceRequestDto.MfaType.HARDWARE_TOKEN, RegisterMfaDeviceRequestDto.MfaType.valueOf("HARDWARE_TOKEN"));
        assertEquals(RegisterMfaDeviceRequestDto.MfaType.BIOMETRIC, RegisterMfaDeviceRequestDto.MfaType.valueOf("BIOMETRIC"));
    }

    @Test
    @DisplayName("Should create EMAIL MfaType DTO")
    void shouldCreateEmailMfaType() {
        RegisterMfaDeviceRequestDto dto = new RegisterMfaDeviceRequestDto(
                "user-123",
                RegisterMfaDeviceRequestDto.MfaType.EMAIL,
                null,
                "email@example.com",
                "Work Email",
                false
        );

        assertEquals(RegisterMfaDeviceRequestDto.MfaType.EMAIL, dto.mfaType());
    }

    @Test
    @DisplayName("Should create BACKUP_CODE MfaType DTO")
    void shouldCreateBackupCodeMfaType() {
        RegisterMfaDeviceRequestDto dto = new RegisterMfaDeviceRequestDto(
                "user-123",
                RegisterMfaDeviceRequestDto.MfaType.BACKUP_CODE,
                null,
                null,
                null,
                false
        );

        assertEquals(RegisterMfaDeviceRequestDto.MfaType.BACKUP_CODE, dto.mfaType());
    }

    @Test
    @DisplayName("Should create HARDWARE_TOKEN MfaType DTO")
    void shouldCreateHardwareTokenMfaType() {
        RegisterMfaDeviceRequestDto dto = new RegisterMfaDeviceRequestDto(
                "user-123",
                RegisterMfaDeviceRequestDto.MfaType.HARDWARE_TOKEN,
                null,
                null,
                "YubiKey",
                false
        );

        assertEquals(RegisterMfaDeviceRequestDto.MfaType.HARDWARE_TOKEN, dto.mfaType());
    }

    @Test
    @DisplayName("Should create BIOMETRIC MfaType DTO")
    void shouldCreateBiometricMfaType() {
        RegisterMfaDeviceRequestDto dto = new RegisterMfaDeviceRequestDto(
                "user-123",
                RegisterMfaDeviceRequestDto.MfaType.BIOMETRIC,
                null,
                null,
                "Fingerprint",
                false
        );

        assertEquals(RegisterMfaDeviceRequestDto.MfaType.BIOMETRIC, dto.mfaType());
    }
}
