package com.gogidix.aiservices.aiauthenticationservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MfaVerificationRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "MFA code is required")
    @Size(min = 6, max = 6, message = "MFA code must be 6 digits")
    private String mfaCode;

    @NotBlank(message = "Session ID is required")
    private String sessionId;
}
