package com.gogidix.shared.infrastructure.services.security.mfa.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyMfaCodeRequestDto(
        @NotBlank(message = "User ID is required")
        String userId,

        @NotBlank(message = "Code is required")
        String code,

        String deviceId
) {
}
