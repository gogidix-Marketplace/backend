package com.gogidix.finance.accountsreceivable.domain.repository;

import com.gogidix.finance.accountsreceivable.domain.model.Payment;

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

    Optional<Payment> findByPaymentNumberAndTenantId(String paymentNumber, String tenantId);

    List<Payment> findByTenantId(String tenantId);

    List<Payment> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Payment> findByTenantIdAndInvoiceId(String tenantId, String invoiceId);

    List<Payment> findByTenantIdAndStatus(String tenantId, Payment.PaymentStatus status);

    List<Payment> findByTenantIdAndStatusIn(String tenantId, List<Payment.PaymentStatus> statuses);

    List<Payment> findByTenantIdAndPaymentType(String tenantId, Payment.PaymentType paymentType);

    List<Payment> findByTenantIdAndPaymentMethod(String tenantId, String paymentMethod);

    List<Payment> findByTenantIdAndPaymentDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Payment> findUnallocatedPaymentsByTenantId(String tenantId);

    List<Payment> findReconciledPaymentsByTenantId(String tenantId);

    List<Payment> findUnreconciledPaymentsByTenantId(String tenantId);

    List<Payment> findByTenantIdAndCustomerIdAndStatus(String tenantId, String customerId, Payment.PaymentStatus status);

    List<Payment> findByTenantIdAndReferenceNumber(String tenantId, String referenceNumber);

    List<Payment> findByTenantIdAndCheckNumber(String tenantId, String checkNumber);

    List<Payment> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Payment> findByTenantIdAndBankAccount(String tenantId, String bankAccount);

    boolean existsByPaymentNumberAndTenantId(String paymentNumber, String tenantId);

    void deleteById(String id);

    void deleteByPaymentIdAndTenantId(String paymentId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Payment.PaymentStatus status);

    BigDecimal sumAmountByTenantIdAndStatus(String tenantId, Payment.PaymentStatus status);

    BigDecimal sumAmountByTenantIdAndCustomerId(String tenantId, String customerId);

    BigDecimal sumUnappliedAmountByTenantId(String tenantId);

    List<Payment> findPaymentsByTransactionId(String tenantId, String transactionId);
}
