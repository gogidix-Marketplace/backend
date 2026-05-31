package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Invoice;
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
    class GetInvoicesByVendorQuery {
        private String tenantId;

        private String vendorId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetInvoicesByStatusQuery {
        private String tenantId;

        private Invoice.InvoiceStatus status;

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
    class GetOverdueInvoicesQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPendingApprovalInvoicesQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetInvoicesByDepartmentQuery {
        private String tenantId;

        private String department;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchInvoicesQuery {
        private String tenantId;

        private String searchTerm;

        private String vendorId;

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
    class GetInvoicesDueForPaymentQuery {
        private String tenantId;

        private LocalDate dueDate;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetAllInvoicesQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }
}
