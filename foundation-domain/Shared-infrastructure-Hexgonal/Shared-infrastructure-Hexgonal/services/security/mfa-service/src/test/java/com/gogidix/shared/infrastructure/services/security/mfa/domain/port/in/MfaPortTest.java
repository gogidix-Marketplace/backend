package com.gogidix.shared.infrastructure.services.security.mfa.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request.RegisterMfaDeviceRequestDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request.VerifyMfaCodeRequestDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response.MfaDeviceResponseDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response.MfaVerificationResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MfaPort interface contract.
 */
@DisplayName("MfaPort Interface Tests")
class MfaPortTest {

    @Test
    @DisplayName("Should have MfaPort interface defined")
    void shouldHaveMfaPortInterface() {
        assertNotNull(MfaPort.class);
        assertTrue(MfaPort.class.isInterface());
    }

    @Test
    @DisplayName("Should have registerDevice method defined")
    void shouldHaveRegisterDeviceMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("registerDevice", RegisterMfaDeviceRequestDto.class);
        assertNotNull(method);
        assertEquals(MfaDeviceResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have verifyCode method defined")
    void shouldHaveVerifyCodeMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("verifyCode", VerifyMfaCodeRequestDto.class);
        assertNotNull(method);
        assertEquals(MfaVerificationResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have generateBackupCodes method defined")
    void shouldHaveGenerateBackupCodesMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("generateBackupCodes", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have getUserDevices method defined")
    void shouldHaveGetUserDevicesMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("getUserDevices", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have revokeDevice method defined")
    void shouldHaveRevokeDeviceMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("revokeDevice", String.class);
        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have setPrimaryDevice method defined")
    void shouldHaveSetPrimaryDeviceMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("setPrimaryDevice", String.class);
        assertNotNull(method);
        assertEquals(MfaDeviceResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have enableMfa method defined")
    void shouldHaveEnableMfaMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("enableMfa", String.class);
        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have disableMfa method defined")
    void shouldHaveDisableMfaMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("disableMfa", String.class);
        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have sendMfaCode method defined")
    void shouldHaveSendMfaCodeMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("sendMfaCode", String.class, String.class);
        assertNotNull(method);
        assertEquals(void.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have validateBackupCode method defined")
    void shouldHaveValidateBackupCodeMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("validateBackupCode", String.class, String.class);
        assertNotNull(method);
        assertEquals(MfaVerificationResponseDto.class, method.getReturnType());
    }

    @Test
    @DisplayName("Should have regenerateBackupCodes method defined")
    void shouldHaveRegenerateBackupCodesMethod() throws NoSuchMethodException {
        var method = MfaPort.class.getMethod("regenerateBackupCodes", String.class);
        assertNotNull(method);
        assertEquals(List.class, method.getReturnType());
    }
}
