package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.model.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

        @NotBlank(message = "Customer code is required")
        private String customerCode;

        @NotBlank(message = "Customer name is required")
        private String customerName;

        @NotNull(message = "Customer type is required")
        private Customer.CustomerType customerType;

        @NotBlank(message = "Currency is required")
        private String currency;

        @Email(message = "Invalid email format")
        private String email;

        private String phone;

        private String website;

        private String taxId;

        private String taxRegistrationNumber;

        private String billingAddressLine1;

        private String billingAddressLine2;

        private String billingCity;

        private String billingState;

        private String billingPostalCode;

        private String billingCountry;

        private String shippingAddressLine1;

        private String shippingAddressLine2;

        private String shippingCity;

        private String shippingState;

        private String shippingPostalCode;

        private String shippingCountry;

        private String paymentTerms;

        private Integer creditLimit;

        private Integer creditDays;

        private String salesRepresentative;

        private String industry;

        private String notes;

        private String defaultPaymentMethod;

        private String bankAccountNumber;

        private String bankName;

        private String bankRoutingNumber;

        private Boolean allowCredit;

        private Boolean sendElectronicInvoices;

        private String invoiceDeliveryEmail;

        private String parentCustomerId;

        private Boolean isParentCustomer;

        private List<String> tags;

        private String paymentGatewayCustomerId;

        private Boolean autoChargePaymentMethod;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateCustomerCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        private String customerName;

        private String email;

        private String phone;

        private String website;

        private String billingAddressLine1;

        private String billingAddressLine2;

        private String billingCity;

        private String billingState;

        private String billingPostalCode;

        private String billingCountry;

        private String shippingAddressLine1;

        private String shippingAddressLine2;

        private String shippingCity;

        private String shippingState;

        private String shippingPostalCode;

        private String shippingCountry;

        private String paymentTerms;

        private Integer creditLimit;

        private Integer creditDays;

        private String salesRepresentative;

        private String industry;

        private String notes;

        private String defaultPaymentMethod;

        private String bankAccountNumber;

        private String bankName;

        private String bankRoutingNumber;

        private Boolean allowCredit;

        private Boolean sendElectronicInvoices;

        private String invoiceDeliveryEmail;

        private List<String> tags;

        private String paymentGatewayCustomerId;

        private Boolean autoChargePaymentMethod;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateCustomerCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SuspendCustomerCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateCreditInfoCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotNull(message = "Credit limit is required")
        @Positive(message = "Credit limit must be positive")
        private Integer creditLimit;

        @NotNull(message = "Credit days is required")
        @Positive(message = "Credit days must be positive")
        private Integer creditDays;
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
