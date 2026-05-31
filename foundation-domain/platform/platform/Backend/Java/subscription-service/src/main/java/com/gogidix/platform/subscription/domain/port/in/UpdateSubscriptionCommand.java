package com.gogidix.platform.subscription.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Input port: Command to update subscription.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubscriptionCommand {

    private String planId;

    private BigDecimal basePrice;

    private BigDecimal discountPercentage;

    private BigDecimal taxRate;

    private Boolean autoRenew;

    private Boolean cancelAtPeriodEnd;

    private Map<String, Object> metadata;
}
