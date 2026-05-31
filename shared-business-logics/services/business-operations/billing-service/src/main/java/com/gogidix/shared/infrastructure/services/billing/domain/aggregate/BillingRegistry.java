package com.gogidix.shared.infrastructure.services.billing.domain.aggregate;

import com.gogidix.shared.infrastructure.services.billing.domain.event.*;
import com.gogidix.shared.infrastructure.services.billing.domain.model.Invoice;
import com.gogidix.shared.infrastructure.services.billing.domain.model.Payment;
import com.gogidix.shared.infrastructure.services.billing.domain.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Billing Registry Aggregate Root.
 * Manages billing operations including subscriptions, invoices, and payments.
 * Publishes domain events for billing lifecycle events.
 */
public class BillingRegistry {

    private static final Logger log = LoggerFactory.getLogger(BillingRegistry.class);

    // In-memory storage (will be replaced by repository adapters)
    private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();
    private final Map<String, Invoice> invoices = new ConcurrentHashMap<>();
    private final Map<String, Payment> payments = new ConcurrentHashMap<>();

    // Event handlers
    private final Map<String, Consumer<SubscriptionCreatedEvent>> subscriptionCreatedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<SubscriptionUpdatedEvent>> subscriptionUpdatedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<SubscriptionCancelledEvent>> subscriptionCancelledHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<InvoiceCreatedEvent>> invoiceCreatedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<PaymentReceivedEvent>> paymentReceivedHandlers = new ConcurrentHashMap<>();

    public BillingRegistry() {
        log.info("BillingRegistry aggregate initialized");
    }

    // Event handler registration
    public void onSubscriptionCreated(Consumer<SubscriptionCreatedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        subscriptionCreatedHandlers.put(handlerId, handler);
    }

    public void onSubscriptionUpdated(Consumer<SubscriptionUpdatedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        subscriptionUpdatedHandlers.put(handlerId, handler);
    }

    public void onSubscriptionCancelled(Consumer<SubscriptionCancelledEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        subscriptionCancelledHandlers.put(handlerId, handler);
    }

    public void onInvoiceCreated(Consumer<InvoiceCreatedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        invoiceCreatedHandlers.put(handlerId, handler);
    }

    public void onPaymentReceived(Consumer<PaymentReceivedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        paymentReceivedHandlers.put(handlerId, handler);
    }

    // Subscription operations
    public Subscription createSubscription(String tenantId, Subscription.SubscriptionPlan plan,
                                          Subscription.BillingPeriod billingPeriod,
                                          String paymentMethodId) {
        log.info("Creating subscription: tenantId={}, plan={}, period={}",
                 tenantId, plan, billingPeriod);

        String subscriptionId = Subscription.generateSubscriptionId();
        LocalDateTime now = LocalDateTime.now();

        Subscription.SubscriptionStatus status = plan == Subscription.SubscriptionPlan.FREE
            ? Subscription.SubscriptionStatus.ACTIVE
            : Subscription.SubscriptionStatus.TRIAL;

        Subscription subscription = Subscription.builder()
            .subscriptionId(subscriptionId)
            .tenantId(tenantId)
            .plan(plan)
            .status(status)
            .billingPeriod(billingPeriod)
            .monthlyPrice(calculateMonthlyPrice(plan))
            .yearlyPrice(calculateYearlyPrice(plan))
            .startDate(now)
            .trialEndDate(plan == Subscription.SubscriptionPlan.FREE ? null : now.plusDays(14))
            .currency("USD")
            .maxUsers(getMaxUsersForPlan(plan))
            .maxStorageGB(getMaxStorageForPlan(plan))
            .autoRenew(true)
            .createdAt(now)
            .updatedAt(now)
            .paymentMethodId(paymentMethodId)
            .currentBalance(BigDecimal.ZERO)
            .build();

        subscription.calculateNextBillingDate();
        subscriptions.put(subscriptionId, subscription);

        publishEvent(new SubscriptionCreatedEvent(
            subscriptionId,
            tenantId,
            plan.name(),
            status.name(),
            billingPeriod.name(),
            subscription.getCurrentPrice(),
            now.toInstant(java.time.ZoneOffset.UTC)
        ));

        log.info("Subscription created successfully: subscriptionId={}", subscriptionId);
        return subscription;
    }

