package com.gogidix.shared.infrastructure.services.security.mfa.application.dto.response;

public record MfaVerificationResponseDto(
        boolean success,
        String message,
        String deviceId,
        boolean requiresBackupCode
) {
}
