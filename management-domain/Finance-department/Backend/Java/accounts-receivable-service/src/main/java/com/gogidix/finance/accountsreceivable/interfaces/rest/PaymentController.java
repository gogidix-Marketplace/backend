package com.gogidix.finance.accountsreceivable.interfaces.rest;

import com.gogidix.finance.accountsreceivable.application.dto.response.PaymentResponseDto;
import com.gogidix.finance.accountsreceivable.application.service.PaymentCommandService;
import com.gogidix.finance.accountsreceivable.application.service.PaymentQueryService;
import com.gogidix.finance.accountsreceivable.domain.model.Payment;
import com.gogidix.finance.accountsreceivable.domain.port.in.PaymentCommand;
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
 * Payment REST Controller
 * Handles HTTP requests for payment operations
 */
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment management endpoints")
public class PaymentController {

    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;

    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<PaymentResponseDto> createPayment(
            @Valid @RequestBody CreatePaymentRequestDto request) {
        PaymentCommand.CreatePaymentCommand command = new PaymentCommand.CreatePaymentCommand(
            RequestContextHolder.getTenantId(),
            request.customerId,
            request.customerName,
            request.invoiceId,
            request.invoiceNumber,
            request.paymentType,
            request.amount,
            request.currency,
            request.paymentDate,
            request.paymentMethod,
            request.referenceNumber,
            request.bankAccount,
            request.checkNumber,
            request.creditCardNumber,
            request.description,
            request.notes,
            request.depositDate,
            request.depositSlipNumber,
            request.batchId,
            request.exchangeRate,
            request.baseCurrency,
            null, // gatewayCustomerId
            request.tags
        );

        Payment payment = paymentCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(payment));
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponseDto> getPayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId) {
        Payment payment = paymentQueryService.getById(paymentId);
        return ResponseEntity.ok(toDto(payment));
    }

    @GetMapping
    @Operation(summary = "Get all payments for tenant")
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        List<Payment> payments = paymentQueryService.getAllForTenant();
        return ResponseEntity.ok(payments.stream().map(this::toDto).toList());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get payments by customer")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Payment> payments = paymentQueryService.getByCustomer(customerId, page, size);
        return ResponseEntity.ok(payments.map(this::toDto));
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Get payments by invoice")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByInvoice(
            @Parameter(description = "Invoice ID") @PathVariable String invoiceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Payment> payments = paymentQueryService.getByInvoice(invoiceId, page, size);
        return ResponseEntity.ok(payments.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByStatus(
            @Parameter(description = "Status") @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Payment> payments = paymentQueryService.getByStatus(status, page, size);
        return ResponseEntity.ok(payments.map(this::toDto));
    }

    @GetMapping("/unallocated")
    @Operation(summary = "Get unallocated payments")
    public ResponseEntity<Page<PaymentResponseDto>> getUnallocatedPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Payment> payments = paymentQueryService.getUnallocatedPayments(page, size);
        return ResponseEntity.ok(payments.map(this::toDto));
    }

    @PostMapping("/{paymentId}/complete")
    @Operation(summary = "Complete payment")
    public ResponseEntity<Void> completePayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody CompletePaymentRequestDto request) {

        PaymentCommand.CompletePaymentCommand command = new PaymentCommand.CompletePaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.transactionId
        );

        paymentCommandService.complete(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/fail")
    @Operation(summary = "Fail payment")
    public ResponseEntity<Void> failPayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody FailPaymentRequestDto request) {

        PaymentCommand.FailPaymentCommand command = new PaymentCommand.FailPaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.reason
        );

        paymentCommandService.fail(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/apply")
    @Operation(summary = "Apply payment to invoice")
    public ResponseEntity<Void> applyPayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody ApplyPaymentRequestDto request) {

        PaymentCommand.ApplyPaymentCommand command = new PaymentCommand.ApplyPaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.invoiceId,
            request.invoiceNumber,
            request.amount
        );

        paymentCommandService.apply(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/reverse")
    @Operation(summary = "Reverse payment")
    public ResponseEntity<Void> reversePayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody ReversePaymentRequestDto request) {

        PaymentCommand.ReversePaymentCommand command = new PaymentCommand.ReversePaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.reason
        );

        paymentCommandService.reverse(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/refund")
    @Operation(summary = "Refund payment")
    public ResponseEntity<Void> refundPayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody RefundPaymentRequestDto request) {

        PaymentCommand.RefundPaymentCommand command = new PaymentCommand.RefundPaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.amount,
            request.reason
        );

        paymentCommandService.refund(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/reconcile")
    @Operation(summary = "Reconcile payment")
    public ResponseEntity<Void> reconcilePayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId) {

        PaymentCommand.ReconcilePaymentCommand command = new PaymentCommand.ReconcilePaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            RequestContextHolder.getUserId().orElse("system")
        );

        paymentCommandService.reconcile(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{paymentId}")
    @Operation(summary = "Delete payment")
    public ResponseEntity<Void> deletePayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId) {

        PaymentCommand.DeletePaymentCommand command = new PaymentCommand.DeletePaymentCommand(
            RequestContextHolder.getTenantId(), paymentId);

        paymentCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get payment summary")
    public ResponseEntity<PaymentQueryService.PaymentSummary> getSummary(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String customerId) {

        PaymentQueryService.PaymentSummary summary =
            paymentQueryService.getSummary(startDate, endDate, customerId);

        return ResponseEntity.ok(summary);
    }

    private PaymentResponseDto toDto(Payment payment) {
        return PaymentResponseDto.builder()
            .id(payment.getId())
            .paymentId(payment.getPaymentId())
            .tenantId(payment.getTenantId())
            .paymentNumber(payment.getPaymentNumber())
            .customerId(payment.getCustomerId())
            .customerName(payment.getCustomerName())
            .invoiceId(payment.getInvoiceId())
            .invoiceNumber(payment.getInvoiceNumber())
            .paymentType(mapPaymentType(payment.getPaymentType()))
            .status(mapPaymentStatus(payment.getStatus()))
            .amount(payment.getAmount())
            .currency(payment.getCurrency())
            .paymentDate(payment.getPaymentDate())
            .paymentMethod(payment.getPaymentMethod())
            .referenceNumber(payment.getReferenceNumber())
            .bankAccount(payment.getBankAccount())
            .transactionId(payment.getTransactionId())
            .checkNumber(payment.getCheckNumber())
            .creditCardNumber(payment.getCreditCardNumber())
            .description(payment.getDescription())
            .notes(payment.getNotes())
            .depositDate(payment.getDepositDate())
            .depositSlipNumber(payment.getDepositSlipNumber())
            .batchId(payment.getBatchId())
            .reconciled(payment.getReconciled())
            .reconciledAt(payment.getReconciledAt())
            .reconciledBy(payment.getReconciledBy())
            .clearedDate(payment.getClearedDate())
            .autoApplied(payment.getAutoApplied())
            .allocations(payment.getAllocations() != null ?
                payment.getAllocations().stream().map(this::mapAllocation).toList() : null)
            .unappliedAmount(payment.getUnappliedAmount())
            .exchangeRate(payment.getExchangeRate())
            .baseCurrency(payment.getBaseCurrency())
            .baseCurrencyAmount(payment.getBaseCurrencyAmount())
            .gatewayTransactionId(payment.getGatewayTransactionId())
            .gatewayResponseCode(payment.getGatewayResponseCode())
            .gatewayResponseMessage(payment.getGatewayResponseMessage())
            .processedAt(payment.getProcessedAt())
            .processedBy(payment.getProcessedBy())
            .approvedBy(payment.getApprovedBy())
            .approvedAt(payment.getApprovedAt())
            .rejectionReason(payment.getRejectionReason())
            .refundAmount(payment.getRefundAmount())
            .refundedAt(payment.getRefundedAt())
            .refundReason(payment.getRefundReason())
            .parentId(payment.getParentId())
            .isReversal(payment.getIsReversal())
            .tags(payment.getTags())
            .createdAt(payment.getCreatedAt())
            .updatedAt(payment.getUpdatedAt())
            .build();
    }

    private PaymentResponseDto.PaymentTypeDto mapPaymentType(Payment.PaymentType type) {
        return type != null ? PaymentResponseDto.PaymentTypeDto.valueOf(type.name()) : null;
    }

    private PaymentResponseDto.PaymentStatusDto mapPaymentStatus(Payment.PaymentStatus status) {
        return status != null ? PaymentResponseDto.PaymentStatusDto.valueOf(status.name()) : null;
    }

    private PaymentResponseDto.PaymentAllocationDto mapAllocation(Payment.PaymentAllocation allocation) {
        return PaymentResponseDto.PaymentAllocationDto.builder()
            .allocationId(allocation.getAllocationId())
            .invoiceId(allocation.getInvoiceId())
            .invoiceNumber(allocation.getInvoiceNumber())
            .allocatedAmount(allocation.getAllocatedAmount())
            .allocationDate(allocation.getAllocationDate())
            .notes(allocation.getNotes())
            .build();
    }

    // Request DTOs
    public static class CreatePaymentRequestDto {
        public String customerId;
        public String customerName;
        public String invoiceId;
        public String invoiceNumber;
        public Payment.PaymentType paymentType;
        public BigDecimal amount;
        public String currency;
        public LocalDate paymentDate;
        public String paymentMethod;
        public String referenceNumber;
        public String bankAccount;
        public String checkNumber;
        public String creditCardNumber;
        public String description;
        public String notes;
        public LocalDate depositDate;
        public String depositSlipNumber;
        public String batchId;
        public String exchangeRate;
        public String baseCurrency;
        public List<String> tags;
    }

    public static class CompletePaymentRequestDto {
        public String transactionId;
    }

    public static class FailPaymentRequestDto {
        public String reason;
    }

    public static class ApplyPaymentRequestDto {
        public String invoiceId;
        public String invoiceNumber;
        public BigDecimal amount;
    }

    public static class ReversePaymentRequestDto {
        public String reason;
    }

    public static class RefundPaymentRequestDto {
        public BigDecimal amount;
        public String reason;
    }
}
