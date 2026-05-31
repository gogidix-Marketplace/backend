package com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MfaVerificationResponseDto.
 */
@DisplayName("MfaVerificationResponseDto Tests")
class MfaVerificationResponseDtoTest {

    @Test
    @DisplayName("Should create successful verification response")
    void shouldCreateSuccessfulVerificationResponse() {
        MfaVerificationResponseDto dto = new MfaVerificationResponseDto(
                true,
                "Verification successful",
                "device-123",
                false
        );

        assertTrue(dto.success());
        assertEquals("Verification successful", dto.message());
        assertEquals("device-123", dto.deviceId());
        assertFalse(dto.requiresBackupCode());
    }

    @Test
    @DisplayName("Should create failed verification response")
    void shouldCreateFailedVerificationResponse() {
        MfaVerificationResponseDto dto = new MfaVerificationResponseDto(
                false,
                "Invalid code",
                null,
                false
        );

        assertFalse(dto.success());
        assertEquals("Invalid code", dto.message());
        assertNull(dto.deviceId());
        assertFalse(dto.requiresBackupCode());
    }

    @Test
    @DisplayName("Should create response requiring backup code")
    void shouldCreateResponseRequiringBackupCode() {
        MfaVerificationResponseDto dto = new MfaVerificationResponseDto(
                false,
                "Too many attempts, use backup code",
                null,
                true
        );

        assertFalse(dto.success());
        assertTrue(dto.requiresBackupCode());
        assertNull(dto.deviceId());
    }

    @Test
    @DisplayName("Should create response with all null values")
    void shouldCreateResponseWithAllNullValues() {
        MfaVerificationResponseDto dto = new MfaVerificationResponseDto(
                false,
                null,
                null,
                false
        );

        assertFalse(dto.success());
        assertNull(dto.message());
        assertNull(dto.deviceId());
        assertFalse(dto.requiresBackupCode());
    }

    @Test
    @DisplayName("Should handle success with null message")
    void shouldHandleSuccessWithNullMessage() {
        MfaVerificationResponseDto dto = new MfaVerificationResponseDto(
                true,
                null,
                "device-456",
                false
        );

        assertTrue(dto.success());
        assertEquals("device-456", dto.deviceId());
        assertNull(dto.message());
    }

    @Test
    @DisplayName("Should handle success with null deviceId")
    void shouldHandleSuccessWithNullDeviceId() {
        MfaVerificationResponseDto dto = new MfaVerificationResponseDto(
                true,
                "Code verified",
                null,
                false
        );

        assertTrue(dto.success());
        assertEquals("Code verified", dto.message());
        assertNull(dto.deviceId());
    }

    @Test
    @DisplayName("Should handle backup code validation success")
    void shouldHandleBackupCodeValidationSuccess() {
        MfaVerificationResponseDto dto = new MfaVerificationResponseDto(
                true,
                "Backup code validated",
                null,
                false
        );

        assertTrue(dto.success());
        assertEquals("Backup code validated", dto.message());
        assertFalse(dto.requiresBackupCode());
    }
}
