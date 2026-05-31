package com.gogidix.customersupport.supportanalytics.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.supportanalytics.domain.model.TicketTrend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketTrendResponseDto {

    private String id;
    private String tenantId;
    private LocalDate trendDate;
    private PeriodTypeDto periodType;
    private Integer totalTickets;
    private Integer newTickets;
    private Integer closedTickets;
    private Integer reopenedTickets;
    private Map<String, Integer> ticketsByStatus;
    private Map<String, Integer> ticketsByPriority;
    private Map<String, Integer> ticketsByChannel;
    private Map<String, Integer> ticketsByCategory;
    private Double changePercentage;
    private Integer changeFromPreviousPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum PeriodTypeDto {
        HOURLY, DAILY, WEEKLY, MONTHLY
    }

    public static PeriodTypeDto fromEntityType(TicketTrend.PeriodType periodType) {
        return PeriodTypeDto.valueOf(periodType.name());
    }
}
