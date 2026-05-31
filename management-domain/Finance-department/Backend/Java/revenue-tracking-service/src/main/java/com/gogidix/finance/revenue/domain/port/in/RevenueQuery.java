package com.gogidix.finance.revenue.domain.port.in;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Revenue Queries (Input Port)
 * Defines query operations for revenue data
 */
public interface RevenueQuery {

    @Data
    class RevenueSummary {
        private BigDecimal totalRevenue;
        private BigDecimal recognizedRevenue;
        private BigDecimal deferredRevenue;
        private BigDecimal pendingRevenue;
        private Long totalCount;
        private Long recognizedCount;
        private Long deferredCount;
        private Long pendingCount;
        private BigDecimal mrr; // Monthly Recurring Revenue
        private BigDecimal arr; // Annual Recurring Revenue
    }

    @Data
    class RevenueByType {
        private String type;
        private BigDecimal totalAmount;
        private BigDecimal recognizedAmount;
        private Long count;
    }

    @Data
    class RevenueByCustomer {
        private String customerId;
        private String customerName;
        private BigDecimal totalRevenue;
        private Long transactionCount;
    }

    @Data
    class RevenueByPeriod {
        private String period;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal amount;
        private BigDecimal recognizedAmount;
        private BigDecimal deferredAmount;
    }

    @Data
    class ForecastRequest {
        private LocalDate startDate;
        private Integer horizonMonths;
        private String method;
        private List<String> customerIds;
        private List<String> types;
        private Boolean includeDeferred;
        private Boolean includeRecurring;
    }
}
