package com.gogidix.finance.accountsreceivable.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer Queries (Input Port)
 * Defines the query operations for customer data
 */
public interface CustomerQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCustomerQuery {
        private String tenantId;
        private String customerId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCustomerByCodeQuery {
        private String tenantId;
        private String customerCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCustomersByTypeQuery {
        private String tenantId;
        private String customerType;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetCustomersByStatusQuery {
        private String tenantId;
        private String status;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetOverdueCustomersQuery {
        private String tenantId;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchCustomersQuery {
        private String tenantId;
        private String searchTerm;
        private String customerType;
        private String status;
        private Integer page;
        private Integer size;
    }
}
