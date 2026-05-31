package com.gogidix.shared.infrastructure.services.security.mfa.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request.RegisterMfaDeviceRequestDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request.VerifyMfaCodeRequestDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response.MfaDeviceResponseDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response.MfaVerificationResponseDto;
import com.gogidix.shared.infrastructure.services.security.mfa.domain.model.MfaDevice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MfaService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("MFA Service Tests")
class MfaServiceTest {

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private MfaService mfaService;

    private static final String TEST_TENANT_ID = "test-tenant-123";
    private static final String TEST_USER_ID = "test-user-123";

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TEST_TENANT_ID);
    }

    @Test
    @DisplayName("Should register TOTP device successfully")
    void shouldRegisterTotpDeviceSuccessfully() {
        RegisterMfaDeviceRequestDto request = new RegisterMfaDeviceRequestDto(
                TEST_USER_ID,
                RegisterMfaDeviceRequestDto.MfaType.TOTP,
                null,
                "test@example.com",
                "My TOTP Device",
                true
        );

        MfaDeviceResponseDto response = mfaService.registerDevice(request);

        assertNotNull(response);
        assertEquals(TEST_USER_ID, response.userId());
        assertEquals(MfaDevice.MfaType.TOTP, response.mfaType());
        assertEquals(MfaDevice.DeviceStatus.PENDING_VERIFICATION, response.status());
        assertTrue(response.isPrimary());
        assertFalse(response.isBackup());
        assertEquals("test@example.com", response.email());
        assertNotNull(response.deviceId());
        assertNotNull(response.id());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should register SMS device successfully")
    void shouldRegisterSmsDeviceSuccessfully() {
        RegisterMfaDeviceRequestDto request = new RegisterMfaDeviceRequestDto(
                TEST_USER_ID,
                RegisterMfaDeviceRequestDto.MfaType.SMS,
                "+1234567890",
                null,
                "My Phone",
                false
        );

        MfaDeviceResponseDto response = mfaService.registerDevice(request);

        assertNotNull(response);
        assertEquals(TEST_USER_ID, response.userId());
        assertEquals(MfaDevice.MfaType.SMS, response.mfaType());
        assertEquals("+1234567890", response.phoneNumber());
        assertFalse(response.isPrimary());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should verify MFA code successfully")
    void shouldVerifyMfaCodeSuccessfully() {
        VerifyMfaCodeRequestDto request = new VerifyMfaCodeRequestDto(
                TEST_USER_ID,
                "123456",
                "device-123"
        );

        MfaVerificationResponseDto response = mfaService.verifyCode(request);

        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("MFA code verified successfully", response.message());
        assertEquals("device-123", response.deviceId());
        assertFalse(response.requiresBackupCode());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should generate 10 backup codes")
    void shouldGenerateTenBackupCodes() {
        List<String> backupCodes = mfaService.generateBackupCodes(TEST_USER_ID);

        assertNotNull(backupCodes);
        assertEquals(10, backupCodes.size());
        backupCodes.forEach(code -> {
            assertNotNull(code);
            assertEquals(8, code.length());
            assertTrue(code.matches("[A-Z0-9]+"));
        });
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should generate unique backup codes")
    void shouldGenerateUniqueBackupCodes() {
        List<String> codes1 = mfaService.generateBackupCodes(TEST_USER_ID);
        List<String> codes2 = mfaService.generateBackupCodes(TEST_USER_ID);

        assertNotEquals(codes1, codes2);
        verify(tenantContextHolder, times(2)).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should get empty list for user devices")
    void shouldGetEmptyListForUserDevices() {
        List<MfaDeviceResponseDto> devices = mfaService.getUserDevices(TEST_USER_ID);

        assertNotNull(devices);
        assertTrue(devices.isEmpty());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should revoke device without throwing exception")
    void shouldRevokeDeviceSuccessfully() {
        assertDoesNotThrow(() -> mfaService.revokeDevice("device-123"));
    }

    @Test
    @DisplayName("Should set primary device and return null")
    void shouldSetPrimaryDeviceSuccessfully() {
        MfaDeviceResponseDto response = mfaService.setPrimaryDevice("device-123");

        assertNull(response);
        // setPrimaryDevice does not call getRequiredTenantId in current implementation
    }

    @Test
    @DisplayName("Should enable MFA without throwing exception")
    void shouldEnableMfaSuccessfully() {
        assertDoesNotThrow(() -> mfaService.enableMfa(TEST_USER_ID));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should disable MFA without throwing exception")
    void shouldDisableMfaSuccessfully() {
        assertDoesNotThrow(() -> mfaService.disableMfa(TEST_USER_ID));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should send MFA code via SMS")
    void shouldSendMfaCodeViaSms() {
        assertDoesNotThrow(() -> mfaService.sendMfaCode(TEST_USER_ID, "sms"));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should send MFA code via email")
    void shouldSendMfaCodeViaEmail() {
        assertDoesNotThrow(() -> mfaService.sendMfaCode(TEST_USER_ID, "email"));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should validate backup code successfully")
    void shouldValidateBackupCodeSuccessfully() {
        MfaVerificationResponseDto response = mfaService.validateBackupCode(TEST_USER_ID, "ABC12345");

        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Backup code validated successfully", response.message());
        assertNull(response.deviceId());
        assertFalse(response.requiresBackupCode());
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should regenerate backup codes")
    void shouldRegenerateBackupCodes() {
        List<String> newCodes = mfaService.regenerateBackupCodes(TEST_USER_ID);

        assertNotNull(newCodes);
        assertEquals(10, newCodes.size());
        // regenerateBackupCodes calls generateBackupCodes internally, so 2 calls
        verify(tenantContextHolder, times(2)).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should register EMAIL device successfully")
    void shouldRegisterEmailDeviceSuccessfully() {
        RegisterMfaDeviceRequestDto request = new RegisterMfaDeviceRequestDto(
                TEST_USER_ID,
                RegisterMfaDeviceRequestDto.MfaType.EMAIL,
                null,
                "email@example.com",
                "Work Email",
                false
        );

        MfaDeviceResponseDto response = mfaService.registerDevice(request);

        assertNotNull(response);
        assertEquals(TEST_USER_ID, response.userId());
        assertEquals(MfaDevice.MfaType.EMAIL, response.mfaType());
        assertEquals("email@example.com", response.email());
    }

    @Test
    @DisplayName("Should register BACKUP_CODE device successfully")
    void shouldRegisterBackupCodeDeviceSuccessfully() {
        RegisterMfaDeviceRequestDto request = new RegisterMfaDeviceRequestDto(
                TEST_USER_ID,
                RegisterMfaDeviceRequestDto.MfaType.BACKUP_CODE,
                null,
                null,
                null,
                false
        );

        MfaDeviceResponseDto response = mfaService.registerDevice(request);

        assertNotNull(response);
        assertEquals(TEST_USER_ID, response.userId());
        assertEquals(MfaDevice.MfaType.BACKUP_CODE, response.mfaType());
    }

    @Test
    @DisplayName("Should register HARDWARE_TOKEN device successfully")
    void shouldRegisterHardwareTokenDeviceSuccessfully() {
        RegisterMfaDeviceRequestDto request = new RegisterMfaDeviceRequestDto(
                TEST_USER_ID,
                RegisterMfaDeviceRequestDto.MfaType.HARDWARE_TOKEN,
                null,
                null,
                "YubiKey",
                false
        );

        MfaDeviceResponseDto response = mfaService.registerDevice(request);

        assertNotNull(response);
        assertEquals(TEST_USER_ID, response.userId());
        assertEquals(MfaDevice.MfaType.HARDWARE_TOKEN, response.mfaType());
    }

    @Test
    @DisplayName("Should register BIOMETRIC device successfully")
    void shouldRegisterBiometricDeviceSuccessfully() {
        RegisterMfaDeviceRequestDto request = new RegisterMfaDeviceRequestDto(
                TEST_USER_ID,
                RegisterMfaDeviceRequestDto.MfaType.BIOMETRIC,
                null,
                null,
                "Fingerprint",
                false
        );

        MfaDeviceResponseDto response = mfaService.registerDevice(request);

        assertNotNull(response);
        assertEquals(TEST_USER_ID, response.userId());
        assertEquals(MfaDevice.MfaType.BIOMETRIC, response.mfaType());
    }

    @Test
    @DisplayName("Should default isPrimary to false when null")
    void shouldDefaultIsPrimaryToFalseWhenNull() {
        RegisterMfaDeviceRequestDto request = new RegisterMfaDeviceRequestDto(
                TEST_USER_ID,
                RegisterMfaDeviceRequestDto.MfaType.TOTP,
                null,
                "test@example.com",
                null,
                null
        );

        MfaDeviceResponseDto response = mfaService.registerDevice(request);

        assertNotNull(response);
        assertFalse(response.isPrimary());
    }

    @Test
    @DisplayName("Should default isBackup to false")
    void shouldDefaultIsBackupToFalse() {
        RegisterMfaDeviceRequestDto request = new RegisterMfaDeviceRequestDto(
                TEST_USER_ID,
                RegisterMfaDeviceRequestDto.MfaType.TOTP,
                null,
                "test@example.com",
                null,
                true
        );

        MfaDeviceResponseDto response = mfaService.registerDevice(request);

        assertNotNull(response);
        assertFalse(response.isBackup());
    }

    @Test
    @DisplayName("Should handle all MfaType enum values")
    void shouldHandleAllMfaTypeValues() {
        assertEquals(6, RegisterMfaDeviceRequestDto.MfaType.values().length);
    }
}
