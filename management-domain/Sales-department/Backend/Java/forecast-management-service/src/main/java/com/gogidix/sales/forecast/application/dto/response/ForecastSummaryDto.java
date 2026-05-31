package com.gogidix.sales.forecast.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Forecast Summary Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastSummaryDto {

    private long totalCount;

    private BigDecimal totalBestCase;

    private BigDecimal totalLikely;

    private BigDecimal totalWorstCase;

    private long draftCount;

    private long submittedCount;

    private long approvedCount;

    private long publishedCount;
}
