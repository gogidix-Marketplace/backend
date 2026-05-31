package com.gogidix.platform.subscription.infrastructure.persistence.postgres;

import com.gogidix.platform.subscription.domain.model.Invoice;
import com.gogidix.platform.subscription.domain.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of InvoiceRepository.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PostgresInvoiceRepository {

    private final InvoiceJpaRepository jpaRepository;

    public Invoice save(Invoice invoice) {
        log.debug("Saving invoice: id={}", invoice.getId());
        return jpaRepository.save(invoice);
    }

    public Optional<Invoice> findById(String id) {
        return jpaRepository.findById(id);
    }

    public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) {
        return jpaRepository.findByInvoiceNumber(invoiceNumber);
    }

    public List<Invoice> findBySubscriptionId(String subscriptionId) {
        return jpaRepository.findBySubscriptionId(subscriptionId);
    }

    public List<Invoice> findByTenantId(String tenantId) {
        return jpaRepository.findByTenantId(tenantId);
    }

    public List<Invoice> findByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status) {
        return jpaRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public void delete(Invoice invoice) {
        jpaRepository.delete(invoice);
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    public List<Invoice> findAll() {
        return jpaRepository.findAll();
    }
}
