package com.gogidix.platform.subscription.domain.repository;

import com.gogidix.platform.subscription.domain.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for PaymentTransaction entity.
 */
@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, String> {

    /**
     * Find transactions by tenant
     */
    List<PaymentTransaction> findByTenantIdOrderByCreatedAtDesc(String tenantId);

    /**
     * Find transactions by invoice
     */
    List<PaymentTransaction> findByInvoiceId(String invoiceId);

    /**
     * Find transactions by status
     */
    List<PaymentTransaction> findByTenantIdAndStatus(String tenantId, PaymentTransaction.TransactionStatus status);

    /**
     * Find transaction by stripe payment intent ID
     */
    Optional<PaymentTransaction> findByStripePaymentIntentId(String paymentIntentId);

    /**
     * Find recent transactions
     */
    List<PaymentTransaction> findByTenantIdAndCreatedAtAfter(String tenantId, LocalDateTime after);
}
