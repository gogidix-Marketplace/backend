package com.gogidix.ecommerce.pricing.domain.policy;

import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PricingCalculationPolicy {

    public BigDecimal calculateFinalPrice(PricingRule rule, BigDecimal basePrice, int quantity) {
        if (rule == null || basePrice == null) return basePrice;

        return switch (rule.getType()) {
            case FIXED -> calculateFixedPrice(rule, basePrice);
            case PERCENTAGE -> calculatePercentagePrice(rule, basePrice);
            case TIERED -> calculateTieredPrice(rule, basePrice, quantity);
            case VOLUME -> calculateVolumePrice(rule, basePrice, quantity);
            case DYNAMIC -> calculateDynamicPrice(rule, basePrice);
        };
    }

    private BigDecimal calculateFixedPrice(PricingRule rule, BigDecimal basePrice) {
        if (rule.getDiscountAmount() != null) {
            BigDecimal result = basePrice.subtract(rule.getDiscountAmount());
            return enforceMinMax(rule, result);
        }
        if (rule.getSalePrice() != null) {
            return enforceMinMax(rule, rule.getSalePrice());
        }
        return basePrice;
    }

    private BigDecimal calculatePercentagePrice(PricingRule rule, BigDecimal basePrice) {
        if (rule.getDiscountPercentage() != null) {
            BigDecimal discount = basePrice.multiply(rule.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal result = basePrice.subtract(discount);
            return enforceMinMax(rule, result);
        }
        return basePrice;
    }

    private BigDecimal calculateTieredPrice(PricingRule rule, BigDecimal basePrice, int quantity) {
        BigDecimal discount = BigDecimal.ZERO;
        if (quantity >= 100) {
            discount = basePrice.multiply(new BigDecimal("0.20"));
        } else if (quantity >= 50) {
            discount = basePrice.multiply(new BigDecimal("0.15"));
        } else if (quantity >= 10) {
            discount = basePrice.multiply(new BigDecimal("0.10"));
        }
        return enforceMinMax(rule, basePrice.subtract(discount));
    }

    private BigDecimal calculateVolumePrice(PricingRule rule, BigDecimal basePrice, int quantity) {
        if (quantity >= 1000 && rule.getDiscountPercentage() != null) {
            BigDecimal extraDiscount = rule.getDiscountPercentage().multiply(new BigDecimal("1.5"));
            BigDecimal discount = basePrice.multiply(extraDiscount)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return enforceMinMax(rule, basePrice.subtract(discount));
        }
        return calculatePercentagePrice(rule, basePrice);
    }

    private BigDecimal calculateDynamicPrice(PricingRule rule, BigDecimal basePrice) {
        if (rule.getSalePrice() != null) {
            BigDecimal result = rule.getSalePrice();
            if (rule.getDiscountPercentage() != null) {
                BigDecimal discount = result.multiply(rule.getDiscountPercentage())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                result = result.subtract(discount);
            }
            return enforceMinMax(rule, result);
        }
        return basePrice;
    }

    private BigDecimal enforceMinMax(PricingRule rule, BigDecimal price) {
        if (rule.getMinimumPrice() != null && price.compareTo(rule.getMinimumPrice()) < 0) {
            return rule.getMinimumPrice();
        }
        if (rule.getMaximumPrice() != null && price.compareTo(rule.getMaximumPrice()) > 0) {
            return rule.getMaximumPrice();
        }
        return price;
    }
}
