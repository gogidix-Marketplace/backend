package com.gogidix.finance.accountsreceivable.application.service;

import com.gogidix.finance.accountsreceivable.domain.model.Invoice;
import com.gogidix.finance.accountsreceivable.domain.repository.InvoiceRepository;
import com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException;
import com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Invoice Query Service
 * Handles all read operations for invoices
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;

    public Invoice getById(String invoiceId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching invoice: {} for tenant: {}", invoiceId, tenantId);

        return invoiceRepository.findByInvoiceIdAndTenantId(invoiceId, tenantId)
            .orElseThrow(() -> new NotFoundException("Invoice", invoiceId));
    }

    public Invoice getByNumber(String invoiceNumber) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching invoice by number: {} for tenant: {}", invoiceNumber, tenantId);

        return invoiceRepository.findByInvoiceNumberAndTenantId(invoiceNumber, tenantId)
            .orElseThrow(() -> new NotFoundException("Invoice", invoiceNumber));
    }

    public Page<Invoice> getByCustomer(String customerId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching invoices for customer: {} in tenant: {}", customerId, tenantId);

        List<Invoice> invoices = invoiceRepository.findByTenantIdAndCustomerId(tenantId, customerId);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getByStatus(String status, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching invoices by status: {} for tenant: {}", status, tenantId);

        Invoice.InvoiceStatus statusEnum = Invoice.InvoiceStatus.valueOf(status);
        List<Invoice> invoices = invoiceRepository.findByTenantIdAndStatus(tenantId, statusEnum);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getOverdueInvoices(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching overdue invoices for tenant: {}", tenantId);

        List<Invoice> invoices = invoiceRepository.findOverdueInvoicesByTenantId(tenantId);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getByDateRange(LocalDate startDate, LocalDate endDate, List<String> statuses, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching invoices for date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        List<Invoice> invoices = invoiceRepository.findByTenantIdAndInvoiceDateBetween(
            tenantId, startDate, endDate);

        if (statuses != null && !statuses.isEmpty()) {
            List<Invoice.InvoiceStatus> statusEnums = statuses.stream()
                .map(Invoice.InvoiceStatus::valueOf)
                .toList();
            invoices = invoices.stream()
                .filter(i -> statusEnums.contains(i.getStatus()))
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getByDueDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching invoices by due date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        List<Invoice> invoices = invoiceRepository.findByTenantIdAndDueDateBetween(
            tenantId, startDate, endDate);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getPendingInvoices(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching pending invoices for tenant: {}", tenantId);

        List<Invoice> invoices = invoiceRepository.findPendingInvoicesByTenantId(tenantId);

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> search(String searchTerm, String customerId, String status,
                                 LocalDate startDate, LocalDate endDate,
                                 BigDecimal minAmount, BigDecimal maxAmount,
                                 int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Searching invoices for tenant: {} with term: {}", tenantId, searchTerm);

        List<Invoice> invoices;

        if (customerId != null && !customerId.isBlank()) {
            invoices = invoiceRepository.findByTenantIdAndCustomerId(tenantId, customerId);
        } else if (searchTerm != null && !searchTerm.isBlank()) {
            invoices = invoiceRepository.findByTenantIdAndCustomerNameContainingIgnoreCase(tenantId, searchTerm);
        } else {
            invoices = invoiceRepository.findByTenantId(tenantId);
        }

        // Filter by status
        if (status != null && !status.isBlank()) {
            Invoice.InvoiceStatus statusEnum = Invoice.InvoiceStatus.valueOf(status);
            invoices = invoices.stream()
                .filter(i -> i.getStatus() == statusEnum)
                .toList();
        }

        // Filter by date range
        if (startDate != null && endDate != null) {
            invoices = invoices.stream()
                .filter(i -> i.getInvoiceDate() != null &&
                    !i.getInvoiceDate().isBefore(startDate) &&
                    !i.getInvoiceDate().isAfter(endDate))
                .toList();
        }

        // Filter by amount range
        if (minAmount != null) {
            invoices = invoices.stream()
                .filter(i -> i.getTotalAmount() != null && i.getTotalAmount().compareTo(minAmount) >= 0)
                .toList();
        }
        if (maxAmount != null) {
            invoices = invoices.stream()
                .filter(i -> i.getTotalAmount() != null && i.getTotalAmount().compareTo(maxAmount) <= 0)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public List<Invoice> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all invoices for tenant: {}", tenantId);

        return invoiceRepository.findByTenantId(tenantId);
    }

    public List<Invoice> getBySalesperson(String salesperson) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching invoices by salesperson: {} for tenant: {}", salesperson, tenantId);

        return invoiceRepository.findByTenantIdAndSalesperson(tenantId, salesperson);
    }

    public List<Invoice> getByProjectId(String projectId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching invoices by project: {} for tenant: {}", projectId, tenantId);

        return invoiceRepository.findByTenantIdAndProjectId(tenantId, projectId);
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return invoiceRepository.countByTenantId(tenantId);
    }

    public long countByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        Invoice.InvoiceStatus statusEnum = Invoice.InvoiceStatus.valueOf(status);
        return invoiceRepository.countByTenantIdAndStatus(tenantId, statusEnum);
    }

    public BigDecimal sumTotalAmountByStatus(String status) {
        String tenantId = RequestContextHolder.getTenantId();
        Invoice.InvoiceStatus statusEnum = Invoice.InvoiceStatus.valueOf(status);
        return invoiceRepository.sumTotalAmountByTenantIdAndStatus(tenantId, statusEnum);
    }

    public BigDecimal sumBalanceDue() {
        String tenantId = RequestContextHolder.getTenantId();
        return invoiceRepository.sumBalanceDueByTenantId(tenantId);
    }

    public InvoiceSummary getSummary(LocalDate startDate, LocalDate endDate, String customerId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching invoice summary for tenant: {}", tenantId);

        List<Invoice> invoices;

        if (customerId != null && !customerId.isBlank()) {
            invoices = invoiceRepository.findByTenantIdAndCustomerId(tenantId, customerId);
        } else if (startDate != null && endDate != null) {
            invoices = invoiceRepository.findByTenantIdAndInvoiceDateBetween(tenantId, startDate, endDate);
        } else {
            invoices = invoiceRepository.findByTenantId(tenantId);
        }

        BigDecimal totalAmount = invoices.stream()
            .map(Invoice::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balanceDue = invoices.stream()
            .map(Invoice::getBalanceDue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long draftCount = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.DRAFT)
            .count();

        long sentCount = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.SENT)
            .count();

        long paidCount = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.PAID)
            .count();

        long overdueCount = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.OVERDUE)
            .count();

        return new InvoiceSummary(
            invoices.size(),
            totalAmount,
            balanceDue,
            draftCount,
            sentCount,
            paidCount,
            overdueCount
        );
    }

    public record InvoiceSummary(
        long totalCount,
        BigDecimal totalAmount,
        BigDecimal balanceDue,
        long draftCount,
        long sentCount,
        long paidCount,
        long overdueCount
    ) {}
}
