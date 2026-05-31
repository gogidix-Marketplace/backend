package com.gogidix.finance.accountspayable.domain.repository;

import com.gogidix.finance.accountspayable.domain.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Payment Repository Interface (Port)
 * Defines the contract for payment persistence operations
 */
public interface PaymentRepository {

    Payment save(Payment payment);

    List<Payment> saveAll(List<Payment> payments);

    Optional<Payment> findById(String id);

    Optional<Payment> findByPaymentIdAndTenantId(String paymentId, String tenantId);

    Optional<Payment> findByPaymentReferenceAndTenantId(String paymentReference, String tenantId);

    List<Payment> findByTenantId(String tenantId);

    List<Payment> findByTenantIdAndStatus(String tenantId, Payment.PaymentStatus status);

    List<Payment> findByTenantIdAndVendorId(String tenantId, String vendorId);

    List<Payment> findByTenantIdAndInvoiceIdsContaining(String tenantId, String invoiceId);

    List<Payment> findByTenantIdAndPaymentMethod(String tenantId, Payment.PaymentMethod paymentMethod);

    List<Payment> findByTenantIdAndPaymentDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Payment> findByTenantIdAndScheduledDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Payment> findByTenantIdAndStatusIn(String tenantId, List<Payment.PaymentStatus> statuses);

    List<Payment> findByTenantIdAndProcessedBy(String tenantId, String processedBy);

    List<Payment> findByTenantIdAndBatchId(String tenantId, String batchId);

    List<Payment> findScheduledPayments(String tenantId);

    List<Payment> findPendingPayments(String tenantId);

    List<Payment> findFailedPayments(String tenantId);

    List<Payment> searchByDescription(String tenantId, String searchTerm);

    List<Payment> findByTenantIdAndAmountBetween(String tenantId, BigDecimal minAmount, BigDecimal maxAmount);

    boolean existsByPaymentIdAndTenantId(String paymentId, String tenantId);

    void deleteById(String id);

    void deleteByPaymentIdAndTenantId(String paymentId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Payment.PaymentStatus status);

    BigDecimal sumAmountByTenantIdAndStatus(String tenantId, Payment.PaymentStatus status);
}