    public Optional<Subscription> updateSubscriptionPlan(String subscriptionId,
                                                         Subscription.SubscriptionPlan newPlan) {
        log.info("Updating subscription plan: subscriptionId={}, newPlan={}", subscriptionId, newPlan);

        Subscription subscription = subscriptions.get(subscriptionId);
        if (subscription == null) {
            log.warn("Subscription not found: {}", subscriptionId);
            return Optional.empty();
        }

        Subscription.SubscriptionPlan oldPlan = subscription.getPlan();
        subscription.setPlan(newPlan);
        subscription.setMonthlyPrice(calculateMonthlyPrice(newPlan));
        subscription.setYearlyPrice(calculateYearlyPrice(newPlan));
        subscription.setMaxUsers(getMaxUsersForPlan(newPlan));
        subscription.setMaxStorageGB(getMaxStorageForPlan(newPlan));
        subscription.setUpdatedAt(LocalDateTime.now());
        subscription.calculateNextBillingDate();

        publishEvent(new SubscriptionUpdatedEvent(
            subscriptionId,
            subscription.getTenantId(),
            "PLAN_CHANGE",
            oldPlan.name(),
            newPlan.name(),
            LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC)
        ));

        log.info("Subscription plan updated: subscriptionId={}, {}->{}", subscriptionId, oldPlan, newPlan);
        return Optional.of(subscription);
    }

    public boolean cancelSubscription(String subscriptionId, String reason, boolean effectiveImmediately) {
        log.info("Cancelling subscription: subscriptionId={}, immediate={}", subscriptionId, effectiveImmediately);

        Subscription subscription = subscriptions.get(subscriptionId);
        if (subscription == null) {
            log.warn("Subscription not found for cancellation: {}", subscriptionId);
            return false;
        }

        subscription.setStatus(Subscription.SubscriptionStatus.CANCELLED);
        subscription.setAutoRenew(false);

        if (effectiveImmediately) {
            subscription.setEndDate(LocalDateTime.now());
        } else {
            subscription.setEndDate(subscription.getNextBillingDate());
        }

        subscription.setUpdatedAt(LocalDateTime.now());

        publishEvent(new SubscriptionCancelledEvent(
            subscriptionId,
            subscription.getTenantId(),
            reason,
            effectiveImmediately,
            LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC),
            LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC)
        ));

        log.info("Subscription cancelled: subscriptionId={}", subscriptionId);
        return true;
    }

    // Invoice operations
    public Invoice createInvoice(String tenantId, String subscriptionId,
                                List<Invoice.InvoiceLineItem> lineItems,
                                LocalDateTime dueDate) {
        log.info("Creating invoice: tenantId={}, subscriptionId={}", tenantId, subscriptionId);

        String invoiceId = Invoice.generateInvoiceId();
        LocalDateTime now = LocalDateTime.now();

        Subscription subscription = subscriptions.get(subscriptionId);
        Invoice.BillingPeriod billingPeriod = subscription != null
            ? Invoice.BillingPeriod.valueOf(subscription.getBillingPeriod().name())
            : Invoice.BillingPeriod.ONE_TIME;

        Invoice invoice = Invoice.builder()
            .invoiceId(invoiceId)
            .tenantId(tenantId)
            .subscriptionId(subscriptionId)
            .invoiceNumber(Invoice.generateInvoiceNumber())
            .status(Invoice.InvoiceStatus.PENDING)
            .issueDate(now)
            .dueDate(dueDate)
            .subtotal(BigDecimal.ZERO)
            .taxAmount(BigDecimal.ZERO)
            .discountAmount(BigDecimal.ZERO)
            .totalAmount(BigDecimal.ZERO)
            .amountPaid(BigDecimal.ZERO)
            .balanceDue(BigDecimal.ZERO)
            .currency("USD")
            .billingPeriod(billingPeriod)
            .lineItems(new ArrayList<>())
            .payments(new ArrayList<>())
            .createdAt(now)
            .updatedAt(now)
            .build();

        if (lineItems != null) {
            invoice.getLineItems().addAll(lineItems);
        }

        invoice.recalculateTotals();
        invoices.put(invoiceId, invoice);

        publishEvent(new InvoiceCreatedEvent(
            invoiceId,
            tenantId,
            subscriptionId,
            invoice.getInvoiceNumber(),
            invoice.getTotalAmount(),
            invoice.getCurrency(),
            dueDate.toInstant(java.time.ZoneOffset.UTC),
            now.toInstant(java.time.ZoneOffset.UTC)
        ));

        log.info("Invoice created: invoiceId={}, amount={}", invoiceId, invoice.getTotalAmount());
        return invoice;
    }

    // Payment operations
    public Payment processPayment(String invoiceId, BigDecimal amount, Payment.PaymentMethod method,
                                 String paymentMethodId, String gateway, String cardLastFour, String cardBrand) {
        log.info("Processing payment: invoiceId={}, amount={}, method={}", invoiceId, amount, method);

        Invoice invoice = invoices.get(invoiceId);
        if (invoice == null) {
            log.warn("Invoice not found for payment: {}", invoiceId);
            return null;
        }

        String paymentId = Payment.generatePaymentId();
        LocalDateTime now = LocalDateTime.now();

        Payment payment = Payment.builder()
            .paymentId(paymentId)
            .tenantId(invoice.getTenantId())
            .invoiceId(invoiceId)
            .subscriptionId(invoice.getSubscriptionId())
            .status(Payment.PaymentStatus.PROCESSING)
            .method(method)
            .amount(amount)
            .currency(invoice.getCurrency())
            .transactionDate(now)
            .transactionReference(Payment.generateTransactionReference())
            .gateway(gateway)
            .paymentMethodId(paymentMethodId)
            .cardLastFour(cardLastFour)
            .cardBrand(cardBrand)
            .createdAt(now)
            .updatedAt(now)
            .build();

        // Simulate payment processing
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setProcessedDate(now);

        payments.put(paymentId, payment);

        // Update invoice
        invoice.setAmountPaid(invoice.getAmountPaid().add(amount));
        invoice.recalculateTotals();

        if (invoice.getBalanceDue().compareTo(BigDecimal.ZERO) <= 0) {
            invoice.markAsPaid(now);
        }

        publishEvent(new PaymentReceivedEvent(
            paymentId,
            invoice.getTenantId(),
            invoiceId,
            amount,
            invoice.getCurrency(),
            method.name(),
            Payment.PaymentStatus.COMPLETED.name(),
            now.toInstant(java.time.ZoneOffset.UTC)
        ));

        log.info("Payment processed successfully: paymentId={}, amount={}", paymentId, amount);
        return payment;
    }

    // Query operations
    public Optional<Subscription> getSubscription(String subscriptionId) {
        return Optional.ofNullable(subscriptions.get(subscriptionId));
    }

    public Optional<Subscription> getTenantSubscription(String tenantId) {
        return subscriptions.values().stream()
            .filter(s -> s.getTenantId().equals(tenantId))
            .filter(Subscription::isActive)
            .findFirst();
    }

    public Optional<Invoice> getInvoice(String invoiceId) {
        return Optional.ofNullable(invoices.get(invoiceId));
    }

    public List<Invoice> getTenantInvoices(String tenantId) {
        return invoices.values().stream()
            .filter(i -> i.getTenantId().equals(tenantId))
            .sorted((a, b) -> b.getIssueDate().compareTo(a.getIssueDate()))
            .toList();
    }

    public List<Payment> getInvoicePayments(String invoiceId) {
        return payments.values().stream()
            .filter(p -> p.getInvoiceId().equals(invoiceId))
            .toList();
    }

    // Helper methods for pricing
    private BigDecimal calculateMonthlyPrice(Subscription.SubscriptionPlan plan) {
        return switch (plan) {
            case FREE -> BigDecimal.ZERO;
            case STARTER -> new BigDecimal("29.00");
            case PROFESSIONAL -> new BigDecimal("99.00");
            case ENTERPRISE -> new BigDecimal("499.00");
            case CUSTOM -> new BigDecimal("0.00");
        };
    }

    private BigDecimal calculateYearlyPrice(Subscription.SubscriptionPlan plan) {
        BigDecimal monthly = calculateMonthlyPrice(plan);
        return monthly.multiply(new BigDecimal("12")).multiply(new BigDecimal("0.8")); // 20% discount
    }

    private int getMaxUsersForPlan(Subscription.SubscriptionPlan plan) {
        return switch (plan) {
            case FREE -> 5;
            case STARTER -> 25;
            case PROFESSIONAL -> 100;
            case ENTERPRISE, CUSTOM -> -1; // Unlimited
        };
    }

    private long getMaxStorageForPlan(Subscription.SubscriptionPlan plan) {
        return switch (plan) {
            case FREE -> 1L;
            case STARTER -> 10L;
            case PROFESSIONAL -> 100L;
            case ENTERPRISE, CUSTOM -> -1L; // Unlimited
        };
    }

    // Event publishing
    private void publishEvent(SubscriptionCreatedEvent event) {
        subscriptionCreatedHandlers.values().forEach(h -> h.accept(event));
    }

    private void publishEvent(SubscriptionUpdatedEvent event) {
        subscriptionUpdatedHandlers.values().forEach(h -> h.accept(event));
    }

    private void publishEvent(SubscriptionCancelledEvent event) {
        subscriptionCancelledHandlers.values().forEach(h -> h.accept(event));
    }

    private void publishEvent(InvoiceCreatedEvent event) {
        invoiceCreatedHandlers.values().forEach(h -> h.accept(event));
    }

    private void publishEvent(PaymentReceivedEvent event) {
        paymentReceivedHandlers.values().forEach(h -> h.accept(event));
    }
}
