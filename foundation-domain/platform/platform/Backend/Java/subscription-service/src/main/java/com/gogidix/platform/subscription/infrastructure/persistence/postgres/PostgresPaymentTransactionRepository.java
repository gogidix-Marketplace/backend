package com.gogidix.platform.subscription.infrastructure.persistence.postgres;

import com.gogidix.platform.subscription.domain.model.PaymentTransaction;
import com.gogidix.platform.subscription.domain.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of PaymentTransactionRepository.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PostgresPaymentTransactionRepository {

    private final PaymentTransactionJpaRepository jpaRepository;

    public PaymentTransaction save(PaymentTransaction transaction) {
        log.debug("Saving payment transaction: id={}", transaction.getId());
        return jpaRepository.save(transaction);
    }

    public Optional<PaymentTransaction> findById(String id) {
        return jpaRepository.findById(id);
    }

    public Optional<PaymentTransaction> findByStripePaymentIntentId(String stripePaymentIntentId) {
        return jpaRepository.findByStripePaymentIntentId(stripePaymentIntentId);
    }

    public List<PaymentTransaction> findBySubscriptionId(String subscriptionId) {
        return jpaRepository.findBySubscriptionId(subscriptionId);
    }

    public List<PaymentTransaction> findByInvoiceId(String invoiceId) {
        return jpaRepository.findByInvoiceId(invoiceId);
    }

    public List<PaymentTransaction> findByTenantId(String tenantId) {
        return jpaRepository.findByTenantId(tenantId);
    }

    public void delete(PaymentTransaction transaction) {
        jpaRepository.delete(transaction);
    }

    public List<PaymentTransaction> findAll() {
        return jpaRepository.findAll();
    }
}
