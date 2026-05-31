package com.gogidix.finance.accountspayable.application.service;

import com.gogidix.finance.accountspayable.application.dto.response.APSummaryDto;
import com.gogidix.finance.accountspayable.domain.model.Invoice;
import com.gogidix.finance.accountspayable.domain.model.Payment;
import com.gogidix.finance.accountspayable.domain.model.Vendor;
import com.gogidix.finance.accountspayable.domain.repository.InvoiceRepository;
import com.gogidix.finance.accountspayable.domain.repository.PaymentRepository;
import com.gogidix.finance.accountspayable.domain.repository.VendorRepository;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Accounts Payable Service Facade
 * Provides high-level operations and summary statistics
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountsPayableService {

    private final VendorRepository vendorRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    /**
     * Gets comprehensive AP summary for the tenant
     */
    public APSummaryDto getSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Generating AP summary for tenant: {}", tenantId);

        // Vendor statistics
        long totalVendors = vendorRepository.countByTenantId(tenantId);
        long activeVendors = vendorRepository.countByTenantIdAndStatus(
            tenantId, Vendor.VendorStatus.ACTIVE);

        // Invoice statistics
        long totalInvoices = invoiceRepository.countByTenantId(tenantId);
        long pendingInvoices = invoiceRepository.countByTenantIdAndStatus(
            tenantId, Invoice.InvoiceStatus.PENDING);
        long approvedInvoices = invoiceRepository.countByTenantIdAndStatus(
            tenantId, Invoice.InvoiceStatus.APPROVED);
        long overdueInvoices = invoiceRepository.countByTenantIdAndStatus(
            tenantId, Invoice.InvoiceStatus.OVERDUE);

        BigDecimal pendingAmount = invoiceRepository.sumAmountByTenantIdAndStatus(
            tenantId, Invoice.InvoiceStatus.PENDING);
        BigDecimal approvedAmount = invoiceRepository.sumAmountByTenantIdAndStatus(
            tenantId, Invoice.InvoiceStatus.APPROVED);
        BigDecimal overdueAmount = invoiceRepository.sumAmountByTenantIdAndStatus(
            tenantId, Invoice.InvoiceStatus.OVERDUE);

        // Payment statistics
        long totalPayments = paymentRepository.countByTenantId(tenantId);
        long pendingPayments = paymentRepository.countByTenantIdAndStatus(
            tenantId, Payment.PaymentStatus.PENDING);
        long scheduledPayments = paymentRepository.countByTenantIdAndStatus(
            tenantId, Payment.PaymentStatus.SCHEDULED);
        long completedPayments = paymentRepository.countByTenantIdAndStatus(
            tenantId, Payment.PaymentStatus.COMPLETED);
        long failedPayments = paymentRepository.countByTenantIdAndStatus(
            tenantId, Payment.PaymentStatus.FAILED);

        BigDecimal totalPaymentAmount = paymentRepository.sumAmountByTenantIdAndStatus(
            tenantId, Payment.PaymentStatus.COMPLETED);

        // Calculate total outstanding
        BigDecimal totalOutstanding = (pendingAmount != null ? pendingAmount : BigDecimal.ZERO)
            .add(approvedAmount != null ? approvedAmount : BigDecimal.ZERO)
            .add(overdueAmount != null ? overdueAmount : BigDecimal.ZERO);

        return APSummaryDto.builder()
            .totalVendors(totalVendors)
            .activeVendors(activeVendors)
            .totalInvoices(totalInvoices)
            .pendingInvoices(pendingInvoices)
            .approvedInvoices(approvedInvoices)
            .overdueInvoices(overdueInvoices)
            .pendingAmount(pendingAmount != null ? pendingAmount : BigDecimal.ZERO)
            .approvedAmount(approvedAmount != null ? approvedAmount : BigDecimal.ZERO)
            .overdueAmount(overdueAmount != null ? overdueAmount : BigDecimal.ZERO)
            .totalOutstanding(totalOutstanding)
            .totalPayments(totalPayments)
            .pendingPayments(pendingPayments)
            .scheduledPayments(scheduledPayments)
            .completedPayments(completedPayments)
            .failedPayments(failedPayments)
            .totalPaymentAmount(totalPaymentAmount != null ? totalPaymentAmount : BigDecimal.ZERO)
            .build();
    }

    /**
     * Gets summary for a specific vendor
     */
    public APSummaryDto getVendorSummary(String vendorId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Generating AP summary for vendor: {} in tenant: {}", vendorId, tenantId);

        Vendor vendor = vendorRepository.findByVendorIdAndTenantId(vendorId, tenantId)
            .orElseThrow(() -> new com.gogidix.finance.accountspayable.shared.exception.NotFoundException("Vendor", vendorId));

        // Get vendor invoices
        var invoices = invoiceRepository.findByTenantIdAndVendorId(tenantId, vendorId);
        long pendingInvoices = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.PENDING)
            .count();
        long approvedInvoices = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.APPROVED)
            .count();
        long overdueInvoices = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.OVERDUE || i.isOverdue())
            .count();

        BigDecimal pendingAmount = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.PENDING)
            .map(Invoice::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal approvedAmount = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.APPROVED)
            .map(Invoice::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Get vendor payments
        var payments = paymentRepository.findByTenantIdAndVendorId(tenantId, vendorId);
        long completedPayments = payments.stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
            .count();

        BigDecimal totalPaid = payments.stream()
            .filter(p -> p.getStatus() == Payment.PaymentStatus.COMPLETED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOutstanding = (pendingAmount != null ? pendingAmount : BigDecimal.ZERO)
            .add(approvedAmount != null ? approvedAmount : BigDecimal.ZERO);

        return APSummaryDto.builder()
            .vendorId(vendorId)
            .vendorName(vendor.getVendorName())
            .vendorStatus(vendor.getStatus().name())
            .totalInvoices((long) invoices.size())
            .pendingInvoices(pendingInvoices)
            .approvedInvoices(approvedInvoices)
            .overdueInvoices(overdueInvoices)
            .pendingAmount(pendingAmount != null ? pendingAmount : BigDecimal.ZERO)
            .approvedAmount(approvedAmount != null ? approvedAmount : BigDecimal.ZERO)
            .totalOutstanding(totalOutstanding)
            .totalPayments((long) payments.size())
            .completedPayments(completedPayments)
            .totalPaymentAmount(totalPaid != null ? totalPaid : BigDecimal.ZERO)
            .build();
    }

    /**
     * Gets cash requirements for upcoming period
     */
    public BigDecimal getCashRequirements(LocalDate fromDate, LocalDate toDate) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Calculating cash requirements from {} to {} for tenant: {}",
            fromDate, toDate, tenantId);

        // Get approved invoices due in the period
        var invoices = invoiceRepository.findInvoicesDueForPayment(tenantId, toDate);

        BigDecimal cashRequirement = invoices.stream()
            .filter(i -> !i.getDueDate().isBefore(fromDate) &&
                        !i.getDueDate().isAfter(toDate) &&
                        (i.getStatus() == Invoice.InvoiceStatus.APPROVED ||
                         i.getStatus() == Invoice.InvoiceStatus.PENDING))
            .map(Invoice::getNetAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return cashRequirement;
    }

    /**
     * Gets aging report
     */
    public AgingReport getAgingReport() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Generating aging report for tenant: {}", tenantId);

        LocalDate today = LocalDate.now();
        var invoices = invoiceRepository.findByTenantIdAndStatusIn(
            tenantId,
            List.of(Invoice.InvoiceStatus.PENDING, Invoice.InvoiceStatus.APPROVED,
                    Invoice.InvoiceStatus.OVERDUE));

        BigDecimal current = BigDecimal.ZERO;
        BigDecimal days1to30 = BigDecimal.ZERO;
        BigDecimal days31to60 = BigDecimal.ZERO;
        BigDecimal days61to90 = BigDecimal.ZERO;
        BigDecimal over90 = BigDecimal.ZERO;

        for (Invoice invoice : invoices) {
            if (invoice.getStatus() == Invoice.InvoiceStatus.PAID) {
                continue;
            }

            long daysOverdue = today.until(invoice.getDueDate()).getDays();
            BigDecimal amount = invoice.getNetAmount();

            if (daysOverdue <= 0) {
                current = current.add(amount);
            } else if (daysOverdue <= 30) {
                days1to30 = days1to30.add(amount);
            } else if (daysOverdue <= 60) {
                days31to60 = days31to60.add(amount);
            } else if (daysOverdue <= 90) {
                days61to90 = days61to90.add(amount);
            } else {
                over90 = over90.add(amount);
            }
        }

        return new AgingReport(current, days1to30, days31to60, days61to90, over90);
    }

    public record AgingReport(
        BigDecimal current,
        BigDecimal days1to30,
        BigDecimal days31to60,
        BigDecimal days61to90,
        BigDecimal over90
    ) {}
}
