package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.model.Invoice;
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

        @NotBlank(message = "Vendor ID is required")
        private String vendorId;

        @NotBlank(message = "Vendor name is required")
        private String vendorName;

        @NotBlank(message = "Invoice number is required")
        private String invoiceNumber;

        private String purchaseOrderNumber;

        @NotNull(message = "Invoice date is required")
        private LocalDate invoiceDate;

        @NotNull(message = "Due date is required")
        private LocalDate dueDate;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;

        @NotBlank(message = "Currency is required")
        private String currency;

        private String description;

        private String notes;

        private String internalReference;

        private String department;

        private String costCenter;

        private String projectId;

        private List<Invoice.InvoiceLineItem> lineItems;

        private List<String> attachments;

        private List<String> tags;

        private Boolean requiresApproval;

        private String glAccount;

        private String taxCode;

        private BigDecimal taxRate;

        private Boolean taxIncluded;

        private LocalDate discountValidUntil;

        private BigDecimal discountPercentage;

        private String paymentTerms;

        @NotBlank(message = "Submitted by is required")
        private String submittedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        private String description;

        private BigDecimal amount;

        private LocalDate dueDate;

        private String notes;

        private String internalReference;

        private String department;

        private String costCenter;

        private String projectId;

        private List<String> tags;

        private List<String> attachments;

        private BigDecimal taxRate;

        private BigDecimal discountPercentage;

        private LocalDate discountValidUntil;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SubmitInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ApproveInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotBlank(message = "Approver is required")
        private String approver;

        @NotNull(message = "Approval level is required")
        private Invoice.ApprovalLevel approvalLevel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RejectInvoiceCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotBlank(message = "Rejecter is required")
        private String rejecter;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsPaidCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        private String paymentReference;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsPartiallyPaidCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        private String paymentReference;

        @NotNull(message = "Amount paid is required")
        @Positive(message = "Amount paid must be positive")
        private BigDecimal amountPaid;
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
    class AddAttachmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotBlank(message = "Attachment URL is required")
        private String attachmentUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CalculateTaxCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotNull(message = "Tax rate is required")
        private BigDecimal taxRate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CalculateDiscountCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Invoice ID is required")
        private String invoiceId;

        @NotNull(message = "Discount percentage is required")
        private BigDecimal discountPercentage;
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
