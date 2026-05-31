package com.gogidix.finance.accountspayable.interfaces.rest;

import com.gogidix.finance.accountspayable.application.dto.response.PaymentResponseDto;
import com.gogidix.finance.accountspayable.application.service.PaymentCommandService;
import com.gogidix.finance.accountspayable.application.service.PaymentQueryService;
import com.gogidix.finance.accountspayable.domain.model.Payment;
import com.gogidix.finance.accountspayable.domain.port.in.PaymentCommand;
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
            request.vendorId,
            request.vendorName,
            request.invoiceIds,
            request.amount,
            request.currency,
            request.paymentMethod,
            request.paymentDate,
            request.description,
            request.notes,
            request.bankAccountNumber,
            request.bankRoutingNumber,
            request.checkNumber,
            request.batchId,
            request.feeAmount,
            request.exchangeRate,
            request.originalCurrency,
            request.originalAmount,
            request.attachmentUrl,
            RequestContextHolder.getUserId()
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
    public ResponseEntity<Page<PaymentResponseDto>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<Payment> payments = paymentQueryService.getAllForTenant();
        Page<Payment> pageResult = new org.springframework.data.domain.PageImpl<>(
            payments,
            org.springframework.data.domain.PageRequest.of(page, size),
            payments.size()
        );
        return ResponseEntity.ok(pageResult.map(this::toDto));
    }

    @GetMapping("/vendor/{vendorId}")
    @Operation(summary = "Get payments by vendor")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByVendor(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payment> payments = paymentQueryService.getByVendor(vendorId, page, size);
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
            @Parameter(description = "Payment Status") @PathVariable Payment.PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payment> payments = paymentQueryService.getByStatus(status, page, size);
        return ResponseEntity.ok(payments.map(this::toDto));
    }

    @GetMapping("/method/{paymentMethod}")
    @Operation(summary = "Get payments by method")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByMethod(
            @Parameter(description = "Payment Method") @PathVariable Payment.PaymentMethod paymentMethod,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payment> payments = paymentQueryService.getByMethod(paymentMethod, page, size);
        return ResponseEntity.ok(payments.map(this::toDto));
    }

    @GetMapping("/scheduled")
    @Operation(summary = "Get scheduled payments")
    public ResponseEntity<Page<PaymentResponseDto>> getScheduledPayments(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payment> payments = paymentQueryService.getScheduledPayments(startDate, endDate, page, size);
        return ResponseEntity.ok(payments.map(this::toDto));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending payments")
    public ResponseEntity<Page<PaymentResponseDto>> getPendingPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payment> payments = paymentQueryService.getPendingPayments(page, size);
        return ResponseEntity.ok(payments.map(this::toDto));
    }

    @GetMapping("/failed")
    @Operation(summary = "Get failed payments")
    public ResponseEntity<Page<PaymentResponseDto>> getFailedPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payment> payments = paymentQueryService.getFailedPayments(page, size);
        return ResponseEntity.ok(payments.map(this::toDto));
    }

    @PostMapping("/{paymentId}/schedule")
    @Operation(summary = "Schedule payment")
    public ResponseEntity<Void> schedulePayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody SchedulePaymentRequestDto request) {
        PaymentCommand.SchedulePaymentCommand command = new PaymentCommand.SchedulePaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.scheduledDate
        );
        paymentCommandService.schedule(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/process")
    @Operation(summary = "Process payment")
    public ResponseEntity<Void> processPayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody ProcessPaymentRequestDto request) {
        PaymentCommand.ProcessPaymentCommand command = new PaymentCommand.ProcessPaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            RequestContextHolder.getUserId(),
            request.paymentReference
        );
        paymentCommandService.process(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/complete")
    @Operation(summary = "Complete payment")
    public ResponseEntity<Void> completePayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody CompletePaymentRequestDto request) {
        PaymentCommand.CompletePaymentCommand command = new PaymentCommand.CompletePaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.transactionReference
        );
        paymentCommandService.complete(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{paymentId}/fail")
    @Operation(summary = "Mark payment as failed")
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

    @PostMapping("/{paymentId}/cancel")
    @Operation(summary = "Cancel payment")
    public ResponseEntity<Void> cancelPayment(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody CancelPaymentRequestDto request) {
        PaymentCommand.CancelPaymentCommand command = new PaymentCommand.CancelPaymentCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            RequestContextHolder.getUserId(),
            request.reason
        );
        paymentCommandService.cancel(command);
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

    @PostMapping("/{paymentId}/allocations")
    @Operation(summary = "Add allocation to payment")
    public ResponseEntity<Void> addAllocation(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody AllocationRequestDto request) {
        PaymentCommand.AddAllocationCommand command = new PaymentCommand.AddAllocationCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.invoiceId,
            request.invoiceNumber,
            request.amount
        );
        paymentCommandService.addAllocation(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{paymentId}/payment-method")
    @Operation(summary = "Set payment method details")
    public ResponseEntity<Void> setPaymentMethodDetails(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody PaymentMethodDetailsRequestDto request) {
        PaymentCommand.SetPaymentMethodDetailsCommand command = new PaymentCommand.SetPaymentMethodDetailsCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.bankAccountNumber,
            request.bankRoutingNumber,
            request.checkNumber
        );
        paymentCommandService.setPaymentMethodDetails(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{paymentId}/fee")
    @Operation(summary = "Set payment fee")
    public ResponseEntity<Void> setFee(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody FeeRequestDto request) {
        PaymentCommand.SetFeeCommand command = new PaymentCommand.SetFeeCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.feeAmount
        );
        paymentCommandService.setFee(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{paymentId}/currency-conversion")
    @Operation(summary = "Set currency conversion")
    public ResponseEntity<Void> setCurrencyConversion(
            @Parameter(description = "Payment ID") @PathVariable String paymentId,
            @RequestBody CurrencyConversionRequestDto request) {
        PaymentCommand.SetCurrencyConversionCommand command = new PaymentCommand.SetCurrencyConversionCommand(
            RequestContextHolder.getTenantId(),
            paymentId,
            request.exchangeRate,
            request.originalCurrency,
            request.originalAmount
        );
        paymentCommandService.setCurrencyConversion(command);
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

    private PaymentResponseDto toDto(Payment payment) {
        return PaymentResponseDto.builder()
            .id(payment.getId())
            .paymentId(payment.getPaymentId())
            .tenantId(payment.getTenantId())
            .vendorId(payment.getVendorId())
            .vendorName(payment.getVendorName())
            .invoiceId(payment.getInvoiceId())
            .invoiceNumber(payment.getInvoiceNumber())
            .amount(payment.getAmount())
            .currency(payment.getCurrency())
            .status(mapPaymentStatus(payment.getStatus()))
            .paymentMethod(mapPaymentMethod(payment.getPaymentMethod()))
            .paymentReference(payment.getPaymentReference())
            .paymentDate(payment.getPaymentDate())
            .scheduledDate(payment.getScheduledDate())
            .processedAt(payment.getProcessedAt())
            .processedBy(payment.getProcessedBy())
            .transactionReference(payment.getTransactionReference())
            .description(payment.getDescription())
            .notes(payment.getNotes())
            .batchId(payment.getBatchId())
            .approvalReference(payment.getApprovalReference())
            .rejectionReason(payment.getRejectionReason())
            .cancelledAt(payment.getCancelledAt())
            .cancelledBy(payment.getCancelledBy())
            .cancellationReason(payment.getCancellationReason())
            .invoiceIds(payment.getInvoiceIds())
            .allocations(mapAllocations(payment.getAllocations()))
            .feeAmount(payment.getFeeAmount())
            .exchangeRate(payment.getExchangeRate())
            .originalCurrency(payment.getOriginalCurrency())
            .originalAmount(payment.getOriginalAmount())
            .attachmentUrl(payment.getAttachmentUrl())
            .createdAt(payment.getCreatedAt())
            .updatedAt(payment.getUpdatedAt())
            .totalAmount(payment.getTotalAmount())
            .build();
    }

    private PaymentResponseDto.PaymentStatusDto mapPaymentStatus(Payment.PaymentStatus status) {
        return status != null ? PaymentResponseDto.PaymentStatusDto.valueOf(status.name()) : null;
    }

    private PaymentResponseDto.PaymentMethodDto mapPaymentMethod(Payment.PaymentMethod method) {
        return method != null ? PaymentResponseDto.PaymentMethodDto.valueOf(method.name()) : null;
    }

    private List<PaymentResponseDto.PaymentAllocationDto> mapAllocations(List<Payment.PaymentAllocation> allocations) {
        if (allocations == null) return null;
        return allocations.stream().map(alloc -> PaymentResponseDto.PaymentAllocationDto.builder()
            .invoiceId(alloc.getInvoiceId())
            .invoiceNumber(alloc.getInvoiceNumber())
            .amount(alloc.getAmount())
            .allocationDate(alloc.getAllocationDate())
            .build()).toList();
    }

    // Request DTOs
    public static class CreatePaymentRequestDto {
        public String vendorId;
        public String vendorName;
        public List<String> invoiceIds;
        public BigDecimal amount;
        public String currency;
        public Payment.PaymentMethod paymentMethod;
        public LocalDate paymentDate;
        public String description;
        public String notes;
        public String bankAccountNumber;
        public String bankRoutingNumber;
        public String checkNumber;
        public String batchId;
        public BigDecimal feeAmount;
        public String exchangeRate;
        public String originalCurrency;
        public BigDecimal originalAmount;
        public String attachmentUrl;
    }

    public static class SchedulePaymentRequestDto {
        public LocalDate scheduledDate;
    }

    public static class ProcessPaymentRequestDto {
        public String paymentReference;
    }

    public static class CompletePaymentRequestDto {
        public String transactionReference;
    }

    public static class FailPaymentRequestDto {
        public String reason;
    }

    public static class CancelPaymentRequestDto {
        public String reason;
    }

    public static class ReversePaymentRequestDto {
        public String reason;
    }

    public static class AllocationRequestDto {
        public String invoiceId;
        public String invoiceNumber;
        public BigDecimal amount;
    }

    public static class PaymentMethodDetailsRequestDto {
        public String bankAccountNumber;
        public String bankRoutingNumber;
        public String checkNumber;
    }

    public static class FeeRequestDto {
        public BigDecimal feeAmount;
    }

    public static class CurrencyConversionRequestDto {
        public String exchangeRate;
        public String originalCurrency;
        public BigDecimal originalAmount;
    }
}
