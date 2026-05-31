package com.gogidix.shared.infrastructure.services.billing.config;

import com.gogidix.shared.infrastructure.services.billing.domain.aggregate.BillingRegistry;
import com.gogidix.shared.infrastructure.services.billing.domain.event.*;
import com.gogidix.shared.infrastructure.services.billing.domain.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for billing domain layer.
 * Creates aggregate roots and wires up event handlers.
 */
@Configuration
public class BillingDomainConfig {

    private static final Logger log = LoggerFactory.getLogger(BillingDomainConfig.class);

    @Bean
    public BillingRegistry billingRegistry() {
        BillingRegistry registry = new BillingRegistry();

        // Register event handlers
        registry.onSubscriptionCreated(this::handleSubscriptionCreated);
        registry.onSubscriptionUpdated(this::handleSubscriptionUpdated);
        registry.onSubscriptionCancelled(this::handleSubscriptionCancelled);
        registry.onInvoiceCreated(this::handleInvoiceCreated);
        registry.onPaymentReceived(this::handlePaymentReceived);

        log.info("BillingRegistry aggregate initialized with event handlers");

        return registry;
    }

    private void handleSubscriptionCreated(SubscriptionCreatedEvent event) {
        log.info("Subscription created: subscriptionId={}, tenantId={}, plan={}, status={}",
            event.getSubscriptionId(), event.getTenantId(), event.getPlan(), event.getStatus());

        // Send welcome email, set up tenant resources, etc.
    }

    private void handleSubscriptionUpdated(SubscriptionUpdatedEvent event) {
        log.info("Subscription updated: subscriptionId={}, tenantId={}, changeType={} -> {}",
            event.getSubscriptionId(), event.getTenantId(), event.getOldValue(), event.getNewValue());

        // Handle plan changes, notify users, etc.
    }

    private void handleSubscriptionCancelled(SubscriptionCancelledEvent event) {
        log.info("Subscription cancelled: subscriptionId={}, tenantId={}, reason={}, immediate={}",
            event.getSubscriptionId(), event.getTenantId(), event.getReason(), event.isEffectiveImmediately());

        // Handle cancellation workflow, send confirmation, etc.
    }

    private void handleInvoiceCreated(InvoiceCreatedEvent event) {
        log.info("Invoice created: invoiceId={}, tenantId={}, amount={}, dueDate={}",
            event.getInvoiceId(), event.getTenantId(), event.getAmount(), event.getDueDate());

        // Send invoice notification, schedule payment reminders, etc.
    }

    private void handlePaymentReceived(PaymentReceivedEvent event) {
        log.info("Payment received: paymentId={}, tenantId={}, amount={}, method={}, status={}",
            event.getPaymentId(), event.getTenantId(), event.getAmount(), event.getPaymentMethod(), event.getStatus());

        // Send payment confirmation, update subscription status, etc.
    }
}
