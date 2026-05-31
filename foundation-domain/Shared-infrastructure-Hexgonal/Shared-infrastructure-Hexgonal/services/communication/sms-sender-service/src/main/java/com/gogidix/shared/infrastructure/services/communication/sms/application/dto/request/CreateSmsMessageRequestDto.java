package com.gogidix.shared.infrastructure.services.communication.sms.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for SmsMessage operations
 */
public record CreateSmsMessageRequestDto(
    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    String phoneNumber,

    @Size(max = 10, message = "Country code must not exceed 10 characters")
    String countryCode,

    @NotBlank(message = "Message is required")
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    String message,

    @Size(max = 100, message = "Template name must not exceed 100 characters")
    String templateName,

    @Size(max = 50, message = "Provider must not exceed 50 characters")
    String provider,

    @Size(max = 100, message = "Campaign ID must not exceed 100 characters")
    String campaignId
) {
}
