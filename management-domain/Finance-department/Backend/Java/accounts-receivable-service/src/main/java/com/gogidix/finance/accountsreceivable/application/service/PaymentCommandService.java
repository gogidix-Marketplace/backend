package com.gogidix.finance.accountsreceivable.application.service;

import com.gogidix.finance.accountsreceivable.domain.model.Payment;
import com.gogidix.finance.accountsreceivable.domain.port.in.PaymentCommand;
import com.gogidix.finance.accountsreceivable.domain.port.out.EventPublisher;
import com.gogidix.finance.accountsreceivable.domain.repository.PaymentRepository;
import com.gogidix.finance.accountsreceivable.shared.exception.ConflictException;
import com.gogidix.finance.accountsreceivable.shared.exception.NotFoundException;
import com.gogidix.finance.accountsreceivable.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        log.info("Creating payment for tenant: {}, customer: {}",
            command.getTenantId(), command.getCustomerId());

        validatePaymentAmount(command.getAmount());

        Payment payment = Payment.create(
            command.getTenantId(),
            command.getCustomerId(),
            command.getCustomerName(),
            command.getInvoiceId(),
            command.getInvoiceNumber(),
            command.getPaymentType(),
            command.getAmount(),
            command.getCurrency(),
            command.getPaymentDate(),
            command.getPaymentMethod()
        );

        // Set additional fields
        payment.setReferenceNumber(command.getReferenceNumber());
        payment.setBankAccount(command.getBankAccount());
        payment.setCheckNumber(command.getCheckNumber());
        payment.setCreditCardNumber(command.getCreditCardNumber());
        payment.setDescription(command.getDescription());
        payment.setNotes(command.getNotes());
        payment.setDepositDate(command.getDepositDate());
        payment.setDepositSlipNumber(command.getDepositSlipNumber());
        payment.setBatchId(command.getBatchId());
        payment.setExchangeRate(command.getExchangeRate());
        payment.setBaseCurrency(command.getBaseCurrency());
        payment.setTags(command.getTags() != null ? command.getTags() : List.of());

        Payment savedPayment = paymentRepository.save(payment);
        publishEvents(savedPayment);

        log.info("Created payment: {} for tenant: {}", savedPayment.getPaymentId(), command.getTenantId());
        return savedPayment;
    }

    @Transactional
    public void complete(PaymentCommand.CompletePaymentCommand command) {
        log.info("Completing payment: {} for tenant: {}", command.getPaymentId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.complete(command.getTransactionId());
        paymentRepository.save(payment);
        publishEvents(payment);

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
        publishEvents(payment);

        log.info("Failed payment: {}", command.getPaymentId());
    }

    @Transactional
    public void apply(PaymentCommand.ApplyPaymentCommand command) {
        log.info("Applying payment: {} to invoice: {} for tenant: {}",
            command.getPaymentId(), command.getInvoiceId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.applyToInvoice(command.getInvoiceId(), command.getInvoiceNumber(), command.getAmount());
        paymentRepository.save(payment);
        publishEvents(payment);

        log.info("Applied payment: {} to invoice: {}", command.getPaymentId(), command.getInvoiceId());
    }

    @Transactional
    public void reverse(PaymentCommand.ReversePaymentCommand command) {
        log.info("Reversing payment: {} for tenant: {}", command.getPaymentId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.reverse(command.getReason());
        paymentRepository.save(payment);
        publishEvents(payment);

        log.info("Reversed payment: {}", command.getPaymentId());
    }

    @Transactional
    public void refund(PaymentCommand.RefundPaymentCommand command) {
        log.info("Refunding payment: {} for tenant: {}", command.getPaymentId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.refund(command.getAmount(), command.getReason());
        paymentRepository.save(payment);
        publishEvents(payment);

        log.info("Refunded payment: {}", command.getPaymentId());
    }

    @Transactional
    public void reconcile(PaymentCommand.ReconcilePaymentCommand command) {
        log.info("Reconciling payment: {} for tenant: {}", command.getPaymentId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        payment.reconcile(command.getReconciledBy());
        paymentRepository.save(payment);

        log.info("Reconciled payment: {}", command.getPaymentId());
    }

    @Transactional
    public void delete(PaymentCommand.DeletePaymentCommand command) {
        log.info("Deleting payment: {} for tenant: {}", command.getPaymentId(), command.getTenantId());

        Payment payment = paymentRepository.findByPaymentIdAndTenantId(
            command.getPaymentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Payment", command.getPaymentId()));

        if (payment.getStatus() == Payment.PaymentStatus.COMPLETED ||
            payment.getStatus() == Payment.PaymentStatus.FULLY_APPLIED) {
            throw new ValidationException("Cannot delete completed or fully applied payments");
        }

        paymentRepository.deleteByPaymentIdAndTenantId(command.getPaymentId(), command.getTenantId());

        log.info("Deleted payment: {}", command.getPaymentId());
    }

    private void validatePaymentAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount");
        }
        if (amount.compareTo(new BigDecimal("1000000")) > 0) {
            throw new ValidationException("amount");
        }
    }

    private void publishEvents(Payment payment) {
        if (!payment.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(payment.getDomainEvents().stream().map(e -> (Object) e).toList());
            payment.clearDomainEvents();
        }
    }
}
