package com.gogidix.finance.accountsreceivable.application.service;

import com.gogidix.finance.accountsreceivable.domain.model.Invoice;
import com.gogidix.finance.accountsreceivable.domain.port.in.InvoiceCommand;
import com.gogidix.finance.accountsreceivable.domain.port.out.EventPublisher;
import com.gogidix.finance.accountsreceivable.domain.repository.InvoiceRepository;
import com.gogidix.finance.accountsreceivable.shared.exception.ConflictException;
import com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException;
import com.gogidix.finance.accountsreceivable.shared.exception.ValidationException;
import com.gogidix.finance.accountsreceivable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Invoice Command Service
 * Handles all write operations for invoices
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceCommandService {

    private final InvoiceRepository invoiceRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Invoice create(InvoiceCommand.CreateInvoiceCommand command) {
        log.info("Creating invoice for tenant: {}, number: {}",
            command.getTenantId(), command.getInvoiceNumber());

        // Check if invoice number already exists
        if (invoiceRepository.existsByInvoiceNumberAndTenantId(
            command.getInvoiceNumber(), command.getTenantId())) {
            throw new ConflictException("Invoice", command.getInvoiceNumber());
        }

        Invoice invoice = Invoice.create(
            command.getTenantId(),
            command.getCustomerId(),
            command.getCustomerName(),
            command.getInvoiceNumber(),
            command.getInvoiceType(),
            command.getInvoiceDate(),
            command.getDueDate(),
            command.getCurrency(),
            command.getLineItems()
        );

        // Set additional fields
        invoice.setCustomerEmail(command.getCustomerEmail());
        invoice.setSalesDate(command.getSalesDate());
        invoice.setPurchaseOrderNumber(command.getPurchaseOrderNumber());
        invoice.setBillingAddressLine1(command.getBillingAddressLine1());
        invoice.setBillingAddressLine2(command.getBillingAddressLine2());
        invoice.setBillingCity(command.getBillingCity());
        invoice.setBillingState(command.getBillingState());
        invoice.setBillingPostalCode(command.getBillingPostalCode());
        invoice.setBillingCountry(command.getBillingCountry());
        invoice.setShippingAddressLine1(command.getShippingAddressLine1());
        invoice.setShippingAddressLine2(command.getShippingAddressLine2());
        invoice.setShippingCity(command.getShippingCity());
        invoice.setShippingState(command.getShippingState());
        invoice.setShippingPostalCode(command.getShippingPostalCode());
        invoice.setShippingCountry(command.getShippingCountry());
        invoice.setPaymentTerms(command.getPaymentTerms());
        invoice.setNotes(command.getNotes());
        invoice.setInternalNotes(command.getInternalNotes());
        invoice.setSalesperson(command.getSalesperson());
        invoice.setProjectId(command.getProjectId());
        invoice.setDepartmentId(command.getDepartmentId());
        invoice.setLocationId(command.getLocationId());
        invoice.setTemplateId(command.getTemplateId());
        invoice.setTaxInclusive(command.getTaxInclusive());
        invoice.setTaxRegistered(command.getTaxRegistered());
        invoice.setTaxCode(command.getTaxCode());
        invoice.setTaxRate(command.getTaxRate());
        invoice.setShippingAmount(command.getShippingAmount() != null ? command.getShippingAmount() : BigDecimal.ZERO);
        invoice.setDiscountCode(command.getDiscountCode());
        invoice.setDiscountRate(command.getDiscountRate());
        invoice.setShippingMethod(command.getShippingMethod());
        invoice.setTrackingNumber(command.getTrackingNumber());
        invoice.setRecurringInvoiceId(command.getRecurringInvoiceId());
        invoice.setIsRecurring(command.getIsRecurring());
        invoice.setParentId(command.getParentId());
        invoice.setCustomerReference(command.getCustomerReference());
        invoice.setGroupId(command.getGroupId());
        invoice.setTags(command.getTags() != null ? command.getTags() : List.of());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        publishEvents(savedInvoice);

        log.info("Created invoice: {} for tenant: {}", savedInvoice.getInvoiceId(), command.getTenantId());
        return savedInvoice;
    }

    @Transactional
    public Invoice update(InvoiceCommand.UpdateInvoiceCommand command) {
        log.info("Updating invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        if (invoice.getStatus() != Invoice.InvoiceStatus.DRAFT) {
            throw new ValidationException("Can only update draft invoices");
        }

        if (command.getCustomerEmail() != null) {
            invoice.setCustomerEmail(command.getCustomerEmail());
        }
        if (command.getDueDate() != null) {
            invoice.setDueDate(command.getDueDate());
        }
        if (command.getPaymentTerms() != null) {
            invoice.setPaymentTerms(command.getPaymentTerms());
        }
        if (command.getNotes() != null) {
            invoice.setNotes(command.getNotes());
        }
        if (command.getInternalNotes() != null) {
            invoice.setInternalNotes(command.getInternalNotes());
        }
        if (command.getSalesperson() != null) {
            invoice.setSalesperson(command.getSalesperson());
        }
        if (command.getPurchaseOrderNumber() != null) {
            invoice.setPurchaseOrderNumber(command.getPurchaseOrderNumber());
        }
        if (command.getCustomerReference() != null) {
            invoice.setCustomerReference(command.getCustomerReference());
        }
        if (command.getTags() != null) {
            invoice.setTags(command.getTags());
        }
        if (command.getShippingMethod() != null) {
            invoice.setShippingMethod(command.getShippingMethod());
        }
        if (command.getTrackingNumber() != null) {
            invoice.setTrackingNumber(command.getTrackingNumber());
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
        publishEvents(savedInvoice);

        return savedInvoice;
    }

    @Transactional
    public void send(InvoiceCommand.SendInvoiceCommand command) {
        log.info("Sending invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.send();
        invoiceRepository.save(invoice);
        publishEvents(invoice);

        log.info("Sent invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void addLineItem(InvoiceCommand.AddLineItemCommand command) {
        log.info("Adding line item to invoice: {} for tenant: {}",
            command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.addLineItem(command.getLineItem());
        invoiceRepository.save(invoice);
        publishEvents(invoice);

        log.info("Added line item to invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void removeLineItem(InvoiceCommand.RemoveLineItemCommand command) {
        log.info("Removing line item from invoice: {} for tenant: {}",
            command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.removeLineItem(command.getLineItemId());
        invoiceRepository.save(invoice);
        publishEvents(invoice);

        log.info("Removed line item from invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void writeOff(InvoiceCommand.WriteOffInvoiceCommand command) {
        log.info("Writing off invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.writeOff(command.getAmount(), command.getReason());
        invoiceRepository.save(invoice);
        publishEvents(invoice);

        log.info("Written off invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void voidInvoice(InvoiceCommand.VoidInvoiceCommand command) {
        log.info("Voiding invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.voidInvoice();
        invoiceRepository.save(invoice);
        publishEvents(invoice);

        log.info("Voided invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void cancel(InvoiceCommand.CancelInvoiceCommand command) {
        log.info("Cancelling invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.cancel(command.getReason());
        invoiceRepository.save(invoice);
        publishEvents(invoice);

        log.info("Cancelled invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void sendReminder(InvoiceCommand.SendReminderCommand command) {
        log.info("Sending reminder for invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.sendReminder();
        invoiceRepository.save(invoice);
        publishEvents(invoice);

        log.info("Sent reminder for invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void applyFinanceCharge(InvoiceCommand.ApplyFinanceChargeCommand command) {
        log.info("Applying finance charge to invoice: {} for tenant: {}",
            command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.applyFinanceCharge(command.getRate());
        invoiceRepository.save(invoice);
        publishEvents(invoice);

        log.info("Applied finance charge to invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void delete(InvoiceCommand.DeleteInvoiceCommand command) {
        log.info("Deleting invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        if (invoice.getStatus() != Invoice.InvoiceStatus.DRAFT) {
            throw new ValidationException("Can only delete draft invoices");
        }

        invoiceRepository.deleteByInvoiceIdAndTenantId(command.getInvoiceId(), command.getTenantId());

        log.info("Deleted invoice: {}", command.getInvoiceId());
    }

    private void publishEvents(Invoice invoice) {
        if (!invoice.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(invoice.getDomainEvents().stream().map(e -> (Object) e).toList());
            invoice.clearDomainEvents();
        }
    }
}
