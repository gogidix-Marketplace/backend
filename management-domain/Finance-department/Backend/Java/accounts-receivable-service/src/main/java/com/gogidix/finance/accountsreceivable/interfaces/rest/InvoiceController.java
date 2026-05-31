package com.gogidix.finance.accountsreceivable.interfaces.rest;

import com.gogidix.finance.accountsreceivable.application.dto.response.InvoiceResponseDto;
import com.gogidix.finance.accountsreceivable.application.service.InvoiceCommandService;
import com.gogidix.finance.accountsreceivable.application.service.InvoiceQueryService;
import com.gogidix.finance.accountsreceivable.domain.model.Invoice;
import com.gogidix.finance.accountsreceivable.domain.port.in.InvoiceCommand;
import com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder;
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
            request.customerId,
            request.customerName,
            request.invoiceNumber,
            request.invoiceType,
            request.invoiceDate,
            request.dueDate,
            request.currency,
            request.lineItems,
            request.salesDate,
            request.purchaseOrderNumber,
            request.customerEmail,
            request.billingAddressLine1,
            request.billingAddressLine2,
            request.billingCity,
            request.billingState,
            request.billingPostalCode,
            request.billingCountry,
            request.shippingAddressLine1,
            request.shippingAddressLine2,
            request.shippingCity,
            request.shippingState,
            request.shippingPostalCode,
            request.shippingCountry,
            request.paymentTerms,
            request.notes,
            request.internalNotes,
            request.salesperson,
            request.projectId,
            request.departmentId,
            request.locationId,
            request.templateId,
            request.taxInclusive,
            request.taxRegistered,
            request.taxCode,
            request.taxRate,
            request.shippingAmount,
            request.discountCode,
            request.discountRate,
            request.shippingMethod,
            request.trackingNumber,
            request.recurringInvoiceId,
            request.isRecurring,
            request.parentId,
            request.customerReference,
            request.groupId,
            request.tags
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
    public ResponseEntity<List<InvoiceResponseDto>> getAllInvoices() {
        List<Invoice> invoices = invoiceQueryService.getAllForTenant();
        return ResponseEntity.ok(invoices.stream().map(this::toDto).toList());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get invoices by customer")
    public ResponseEntity<Page<InvoiceResponseDto>> getInvoicesByCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Invoice> invoices = invoiceQueryService.getByCustomer(customerId, page, size);
        return ResponseEntity.ok(invoices.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get invoices by status")
    public ResponseEntity<Page<InvoiceResponseDto>> getInvoicesByStatus(
            @Parameter(description = "Status") @PathVariable String status,
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

    @GetMapping("/pending")
    @Operation(summary = "Get pending invoices")
    public ResponseEntity<Page<InvoiceResponseDto>> getPendingInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Invoice> invoices = invoiceQueryService.getPendingInvoices(page, size);
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
            request.customerEmail,
            request.dueDate,
            request.paymentTerms,
            request.notes,
            request.internalNotes,
            request.salesperson,
            request.purchaseOrderNumber,
            request.customerReference,
            request.tags,
            request.shippingMethod,
            request.trackingNumber
        );

        Invoice invoice = invoiceCommandService.update(command);
        return ResponseEntity.ok(toDto(invoice));
    }

    @PostMapping("/{invoiceId}/send")
    @Operation(summary = "Send invoice to customer")
    public ResponseEntity<Void> sendInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody(required = false) SendInvoiceRequestDto request) {

        InvoiceCommand.SendInvoiceCommand command = new InvoiceCommand.SendInvoiceCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            request != null ? request.recipientEmail : null
        );

        invoiceCommandService.send(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/line-items")
    @Operation(summary = "Add line item to invoice")
    public ResponseEntity<Void> addLineItem(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody AddLineItemRequestDto request) {

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
            RequestContextHolder.getTenantId(),
            invoiceId,
            lineItemId
        );

        invoiceCommandService.removeLineItem(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/write-off")
    @Operation(summary = "Write off invoice")
    public ResponseEntity<Void> writeOffInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody WriteOffRequestDto request) {

        InvoiceCommand.WriteOffInvoiceCommand command = new InvoiceCommand.WriteOffInvoiceCommand(
            RequestContextHolder.getTenantId(),
            invoiceId,
            request.amount,
            request.reason
        );

        invoiceCommandService.writeOff(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/void")
    @Operation(summary = "Void invoice")
    public ResponseEntity<Void> voidInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId) {

        InvoiceCommand.VoidInvoiceCommand command = new InvoiceCommand.VoidInvoiceCommand(
            RequestContextHolder.getTenantId(), invoiceId);

        invoiceCommandService.voidInvoice(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/cancel")
    @Operation(summary = "Cancel invoice")
    public ResponseEntity<Void> cancelInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody CancelInvoiceRequestDto request) {

        InvoiceCommand.CancelInvoiceCommand command = new InvoiceCommand.CancelInvoiceCommand(
            RequestContextHolder.getTenantId(), invoiceId, request.reason);

        invoiceCommandService.cancel(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/reminder")
    @Operation(summary = "Send payment reminder")
    public ResponseEntity<Void> sendReminder(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId) {

        InvoiceCommand.SendReminderCommand command = new InvoiceCommand.SendReminderCommand(
            RequestContextHolder.getTenantId(), invoiceId);

        invoiceCommandService.sendReminder(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/finance-charge")
    @Operation(summary = "Apply finance charge")
    public ResponseEntity<Void> applyFinanceCharge(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestBody FinanceChargeRequestDto request) {

        InvoiceCommand.ApplyFinanceChargeCommand command = new InvoiceCommand.ApplyFinanceChargeCommand(
            RequestContextHolder.getTenantId(), invoiceId, request.rate);

        invoiceCommandService.applyFinanceCharge(command);
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

    @GetMapping("/summary")
    @Operation(summary = "Get invoice summary")
    public ResponseEntity<InvoiceQueryService.InvoiceSummary> getSummary(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String customerId) {

        InvoiceQueryService.InvoiceSummary summary =
            invoiceQueryService.getSummary(startDate, endDate, customerId);

        return ResponseEntity.ok(summary);
    }

    private InvoiceResponseDto toDto(Invoice invoice) {
        return InvoiceResponseDto.builder()
            .id(invoice.getId())
            .invoiceId(invoice.getInvoiceId())
            .tenantId(invoice.getTenantId())
            .customerId(invoice.getCustomerId())
            .customerName(invoice.getCustomerName())
            .customerEmail(invoice.getCustomerEmail())
            .invoiceNumber(invoice.getInvoiceNumber())
            .invoiceType(mapInvoiceType(invoice.getInvoiceType()))
            .status(mapInvoiceStatus(invoice.getStatus()))
            .invoiceDate(invoice.getInvoiceDate())
            .dueDate(invoice.getDueDate())
            .salesDate(invoice.getSalesDate())
            .purchaseOrderNumber(invoice.getPurchaseOrderNumber())
            .currency(invoice.getCurrency())
            .subtotal(invoice.getSubtotal())
            .taxAmount(invoice.getTaxAmount())
            .discountAmount(invoice.getDiscountAmount())
            .shippingAmount(invoice.getShippingAmount())
            .totalAmount(invoice.getTotalAmount())
            .amountPaid(invoice.getAmountPaid())
            .balanceDue(invoice.getBalanceDue())
            .lineItems(invoice.getLineItems() != null ?
                invoice.getLineItems().stream().map(this::mapLineItem).toList() : null)
            .billingAddressLine1(invoice.getBillingAddressLine1())
            .billingAddressLine2(invoice.getBillingAddressLine2())
            .billingCity(invoice.getBillingCity())
            .billingState(invoice.getBillingState())
            .billingPostalCode(invoice.getBillingPostalCode())
            .billingCountry(invoice.getBillingCountry())
            .shippingAddressLine1(invoice.getShippingAddressLine1())
            .shippingAddressLine2(invoice.getShippingAddressLine2())
            .shippingCity(invoice.getShippingCity())
            .shippingState(invoice.getShippingState())
            .shippingPostalCode(invoice.getShippingPostalCode())
            .shippingCountry(invoice.getShippingCountry())
            .paymentTerms(invoice.getPaymentTerms())
            .notes(invoice.getNotes())
            .internalNotes(invoice.getInternalNotes())
            .salesperson(invoice.getSalesperson())
            .projectId(invoice.getProjectId())
            .departmentId(invoice.getDepartmentId())
            .locationId(invoice.getLocationId())
            .templateId(invoice.getTemplateId())
            .taxInclusive(invoice.getTaxInclusive())
            .taxRegistered(invoice.getTaxRegistered())
            .taxCode(invoice.getTaxCode())
            .taxRate(invoice.getTaxRate())
            .discountCode(invoice.getDiscountCode())
            .discountRate(invoice.getDiscountRate())
            .shippingMethod(invoice.getShippingMethod())
            .trackingNumber(invoice.getTrackingNumber())
            .recurringInvoiceId(invoice.getRecurringInvoiceId())
            .isRecurring(invoice.getIsRecurring())
            .parentId(invoice.getParentId())
            .customerReference(invoice.getCustomerReference())
            .groupId(invoice.getGroupId())
            .tags(invoice.getTags())
            .reminderCount(invoice.getReminderCount())
            .sentAt(invoice.getSentAt())
            .viewedAt(invoice.getViewedAt())
            .approvedAt(invoice.getApprovedAt())
            .approvedBy(invoice.getApprovedBy())
            .rejectionReason(invoice.getRejectionReason())
            .daysOverdue(invoice.getDaysOverdue())
            .writeOffAmount(invoice.getWriteOffAmount())
            .writeOffDate(invoice.getWriteOffDate())
            .writeOffReason(invoice.getWriteOffReason())
            .createdAt(invoice.getCreatedAt())
            .updatedAt(invoice.getUpdatedAt())
            .build();
    }

    private InvoiceResponseDto.InvoiceTypeDto mapInvoiceType(Invoice.InvoiceType type) {
        return type != null ? InvoiceResponseDto.InvoiceTypeDto.valueOf(type.name()) : null;
    }

    private InvoiceResponseDto.InvoiceStatusDto mapInvoiceStatus(Invoice.InvoiceStatus status) {
        return status != null ? InvoiceResponseDto.InvoiceStatusDto.valueOf(status.name()) : null;
    }

    private InvoiceResponseDto.InvoiceLineItemDto mapLineItem(Invoice.InvoiceLineItem item) {
        return InvoiceResponseDto.InvoiceLineItemDto.builder()
            .lineItemId(item.getLineItemId())
            .itemId(item.getItemId())
            .itemCode(item.getItemCode())
            .description(item.getDescription())
            .quantity(item.getQuantity())
            .unitPrice(item.getUnitPrice())
            .discountAmount(item.getDiscountAmount())
            .taxAmount(item.getTaxAmount())
            .lineTotal(item.getLineTotal())
            .accountCode(item.getAccountCode())
            .taxCode(item.getTaxCode())
            .itemType(item.getItemType())
            .serviceStartDate(item.getServiceStartDate())
            .serviceEndDate(item.getServiceEndDate())
            .build();
    }

    // Request DTOs
    public static class CreateInvoiceRequestDto {
        public String customerId;
        public String customerName;
        public String invoiceNumber;
        public Invoice.InvoiceType invoiceType;
        public LocalDate invoiceDate;
        public LocalDate dueDate;
        public String currency;
        public List<Invoice.InvoiceLineItem> lineItems;
        public LocalDate salesDate;
        public String purchaseOrderNumber;
        public String customerEmail;
        public String billingAddressLine1;
        public String billingAddressLine2;
        public String billingCity;
        public String billingState;
        public String billingPostalCode;
        public String billingCountry;
        public String shippingAddressLine1;
        public String shippingAddressLine2;
        public String shippingCity;
        public String shippingState;
        public String shippingPostalCode;
        public String shippingCountry;
        public String paymentTerms;
        public String notes;
        public String internalNotes;
        public String salesperson;
        public String projectId;
        public String departmentId;
        public String locationId;
        public String templateId;
        public Boolean taxInclusive;
        public Boolean taxRegistered;
        public String taxCode;
        public BigDecimal taxRate;
        public BigDecimal shippingAmount;
        public String discountCode;
        public BigDecimal discountRate;
        public String shippingMethod;
        public String trackingNumber;
        public String recurringInvoiceId;
        public Boolean isRecurring;
        public String parentId;
        public String customerReference;
        public String groupId;
        public List<String> tags;
    }

    public static class UpdateInvoiceRequestDto {
        public String customerEmail;
        public LocalDate dueDate;
        public String paymentTerms;
        public String notes;
        public String internalNotes;
        public String salesperson;
        public String purchaseOrderNumber;
        public String customerReference;
        public List<String> tags;
        public String shippingMethod;
        public String trackingNumber;
    }

    public static class SendInvoiceRequestDto {
        public String recipientEmail;
    }

    public static class AddLineItemRequestDto {
        public Invoice.InvoiceLineItem lineItem;
    }

    public static class WriteOffRequestDto {
        public BigDecimal amount;
        public String reason;
    }

    public static class CancelInvoiceRequestDto {
        public String reason;
    }

    public static class FinanceChargeRequestDto {
        public BigDecimal rate;
    }
}
