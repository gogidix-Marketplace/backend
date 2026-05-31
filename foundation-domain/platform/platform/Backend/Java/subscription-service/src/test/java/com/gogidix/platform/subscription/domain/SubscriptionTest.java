package com.gogidix.platform.subscription.domain;

import com.gogidix.platform.subscription.domain.model.Subscription;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for Subscription domain model.
 */
class SubscriptionTest {

    @Test
    void testSubscriptionCreation() {
        Subscription subscription = Subscription.builder()
            .tenantId("tenant-1")
            .customerId("customer-1")
            .planId("plan-1")
            .status(Subscription.SubscriptionStatus.ACTIVE)
            .billingCycle(Subscription.BillingCycle.MONTHLY)
            .basePrice(new BigDecimal("99.00"))
            .totalAmount(new BigDecimal("99.00"))
            .currentPeriodStart(LocalDateTime.now())
            .currentPeriodEnd(LocalDateTime.now().plusMonths(1))
            .build();

        assertNotNull(subscription);
        assertEquals("tenant-1", subscription.getTenantId());
        assertEquals(Subscription.SubscriptionStatus.ACTIVE, subscription.getStatus());
        assertTrue(subscription.isActive());
    }

    @Test
    void testTrialPeriod() {
        Subscription subscription = Subscription.builder()
            .tenantId("tenant-1")
            .customerId("customer-1")
            .planId("plan-1")
            .isTrial(true)
            .trialStart(LocalDate.now())
            .trialEnd(LocalDate.now().plusDays(14))
            .build();

        assertTrue(subscription.isInTrial());
        assertFalse(subscription.isTrialExpired());
    }

    @Test
    void testCancelSubscription() {
        Subscription subscription = Subscription.builder()
            .tenantId("tenant-1")
            .customerId("customer-1")
            .planId("plan-1")
            .status(Subscription.SubscriptionStatus.ACTIVE)
            .build();

        subscription.cancel("Customer request");

        assertEquals(Subscription.SubscriptionStatus.CANCELLED, subscription.getStatus());
        assertEquals("Customer request", subscription.getCancellationReason());
        assertNotNull(subscription.getCancelledAt());
    }
}
