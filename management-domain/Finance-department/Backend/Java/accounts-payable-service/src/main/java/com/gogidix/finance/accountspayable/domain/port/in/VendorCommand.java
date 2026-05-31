package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Vendor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Vendor Commands (Input Port)
 * Defines the input commands for vendor operations
 */
public interface VendorCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class CreateVendorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor code is required")
        private String vendorCode;

        @NotBlank(message = "Vendor name is required")
        private String vendorName;

        @NotNull(message = "Vendor type is required")
        private Vendor.VendorType vendorType;

        private String taxId;

        @NotBlank(message = "Currency is required")
        private String currency;

        private String paymentTerms;

        private Integer paymentDays;

        private String contactPerson;

        @Email(message = "Invalid email format")
        private String email;

        private String phone;

        private String website;

        @NotNull(message = "Billing address is required")
        private Vendor.Address billingAddress;

        private Vendor.Address shippingAddress;

        private String bankAccountNumber;

        private String bankRoutingNumber;

        private String bankName;

        private String bankAccountType;

        private LocalDate creditLimit;

        private String notes;

        private List<String> tags;

        private String parentVendorId;

        @NotNull(message = "Created by is required")
        private String createdBy;

        private Double discountPercentage;

        private LocalDate validFrom;

        private LocalDate validUntil;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class UpdateVendorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        private String vendorName;

        private String contactPerson;

        private String email;

        private String phone;

        private Vendor.Address billingAddress;

        private Vendor.Address shippingAddress;

        private String notes;

        private List<String> tags;

        private String website;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateVendorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeactivateVendorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SuspendVendorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class BlacklistVendorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetPreferredVendorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        @NotNull(message = "Preferred flag is required")
        private Boolean preferred;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdatePaymentTermsCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        private String paymentTerms;

        @NotNull(message = "Payment days is required")
        @Positive(message = "Payment days must be positive")
        private Integer paymentDays;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateBankInfoCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        private String bankAccountNumber;

        private String bankRoutingNumber;

        private String bankName;

        private String bankAccountType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddTagCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        @NotBlank(message = "Tag is required")
        private String tag;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveTagCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        @NotBlank(message = "Tag is required")
        private String tag;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteVendorCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;
    }
}
