package com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionalAnalyticsResponseDto {
    private String id;
    private String tenantId;
    private String metricName;
    private String metricValue;
    private String region;
    private String country;
    private String period;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
}
