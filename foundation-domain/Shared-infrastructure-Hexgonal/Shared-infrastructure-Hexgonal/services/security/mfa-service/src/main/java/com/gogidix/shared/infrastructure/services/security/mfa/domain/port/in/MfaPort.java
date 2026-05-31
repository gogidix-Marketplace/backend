package com.gogidix.shared.infrastructure.services.security.mfa.domain.port.in;

import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request.RegisterMfaDeviceRequestDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request.VerifyMfaCodeRequestDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response.MfaDeviceResponseDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response.MfaVerificationResponseDto;

import java.util.List;

/**
 * Input port for MFA operations.
 * Defines the contract for MFA use cases.
 */
public interface MfaPort {

    /**
     * Register a new MFA device for a user.
     */
    MfaDeviceResponseDto registerDevice(RegisterMfaDeviceRequestDto request);

    /**
     * Verify MFA code during authentication.
     */
    MfaVerificationResponseDto verifyCode(VerifyMfaCodeRequestDto request);

    /**
     * Generate backup codes for a user.
     */
    List<String> generateBackupCodes(String userId);

    /**
     * Get all MFA devices for a user.
     */
    List<MfaDeviceResponseDto> getUserDevices(String userId);

    /**
     * Revoke an MFA device.
     */
    void revokeDevice(String deviceId);

    /**
     * Set primary MFA device.
     */
    MfaDeviceResponseDto setPrimaryDevice(String deviceId);

    /**
     * Enable MFA for a user.
     */
    void enableMfa(String userId);

    /**
     * Disable MFA for a user.
     */
    void disableMfa(String userId);

    /**
     * Send MFA code via SMS or email.
     */
    void sendMfaCode(String userId, String channel);

    /**
     * Validate backup code.
     */
    MfaVerificationResponseDto validateBackupCode(String userId, String code);

    /**
     * Regenerate backup codes.
     */
    List<String> regenerateBackupCodes(String userId);
}
