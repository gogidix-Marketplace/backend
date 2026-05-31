package com.gogidix.finance.accountspayable.application.service;

import com.gogidix.finance.accountspayable.domain.event.InvoiceApprovedEvent;
import com.gogidix.finance.accountspayable.domain.model.Invoice;
import com.gogidix.finance.accountspayable.domain.port.in.InvoiceCommand;
import com.gogidix.finance.accountspayable.domain.port.out.EventPublisher;
import com.gogidix.finance.accountspayable.domain.repository.InvoiceRepository;
import com.gogidix.finance.accountspayable.shared.exception.ConflictException;
import com.gogidix.finance.accountspayable.shared.exception.NotFoundException;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
        log.info("Creating invoice for tenant: {}, vendor: {}, number: {}",
            command.getTenantId(), command.getVendorId(), command.getInvoiceNumber());

        // Check if invoice number already exists
        if (invoiceRepository.existsByInvoiceNumberAndTenantId(
                command.getInvoiceNumber(), command.getTenantId())) {
            throw new ConflictException("Invoice", command.getInvoiceNumber());
        }

        Invoice invoice = Invoice.create(
            command.getTenantId(),
            command.getVendorId(),
            command.getVendorName(),
            command.getInvoiceNumber(),
            command.getInvoiceDate(),
            command.getDueDate(),
            command.getAmount(),
            command.getCurrency(),
            command.getSubmittedBy()
        );

        // Set additional fields
        invoice.setPurchaseOrderNumber(command.getPurchaseOrderNumber());
        invoice.setDescription(command.getDescription());
        invoice.setNotes(command.getNotes());
        invoice.setInternalReference(command.getInternalReference());
        invoice.setDepartment(command.getDepartment());
        invoice.setCostCenter(command.getCostCenter());
        invoice.setProjectId(command.getProjectId());
        invoice.setAttachments(command.getAttachments());
        invoice.setTags(command.getTags());
        invoice.setRequiresApproval(command.getRequiresApproval());
        invoice.setGlAccount(command.getGlAccount());
        invoice.setTaxCode(command.getTaxCode());
        invoice.setTaxIncluded(command.getTaxIncluded());
        invoice.setDiscountValidUntil(command.getDiscountValidUntil());
        invoice.setPaymentTerms(command.getPaymentTerms());

        // Add line items if provided
        if (command.getLineItems() != null) {
            for (var lineItem : command.getLineItems()) {
                invoice.addLineItem(lineItem);
            }
        }

        // Calculate tax if rate provided
        if (command.getTaxRate() != null) {
            invoice.calculateTax(command.getTaxRate());
        }

        // Calculate discount if percentage provided
        if (command.getDiscountPercentage() != null) {
            invoice.calculateDiscount(command.getDiscountPercentage());
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
        publishInvoiceEvents(savedInvoice);

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
            throw new IllegalStateException("Can only update draft invoices");
        }

        if (command.getDescription() != null) {
            invoice.setDescription(command.getDescription());
        }
        if (command.getAmount() != null) {
            invoice.setAmount(command.getAmount());
            invoice.calculateNetAmount();
        }
        if (command.getDueDate() != null) {
            invoice.setDueDate(command.getDueDate());
        }
        if (command.getNotes() != null) {
            invoice.setNotes(command.getNotes());
        }
        if (command.getInternalReference() != null) {
            invoice.setInternalReference(command.getInternalReference());
        }
        if (command.getDepartment() != null) {
            invoice.setDepartment(command.getDepartment());
        }
        if (command.getCostCenter() != null) {
            invoice.setCostCenter(command.getCostCenter());
        }
        if (command.getProjectId() != null) {
            invoice.setProjectId(command.getProjectId());
        }
        if (command.getTags() != null) {
            invoice.setTags(command.getTags());
        }
        if (command.getAttachments() != null) {
            invoice.setAttachments(command.getAttachments());
        }
        if (command.getTaxRate() != null) {
            invoice.calculateTax(command.getTaxRate());
        }
        if (command.getDiscountPercentage() != null) {
            invoice.calculateDiscount(command.getDiscountPercentage());
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
        publishInvoiceEvents(savedInvoice);

        return savedInvoice;
    }

    @Transactional
    public void submit(InvoiceCommand.SubmitInvoiceCommand command) {
        log.info("Submitting invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.submit();
        invoiceRepository.save(invoice);
        publishInvoiceEvents(invoice);

        log.info("Submitted invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void approve(InvoiceCommand.ApproveInvoiceCommand command) {
        log.info("Approving invoice: {} by: {} for tenant: {}",
            command.getInvoiceId(), command.getApprover(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.approve(command.getApprover(), command.getApprovalLevel());
        invoiceRepository.save(invoice);
        publishInvoiceEvents(invoice);

        // Publish approval specific event
        if (invoice.getStatus() == Invoice.InvoiceStatus.APPROVED) {
            InvoiceApprovedEvent approvalEvent = InvoiceApprovedEvent.create(
                invoice.getInvoiceId(),
                invoice.getTenantId(),
                invoice.getVendorId(),
                invoice.getInvoiceNumber(),
                invoice.getAmount(),
                invoice.getCurrency(),
                command.getApprover(),
                command.getApprovalLevel().name()
            );
            eventPublisher.publishInvoiceApprovalEvent(approvalEvent);
        }

        log.info("Approved invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void reject(InvoiceCommand.RejectInvoiceCommand command) {
        log.info("Rejecting invoice: {} by: {} for tenant: {}",
            command.getInvoiceId(), command.getRejecter(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.reject(command.getRejecter(), command.getReason());
        invoiceRepository.save(invoice);
        publishInvoiceEvents(invoice);

        log.info("Rejected invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void markAsPaid(InvoiceCommand.MarkAsPaidCommand command) {
        log.info("Marking invoice as paid: {} for tenant: {}",
            command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        String paymentRef = command.getPaymentReference() != null
            ? command.getPaymentReference()
            : "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        invoice.markAsPaid(paymentRef);
        invoiceRepository.save(invoice);
        publishInvoiceEvents(invoice);

        log.info("Marked invoice as paid: {}", command.getInvoiceId());
    }

    @Transactional
    public void markAsPartiallyPaid(InvoiceCommand.MarkAsPartiallyPaidCommand command) {
        log.info("Marking invoice as partially paid: {} for tenant: {}",
            command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.markAsPartiallyPaid(command.getPaymentReference(), command.getAmountPaid());
        invoiceRepository.save(invoice);
        publishInvoiceEvents(invoice);

        log.info("Marked invoice as partially paid: {}", command.getInvoiceId());
    }

    @Transactional
    public void cancel(InvoiceCommand.CancelInvoiceCommand command) {
        log.info("Cancelling invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.cancel(command.getReason());
        invoiceRepository.save(invoice);
        publishInvoiceEvents(invoice);

        log.info("Cancelled invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void addLineItem(InvoiceCommand.AddLineItemCommand command) {
        log.info("Adding line item to invoice: {}", command.getInvoiceId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.addLineItem(command.getLineItem());
        invoiceRepository.save(invoice);

        log.info("Added line item to invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void removeLineItem(InvoiceCommand.RemoveLineItemCommand command) {
        log.info("Removing line item from invoice: {}", command.getInvoiceId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.removeLineItem(command.getLineItemId());
        invoiceRepository.save(invoice);

        log.info("Removed line item from invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void addAttachment(InvoiceCommand.AddAttachmentCommand command) {
        log.info("Adding attachment to invoice: {}", command.getInvoiceId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.addAttachment(command.getAttachmentUrl());
        invoiceRepository.save(invoice);

        log.info("Added attachment to invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void calculateTax(InvoiceCommand.CalculateTaxCommand command) {
        log.info("Calculating tax for invoice: {}", command.getInvoiceId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.calculateTax(command.getTaxRate());
        invoiceRepository.save(invoice);

        log.info("Calculated tax for invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void calculateDiscount(InvoiceCommand.CalculateDiscountCommand command) {
        log.info("Calculating discount for invoice: {}", command.getInvoiceId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        invoice.calculateDiscount(command.getDiscountPercentage());
        invoiceRepository.save(invoice);

        log.info("Calculated discount for invoice: {}", command.getInvoiceId());
    }

    @Transactional
    public void delete(InvoiceCommand.DeleteInvoiceCommand command) {
        log.info("Deleting invoice: {} for tenant: {}", command.getInvoiceId(), command.getTenantId());

        Invoice invoice = invoiceRepository.findByInvoiceIdAndTenantId(
            command.getInvoiceId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Invoice", command.getInvoiceId()));

        if (invoice.getStatus() != Invoice.InvoiceStatus.DRAFT &&
            invoice.getStatus() != Invoice.InvoiceStatus.REJECTED) {
            throw new IllegalStateException("Can only delete draft or rejected invoices");
        }

        invoiceRepository.deleteByInvoiceIdAndTenantId(command.getInvoiceId(), command.getTenantId());

        log.info("Deleted invoice: {}", command.getInvoiceId());
    }

    private void publishInvoiceEvents(Invoice invoice) {
        if (!invoice.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            for (var event : invoice.getDomainEvents()) {
                eventPublisher.publishInvoiceEvent(event);
            }
            invoice.clearDomainEvents();
        }
    }
}
