package com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionalAnalyticsRequestDto {
    private String tenantId;
    private String metricName;
    private String metricValue;
    private String region;
    private String country;
    private String period;
    private String category;
}
