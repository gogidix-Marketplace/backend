package com.gogidix.finance.accountspayable.domain.repository;

import com.gogidix.finance.accountspayable.domain.model.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Invoice Repository Interface (Port)
 * Defines the contract for invoice persistence operations
 */
public interface InvoiceRepository {

    Invoice save(Invoice invoice);

    List<Invoice> saveAll(List<Invoice> invoices);

    Optional<Invoice> findById(String id);

    Optional<Invoice> findByInvoiceIdAndTenantId(String invoiceId, String tenantId);

    Optional<Invoice> findByInvoiceNumberAndTenantId(String invoiceNumber, String tenantId);

    List<Invoice> findByTenantId(String tenantId);

    List<Invoice> findByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status);

    List<Invoice> findByTenantIdAndVendorId(String tenantId, String vendorId);

    List<Invoice> findByTenantIdAndDepartment(String tenantId, String department);

    List<Invoice> findByTenantIdAndCostCenter(String tenantId, String costCenter);

    List<Invoice> findByTenantIdAndInvoiceDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Invoice> findByTenantIdAndDueDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Invoice> findByTenantIdAndStatusIn(String tenantId, List<Invoice.InvoiceStatus> statuses);

    List<Invoice> findByTenantIdAndApprovedBy(String tenantId, String approvedBy);

    List<Invoice> findByTenantIdAndSubmittedBy(String tenantId, String submittedBy);

    List<Invoice> findOverdueInvoices(String tenantId);

    List<Invoice> findInvoicesDueForPayment(String tenantId, LocalDate dueDate);

    List<Invoice> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Invoice> searchByDescription(String tenantId, String searchTerm);

    List<Invoice> findByTenantIdAndAmountBetween(String tenantId, BigDecimal minAmount, BigDecimal maxAmount);

    boolean existsByInvoiceNumberAndTenantId(String invoiceNumber, String tenantId);

    boolean existsByInvoiceIdAndTenantId(String invoiceId, String tenantId);

    void deleteById(String id);

    void deleteByInvoiceIdAndTenantId(String invoiceId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status);

    BigDecimal sumAmountByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status);
}
