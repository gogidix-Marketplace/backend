package com.gogidix.shared.infrastructure.services.security.dlp.application.dto.response;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Response DTO for DLP policy operations.
 */
public record DlpPolicyResponseDto(
    String id,
    String tenantId,
    String policyId,
    String policyName,
    String description,
    String status,
    List<String> sensitiveDataPatterns,
    String action,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
