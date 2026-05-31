package com.gogidix.globalbusinessmanagement.regionalanalytics.application.mapper;

import com.gogidix.globalbusinessmanagement.regionalanalytics.domain.model.RegionalAnalytics;
import com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto.RegionalAnalyticsRequestDto;
import com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto.RegionalAnalyticsResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RegionalAnalyticsMapper {

    public RegionalAnalytics toEntity(RegionalAnalyticsRequestDto dto) {
        return RegionalAnalytics.builder()
            .tenantId(dto.getTenantId())
            .metricName(dto.getMetricName())
            .metricValue(dto.getMetricValue())
            .region(dto.getRegion())
            .country(dto.getCountry())
            .period(dto.getPeriod())
            .category(dto.getCategory())
            .build();
    }

    public RegionalAnalyticsResponseDto toResponseDto(RegionalAnalytics entity) {
        return RegionalAnalyticsResponseDto.builder()
            .id(entity.getId())
            .tenantId(entity.getTenantId())
            .metricName(entity.getMetricName())
            .metricValue(entity.getMetricValue())
            .region(entity.getRegion())
            .country(entity.getCountry())
            .period(entity.getPeriod())
            .category(entity.getCategory())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
