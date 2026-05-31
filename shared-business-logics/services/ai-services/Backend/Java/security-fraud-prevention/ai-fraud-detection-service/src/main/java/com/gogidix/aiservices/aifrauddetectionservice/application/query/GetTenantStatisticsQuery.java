package com.gogidix.aiservices.aifrauddetectionservice.application.query;

import com.gogidix.aiservices.aifrauddetectionservice.application.dto.TenantStatisticsDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * CQRS Query for retrieving tenant statistics.
 * Used to fetch aggregated fraud detection statistics for a specific tenant.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTenantStatisticsQuery implements Query<TenantStatisticsDTO> {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotNull(message = "Period is required")
    private StatisticsPeriod period;

    private Instant startDate;

    private Instant endDate;

    @Builder.Default
    private boolean includeComparisons = false;

    @Builder.Default
    private boolean includeTrends = true;

    /**
     * Time periods for statistics aggregation.
     */
    public enum StatisticsPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY,
        CUSTOM
    }
}
