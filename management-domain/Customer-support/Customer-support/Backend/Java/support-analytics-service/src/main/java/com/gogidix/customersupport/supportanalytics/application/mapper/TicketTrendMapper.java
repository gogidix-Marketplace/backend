package com.gogidix.customersupport.supportanalytics.application.mapper;

import com.gogidix.customersupport.supportanalytics.application.dto.TicketTrendResponseDto;
import com.gogidix.customersupport.supportanalytics.domain.model.TicketTrend;
import org.springframework.stereotype.Component;

@Component
public class TicketTrendMapper {

    public TicketTrendResponseDto toResponseDto(TicketTrend entity) {
        return TicketTrendResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .trendDate(entity.getTrendDate())
                .periodType(TicketTrendResponseDto.fromEntityType(entity.getPeriodType()))
                .totalTickets(entity.getTotalTickets())
                .newTickets(entity.getNewTickets())
                .closedTickets(entity.getClosedTickets())
                .reopenedTickets(entity.getReopenedTickets())
                .ticketsByStatus(entity.getTicketsByStatus())
                .ticketsByPriority(entity.getTicketsByPriority())
                .ticketsByChannel(entity.getTicketsByChannel())
                .ticketsByCategory(entity.getTicketsByCategory())
                .changePercentage(entity.getChangePercentage())
                .changeFromPreviousPeriod(entity.getChangeFromPreviousPeriod())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
