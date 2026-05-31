package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
/**
 * Request DTO for updating ServiceLevelAgreement
 */
public record UpdateServiceLevelAgreementRequestDto(
        @NotBlank(message = "Name is required")
        String name,
        String description,
        @NotBlank(message = "Service type is required")
        String serviceType,
        @NotNull(message = "Response time threshold is required")
        @PositiveOrZero(message = "Response time threshold must be positive or zero")
        Double responseTimeThreshold,
        @NotNull(message = "Uptime percentage is required")
        @PositiveOrZero(message = "Uptime percentage must be positive or zero")
        Double uptimePercentage,
        @NotNull(message = "Penalty percentage is required")
        @PositiveOrZero(message = "Penalty percentage must be positive or zero")
        Integer penaltyPercentage,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        String status
) {
}
