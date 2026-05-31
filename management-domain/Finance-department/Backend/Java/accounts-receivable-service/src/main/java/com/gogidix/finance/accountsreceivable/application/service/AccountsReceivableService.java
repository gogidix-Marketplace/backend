package com.gogidix.finance.accountsreceivable.application.service;

import com.gogidix.finance.accountsreceivable.domain.model.Customer;
import com.gogidix.finance.accountsreceivable.domain.model.Invoice;
import com.gogidix.finance.accountsreceivable.domain.model.Payment;
import com.gogidix.finance.accountsreceivable.domain.repository.CustomerRepository;
import com.gogidix.finance.accountsreceivable.domain.repository.InvoiceRepository;
import com.gogidix.finance.accountsreceivable.domain.repository.PaymentRepository;
import com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Accounts Receivable Service
 * Orchestrates complex operations across multiple AR entities
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountsReceivableService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerCommandService customerCommandService;
    private final InvoiceCommandService invoiceCommandService;
    private final PaymentCommandService paymentCommandService;

    /**
     * Records a payment and applies it to an invoice
     */
    public void recordPaymentAndApply(String customerId, String invoiceId,
                                       BigDecimal amount, String paymentMethod,
                                       LocalDate paymentDate, String referenceNumber) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Recording payment of {} for customer {} and invoice {}",
            amount, customerId, invoiceId);

        Customer customer = customerRepository.findByCustomerIdAndTenantId(customerId, tenantId)
            .orElseThrow(() -> new com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException(
                "Customer", customerId));

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(invoiceId, tenantId)
            .orElseThrow(() -> new com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException(
                "Invoice", invoiceId));

        // Record payment on customer
        customer.recordPayment(amount);
        customerRepository.save(customer);

        // Record payment on invoice
        invoice.recordPayment(amount);
        invoiceRepository.save(invoice);
    }

    /**
     * Processes an invoice and updates customer metrics
     */
    public void processInvoice(String customerId, String invoiceId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Processing invoice {} for customer {}", invoiceId, customerId);

        Customer customer = customerRepository.findByCustomerIdAndTenantId(customerId, tenantId)
            .orElseThrow(() -> new com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException(
                "Customer", customerId));

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(invoiceId, tenantId)
            .orElseThrow(() -> new com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException(
                "Invoice", invoiceId));

        // Record invoice on customer
        customer.recordInvoice(invoice.getTotalAmount());
        customerRepository.save(customer);
    }

    /**
     * Gets AR aging report
     */
    public AgingReport getAgingReport() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Generating aging report for tenant: {}", tenantId);

        List<Invoice> allInvoices = invoiceRepository.findByTenantId(tenantId);

        BigDecimal current = BigDecimal.ZERO;
        BigDecimal days1_30 = BigDecimal.ZERO;
        BigDecimal days31_60 = BigDecimal.ZERO;
        BigDecimal days61_90 = BigDecimal.ZERO;
        BigDecimal over90 = BigDecimal.ZERO;

        LocalDate today = LocalDate.now();

        for (Invoice invoice : allInvoices) {
            if (invoice.getBalanceDue() == null || invoice.getBalanceDue().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            if (invoice.getStatus() == Invoice.InvoiceStatus.PAID) {
                continue;
            }

            LocalDate dueDate = invoice.getDueDate() != null ? invoice.getDueDate() : invoice.getInvoiceDate();

            if (today.isBefore(dueDate) || today.equals(dueDate)) {
                current = current.add(invoice.getBalanceDue());
            } else {
                long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);

                if (daysOverdue <= 30) {
                    days1_30 = days1_30.add(invoice.getBalanceDue());
                } else if (daysOverdue <= 60) {
                    days31_60 = days31_60.add(invoice.getBalanceDue());
                } else if (daysOverdue <= 90) {
                    days61_90 = days61_90.add(invoice.getBalanceDue());
                } else {
                    over90 = over90.add(invoice.getBalanceDue());
                }
            }
        }

        return new AgingReport(
            current,
            days1_30,
            days31_60,
            days61_90,
            over90,
            current.add(days1_30).add(days31_60).add(days61_90).add(over90),
            today
        );
    }

    /**
     * Gets customer AR summary
     */
    public CustomerARSummary getCustomerARSummary(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();

        Customer customer = customerRepository.findByCustomerIdAndTenantId(customerId, tenantId)
            .orElseThrow(() -> new com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException(
                "Customer", customerId));

        List<Invoice> invoices = invoiceRepository.findByTenantIdAndCustomerId(tenantId, customerId);
        List<Payment> payments = paymentRepository.findByTenantIdAndCustomerId(tenantId, customerId);

        BigDecimal totalInvoiced = invoices.stream()
            .map(Invoice::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaid = payments.stream()
            .filter(p -> p.getStatus() == com.gogidix.finance.accountsreceivable.domain.model.Payment.PaymentStatus.COMPLETED ||
                       p.getStatus() == com.gogidix.finance.accountsreceivable.domain.model.Payment.PaymentStatus.FULLY_APPLIED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long openInvoices = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.SENT ||
                       i.getStatus() == Invoice.InvoiceStatus.VIEWED ||
                       i.getStatus() == Invoice.InvoiceStatus.PARTIALLY_PAID)
            .count();

        long overdueInvoices = invoices.stream()
            .filter(i -> i.getStatus() == Invoice.InvoiceStatus.OVERDUE)
            .count();

        return new CustomerARSummary(
            customerId,
            customer.getCustomerName(),
            customer.getOutstandingBalance(),
            customer.getAvailableCredit(),
            customer.getCreditLimit() != null ? BigDecimal.valueOf(customer.getCreditLimit()) : BigDecimal.ZERO,
            totalInvoiced,
            totalPaid,
            openInvoices,
            overdueInvoices,
            customer.getCollectionStage() != null ? customer.getCollectionStage().name() : null
        );
    }

    /**
     * Updates collection stage for all customers based on overdue invoices
     */
    public void updateCollectionStages() {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Updating collection stages for tenant: {}", tenantId);

        List<Customer> customers = customerRepository.findByTenantId(tenantId);

        for (Customer customer : customers) {
            List<Invoice> overdueInvoices = invoiceRepository.findByTenantIdAndCustomerId(tenantId, customer.getCustomerId())
                .stream()
                .filter(i -> i.getStatus() == Invoice.InvoiceStatus.OVERDUE)
                .toList();

            customer.setOverdueInvoicesCount(overdueInvoices.size());

            // Update collection stage based on overdue count
            if (overdueInvoices.isEmpty()) {
                customer.setCollectionStage(Customer.CollectionStage.CURRENT);
            } else if (overdueInvoices.size() <= 2) {
                customer.setCollectionStage(Customer.CollectionStage.OVERDUE_1_30);
            } else if (overdueInvoices.size() <= 5) {
                customer.setCollectionStage(Customer.CollectionStage.OVERDUE_31_60);
            } else if (overdueInvoices.size() <= 10) {
                customer.setCollectionStage(Customer.CollectionStage.OVERDUE_61_90);
            } else {
                customer.setCollectionStage(Customer.CollectionStage.OVERDUE_90_PLUS);
            }

            customerRepository.save(customer);
        }

        log.info("Updated collection stages for {} customers", customers.size());
    }

    public record AgingReport(
        BigDecimal current,
        BigDecimal days1_30,
        BigDecimal days31_60,
        BigDecimal days61_90,
        BigDecimal over90,
        BigDecimal total,
        LocalDate reportDate
    ) {}

    public record CustomerARSummary(
        String customerId,
        String customerName,
        BigDecimal outstandingBalance,
        BigDecimal availableCredit,
        BigDecimal creditLimit,
        BigDecimal totalInvoiced,
        BigDecimal totalPaid,
        long openInvoices,
        long overdueInvoices,
        String collectionStage
    ) {}
}
