package com.gogidix.shared.infrastructure.services.communication.email.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for EmailMessage operations
 */
public record CreateEmailMessageRequestDto(
    @NotBlank(message = "To is required")
    @Email(message = "To must be a valid email")
    String to,

    @Email(message = "CC must be a valid email")
    String cc,

    @Email(message = "BCC must be a valid email")
    String bcc,

    @NotBlank(message = "Subject is required")
    @Size(max = 500, message = "Subject must not exceed 500 characters")
    String subject,

    @NotBlank(message = "Body is required")
    String body,

    @Size(max = 100, message = "Template name must not exceed 100 characters")
    String templateName,

    @Size(max = 50, message = "Provider must not exceed 50 characters")
    String provider,

    @Size(max = 100, message = "Campaign ID must not exceed 100 characters")
    String campaignId
) {
}
