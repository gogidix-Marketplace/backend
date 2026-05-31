package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Vendor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Vendor Queries (Input Port)
 * Defines the query operations for vendor data
 */
public interface VendorQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetVendorQuery {
        private String tenantId;

        private String vendorId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetVendorByCodeQuery {
        private String tenantId;

        private String vendorCode;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetVendorsByTypeQuery {
        private String tenantId;

        private Vendor.VendorType vendorType;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetVendorsByStatusQuery {
        private String tenantId;

        private Vendor.VendorStatus status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPreferredVendorsQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchVendorsQuery {
        private String tenantId;

        private String searchTerm;

        private Vendor.VendorType vendorType;

        private Vendor.VendorStatus status;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetVendorsByTagQuery {
        private String tenantId;

        private String tag;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetActiveVendorsQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetAllVendorsQuery {
        private String tenantId;

        private Integer page;

        private Integer size;
    }
}
