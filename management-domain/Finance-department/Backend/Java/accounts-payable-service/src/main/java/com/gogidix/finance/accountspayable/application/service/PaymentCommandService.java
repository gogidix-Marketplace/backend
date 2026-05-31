package com.gogidix.finance.accountspayable.application.service;

import com.gogidix.finance.accountspayable.domain.model.Payment;
import com.gogidix.finance.accountspayable.domain.port.in.PaymentCommand;
import com.gogidix.finance.accountspayable.domain.port.out.EventPublisher;
import com.gogidix.finance.accountspayable.domain.repository.PaymentRepository;
import com.gogidix.finance.accountspayable.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Payment Command Service
 * Handles all write operations for payments
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentCommandService {

    private final PaymentRepository paymentRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Payment create(PaymentCommand.CreatePaymentCommand command) {
        log.info("Creating payment for tenant: {}, vendor: {}, amount: {}",
            command.getTenantId(), command.getVendorId(), command.getAmount());

        Payment payment = Payment.create(
            command.getTenantId(),
            command.getVendorId(),
            command.getVendorName(),
            command.getInvoiceIds(),
            command.getAmount(),
            command.getCurrency(),
            command.getPaymentMethod(),
            command.getCreatedBy()
        );

        // Set additional fields
        payment.setPaymentDate(command.getPaymentDate());
        payment.setDescription(command.getDescription());
        payment.setNotes(command.getNotes());
        payment.setBankAccountNumber(command.getBankAccountNumber());
        payment.setBankRoutingNumber(command.getBankRoutingNumber());
        payment.setCheckNumber(command.getCheckNumber());
        payment.setBatchId(command.getBatchId());
        payment.setFeeAmount(command.getFeeAmount());
        payment.setAttachmentUrl(command.getAttachmentUrl());

        // Set currency conversion details if provided
        if (command.getExchangeRate() != null) {
            payment.setCurrencyConversion(
                command.getExchangeRate(),
                command.getOriginalCurrency(),
                command.getOriginalAmount()
            );
        }

        Payment savedPayment = paymentRepository.save(payment);
        publishPaymentEvents(savedPayment);

        log.info("Created payment: {} for tenant: {}", savedPayment.getPaymentId(), command.getTenantId());
        return savedPayment;
    }

    @Transactional
    public void schedule(PaymentCommand.SchedulePaymentCommand command) {
        log.info("Scheduling payment: {} for date: {}", command.getPaymentId(), command.getScheduledDate());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.schedule(command.getScheduledDate());
        paymentRepository.save(payment);
        publishPaymentEvents(payment);

        log.info("Scheduled payment: {}", command.getPaymentId());
    }

    @Transactional
    public void process(PaymentCommand.ProcessPaymentCommand command) {
        log.info("Processing payment: {} by: {} for tenant: {}",
            command.getPaymentId(), command.getProcessedBy(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.process(command.getProcessedBy(), command.getPaymentReference());
        paymentRepository.save(payment);
        publishPaymentEvents(payment);

        log.info("Processing payment: {}", command.getPaymentId());
    }

    @Transactional
    public void complete(PaymentCommand.CompletePaymentCommand command) {
        log.info("Completing payment: {} for tenant: {}", command.getPaymentId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        String transactionRef = command.getTransactionReference() != null
            ? command.getTransactionReference()
            : "TXN-" + System.currentTimeMillis();

        payment.complete(transactionRef);
        paymentRepository.save(payment);
        publishPaymentEvents(payment);

        log.info("Completed payment: {}", command.getPaymentId());
    }

    @Transactional
    public void fail(PaymentCommand.FailPaymentCommand command) {
        log.info("Failing payment: {} for tenant: {}", command.getPaymentId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.fail(command.getReason());
        paymentRepository.save(payment);
        publishPaymentEvents(payment);

        log.info("Failed payment: {}", command.getPaymentId());
    }

    @Transactional
    public void cancel(PaymentCommand.CancelPaymentCommand command) {
        log.info("Cancelling payment: {} by: {} for tenant: {}",
            command.getPaymentId(), command.getCancelledBy(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.cancel(command.getCancelledBy(), command.getReason());
        paymentRepository.save(payment);
        publishPaymentEvents(payment);

        log.info("Cancelled payment: {}", command.getPaymentId());
    }

    @Transactional
    public void reverse(PaymentCommand.ReversePaymentCommand command) {
        log.info("Reversing payment: {} for tenant: {}", command.getPaymentId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.reverse(command.getReason());
        paymentRepository.save(payment);
        publishPaymentEvents(payment);

        log.info("Reversed payment: {}", command.getPaymentId());
    }

    @Transactional
    public void addAllocation(PaymentCommand.AddAllocationCommand command) {
        log.info("Adding allocation to payment: {} for invoice: {}",
            command.getPaymentId(), command.getInvoiceId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.addAllocation(
            command.getInvoiceId(),
            command.getInvoiceNumber(),
            command.getAmount()
        );
        paymentRepository.save(payment);

        log.info("Added allocation to payment: {}", command.getPaymentId());
    }

    @Transactional
    public void setPaymentMethodDetails(PaymentCommand.SetPaymentMethodDetailsCommand command) {
        log.info("Setting payment method details for payment: {}", command.getPaymentId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.setPaymentMethodDetails(
            command.getBankAccountNumber(),
            command.getBankRoutingNumber(),
            command.getCheckNumber()
        );
        paymentRepository.save(payment);

        log.info("Set payment method details for payment: {}", command.getPaymentId());
    }

    @Transactional
    public void setFee(PaymentCommand.SetFeeCommand command) {
        log.info("Setting fee for payment: {}", command.getPaymentId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.setFee(command.getFeeAmount());
        paymentRepository.save(payment);

        log.info("Set fee for payment: {}", command.getPaymentId());
    }

    @Transactional
    public void setCurrencyConversion(PaymentCommand.SetCurrencyConversionCommand command) {
        log.info("Setting currency conversion for payment: {}", command.getPaymentId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.setCurrencyConversion(
            command.getExchangeRate(),
            command.getOriginalCurrency(),
            command.getOriginalAmount()
        );
        paymentRepository.save(payment);

        log.info("Set currency conversion for payment: {}", command.getPaymentId());
    }

    @Transactional
    public void delete(PaymentCommand.DeletePaymentCommand command) {
        log.info("Deleting payment: {} for tenant: {}", command.getPaymentId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        if (payment.getStatus() == Payment.PaymentStatus.COMPLETED ||
            payment.getStatus() == Payment.PaymentStatus.REVERSED) {
            throw new IllegalStateException("Cannot delete completed or reversed payments");
        }

        paymentRepository.deleteByPaymentIdAndTenantId(command.getPaymentId(), command.getTenantId());

        log.info("Deleted payment: {}", command.getPaymentId());
    }

    private void publishPaymentEvents(Payment payment) {
        if (!payment.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            for (var event : payment.getDomainEvents()) {
                eventPublisher.publishPaymentEvent(event);
            }
            payment.clearDomainEvents();
        }
    }
}
