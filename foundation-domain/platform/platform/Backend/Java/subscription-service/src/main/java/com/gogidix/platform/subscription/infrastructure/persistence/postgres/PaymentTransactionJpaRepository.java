package com.gogidix.platform.subscription.infrastructure.persistence.postgres;

import com.gogidix.platform.subscription.domain.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for PaymentTransaction entity.
 */
@Repository
public interface PaymentTransactionJpaRepository extends JpaRepository<PaymentTransaction, String> {

    Optional<PaymentTransaction> findByStripePaymentIntentId(String stripePaymentIntentId);

    List<PaymentTransaction> findBySubscriptionId(String subscriptionId);

    List<PaymentTransaction> findByInvoiceId(String invoiceId);

    List<PaymentTransaction> findByTenantId(String tenantId);
}
