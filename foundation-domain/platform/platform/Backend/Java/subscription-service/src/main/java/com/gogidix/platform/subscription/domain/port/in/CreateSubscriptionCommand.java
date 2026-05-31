package com.gogidix.platform.subscription.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Input port: Command to create subscription.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionCommand {

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Plan ID is required")
    private String planId;

    @NotNull(message = "Billing cycle is required")
    private com.gogidix.platform.subscription.domain.model.Subscription.BillingCycle billingCycle;

    @NotNull(message = "Base price is required")
    private BigDecimal basePrice;

    @Builder.Default
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Builder.Default
    private String currency = "USD";

    @Builder.Default
    private boolean isTrial = false;

    private Integer trialDays;

    @Builder.Default
    private boolean autoRenew = true;

    private String stripeCustomerId;

    private String stripePaymentMethodId;

    private Map<String, Object> metadata;
}
