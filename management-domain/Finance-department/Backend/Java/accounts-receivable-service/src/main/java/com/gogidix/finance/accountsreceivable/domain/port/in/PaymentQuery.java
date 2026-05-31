package com.gogidix.finance.accountsreceivable.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Payment Queries (Input Port)
 * Defines the query operations for payment data
 */
public interface PaymentQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPaymentQuery {
        private String tenantId;
        private String paymentId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPaymentsByCustomerQuery {
        private String tenantId;
        private String customerId;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPaymentsByInvoiceQuery {
        private String tenantId;
        private String invoiceId;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPaymentsByStatusQuery {
        private String tenantId;
        private String status;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPaymentsByDateRangeQuery {
        private String tenantId;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<String> statuses;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPaymentsByMethodQuery {
        private String tenantId;
        private String paymentMethod;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetUnallocatedPaymentsQuery {
        private String tenantId;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchPaymentsQuery {
        private String tenantId;
        private String searchTerm;
        private String customerId;
        private String status;
        private String paymentMethod;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal minAmount;
        private BigDecimal maxAmount;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPaymentSummaryQuery {
        private String tenantId;
        private LocalDate startDate;
        private LocalDate endDate;
        private String customerId;
    }
}
