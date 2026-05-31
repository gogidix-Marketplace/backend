package com.gogidix.customersupport.countrysupportdashboard.application.mapper;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsRequestDto;
import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsResponseDto;
import com.gogidix.customersupport.countrysupportdashboard.domain.model.CountrySpecificMetrics;
import org.springframework.stereotype.Component;

@Component
public class CountrySpecificMetricsMapper {

    public CountrySpecificMetrics toEntity(CountrySpecificMetricsRequestDto dto, String tenantId) {
        CountrySpecificMetrics metrics = CountrySpecificMetrics.create(
                tenantId,
                dto.getCountryCode(),
                dto.getCountryName(),
                dto.getMetricDate()
        );

        metrics.setTotalTickets(dto.getTotalTickets());
        metrics.setOpenTickets(dto.getOpenTickets());
        metrics.setResolvedTickets(dto.getResolvedTickets());
        metrics.setEscalatedTickets(dto.getEscalatedTickets());
        metrics.setAverageResolutionTimeMinutes(dto.getAverageResolutionTimeMinutes());
        metrics.setAverageResponseTimeMinutes(dto.getAverageResponseTimeMinutes());
        metrics.setCustomerSatisfactionScore(dto.getCustomerSatisfactionScore());
        metrics.setActiveAgents(dto.getActiveAgents());
        metrics.setTicketVolumeByChannel(dto.getTicketVolumeByChannel());
        metrics.setTicketVolumeByPriority(dto.getTicketVolumeByPriority());
        metrics.setTicketVolumeByCategory(dto.getTicketVolumeByCategory());
        metrics.setSlaComplianceRate(dto.getSlaComplianceRate());
        metrics.setFirstContactResolutionRate(dto.getFirstContactResolutionRate());
        metrics.setPeakHours(dto.getPeakHours());
        metrics.setRegion(dto.getRegion());
        metrics.setLanguage(dto.getLanguage());
        metrics.setTimezone(dto.getTimezone());

        if (dto.getBusinessHours() != null) {
            metrics.setBusinessHours(CountrySpecificMetrics.BusinessHours.builder()
                    .startTime(dto.getBusinessHours().getStartTime())
                    .endTime(dto.getBusinessHours().getEndTime())
                    .timezone(dto.getBusinessHours().getTimezone())
                    .workingDays(dto.getBusinessHours().getWorkingDays())
                    .build());
        }

        return metrics;
    }

    public CountrySpecificMetricsResponseDto toResponseDto(CountrySpecificMetrics entity) {
        CountrySpecificMetricsResponseDto.BusinessHoursDto businessHoursDto = null;
        if (entity.getBusinessHours() != null) {
            businessHoursDto = CountrySpecificMetricsResponseDto.BusinessHoursDto.builder()
                    .startTime(entity.getBusinessHours().getStartTime())
                    .endTime(entity.getBusinessHours().getEndTime())
                    .timezone(entity.getBusinessHours().getTimezone())
                    .workingDays(entity.getBusinessHours().getWorkingDays())
                    .build();
        }

        return CountrySpecificMetricsResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .countryCode(entity.getCountryCode())
                .countryName(entity.getCountryName())
                .metricDate(entity.getMetricDate())
                .totalTickets(entity.getTotalTickets())
                .openTickets(entity.getOpenTickets())
                .resolvedTickets(entity.getResolvedTickets())
                .escalatedTickets(entity.getEscalatedTickets())
                .averageResolutionTimeMinutes(entity.getAverageResolutionTimeMinutes())
                .averageResponseTimeMinutes(entity.getAverageResponseTimeMinutes())
                .customerSatisfactionScore(entity.getCustomerSatisfactionScore())
                .activeAgents(entity.getActiveAgents())
                .ticketVolumeByChannel(entity.getTicketVolumeByChannel())
                .ticketVolumeByPriority(entity.getTicketVolumeByPriority())
                .ticketVolumeByCategory(entity.getTicketVolumeByCategory())
                .slaComplianceRate(entity.getSlaComplianceRate())
                .firstContactResolutionRate(entity.getFirstContactResolutionRate())
                .peakHours(entity.getPeakHours())
                .region(entity.getRegion())
                .language(entity.getLanguage())
                .timezone(entity.getTimezone())
                .businessHours(businessHoursDto)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDto(CountrySpecificMetricsRequestDto dto, CountrySpecificMetrics entity) {
        entity.setCountryCode(dto.getCountryCode());
        entity.setCountryName(dto.getCountryName());
        entity.setMetricDate(dto.getMetricDate());
        entity.setTotalTickets(dto.getTotalTickets());
        entity.setOpenTickets(dto.getOpenTickets());
        entity.setResolvedTickets(dto.getResolvedTickets());
        entity.setEscalatedTickets(dto.getEscalatedTickets());
        entity.setAverageResolutionTimeMinutes(dto.getAverageResolutionTimeMinutes());
        entity.setAverageResponseTimeMinutes(dto.getAverageResponseTimeMinutes());
        entity.setCustomerSatisfactionScore(dto.getCustomerSatisfactionScore());
        entity.setActiveAgents(dto.getActiveAgents());
        entity.setTicketVolumeByChannel(dto.getTicketVolumeByChannel());
        entity.setTicketVolumeByPriority(dto.getTicketVolumeByPriority());
        entity.setTicketVolumeByCategory(dto.getTicketVolumeByCategory());
        entity.setSlaComplianceRate(dto.getSlaComplianceRate());
        entity.setFirstContactResolutionRate(dto.getFirstContactResolutionRate());
        entity.setPeakHours(dto.getPeakHours());
        entity.setRegion(dto.getRegion());
        entity.setLanguage(dto.getLanguage());
        entity.setTimezone(dto.getTimezone());
        entity.updateTimestamp();
    }
}
