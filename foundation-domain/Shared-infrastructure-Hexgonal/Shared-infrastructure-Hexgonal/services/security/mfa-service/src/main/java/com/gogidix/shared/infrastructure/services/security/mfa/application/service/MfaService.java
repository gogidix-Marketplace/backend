package com.gogidix.shared.infrastructure.services.security.mfa.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request.RegisterMfaDeviceRequestDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request.VerifyMfaCodeRequestDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response.MfaDeviceResponseDto;
import com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response.MfaVerificationResponseDto;
import com.gogidix.shared.infrastructure.services.security.mfa.domain.model.MfaDevice;
import com.gogidix.shared.infrastructure.services.security.mfa.domain.port.in.MfaPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Multi-Factor Authentication Service implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MfaService implements MfaPort {

    private final TenantContextHolder tenantContextHolder;

    @Override
    public MfaDeviceResponseDto registerDevice(RegisterMfaDeviceRequestDto request) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Registering MFA device for user: {}, type: {}", request.userId(), request.mfaType());

        // In a real implementation, this would:
        // 1. Generate a secret key for TOTP
        // 2. Store the device in MongoDB
        // 3. Send verification code via SMS/email

        MfaDevice device = new MfaDevice();
        device.setId(UUID.randomUUID().toString());
        device.setTenantId(com.gogidix.shared.infrastructure.core.tenancy.model.TenantId.of(tenantId));
        device.setUserId(request.userId());
        device.setDeviceId(UUID.randomUUID().toString());
        device.setMfaType(MfaDevice.MfaType.valueOf(request.mfaType().name()));
        device.setStatus(MfaDevice.DeviceStatus.PENDING_VERIFICATION);
        device.setPhoneNumber(request.phoneNumber());
        device.setEmail(request.email());
        device.setIsPrimary(request.isPrimary() != null ? request.isPrimary() : false);
        device.setIsBackup(false);
        device.setCreatedAt(LocalDateTime.now());
        device.setUpdatedAt(LocalDateTime.now());
        device.setFailedAttempts(0);

        return toResponseDto(device);
    }

    @Override
    public MfaVerificationResponseDto verifyCode(VerifyMfaCodeRequestDto request) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Verifying MFA code for user: {}", request.userId());

        // In a real implementation, this would:
        // 1. Retrieve the device from MongoDB
        // 2. Verify the TOTP code using the secret key
        // 3. Update the device's last used timestamp
        // 4. Handle failed attempts and lockout

        boolean success = true; // Placeholder
        return new MfaVerificationResponseDto(
                success,
                success ? "MFA code verified successfully" : "Invalid MFA code",
                request.deviceId(),
                false
        );
    }

    @Override
    public List<String> generateBackupCodes(String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Generating backup codes for user: {}", userId);

        // Generate 10 random backup codes
        List<String> backupCodes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            backupCodes.add(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        return backupCodes;
    }

    @Override
    public List<MfaDeviceResponseDto> getUserDevices(String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Getting MFA devices for user: {}", userId);

        // In a real implementation, this would query MongoDB
        return List.of();
    }

    @Override
    public void revokeDevice(String deviceId) {
        log.info("Revoking MFA device: {}", deviceId);

        // In a real implementation, this would update the device status in MongoDB
    }

    @Override
    public MfaDeviceResponseDto setPrimaryDevice(String deviceId) {
        log.info("Setting primary MFA device: {}", deviceId);

        // In a real implementation, this would update the device in MongoDB
        return null;
    }

    @Override
    public void enableMfa(String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Enabling MFA for user: {}", userId);

        // In a real implementation, this would update the user's MFA status in MongoDB
    }

    @Override
    public void disableMfa(String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Disabling MFA for user: {}", userId);

        // In a real implementation, this would update the user's MFA status in MongoDB
    }

    @Override
    public void sendMfaCode(String userId, String channel) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Sending MFA code via {} for user: {}", channel, userId);

        // In a real implementation, this would:
        // 1. Generate a random code
        // 2. Send it via SMS or email using a notification service
    }

    @Override
    public MfaVerificationResponseDto validateBackupCode(String userId, String code) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Validating backup code for user: {}", userId);

        // In a real implementation, this would verify the backup code in MongoDB
        return new MfaVerificationResponseDto(
                true,
                "Backup code validated successfully",
                null,
                false
        );
    }

    @Override
    public List<String> regenerateBackupCodes(String userId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();

        log.info("Regenerating backup codes for user: {}", userId);

        // In a real implementation, this would invalidate old codes and generate new ones
        return generateBackupCodes(userId);
    }

    private MfaDeviceResponseDto toResponseDto(MfaDevice device) {
        return new MfaDeviceResponseDto(
                device.getId(),
                device.getTenantId() != null ? device.getTenantId().getValue() : null,
                device.getUserId(),
                device.getDeviceId(),
                device.getMfaType(),
                device.getStatus(),
                device.getPhoneNumber(),
                device.getEmail(),
                device.getIsPrimary(),
                device.getIsBackup(),
                device.getLastUsedAt(),
                device.getCreatedAt(),
                device.getUpdatedAt()
        );
    }
}
