package com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VerifyMfaCodeRequestDto.
 */
@DisplayName("VerifyMfaCodeRequestDto Tests")
class VerifyMfaCodeRequestDtoTest {

    @Test
    @DisplayName("Should create VerifyMfaCodeRequestDto with all values")
    void shouldCreateDtoWithAllValues() {
        VerifyMfaCodeRequestDto dto = new VerifyMfaCodeRequestDto(
                "user-123",
                "123456",
                "device-123"
        );

        assertEquals("user-123", dto.userId());
        assertEquals("123456", dto.code());
        assertEquals("device-123", dto.deviceId());
    }

    @Test
    @DisplayName("Should create VerifyMfaCodeRequestDto with null deviceId")
    void shouldCreateDtoWithNullDeviceId() {
        VerifyMfaCodeRequestDto dto = new VerifyMfaCodeRequestDto(
                "user-123",
                "123456",
                null
        );

        assertEquals("user-123", dto.userId());
        assertEquals("123456", dto.code());
        assertNull(dto.deviceId());
    }

    @Test
    @DisplayName("Should handle empty code")
    void shouldHandleEmptyCode() {
        VerifyMfaCodeRequestDto dto = new VerifyMfaCodeRequestDto(
                "user-123",
                "",
                "device-123"
        );

        assertEquals("", dto.code());
    }

    @Test
    @DisplayName("Should handle numeric code")
    void shouldHandleNumericCode() {
        VerifyMfaCodeRequestDto dto = new VerifyMfaCodeRequestDto(
                "user-123",
                "987654",
                "device-456"
        );

        assertEquals("987654", dto.code());
    }
}
