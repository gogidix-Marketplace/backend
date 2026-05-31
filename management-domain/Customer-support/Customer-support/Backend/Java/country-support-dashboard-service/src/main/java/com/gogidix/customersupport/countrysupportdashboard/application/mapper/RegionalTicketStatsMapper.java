package com.gogidix.customersupport.countrysupportdashboard.application.mapper;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.RegionalTicketStatsResponseDto;
import com.gogidix.customersupport.countrysupportdashboard.domain.model.RegionalTicketStats;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RegionalTicketStatsMapper {

    public RegionalTicketStatsResponseDto toResponseDto(RegionalTicketStats entity) {
        Map<String, RegionalTicketStatsResponseDto.CountryTicketSummaryDto> ticketsByCountry = null;
        if (entity.getTicketsByCountry() != null) {
            ticketsByCountry = entity.getTicketsByCountry().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> RegionalTicketStatsResponseDto.CountryTicketSummaryDto.builder()
                                    .countryCode(e.getValue().getCountryCode())
                                    .countryName(e.getValue().getCountryName())
                                    .totalTickets(e.getValue().getTotalTickets())
                                    .openTickets(e.getValue().getOpenTickets())
                                    .resolvedTickets(e.getValue().getResolvedTickets())
                                    .satisfactionScore(e.getValue().getSatisfactionScore())
                                    .build()
                    ));
        }

        return RegionalTicketStatsResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .regionName(entity.getRegionName())
                .countryCodes(entity.getCountryCodes())
                .statDate(entity.getStatDate())
                .totalTickets(entity.getTotalTickets())
                .newTickets(entity.getNewTickets())
                .closedTickets(entity.getClosedTickets())
                .pendingTickets(entity.getPendingTickets())
                .ticketsByCountry(ticketsByCountry)
                .ticketsByStatus(entity.getTicketsByStatus())
                .ticketsByPriority(entity.getTicketsByPriority())
                .ticketsByChannel(entity.getTicketsByChannel())
                .averageResolutionTimeMinutes(entity.getAverageResolutionTimeMinutes())
                .averageResponseTimeMinutes(entity.getAverageResponseTimeMinutes())
                .customerSatisfactionScore(entity.getCustomerSatisfactionScore())
                .slaComplianceRate(entity.getSlaComplianceRate())
                .changePercentage(entity.getChangePercentage())
                .activeAgents(entity.getActiveAgents())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
