package com.gogidix.platform.subscription.application.service;

import com.gogidix.platform.subscription.domain.model.Subscription;
import com.gogidix.platform.subscription.domain.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SubscriptionService.
 * Tests subscription lifecycle operations.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SubscriptionService Tests")
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private Subscription testSubscription;
    private UUID subscriptionId;

    @BeforeEach
    void setUp() {
        subscriptionId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        testSubscription = Subscription.builder()
            .id(String.valueOf(subscriptionId))
            .tenantId("tenant-123")
            .subscriptionNumber("SUB-1234567890-123")
            .customerId("customer-123")
            .planId("plan-pro")
            .status(Subscription.SubscriptionStatus.PENDING)
            .currentPeriodStart(now)
            .currentPeriodEnd(now.plusMonths(1))
            .billingCycle(Subscription.BillingCycle.MONTHLY)
            .basePrice(new BigDecimal("99.00"))
            .totalAmount(new BigDecimal("108.90"))
            .currency("USD")
            .autoRenew(true)
            .build();
    }

    @Test
    @DisplayName("Should create subscription successfully")
    void createSubscription_Success() {
        // Arrange
        testSubscription.setStatus(Subscription.SubscriptionStatus.PENDING);
        when(subscriptionRepository.save(any(Subscription.class)))
            .thenReturn(testSubscription);

        // Act
        Subscription result = subscriptionService.createSubscription(testSubscription);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(String.valueOf(subscriptionId));
        assertThat(result.getTenantId()).isEqualTo("tenant-123");

        verify(subscriptionRepository).save(testSubscription);
    }

    @Test
    @DisplayName("Should get subscription by ID")
    void getSubscription_Success() {
        // Arrange
        when(subscriptionRepository.findById(subscriptionId))
            .thenReturn(Optional.of(testSubscription));

        // Act
        Subscription result = subscriptionService.getSubscription(subscriptionId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(String.valueOf(subscriptionId));
        assertThat(result.getCustomerId()).isEqualTo("customer-123");

        verify(subscriptionRepository).findById(subscriptionId);
    }

    @Test
    @DisplayName("Should throw exception when subscription not found by ID")
    void getSubscription_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(subscriptionRepository.findById(nonExistentId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> subscriptionService.getSubscription(nonExistentId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Subscription not found");

        verify(subscriptionRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should get subscriptions by tenant")
    void getSubscriptionsByTenant_Success() {
        // Arrange
        Subscription sub2 = Subscription.builder()
            .id(UUID.randomUUID().toString())
            .tenantId("tenant-123")
            .customerId("customer-456")
            .planId("plan-basic")
            .status(Subscription.SubscriptionStatus.ACTIVE)
            .currentPeriodStart(LocalDateTime.now())
            .currentPeriodEnd(LocalDateTime.now().plusMonths(1))
            .billingCycle(Subscription.BillingCycle.MONTHLY)
            .basePrice(new BigDecimal("49.00"))
            .totalAmount(new BigDecimal("49.00"))
            .build();

        when(subscriptionRepository.findByTenantId("tenant-123"))
            .thenReturn(Arrays.asList(testSubscription, sub2));

        // Act
        List<Subscription> result = subscriptionService.getSubscriptionsByTenant("tenant-123");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTenantId()).isEqualTo("tenant-123");
        assertThat(result.get(1).getTenantId()).isEqualTo("tenant-123");

        verify(subscriptionRepository).findByTenantId("tenant-123");
    }

    @Test
    @DisplayName("Should return empty list when tenant has no subscriptions")
    void getSubscriptionsByTenant_EmptyList() {
        // Arrange
        when(subscriptionRepository.findByTenantId("nonexistent-tenant"))
            .thenReturn(List.of());

        // Act
        List<Subscription> result = subscriptionService.getSubscriptionsByTenant("nonexistent-tenant");

        // Assert
        assertThat(result).isEmpty();
        verify(subscriptionRepository).findByTenantId("nonexistent-tenant");
    }

    @Test
    @DisplayName("Should activate subscription successfully")
    void activateSubscription_Success() {
        // Arrange
        testSubscription.setStatus(Subscription.SubscriptionStatus.PENDING);
        when(subscriptionRepository.findById(subscriptionId))
            .thenReturn(Optional.of(testSubscription));
        when(subscriptionRepository.save(any(Subscription.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        subscriptionService.activateSubscription(subscriptionId);

        // Assert
        assertThat(testSubscription.getStatus()).isEqualTo(Subscription.SubscriptionStatus.ACTIVE);
        verify(subscriptionRepository).save(testSubscription);
    }

    @Test
    @DisplayName("Should throw exception when activating non-existent subscription")
    void activateSubscription_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(subscriptionRepository.findById(nonExistentId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> subscriptionService.activateSubscription(nonExistentId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Subscription not found");

        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Should cancel subscription successfully")
    void cancelSubscription_Success() {
        // Arrange
        testSubscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
        when(subscriptionRepository.findById(subscriptionId))
            .thenReturn(Optional.of(testSubscription));
        when(subscriptionRepository.save(any(Subscription.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        subscriptionService.cancelSubscription(subscriptionId);

        // Assert
        assertThat(testSubscription.getStatus()).isEqualTo(Subscription.SubscriptionStatus.CANCELLED);
        assertThat(testSubscription.getCancelledAt()).isNotNull();
        verify(subscriptionRepository).save(testSubscription);
    }

    @Test
    @DisplayName("Should throw exception when cancelling non-existent subscription")
    void cancelSubscription_NotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        when(subscriptionRepository.findById(nonExistentId))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> subscriptionService.cancelSubscription(nonExistentId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Subscription not found");

        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    @DisplayName("Should handle multiple activations correctly")
    void activateSubscription_MultipleActivations() {
        // Arrange
        testSubscription.setStatus(Subscription.SubscriptionStatus.PENDING);
        when(subscriptionRepository.findById(subscriptionId))
            .thenReturn(Optional.of(testSubscription));
        when(subscriptionRepository.save(any(Subscription.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Act - first activation
        subscriptionService.activateSubscription(subscriptionId);

        // Act - second activation (should still work, just no-op)
        subscriptionService.activateSubscription(subscriptionId);

        // Assert - status should remain ACTIVE
        assertThat(testSubscription.getStatus()).isEqualTo(Subscription.SubscriptionStatus.ACTIVE);
        verify(subscriptionRepository, times(2)).save(testSubscription);
    }
}
