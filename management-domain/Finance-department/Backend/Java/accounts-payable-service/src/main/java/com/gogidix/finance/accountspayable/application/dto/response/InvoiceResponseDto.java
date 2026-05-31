package com.gogidix.finance.accountspayable.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.finance.accountspayable.domain.model.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Invoice Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto {

    private String id;

    private String invoiceId;

    private String tenantId;

    private String vendorId;

    private String vendorName;

    private String vendorCode;

    private String invoiceNumber;

    private String purchaseOrderNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate receivedDate;

    private BigDecimal amount;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private BigDecimal netAmount;

    private String currency;

    private InvoiceStatusDto status;

    private String submittedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant submittedAt;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant approvedAt;

    private String rejectionReason;

    private String paymentReference;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant paidAt;

    private String description;

    private String notes;

    private String internalReference;

    private String department;

    private String costCenter;

    private String projectId;

    private List<InvoiceLineItemDto> lineItems;

    private List<String> attachments;

    private List<String> tags;

    private Boolean requiresApproval;

    private ApprovalLevelDto approvalLevel;

    private String glAccount;

    private String taxCode;

    private Boolean taxIncluded;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate discountValidUntil;

    private BigDecimal discountPercentage;

    private String paymentTerms;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    private Boolean isOverdue;

    private Long daysUntilDue;

    public enum InvoiceStatusDto {
        DRAFT,
        PENDING,
        APPROVED,
        PAID,
        CANCELLED,
        REJECTED,
        OVERDUE,
        PARTIALLY_PAID
    }

    public enum ApprovalLevelDto {
        NONE,
        MANAGER,
        FINANCE,
        EXECUTIVE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceLineItemDto {
        private String lineItemId;
        private String description;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private BigDecimal amount;
        private String accountCode;
        private String taxCode;
    }
}
