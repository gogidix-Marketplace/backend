package com.gogidix.finance.accountsreceivable.domain.port.in;

import com.gogidix.finance.accountsreceivable.domain.model.Invoice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Invoice Commands (Input Port)
 * Defines the input commands for invoice operations
 */
public interface InvoiceCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "Customer name is required")
        private String customerName;

        @NotBlank(message = "Invoice number is required")
        private String invoiceNumber;

        @NotNull(message = "Invoice type is required")
        private Invoice.InvoiceType invoiceType;

        @NotNull(message = "Invoice date is required")
        private LocalDate invoiceDate;

        @NotNull(message = "Due date is required")
        private LocalDate dueDate;

        @NotBlank(message = "Currency is required")
        private String currency;

        @NotNull(message = "Line items are required")
        private List<Invoice.InvoiceLineItem> lineItems;

        private LocalDate salesDate;

        private String purchaseOrderNumber;

        private String customerEmail;

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

        private String notes;

        private String internalNotes;

        private String salesperson;

        private String projectId;

        private String departmentId;

        private String locationId;

        private String templateId;

        private Boolean taxInclusive;

        private Boolean taxRegistered;

        private String taxCode;

        private BigDecimal taxRate;

        private BigDecimal shippingAmount;

        private String discountCode;

        private BigDecimal discountRate;

        private String shippingMethod;

        private String trackingNumber;

        private String recurringInvoiceId;

        private Boolean isRecurring;

        private String parentId;

        private String customerReference;

        private String groupId;

        private List<String> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        private String customerEmail;

        private LocalDate dueDate;

        private String paymentTerms;

        private String notes;

        private String internalNotes;

        private String salesperson;

        private String purchaseOrderNumber;

        private String customerReference;

        private List<String> tags;

        private String shippingMethod;

        private String trackingNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SendInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        private String recipientEmail;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddLineItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotNull(message = "Line item is required")
        private Invoice.InvoiceLineItem lineItem;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveLineItemCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotBlank(message = "Line item ID is required")
        private String lineItemId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class WriteOffInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotNull(message = "Write off amount is required")
        @Positive(message = "Write off amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class VoidInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SendReminderCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApplyFinanceChargeCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotNull(message = "Finance charge rate is required")
        private BigDecimal rate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;
    }
}
