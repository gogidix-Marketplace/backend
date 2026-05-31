package com.gogidix.finance.accountsreceivable.domain.repository;

import com.gogidix.finance.accountsreceivable.domain.model.Invoice;

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

    List<Invoice> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Invoice> findByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status);

    List<Invoice> findByTenantIdAndStatusIn(String tenantId, List<Invoice.InvoiceStatus> statuses);

    List<Invoice> findByTenantIdAndInvoiceType(String tenantId, Invoice.InvoiceType invoiceType);

    List<Invoice> findByTenantIdAndInvoiceDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Invoice> findByTenantIdAndDueDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Invoice> findOverdueInvoicesByTenantId(String tenantId);

    List<Invoice> findPendingInvoicesByTenantId(String tenantId);

    List<Invoice> findByTenantIdAndSalesperson(String tenantId, String salesperson);

    List<Invoice> findByTenantIdAndProjectId(String tenantId, String projectId);

    List<Invoice> findByTenantIdAndDepartmentId(String tenantId, String departmentId);

    List<Invoice> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Invoice> findByTenantIdAndCustomerNameContainingIgnoreCase(String tenantId, String customerName);

    List<Invoice> findByTenantIdAndPurchaseOrderNumber(String tenantId, String purchaseOrderNumber);

    List<Invoice> findByTenantIdAndGroupId(String tenantId, String groupId);

    List<Invoice> findByTenantIdAndParentId(String tenantId, String parentId);

    List<Invoice> findRecurringInvoicesByTenantId(String tenantId);

    boolean existsByInvoiceNumberAndTenantId(String invoiceNumber, String tenantId);

    void deleteById(String id);

    void deleteByInvoiceIdAndTenantId(String invoiceId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status);

    BigDecimal sumTotalAmountByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status);

    BigDecimal sumBalanceDueByTenantId(String tenantId);

    BigDecimal sumBalanceDueByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Invoice> findByTenantIdAndDaysOverdueGreaterThan(String tenantId, Integer daysOverdue);
}
