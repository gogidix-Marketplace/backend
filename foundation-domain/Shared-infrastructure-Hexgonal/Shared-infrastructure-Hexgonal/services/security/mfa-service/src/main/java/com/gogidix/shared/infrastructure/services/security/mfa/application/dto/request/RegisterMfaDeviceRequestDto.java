package com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterMfaDeviceRequestDto(
        @NotBlank(message = "User ID is required")
        String userId,

        @NotNull(message = "MFA type is required")
        MfaType mfaType,

        String phoneNumber,

        String email,

        String deviceName,

        Boolean isPrimary
) {
    public enum MfaType {
        TOTP,
        SMS,
        EMAIL,
        BACKUP_CODE,
        HARDWARE_TOKEN,
        BIOMETRIC
    }
}
