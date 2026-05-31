package com.gogidix.finance.accountsreceivable.application.service;

import com.gogidix.finance.accountsreceivable.domain.model.Payment;
import com.gogidix.finance.accountsreceivable.domain.repository.PaymentRepository;
import com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException;
import com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Payment Query Service
 * Handles all read operations for payments
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentQueryService {

    private final PaymentRepository paymentRepository;

    public Payment getById(String paymentId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching payment: {} for tenant: {}", paymentId, tenantId);

        return paymentRepository.findByPaymentIdAndTenantId(paymentId, tenantId)
            .orElseThrow(() -> new NotFoundException("Payment", paymentId));
    }

    public Page<Payment> getByCustomer(String customerId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching payments for customer: {} in tenant: {}", customerId, tenantId);

        List<Payment> payments = paymentRepository.findByTenantIdAndCustomerId(tenantId, customerId);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getByInvoice(String invoiceId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching payments for invoice: {} in tenant: {}", invoiceId, tenantId);

        List<Payment> payments = paymentRepository.findByTenantIdAndInvoiceId(tenantId, invoiceId);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getByStatus(String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching payments by status: {} for tenant: {}", status, tenantId);

        Payment.PaymentStatus statusEnum = Payment.PaymentStatus.valueOf(status);
        List<Payment> payments = paymentRepository.findByTenantIdAndStatus(tenantId, statusEnum);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getByDateRange(LocalDate startDate, LocalDate endDate,
                                         List<String> statuses, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching payments for date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        List<Payment> payments = paymentRepository.findByTenantIdAndPaymentDateBetween(
            tenantId, startDate, endDate);

        if (statuses != null && !statuses.isEmpty()) {
            List<Payment.PaymentStatus> statusEnums = statuses.stream()
                .map(Payment.PaymentStatus::valueOf)
                .toList();
            payments = payments.stream()
                .filter(p -> statusEnums.contains(p.getStatus()))
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getByPaymentMethod(String paymentMethod, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching payments by method: {} for tenant: {}", paymentMethod, tenantId);

        List<Payment> payments = paymentRepository.findByTenantIdAndPaymentMethod(tenantId, paymentMethod);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getUnallocatedPayments(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching unallocated payments for tenant: {}", tenantId);

        List<Payment> payments = paymentRepository.findUnallocatedPaymentsByTenantId(tenantId);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> search(String searchTerm, String customerId, String status,
                                 String paymentMethod, LocalDate startDate, LocalDate endDate,
                                 BigDecimal minAmount, BigDecimal maxAmount,
                                 int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Searching payments for tenant: {} with term: {}", tenantId, searchTerm);

        List<Payment> payments;

        if (customerId != null && !customerId.isBlank()) {
            payments = paymentRepository.findByTenantIdAndCustomerId(tenantId, customerId);
        } else {
            payments = paymentRepository.findByTenantId(tenantId);
        }

        // Filter by status
        if (status != null && !status.isBlank()) {
            Payment.PaymentStatus statusEnum = Payment.PaymentStatus.valueOf(status);
            payments = payments.stream()
                .filter(p -> p.getStatus() == statusEnum)
                .toList();
        }

        // Filter by payment method
        if (paymentMethod != null && !paymentMethod.isBlank()) {
            payments = payments.stream()
                .filter(p -> paymentMethod.equals(p.getPaymentMethod()))
                .toList();
        }

        // Filter by date range
        if (startDate != null && endDate != null) {
            payments = payments.stream()
                .filter(p -> p.getPaymentDate() != null &&
                    !p.getPaymentDate().isBefore(startDate) &&
                    !p.getPaymentDate().isAfter(endDate))
                .toList();
        }

        // Filter by amount range
        if (minAmount != null) {
            payments = payments.stream()
                .filter(p -> p.getAmount() != null && p.getAmount().compareTo(minAmount) >= 0)
                .toList();
        }
        if (maxAmount != null) {
            payments = payments.stream()
                .filter(p -> p.getAmount() != null && p.getAmount().compareTo(maxAmount) <= 0)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public List<Payment> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all payments for tenant: {}", tenantId);

        return paymentRepository.findByTenantId(tenantId);
    }

    public List<Payment> getReconciledPayments() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching reconciled payments for tenant: {}", tenantId);

        return paymentRepository.findReconciledPaymentsByTenantId(tenantId);
    }

    public List<Payment> getUnreconciledPayments() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching unreconciled payments for tenant: {}", tenantId);

        return paymentRepository.findUnreconciledPaymentsByTenantId(tenantId);
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return paymentRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        Payment.PaymentStatus statusEnum = Payment.PaymentStatus.valueOf(status);
        return paymentRepository.countByTenantIdAndStatus(tenantId, statusEnum);
    }

    public BigDecimal sumAmountByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        Payment.PaymentStatus statusEnum = Payment.PaymentStatus.valueOf(status);
        return paymentRepository.sumAmountByTenantIdAndStatus(tenantId, statusEnum);
    }

    public BigDecimal sumUnappliedAmount() {
        String tenantId = RequestContextHolder.getTenantId();
        return paymentRepository.sumUnappliedAmountByTenantId(tenantId);
    }

    public PaymentSummary getSummary(LocalDate startDate, LocalDate endDate, String customerId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching payment summary for tenant: {}", tenantId);

        List<Payment> payments;

        if (customerId != null && !customerId.isBlank()) {
            payments = paymentRepository.findByTenantIdAndCustomerId(tenantId, customerId);
        } else if (startDate != null && endDate != null) {
            payments = paymentRepository.findByTenantIdAndPaymentDateBetween(tenantId, startDate, endDate);
        } else {
            payments = paymentRepository.findByTenantId(tenantId);
        }

        BigDecimal totalAmount = payments.stream()
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal unappliedAmount = payments.stream()
            .map(Payment::getUnappliedAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long completedCount = payments.stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
            .count();

        long pendingCount = payments.stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.PENDING)
            .count();

        long failedCount = payments.stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.FAILED)
            .count();

        return new PaymentSummary(
            payments.size(),
            totalAmount,
            unappliedAmount,
            completedCount,
            pendingCount,
            failedCount
        );
    }

    public record PaymentSummary(
        long totalCount,
        BigDecimal totalAmount,
        BigDecimal unappliedAmount,
        long completedCount,
        long pendingCount,
        long failedCount
    ) {}
}
