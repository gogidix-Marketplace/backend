package com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
/**
 * Request DTO for creating a DLP policy.
 */
public record CreateDlpPolicyRequestDto(
    @NotBlank(message = "Policy name is required")
    String policyName,
    String description,
    List<String> sensitiveDataPatterns,
    String action
) {
}
