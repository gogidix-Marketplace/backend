package com.gogidix.infrastructure.lockservice.application.dto;

import com.gogidix.infrastructure.lockservice.domain.model.LockType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for lock statistics response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Lock statistics and metrics")
public class LockStatisticsResponseDto {

    @Schema(description = "Tenant ID for these statistics")
    private String tenantId;

    @Schema(description = "Total number of active locks")
    private Long activeLocks;

    @Schema(description = "Total number of expired locks")
    private Long expiredLocks;

    @Schema(description = "Total number of released locks")
    private Long releasedLocks;

    @Schema(description = "Total number of failed lock attempts")
    private Long failedAttempts;

    @Schema(description = "Average lock acquisition time in milliseconds")
    private Double averageAcquisitionTimeMs;

    @Schema(description = "Average lock hold time in milliseconds")
    private Double averageHoldTimeMs;

    @Schema(description = "Lock count by type")
    private Map<String, Long> locksByType;

    @Schema(description = "When these statistics were calculated")
    private LocalDateTime calculatedAt;

    @Schema(description = "Total number of lock operations")
    private Long totalOperations;

    @Schema(description = "Peak concurrent locks for this tenant")
    private Long peakConcurrentLocks;

    @Schema(description = "Current waiting lock requests")
    private Long waitingRequests;

    public static LockStatisticsResponseDto fromDomain(com.gogidix.infrastructure.lockservice.domain.model.LockStatistics stats) {
        return LockStatisticsResponseDto.builder()
            .tenantId(stats.getTenantId())
            .activeLocks(stats.getActiveLocks())
            .expiredLocks(stats.getExpiredLocks())
            .releasedLocks(stats.getReleasedLocks())
            .failedAttempts(stats.getFailedAttempts())
            .averageAcquisitionTimeMs(stats.getAverageAcquisitionTimeMs())
            .averageHoldTimeMs(stats.getAverageHoldTimeMs())
            .locksByType(stats.getLocksByType() != null
                ? stats.getLocksByType().entrySet().stream()
                    .collect(java.util.stream.Collectors.toMap(
                        e -> e.getKey().name(),
                        java.util.Map.Entry::getValue
                    ))
                : null)
            .calculatedAt(stats.getCalculatedAt())
            .totalOperations(stats.getTotalOperations())
            .peakConcurrentLocks(stats.getPeakConcurrentLocks())
            .waitingRequests(stats.getWaitingRequests())
            .build();
    }
}
