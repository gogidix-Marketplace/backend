package com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response;

import com.gogidix.shared.infrastructure.services.security.mfa.domain.model.MfaDevice;

import java.time.LocalDateTime;

public record MfaDeviceResponseDto(
        String id,
        String tenantId,
        String userId,
        String deviceId,
        MfaDevice.MfaType mfaType,
        MfaDevice.DeviceStatus status,
        String phoneNumber,
        String email,
        Boolean isPrimary,
        Boolean isBackup,
        LocalDateTime lastUsedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
