package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.response;
import java.time.LocalDateTime;
/**
 * Response DTO for ServiceLevelAgreement
 */
public record ServiceLevelAgreementResponseDto(
        String id,
        String tenantId,
        String name,
        String description,
        String serviceType,
        Double responseTimeThreshold,
        Double uptimePercentage,
        Integer penaltyPercentage,
        String status,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
