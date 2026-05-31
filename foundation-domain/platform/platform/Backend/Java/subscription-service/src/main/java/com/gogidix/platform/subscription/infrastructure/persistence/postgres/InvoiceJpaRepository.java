package com.gogidix.platform.subscription.infrastructure.persistence.postgres;

import com.gogidix.platform.subscription.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Invoice entity.
 */
@Repository
public interface InvoiceJpaRepository extends JpaRepository<Invoice, String> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    List<Invoice> findBySubscriptionId(String subscriptionId);

    List<Invoice> findByTenantId(String tenantId);

    List<Invoice> findByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status);
}
