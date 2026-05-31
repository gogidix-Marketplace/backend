package com.gogidix.platform.subscription.domain.repository;

import com.gogidix.platform.subscription.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Invoice entity.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    /**
     * Find invoices by tenant
     */
    List<Invoice> findByTenantIdOrderByCreatedAtDesc(String tenantId);

    /**
     * Find invoices by subscription
     */
    List<Invoice> findBySubscriptionIdOrderByCreatedAtDesc(String subscriptionId);

    /**
     * Find invoices by status
     */
    List<Invoice> findByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status);

    /**
     * Find invoice by invoice number
     */
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    /**
     * Find overdue invoices
     */
    List<Invoice> findByDueDateBeforeAndStatusNot(LocalDateTime dueDate, Invoice.InvoiceStatus status);
}
