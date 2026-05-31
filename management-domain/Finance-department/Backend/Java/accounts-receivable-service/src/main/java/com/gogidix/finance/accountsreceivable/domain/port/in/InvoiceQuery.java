package com.gogidix.finance.accountsreceivable.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Invoice Queries (Input Port)
 * Defines the query operations for invoice data
 */
public interface InvoiceQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetInvoiceQuery {
        private String tenantId;
        private String invoiceId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetInvoiceByNumberQuery {
        private String tenantId;
        private String invoiceNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetInvoicesByCustomerQuery {
        private String tenantId;
        private String customerId;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetInvoicesByStatusQuery {
        private String tenantId;
        private String status;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetOverdueInvoicesQuery {
        private String tenantId;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetInvoicesByDateRangeQuery {
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
    class GetInvoicesByDueDateRangeQuery {
        private String tenantId;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPendingInvoicesQuery {
        private String tenantId;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchInvoicesQuery {
        private String tenantId;
        private String searchTerm;
        private String customerId;
        private String status;
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
    class GetInvoiceSummaryQuery {
        private String tenantId;
        private LocalDate startDate;
        private LocalDate endDate;
        private String customerId;
    }
}
