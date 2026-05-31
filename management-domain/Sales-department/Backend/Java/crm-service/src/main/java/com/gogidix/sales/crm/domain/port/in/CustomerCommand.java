package com.gogidix.sales.crm.domain.port.in;

import com.gogidix.sales.crm.domain.model.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Customer Commands (Input Port)
 * Defines the input commands for customer operations
 */
public interface CustomerCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateCustomerCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Created by is required")
        private String createdBy;

        @NotBlank(message = "Company name is required")
        @Size(min = 2, max = 255, message = "Company name must be between 2 and 255 characters")
        private String companyName;

        private String industry;

        private Customer.CustomerSegment segment;

        @NotNull(message = "Lifecycle stage is required")
        private Customer.CustomerLifecycleStage lifecycleStage;

        private String leadSource;

        private String website;

        private String description;

        private Integer employeeCount;

        @Positive(message = "Annual revenue must be positive")
        private Double annualRevenue;

        private String ownerId;

        private String ownerName;

        private String territory;

        private String billingAddressStreet;

        private String billingAddressCity;

        private String billingAddressState;

        private String billingAddressPostalCode;

        private String billingAddressCountry;

        private String shippingAddressStreet;

        private String shippingAddressCity;

        private String shippingAddressState;

        private String shippingAddressPostalCode;

        private String shippingAddressCountry;

        private String phoneNumber;

        private String email;

        private String accountNumber;

        private Customer.AccountType accountType;

        private String parentAccountId;

        private String taxId;

        private String paymentTerms;

        private String currency;

        private Double creditLimit;

        private List<String> tags;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateCustomerCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        private String companyName;

        private String industry;

        private Customer.CustomerSegment segment;

        private String description;

        private String website;

        private Integer employeeCount;

        private Double annualRevenue;

        private String phoneNumber;

        private String email;

        private String territory;

        private Integer satisfactionScore;

        private String paymentTerms;

        private String currency;

        private Double creditLimit;

        private List<String> tags;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AdvanceLifecycleStageCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotNull(message = "New lifecycle stage is required")
        private Customer.CustomerLifecycleStage newStage;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AssignOwnerCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "Owner ID is required")
        private String ownerId;

        private String ownerName;

        private String territory;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetFollowUpDateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotNull(message = "Follow-up date is required")
        private LocalDate followUpDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddTagCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "Tag is required")
        private String tag;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveTagCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "Tag is required")
        private String tag;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsChurnedCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        private String reason;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ReactivateCustomerCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetParentAccountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        private String parentAccountId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteCustomerCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;
    }
}
