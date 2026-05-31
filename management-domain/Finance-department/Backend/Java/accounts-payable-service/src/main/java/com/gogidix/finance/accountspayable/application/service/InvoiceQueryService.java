package com.gogidix.finance.accountspayable.application.service;

import com.gogidix.finance.accountspayable.domain.model.Invoice;
import com.gogidix.finance.accountspayable.domain.repository.InvoiceRepository;
import com.gogidix.finance.accountspayable.shared.exception.NotFoundException;
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
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching invoice: {} for tenant: {}", invoiceId, tenantId);

        return invoiceRepository.findByInvoiceIdAndTenantId(invoiceId, tenantId)
            .orElseThrow(() -> new NotFoundException("Invoice", invoiceId));
    }

    public Invoice getByNumber(String invoiceNumber) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching invoice by number: {} for tenant: {}", invoiceNumber, tenantId);

        return invoiceRepository.findByInvoiceNumberAndTenantId(invoiceNumber, tenantId)
            .orElseThrow(() -> new NotFoundException("Invoice", invoiceNumber));
    }

    public Page<Invoice> getByVendor(String vendorId, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching invoices for vendor: {} in tenant: {}", vendorId, tenantId);

        List<Invoice> invoices = invoiceRepository.findByTenantIdAndVendorId(tenantId, vendorId);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getByStatus(Invoice.InvoiceStatus status, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching invoices by status: {} for tenant: {}", status, tenantId);

        List<Invoice> invoices = invoiceRepository.findByTenantIdAndStatus(tenantId, status);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getByDateRange(LocalDate startDate, LocalDate endDate,
                                         List<String> statuses, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching invoices for date range: {} to {} in tenant: {}",
            startDate, endDate, tenantId);

        List<Invoice> invoices = invoiceRepository.findByTenantIdAndInvoiceDateBetween(
            tenantId, startDate, endDate);

        if (statuses != null && !statuses.isEmpty()) {
            List<Invoice.InvoiceStatus> statusEnums = statuses.stream()
                .map(Invoice.InvoiceStatus::valueOf)
                .toList();
            invoices = invoices.stream()
                .filter(e -> statusEnums.contains(e.getStatus()))
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getOverdueInvoices(int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching overdue invoices for tenant: {}", tenantId);

        List<Invoice> invoices = invoiceRepository.findOverdueInvoices(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getPendingApproval(int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching invoices pending approval for tenant: {}", tenantId);

        List<Invoice> invoices = invoiceRepository.findByTenantIdAndStatus(
            tenantId, Invoice.InvoiceStatus.PENDING);

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getByDepartment(String department, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching invoices for department: {} in tenant: {}", department, tenantId);

        List<Invoice> invoices = invoiceRepository.findByTenantIdAndDepartment(tenantId, department);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> search(String searchTerm, String vendorId, String status,
                                 LocalDate startDate, LocalDate endDate,
                                 BigDecimal minAmount, BigDecimal maxAmount,
                                 int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Searching invoices for tenant: {} with term: {}", tenantId, searchTerm);

        List<Invoice> invoices = invoiceRepository.searchByDescription(tenantId, searchTerm);

        if (vendorId != null) {
            invoices = invoices.stream()
                .filter(i -> vendorId.equals(i.getVendorId()))
                .toList();
        }

        if (status != null) {
            Invoice.InvoiceStatus statusEnum = Invoice.InvoiceStatus.valueOf(status);
            invoices = invoices.stream()
                .filter(i -> i.getStatus() == statusEnum)
                .toList();
        }

        if (startDate != null && endDate != null) {
            invoices = invoices.stream()
                .filter(i -> !i.getInvoiceDate().isBefore(startDate) && !i.getInvoiceDate().isAfter(endDate))
                .toList();
        }

        if (minAmount != null) {
            invoices = invoices.stream()
                .filter(i -> i.getAmount().compareTo(minAmount) >= 0)
                .toList();
        }

        if (maxAmount != null) {
            invoices = invoices.stream()
                .filter(i -> i.getAmount().compareTo(maxAmount) <= 0)
                .toList();
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public Page<Invoice> getInvoicesDueForPayment(LocalDate dueDate, int page, int size) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching invoices due for payment: {} in tenant: {}", dueDate, tenantId);

        LocalDate queryDate = dueDate != null ? dueDate : LocalDate.now();
        List<Invoice> invoices = invoiceRepository.findInvoicesDueForPayment(tenantId, queryDate);
        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(invoices, pageRequest, invoices.size());
    }

    public List<Invoice> getAllForTenant() {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching all invoices for tenant: {}", tenantId);

        return invoiceRepository.findByTenantId(tenantId);
    }

    public long countByTenant() {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();
        return invoiceRepository.countByTenantId(tenantId);
    }

    public long countByStatus(Invoice.InvoiceStatus status) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();
        return invoiceRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public BigDecimal sumAmountByStatus(Invoice.InvoiceStatus status) {
        String tenantId = com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder.getTenantId();
        return invoiceRepository.sumAmountByTenantIdAndStatus(tenantId, status);
    }
}
