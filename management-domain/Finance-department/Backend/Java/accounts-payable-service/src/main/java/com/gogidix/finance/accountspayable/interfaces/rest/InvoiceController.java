package com.gogidix.finance.accountspayable.interfaces.rest;

import com.gogidix.finance.accountspayable.application.dto.response.InvoiceResponseDto;
import com.gogidix.finance.accountspayable.application.service.InvoiceCommandService;
import com.gogidix.finance.accountspayable.application.service.InvoiceQueryService;
import com.gogidix.finance.accountspayable.domain.model.Invoice;
import com.gogidix.finance.accountspayable.domain.port.in.InvoiceCommand;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Invoice REST Controller
 * Handles HTTP requests for invoice operations
 */
@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoices", description = "Invoice management endpoints")
public class InvoiceController {

    private final InvoiceCommandService invoiceCommandService;
    private final InvoiceQueryService invoiceQueryService;

    @PostMapping
    @Operation(summary = "Create a new invoice")
    public ResponseEntity<InvoiceResponseDto> createInvoice(
            @Valid @RequestBody CreateInvoiceRequestDto request) {
        InvoiceCommand.CreateInvoiceCommand command = new InvoiceCommand.CreateInvoiceCommand(
            RequestContextHolder.getTenantId(),
            request.vendorId,
            request.vendorName,
            request.invoiceNumber,
            request.purchaseOrderNumber,
            request.invoiceDate,
            request.dueDate,
            request.amount,
            request.currency,
            request.description,
            request.notes,
            request.internalReference,
            request.department,
            request.costCenter,
            request.projectId,
            request.lineItems,
            request.attachments,
            request.tags,
            request.requiresApproval,
            request.glAccount,
            request.taxCode,
            request.taxRate,
            request.taxIncluded,
            request.discountValidUntil,
            request.discountPercentage,
            request.paymentTerms,
            RequestContextHolder.getUserId()
        );

        Invoice invoice = invoiceCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(invoice));
    }

    @GetMapping("/{invoiceId}")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<InvoiceResponseDto> getInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId) {
        Invoice invoice = invoiceQueryService.getById(invoiceId);
        return ResponseEntity.ok(toDto(invoice));
    }

    @GetMapping("/number/{invoiceNumber}")
    @Operation(summary = "Get invoice by number")
    public ResponseEntity<InvoiceResponseDto> getInvoiceByNumber(
            @Parameter(description = "Invoice Number") @PathVariable String invoiceNumber) {
        Invoice invoice = invoiceQueryService.getByNumber(invoiceNumber);
        return ResponseEntity.ok(toDto(invoice));
    }

    @GetMapping
    @Operation(summary = "Get all invoices for tenant")
    public ResponseEntity<Page<InvoiceResponseDto>> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Invoice> invoices = invoiceQueryService.getAllForTenant()
            .stream()
            .collect(java.util.stream.Collectors.collectingAndThen(
                java.util.stream.Collectors.toList(),
                list -> new org.springframework.data.domain.PageImpl<>(
                    list,
                    org.springframework.data.domain.PageRequest.of(page, size),
                    list.size()
                )
            ));
        return ResponseEntity.ok(invoices.map(this::toDto));
    }

    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get invoices by vendor")
    public ResponseEntity<Page<InvoiceResponseDto>> getInvoicesByVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Invoice> invoices = invoiceQueryService.getByVendor(vendorId, page, size);
        return ResponseEntity.ok(invoices.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get invoices by status")
    public ResponseEntity<Page<InvoiceResponseDto>> getInvoicesByStatus(
            @Parameter(description = "Invoice Status") @PathVariable Invoice.InvoiceStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Invoice> invoices = invoiceQueryService.getByStatus(status, page, size);
        return ResponseEntity.ok(invoices.map(this::toDto));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue invoices")
    public ResponseEntity<Page<InvoiceResponseDto>> getOverdueInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Invoice> invoices = invoiceQueryService.getOverdueInvoices(page, size);
        return ResponseEntity.ok(invoices.map(this::toDto));
    }

    @GetMapping("/pending-approval")
    @Operation(summary = "Get invoices pending approval")
    public ResponseEntity<Page<InvoiceResponseDto>> getPendingApproval(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Invoice> invoices = invoiceQueryService.getPendingApproval(page, size);
        return ResponseEntity.ok(invoices.map(this::toDto));
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get invoices by department")
    public ResponseEntity<Page<InvoiceResponseDto>> getInvoicesByDepartment(
            @Parameter(description = "Department") @PathVariable String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Invoice> invoices = invoiceQueryService.getByDepartment(department, page, size);
        return ResponseEntity.ok(invoices.map(this::toDto));
    }

    @PutMapping("/{invoiceId}")
    @Operation(summary = "Update invoice")
    public ResponseEntity<InvoiceResponseDto> updateInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @Valid @RequestBody UpdateInvoiceRequestDto request) {
        InvoiceCommand.UpdateInvoiceCommand command = new InvoiceCommand.UpdateInvoiceCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            request.description,
            request.amount,
            request.dueDate,
            request.notes,
            request.internalReference,
            request.department,
            request.costCenter,
            request.projectId,
            request.tags,
            request.attachments,
            request.taxRate,
            request.discountPercentage,
            request.discountValidUntil
        );

        Invoice invoice = invoiceCommandService.update(command);
        return ResponseEntity.ok(toDto(invoice));
    }

    @PostMapping("/{invoiceId}/submit")
    @Operation(summary = "Submit invoice for approval")
    public ResponseEntity<Void> submitInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId) {
        InvoiceCommand.SubmitInvoiceCommand command = new InvoiceCommand.SubmitInvoiceCommand(
            RequestContextHolder.getTenantId(), invoiceId);
        invoiceCommandService.submit(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{invoiceId}/approve")
    @Operation(summary = "Approve invoice")
    public ResponseEntity<Void> approveInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody ApprovalRequestDto request) {
        InvoiceCommand.ApproveInvoiceCommand command = new InvoiceCommand.ApproveInvoiceCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            RequestContextHolder.getUserId(),
            request.approvalLevel
        );
        invoiceCommandService.approve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/reject")
    @Operation(summary = "Reject invoice")
    public ResponseEntity<Void> rejectInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody RejectionRequestDto request) {
        InvoiceCommand.RejectInvoiceCommand command = new InvoiceCommand.RejectInvoiceCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            RequestContextHolder.getUserId(),
            request.reason
        );
        invoiceCommandService.reject(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/pay")
    @Operation(summary = "Mark invoice as paid")
    public ResponseEntity<Void> markAsPaid(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody PaymentRequestDto request) {
        InvoiceCommand.MarkAsPaidCommand command = new InvoiceCommand.MarkAsPaidCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            request.paymentReference
        );
        invoiceCommandService.markAsPaid(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/partially-pay")
    @Operation(summary = "Mark invoice as partially paid")
    public ResponseEntity<Void> markAsPartiallyPaid(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody PartialPaymentRequestDto request) {
        InvoiceCommand.MarkAsPartiallyPaidCommand command = new InvoiceCommand.MarkAsPartiallyPaidCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            request.paymentReference,
            request.amountPaid
        );
        invoiceCommandService.markAsPartiallyPaid(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/cancel")
    @Operation(summary = "Cancel invoice")
    public ResponseEntity<Void> cancelInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody CancelRequestDto request) {
        InvoiceCommand.CancelInvoiceCommand command = new InvoiceCommand.CancelInvoiceCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            request.reason
        );
        invoiceCommandService.cancel(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/line-items")
    @Operation(summary = "Add line item to invoice")
    public ResponseEntity<Void> addLineItem(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody LineItemRequestDto request) {
        InvoiceCommand.AddLineItemCommand command = new InvoiceCommand.AddLineItemCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            request.lineItem
        );
        invoiceCommandService.addLineItem(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{invoiceId}/line-items/{lineItemId}")
    @Operation(summary = "Remove line item from invoice")
    public ResponseEntity<Void> removeLineItem(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @Parameter(description = "Line Item ID") @PathVariable String lineItemId) {
        InvoiceCommand.RemoveLineItemCommand command = new InvoiceCommand.RemoveLineItemCommand(
            RequestContextHolder.getTenantId(), invoiceId, lineItemId);
        invoiceCommandService.removeLineItem(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/attachments")
    @Operation(summary = "Add attachment to invoice")
    public ResponseEntity<Void> addAttachment(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody AttachmentRequestDto request) {
        InvoiceCommand.AddAttachmentCommand command = new InvoiceCommand.AddAttachmentCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            request.attachmentUrl
        );
        invoiceCommandService.addAttachment(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/calculate-tax")
    @Operation(summary = "Calculate tax for invoice")
    public ResponseEntity<Void> calculateTax(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody TaxCalculationRequestDto request) {
        InvoiceCommand.CalculateTaxCommand command = new InvoiceCommand.CalculateTaxCommand(
            RequestContextHolder.getTenantId(), invoiceId, request.taxRate);
        invoiceCommandService.calculateTax(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/calculate-discount")
    @Operation(summary = "Calculate discount for invoice")
    public ResponseEntity<Void> calculateDiscount(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody DiscountCalculationRequestDto request) {
        InvoiceCommand.CalculateDiscountCommand command = new InvoiceCommand.CalculateDiscountCommand(
            RequestContextHolder.getTenantId(), invoiceId, request.discountPercentage);
        invoiceCommandService.calculateDiscount(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{invoiceId}")
    @Operation(summary = "Delete invoice")
    public ResponseEntity<Void> deleteInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId) {
        InvoiceCommand.DeleteInvoiceCommand command = new InvoiceCommand.DeleteInvoiceCommand(
            RequestContextHolder.getTenantId(), invoiceId);
        invoiceCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    private InvoiceResponseDto toDto(Invoice invoice) {
        return InvoiceResponseDto.builder()
            .id(invoice.getId())
            .invoiceId(invoice.getInvoiceId())
            .tenantId(invoice.getTenantId())
            .vendorId(invoice.getVendorId())
            .vendorName(invoice.getVendorName())
            .invoiceNumber(invoice.getInvoiceNumber())
            .purchaseOrderNumber(invoice.getPurchaseOrderNumber())
            .invoiceDate(invoice.getInvoiceDate())
            .dueDate(invoice.getDueDate())
            .receivedDate(invoice.getReceivedDate())
            .amount(invoice.getAmount())
            .taxAmount(invoice.getTaxAmount())
            .discountAmount(invoice.getDiscountAmount())
            .netAmount(invoice.getNetAmount())
            .currency(invoice.getCurrency())
            .status(mapInvoiceStatus(invoice.getStatus()))
            .submittedBy(invoice.getSubmittedBy())
            .submittedAt(invoice.getSubmittedAt())
            .approvedBy(invoice.getApprovedBy())
            .approvedAt(invoice.getApprovedAt())
            .rejectionReason(invoice.getRejectionReason())
            .paymentReference(invoice.getPaymentReference())
            .paidAt(invoice.getPaidAt())
            .description(invoice.getDescription())
            .notes(invoice.getNotes())
            .internalReference(invoice.getInternalReference())
            .department(invoice.getDepartment())
            .costCenter(invoice.getCostCenter())
            .projectId(invoice.getProjectId())
            .lineItems(mapLineItems(invoice.getLineItems()))
            .attachments(invoice.getAttachments())
            .tags(invoice.getTags())
            .requiresApproval(invoice.getRequiresApproval())
            .approvalLevel(mapApprovalLevel(invoice.getApprovalLevel()))
            .glAccount(invoice.getGlAccount())
            .taxCode(invoice.getTaxCode())
            .taxIncluded(invoice.getTaxIncluded())
            .discountValidUntil(invoice.getDiscountValidUntil())
            .discountPercentage(invoice.getDiscountPercentage())
            .paymentTerms(invoice.getPaymentTerms())
            .createdAt(invoice.getCreatedAt())
            .updatedAt(invoice.getUpdatedAt())
            .isOverdue(invoice.isOverdue())
            .daysUntilDue(invoice.getDaysUntilDue())
            .build();
    }

    private InvoiceResponseDto.InvoiceStatusDto mapInvoiceStatus(Invoice.InvoiceStatus status) {
        return status != null ? InvoiceResponseDto.InvoiceStatusDto.valueOf(status.name()) : null;
    }

    private InvoiceResponseDto.ApprovalLevelDto mapApprovalLevel(Invoice.ApprovalLevel level) {
        return level != null ? InvoiceResponseDto.ApprovalLevelDto.valueOf(level.name()) : null;
    }

    private List<InvoiceResponseDto.InvoiceLineItemDto> mapLineItems(List<Invoice.InvoiceLineItem> items) {
        if (items == null) return null;
        return items.stream().map(item -> InvoiceResponseDto.InvoiceLineItemDto.builder()
            .lineItemId(item.getLineItemId())
            .description(item.getDescription())
            .quantity(item.getQuantity())
            .unitPrice(item.getUnitPrice())
            .amount(item.getAmount())
            .accountCode(item.getAccountCode())
            .taxCode(item.getTaxCode())
            .build()).toList();
    }

    // Request DTOs
    public static class CreateInvoiceRequestDto {
        public String vendorId;
        public String vendorName;
        public String invoiceNumber;
        public String purchaseOrderNumber;
        public LocalDate invoiceDate;
        public LocalDate dueDate;
        public BigDecimal amount;
        public String currency;
        public String description;
        public String notes;
        public String internalReference;
        public String department;
        public String costCenter;
        public String projectId;
        public List<Invoice.InvoiceLineItem> lineItems;
        public List<String> attachments;
        public List<String> tags;
        public Boolean requiresApproval;
        public String glAccount;
        public String taxCode;
        public BigDecimal taxRate;
        public Boolean taxIncluded;
        public LocalDate discountValidUntil;
        public BigDecimal discountPercentage;
        public String paymentTerms;
    }

    public static class UpdateInvoiceRequestDto {
        public String description;
        public BigDecimal amount;
        public LocalDate dueDate;
        public String notes;
        public String internalReference;
        public String department;
        public String costCenter;
        public String projectId;
        public List<String> tags;
        public List<String> attachments;
        public BigDecimal taxRate;
        public BigDecimal discountPercentage;
        public LocalDate discountValidUntil;
    }

    public static class ApprovalRequestDto {
        public Invoice.ApprovalLevel approvalLevel;
    }

    public static class RejectionRequestDto {
        public String reason;
    }

    public static class PaymentRequestDto {
        public String paymentReference;
    }

    public static class PartialPaymentRequestDto {
        public String paymentReference;
        public BigDecimal amountPaid;
    }

    public static class CancelRequestDto {
        public String reason;
    }

    public static class LineItemRequestDto {
        public Invoice.InvoiceLineItem lineItem;
    }

    public static class AttachmentRequestDto {
        public String attachmentUrl;
    }

    public static class TaxCalculationRequestDto {
        public BigDecimal taxRate;
    }

    public static class DiscountCalculationRequestDto {
        public BigDecimal discountPercentage;
    }
}
