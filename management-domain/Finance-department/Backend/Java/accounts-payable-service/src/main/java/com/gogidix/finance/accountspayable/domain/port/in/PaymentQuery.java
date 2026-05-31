package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Payment;
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
    class GetPaymentsByVendorQuery {
        private String tenantId;

        private String vendorId;

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

        private Payment.PaymentStatus status;

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
    class GetScheduledPaymentsQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPendingPaymentsQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetFailedPaymentsQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPaymentsByMethodQuery {
        private String tenantId;

        private Payment.PaymentMethod paymentMethod;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchPaymentsQuery {
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
    class GetAllPaymentsQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }
}
