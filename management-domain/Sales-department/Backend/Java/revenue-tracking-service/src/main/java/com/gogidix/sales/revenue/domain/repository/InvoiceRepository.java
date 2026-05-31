package com.gogidix.sales.revenue.domain.repository;

import com.gogidix.sales.revenue.domain.model.Invoice;

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

    List<Invoice> findByTenantIdAndPaymentStatus(String tenantId, Invoice.PaymentStatus paymentStatus);

    List<Invoice> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<Invoice> findByTenantIdAndContractId(String tenantId, String contractId);

    List<Invoice> findByTenantIdAndRevenueId(String tenantId, String revenueId);

    List<Invoice> findByTenantIdAndInvoiceDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Invoice> findByTenantIdAndDueDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<Invoice> findOverdueInvoicesByTenantId(String tenantId);

    List<Invoice> findUnpaidInvoicesByTenantId(String tenantId);

    List<Invoice> findDueInvoicesByTenantIdAndDate(String tenantId, LocalDate dueDate);

    BigDecimal sumTotalAmountByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    BigDecimal sumPaidAmountByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    BigDecimal sumOutstandingAmountByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, Invoice.InvoiceStatus status);

    long countOverdueByTenantId(String tenantId);

    boolean existsByInvoiceIdAndTenantId(String invoiceId, String tenantId);

    boolean existsByInvoiceNumberAndTenantId(String invoiceNumber, String tenantId);

    void deleteById(String id);

    void deleteByInvoiceIdAndTenantId(String invoiceId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    List<Invoice> findByTenantIdAndSalespersonId(String tenantId, String salespersonId);

    List<Invoice> findByTenantIdAndTerritory(String tenantId, String territory);

    List<Invoice> findUnreconciledInvoicesByTenantId(String tenantId);
}
