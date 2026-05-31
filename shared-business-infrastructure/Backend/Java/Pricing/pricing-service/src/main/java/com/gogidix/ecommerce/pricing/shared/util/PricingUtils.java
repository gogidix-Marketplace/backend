package com.gogidix.ecommerce.pricing.shared.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public final class PricingUtils {

    private PricingUtils() {}

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static BigDecimal scalePrice(BigDecimal price) {
        if (price == null) return BigDecimal.ZERO;
        return price.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculatePercentage(BigDecimal base, BigDecimal percentage) {
        if (base == null || percentage == null) return BigDecimal.ZERO;
        return base.multiply(percentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public static boolean isWithinRange(BigDecimal value, BigDecimal min, BigDecimal max) {
        if (value == null) return false;
        boolean aboveMin = min == null || value.compareTo(min) >= 0;
        boolean belowMax = max == null || value.compareTo(max) <= 0;
        return aboveMin && belowMax;
    }

    public static boolean isEffectiveNow(Instant effectiveFrom, Instant effectiveTo) {
        Instant now = Instant.now();
        boolean afterStart = effectiveFrom == null || !now.isBefore(effectiveFrom);
        boolean beforeEnd = effectiveTo == null || !now.isAfter(effectiveTo);
        return afterStart && beforeEnd;
    }

    public static Instant defaultExpiry() {
        return Instant.now().plus(30, ChronoUnit.DAYS);
    }

    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }
}
