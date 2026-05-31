package com.gogidix.platform.subscription.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Domain model tests for Subscription.
 * Tests subscription lifecycle, billing, and business rules.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Subscription Domain Model Tests")
class SubscriptionTest {

    private Subscription subscription;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        subscription = Subscription.builder()
            .id("sub-123")
            .tenantId("tenant-123")
            .subscriptionNumber("SUB-1234567890-123")
            .customerId("customer-123")
            .planId("plan-pro")
            .status(Subscription.SubscriptionStatus.ACTIVE)
            .trialStart(null)
            .trialEnd(null)
            .currentPeriodStart(now)
            .currentPeriodEnd(now.plusMonths(1))
            .billingCycle(Subscription.BillingCycle.MONTHLY)
            .basePrice(new BigDecimal("99.00"))
            .discountPercentage(BigDecimal.ZERO)
            .taxRate(new BigDecimal("0.10"))
            .totalAmount(new BigDecimal("108.90"))
            .currency("USD")
            .currentUsers(5)
            .currentStorageGb(new BigDecimal("2.5"))
            .isTrial(false)
            .autoRenew(true)
            .cancelAtPeriodEnd(false)
            .build();
    }

    @Test
    @DisplayName("Should identify subscription as active")
    void isActive_ReturnsTrue() {
        // Act
        boolean result = subscription.isActive();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should identify subscription as not active when cancelled")
    void isActive_Cancelled_ReturnsFalse() {
        // Arrange
        subscription.setStatus(Subscription.SubscriptionStatus.CANCELLED);

        // Act
        boolean result = subscription.isActive();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should start trial successfully")
    void startTrial_Success() {
        // Act
        subscription.startTrial(14);

        // Assert
        assertThat(subscription.isTrial()).isTrue();
        assertThat(subscription.getTrialStart()).isEqualTo(LocalDate.now());
        assertThat(subscription.getTrialEnd()).isEqualTo(LocalDate.now().plusDays(14));
        assertThat(subscription.getTrialDaysRemaining()).isEqualTo(14);
        assertThat(subscription.getStatus()).isEqualTo(Subscription.SubscriptionStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should identify subscription as in trial")
    void isInTrial_ReturnsTrue() {
        // Arrange
        subscription.startTrial(14);

        // Act
        boolean result = subscription.isInTrial();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should identify trial as expired")
    void isTrialExpired_ReturnsTrue() {
        // Arrange
        subscription.setTrial(true);
        subscription.setTrialStart(LocalDate.now().minusDays(20));
        subscription.setTrialEnd(LocalDate.now().minusDays(6));

        // Act
        boolean result = subscription.isTrialExpired();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should end trial successfully")
    void endTrial_Success() {
        // Arrange
        subscription.startTrial(14);

        // Act
        subscription.endTrial();

        // Assert
        assertThat(subscription.isTrial()).isFalse();
        assertThat(subscription.getTrialDaysRemaining()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should cancel subscription successfully")
    void cancel_Success() {
        // Act
        subscription.cancel("Too expensive");

        // Assert
        assertThat(subscription.getStatus()).isEqualTo(Subscription.SubscriptionStatus.CANCELLED);
        assertThat(subscription.getCancelledAt()).isNotNull();
        assertThat(subscription.getCancellationReason()).isEqualTo("Too expensive");
    }

    @Test
    @DisplayName("Should throw exception when cancelling already cancelled subscription")
    void cancel_AlreadyCancelled_ThrowsException() {
        // Arrange
        subscription.setStatus(Subscription.SubscriptionStatus.CANCELLED);

        // Act & Assert
        assertThatThrownBy(() -> subscription.cancel("Reason"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Cannot cancel subscription with status");
    }

    @Test
    @DisplayName("Should throw exception when cancelling expired subscription")
    void cancel_Expired_ThrowsException() {
        // Arrange
        subscription.setStatus(Subscription.SubscriptionStatus.EXPIRED);

        // Act & Assert
        assertThatThrownBy(() -> subscription.cancel("Reason"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Cannot cancel subscription with status");
    }

    @Test
    @DisplayName("Should renew monthly subscription")
    void renew_Monthly_Success() {
        // Arrange
        LocalDateTime oldEnd = subscription.getCurrentPeriodEnd();

        // Act
        subscription.renew();

        // Assert
        assertThat(subscription.getCurrentPeriodStart()).isEqualTo(oldEnd);
        assertThat(subscription.getCurrentPeriodEnd()).isEqualTo(oldEnd.plusMonths(1));
        assertThat(subscription.getStatus()).isEqualTo(Subscription.SubscriptionStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should renew quarterly subscription")
    void renew_Quarterly_Success() {
        // Arrange
        subscription.setBillingCycle(Subscription.BillingCycle.QUARTERLY);
        LocalDateTime oldEnd = subscription.getCurrentPeriodEnd();

        // Act
        subscription.renew();

        // Assert
        assertThat(subscription.getCurrentPeriodEnd()).isEqualTo(oldEnd.plusMonths(3));
    }

    @Test
    @DisplayName("Should renew annual subscription")
    void renew_Annual_Success() {
        // Arrange
        subscription.setBillingCycle(Subscription.BillingCycle.ANNUAL);
        LocalDateTime oldEnd = subscription.getCurrentPeriodEnd();

        // Act
        subscription.renew();

        // Assert
        assertThat(subscription.getCurrentPeriodEnd()).isEqualTo(oldEnd.plusYears(1));
    }

    @Test
    @DisplayName("Should cancel at period end when flag is set")
    void renew_CancelAtPeriodEnd_CancelsSubscription() {
        // Arrange
        subscription.setCancelAtPeriodEnd(true);

        // Act
        subscription.renew();

        // Assert
        assertThat(subscription.getStatus()).isEqualTo(Subscription.SubscriptionStatus.CANCELLED);
        assertThat(subscription.getCancellationReason()).isEqualTo("Auto-cancel at period end");
    }

    @Test
    @DisplayName("Should calculate MRR for monthly subscription")
    void calculateMRR_Monthly_Success() {
        // Arrange
        subscription.setBillingCycle(Subscription.BillingCycle.MONTHLY);
        subscription.setTotalAmount(new BigDecimal("100.00"));
        subscription.setDiscountPercentage(BigDecimal.ZERO);

        // Act
        BigDecimal mrr = subscription.calculateMRR();

        // Assert
        assertThat(mrr).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Should calculate MRR for quarterly subscription")
    void calculateMRR_Quarterly_Success() {
        // Arrange
        subscription.setBillingCycle(Subscription.BillingCycle.QUARTERLY);
        subscription.setTotalAmount(new BigDecimal("300.00"));
        subscription.setDiscountPercentage(BigDecimal.ZERO);

        // Act
        BigDecimal mrr = subscription.calculateMRR();

        // Assert
        assertThat(mrr).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Should calculate MRR for annual subscription")
    void calculateMRR_Annual_Success() {
        // Arrange
        subscription.setBillingCycle(Subscription.BillingCycle.ANNUAL);
        subscription.setTotalAmount(new BigDecimal("1200.00"));
        subscription.setDiscountPercentage(BigDecimal.ZERO);

        // Act
        BigDecimal mrr = subscription.calculateMRR();

        // Assert
        assertThat(mrr).isEqualByComparingTo("100.00");
    }

    @Test
    @DisplayName("Should calculate MRR with discount")
    void calculateMRR_WithDiscount_Success() {
        // Arrange
        subscription.setBillingCycle(Subscription.BillingCycle.MONTHLY);
        subscription.setTotalAmount(new BigDecimal("100.00"));
        subscription.setDiscountPercentage(new BigDecimal("0.20")); // 20% discount

        // Act
        BigDecimal mrr = subscription.calculateMRR();

        // Assert
        assertThat(mrr).isEqualByComparingTo("80.00");
    }

    @Test
    @DisplayName("Builder should create valid subscription")
    void builder_ValidSubscription() {
        // Act
        Subscription sub = Subscription.builder()
            .tenantId("tenant-1")
            .customerId("customer-1")
            .planId("plan-basic")
            .status(Subscription.SubscriptionStatus.TRIAL)
            .billingCycle(Subscription.BillingCycle.MONTHLY)
            .basePrice(new BigDecimal("49.00"))
            .totalAmount(new BigDecimal("49.00"))
            .currentPeriodStart(LocalDateTime.now())
            .currentPeriodEnd(LocalDateTime.now().plusMonths(1))
            .build();

        // Assert
        assertThat(sub.getTenantId()).isEqualTo("tenant-1");
        assertThat(sub.getCustomerId()).isEqualTo("customer-1");
        assertThat(sub.getPlanId()).isEqualTo("plan-basic");
        assertThat(sub.getStatus()).isEqualTo(Subscription.SubscriptionStatus.TRIAL);
        assertThat(sub.getBillingCycle()).isEqualTo(Subscription.BillingCycle.MONTHLY);
    }

    @Test
    @DisplayName("Should return false for isInTrial when not in trial")
    void isInTrial_NotInTrial_ReturnsFalse() {
        // Arrange - subscription is already set up with isTrial = false

        // Act
        boolean result = subscription.isInTrial();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false for isTrialExpired when not in trial")
    void isTrialExpired_NotInTrial_ReturnsFalse() {
        // Arrange - subscription is already set up with isTrial = false

        // Act
        boolean result = subscription.isTrialExpired();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should handle zero discount correctly")
    void calculateMRR_ZeroDiscount_Success() {
        // Arrange
        subscription.setBillingCycle(Subscription.BillingCycle.MONTHLY);
        subscription.setTotalAmount(new BigDecimal("50.00"));
        subscription.setDiscountPercentage(BigDecimal.ZERO);

        // Act
        BigDecimal mrr = subscription.calculateMRR();

        // Assert
        assertThat(mrr).isEqualByComparingTo("50.00");
    }

    @Test
    @DisplayName("Should have default currency USD")
    void builder_DefaultCurrency() {
        // Act
        Subscription sub = Subscription.builder()
            .tenantId("tenant-1")
            .customerId("customer-1")
            .planId("plan-1")
            .status(Subscription.SubscriptionStatus.ACTIVE)
            .billingCycle(Subscription.BillingCycle.MONTHLY)
            .basePrice(new BigDecimal("10.00"))
            .totalAmount(new BigDecimal("10.00"))
            .currentPeriodStart(LocalDateTime.now())
            .currentPeriodEnd(LocalDateTime.now().plusMonths(1))
            .build();

        // Assert
        assertThat(sub.getCurrency()).isEqualTo("USD");
    }

    @Test
    @DisplayName("Should have default auto-renew enabled")
    void builder_DefaultAutoRenew() {
        // Act
        Subscription sub = Subscription.builder()
            .tenantId("tenant-1")
            .customerId("customer-1")
            .planId("plan-1")
            .status(Subscription.SubscriptionStatus.ACTIVE)
            .billingCycle(Subscription.BillingCycle.MONTHLY)
            .basePrice(new BigDecimal("10.00"))
            .totalAmount(new BigDecimal("10.00"))
            .currentPeriodStart(LocalDateTime.now())
            .currentPeriodEnd(LocalDateTime.now().plusMonths(1))
            .build();

        // Assert
        assertThat(sub.isAutoRenew()).isTrue();
    }
}
