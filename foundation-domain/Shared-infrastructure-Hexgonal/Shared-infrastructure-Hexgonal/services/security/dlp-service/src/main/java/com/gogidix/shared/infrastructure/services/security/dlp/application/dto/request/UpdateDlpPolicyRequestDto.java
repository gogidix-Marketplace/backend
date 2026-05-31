package com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request;
import java.util.List;
/**
 * Request DTO for updating a DLP policy.
 */
public record UpdateDlpPolicyRequestDto(
    String policyName,
    String description,
    List<String> sensitiveDataPatterns,
    String action
) {
}
