package com.gogidix.customersupport.supportanalytics.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "ticket_trends")
public class TicketTrend extends BaseEntity {

    @Field("trend_date")
    @Indexed
    private LocalDate trendDate;

    @Field("period_type")
    @Indexed
    private PeriodType periodType;

    @Field("total_tickets")
    private Integer totalTickets;

    @Field("new_tickets")
    private Integer newTickets;

    @Field("closed_tickets")
    private Integer closedTickets;

    @Field("reopened_tickets")
    private Integer reopenedTickets;

    @Field("tickets_by_status")
    private Map<String, Integer> ticketsByStatus;

    @Field("tickets_by_priority")
    private Map<String, Integer> ticketsByPriority;

    @Field("tickets_by_channel")
    private Map<String, Integer> ticketsByChannel;

    @Field("tickets_by_category")
    private Map<String, Integer> ticketsByCategory;

    @Field("change_percentage")
    private Double changePercentage;

    @Field("change_from_previous_period")
    private Integer changeFromPreviousPeriod;

    public static TicketTrend create(String tenantId, LocalDate trendDate, PeriodType periodType) {
        TicketTrend trend = new TicketTrend();
        trend.setId(java.util.UUID.randomUUID().toString());
        trend.setTenantId(tenantId);
        trend.setTrendDate(trendDate);
        trend.setPeriodType(periodType);
        trend.setCreatedAt(java.time.Instant.now());
        trend.setUpdatedAt(java.time.Instant.now());
        return trend;
    }

    public enum PeriodType {
        HOURLY, DAILY, WEEKLY, MONTHLY
    }
}
