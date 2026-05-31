package com.gogidix.finance.accountspayable.application.service;

import com.gogidix.finance.accountspayable.domain.model.Payment;
import com.gogidix.finance.accountspayable.domain.repository.PaymentRepository;
import com.gogidix.finance.accountspayable.shared.exception.NotFoundException;
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
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching payment: {} for tenant: {}", paymentId, tenantId);

        return paymentRepository.findByPaymentIdAndTenantId(paymentId, tenantId)
            .orElseThrow(() -> new NotFoundException("Payment", paymentId));
    }

    public Page<Payment> getByVendor(String vendorId, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching payments for vendor: {} in tenant: {}", vendorId, tenantId);

        List<Payment> payments = paymentRepository.findByTenantIdAndVendorId(tenantId, vendorId);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getByInvoice(String invoiceId, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching payments for invoice: {} in tenant: {}", invoiceId, tenantId);

        List<Payment> payments = paymentRepository.findByTenantIdAndInvoiceIdsContaining(tenantId, invoiceId);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getByStatus(Payment.PaymentStatus status, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching payments by status: {} for tenant: {}", status, tenantId);

        List<Payment> payments = paymentRepository.findByTenantIdAndStatus(tenantId, status);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getByDateRange(LocalDate startDate, LocalDate endDate,
                                         List<String> statuses, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

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

    public Page<Payment> getScheduledPayments(LocalDate startDate, LocalDate endDate, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching scheduled payments for tenant: {}", tenantId);

        LocalDate start = startDate != null ? startDate : LocalDate.now();
        LocalDate end = endDate != null ? endDate : start.plusMonths(1);

        List<Payment> payments = paymentRepository.findByTenantIdAndScheduledDateBetween(
            tenantId, start, end);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getPendingPayments(int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching pending payments for tenant: {}", tenantId);

        List<Payment> payments = paymentRepository.findPendingPayments(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getFailedPayments(int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching failed payments for tenant: {}", tenantId);

        List<Payment> payments = paymentRepository.findFailedPayments(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> getByMethod(Payment.PaymentMethod paymentMethod, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching payments by method: {} for tenant: {}", paymentMethod, tenantId);

        List<Payment> payments = paymentRepository.findByTenantIdAndPaymentMethod(tenantId, paymentMethod);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public Page<Payment> search(String searchTerm, String vendorId, String status,
                                 LocalDate startDate, LocalDate endDate,
                                 BigDecimal minAmount, BigDecimal maxAmount,
                                 int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Searching payments for tenant: {} with term: {}", tenantId, searchTerm);

        List<Payment> payments = paymentRepository.searchByDescription(tenantId, searchTerm);

        if (vendorId != null) {
            payments = payments.stream()
                .filter(p -> vendorId.equals(p.getVendorId()))
                .toList();
        }

        if (status != null) {
            Payment.PaymentStatus statusEnum = Payment.PaymentStatus.valueOf(status);
            payments = payments.stream()
                .filter(p -> p.getStatus() == statusEnum)
                .toList();
        }

        if (startDate != null && endDate != null) {
            payments = payments.stream()
                .filter(p -> p.getPaymentDate() != null &&
                               !p.getPaymentDate().isBefore(startDate) &&
                               !p.getPaymentDate().isAfter(endDate))
                .toList();
        }

        if (minAmount != null) {
            payments = payments.stream()
                .filter(p -> p.getAmount().compareTo(minAmount) >= 0)
                .toList();
        }

        if (maxAmount != null) {
            payments = payments.stream()
                .filter(p -> p.getAmount().compareTo(maxAmount) <= 0)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(payments, pageRequest, payments.size());
    }

    public List<Payment> getAllForTenant() {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching all payments for tenant: {}", tenantId);

        return paymentRepository.findByTenantId(tenantId);
    }

    public long countByTenant() {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();
        return paymentRepository.countByTenantId(tenantId);
    }

    public long countByStatus(Payment.PaymentStatus status) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();
        return paymentRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public BigDecimal sumAmountByStatus(Payment.PaymentStatus status) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();
        return paymentRepository.sumAmountByTenantIdAndStatus(tenantId, status);
    }
}
