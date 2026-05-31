package com.gogidix.shared.courier.pricing.domain.service;

import com.gogidix.shared.courier.pricing.application.command.CalculatePriceCommand;
import com.gogidix.shared.courier.pricing.domain.entity.PricingModel;
import com.gogidix.shared.courier.pricing.domain.entity.PricingRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Domain Service for Price Calculation Engine
 * Implements pricing logic and rule application
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PricingEngineService {

    private static final BigDecimal DEFAULT_BASE_FARE = new BigDecimal("5.00");
    private static final BigDecimal DEFAULT_PER_KM_RATE = new BigDecimal("1.50");
    private static final BigDecimal DEFAULT_PER_MINUTE_RATE = new BigDecimal("0.25");
    private static final BigDecimal DEFAULT_PER_KG_RATE = new BigDecimal("0.50");
    private static final BigDecimal TAX_RATE = new BigDecimal("0.10"); // 10%

    /**
     * Calculate price based on command and applicable rules
     */
    public PricingModel calculatePrice(CalculatePriceCommand command, List<PricingRule> applicableRules) {
        log.debug("Calculating price for tenant: {}, service: {}, vehicle: {}",
                command.getTenantId(), command.getServiceType(), command.getVehicleType());

        // Sort rules by priority (lower number = higher priority)
        applicableRules.sort(Comparator.comparingInt(PricingRule::getPriority));

        PricingModel model = createInitialModel(command);

        // Apply base rate
        BigDecimal baseFare = calculateBaseFare(applicableRules);
        model.setBaseFare(baseFare);

        // Calculate distance charge
        BigDecimal distanceCharge = calculateDistanceCharge(command, applicableRules);
        model.setDistanceCharge(distanceCharge);

        // Calculate time charge
        BigDecimal timeCharge = calculateTimeCharge(command, applicableRules);
        model.setTimeCharge(timeCharge);

        // Calculate weight charge
        BigDecimal weightCharge = calculateWeightCharge(command, applicableRules);
        model.setWeightCharge(weightCharge);

        // Calculate subtotal
        BigDecimal subtotal = baseFare
                .add(distanceCharge)
                .add(timeCharge)
                .add(weightCharge);
        model.setSubtotal(subtotal);

        // Apply surcharges
        BigDecimal surcharge = calculateSurcharge(command, applicableRules, subtotal);
        model.setSurcharge(surcharge);

        // Apply discounts
        BigDecimal discount = calculateDiscount(command, applicableRules, subtotal);
        model.setDiscount(discount);

        // Calculate total before tax
        BigDecimal totalBeforeTax = subtotal.add(surcharge).subtract(discount);

        // Calculate tax
        BigDecimal tax = totalBeforeTax.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        model.setTax(tax);

        // Calculate total
        BigDecimal totalAmount = totalBeforeTax.add(tax);
        model.setTotalAmount(totalAmount);

        // Set expiry
        model.setExpiresAt(LocalDateTime.now().plusMinutes(model.getValidityMinutes()));

        // Build breakdown
        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("baseFare", baseFare.toString());
        breakdown.put("distanceCharge", distanceCharge.toString());
        breakdown.put("timeCharge", timeCharge.toString());
        breakdown.put("weightCharge", weightCharge.toString());
        breakdown.put("surcharge", surcharge.toString());
        breakdown.put("discount", discount.toString());
        breakdown.put("tax", tax.toString());
        breakdown.put("subtotal", subtotal.toString());
        breakdown.put("distanceKm", command.getDistanceKm());
        breakdown.put("durationMinutes", command.getEstimatedDurationMinutes());
        model.setBreakdown(breakdown);

        log.info("Price calculated: {} for quote: {}", totalAmount, model.getQuoteId());

        return model;
    }

    /**
     * Create initial pricing model with default values
     */
    private PricingModel createInitialModel(CalculatePriceCommand command) {
        return PricingModel.builder()
                .tenantId(command.getTenantId())
                .quoteId(java.util.UUID.randomUUID().toString())
                .serviceType(command.getServiceType())
                .vehicleType(command.getVehicleType())
                .currency("USD")
                .validityMinutes(15)
                .calculatedAt(LocalDateTime.now())
                .accepted(false)
                .build();
    }

    /**
     * Calculate base fare from rules
     */
    private BigDecimal calculateBaseFare(List<PricingRule> rules) {
        return rules.stream()
                .filter(r -> "BASE_RATE".equals(r.getRuleType()))
                .findFirst()
                .map(rule -> extractBigDecimal(rule.getParameters(), "baseFare"))
                .orElse(DEFAULT_BASE_FARE);
    }

    /**
     * Calculate distance charge
     */
    private BigDecimal calculateDistanceCharge(CalculatePriceCommand command, List<PricingRule> rules) {
        BigDecimal perKmRate = rules.stream()
                .filter(r -> "DISTANCE_BASED".equals(r.getRuleType()))
                .findFirst()
                .map(rule -> extractBigDecimal(rule.getParameters(), "perKmRate"))
                .orElse(DEFAULT_PER_KM_RATE);

        return perKmRate.multiply(BigDecimal.valueOf(command.getDistanceKm()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate time charge
     */
    private BigDecimal calculateTimeCharge(CalculatePriceCommand command, List<PricingRule> rules) {
        BigDecimal perMinuteRate = rules.stream()
                .filter(r -> "TIME_BASED".equals(r.getRuleType()))
                .findFirst()
                .map(rule -> extractBigDecimal(rule.getParameters(), "perMinuteRate"))
                .orElse(DEFAULT_PER_MINUTE_RATE);

        return perMinuteRate.multiply(BigDecimal.valueOf(command.getEstimatedDurationMinutes()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate weight charge
     */
    private BigDecimal calculateWeightCharge(CalculatePriceCommand command, List<PricingRule> rules) {
        if (command.getPackageWeightKg() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal perKgRate = rules.stream()
                .filter(r -> "WEIGHT_BASED".equals(r.getRuleType()))
                .findFirst()
                .map(rule -> extractBigDecimal(rule.getParameters(), "perKgRate"))
                .orElse(DEFAULT_PER_KG_RATE);

        return perKgRate.multiply(BigDecimal.valueOf(command.getPackageWeightKg()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate surcharge (e.g., surge pricing)
     */
    private BigDecimal calculateSurcharge(CalculatePriceCommand command, List<PricingRule> rules, BigDecimal subtotal) {
        return rules.stream()
                .filter(r -> "SURGE".equals(r.getRuleType()))
                .findFirst()
                .map(rule -> {
                    BigDecimal multiplier = extractBigDecimal(rule.getParameters(), "surgeMultiplier");
                    return subtotal.multiply(multiplier.subtract(BigDecimal.ONE))
                            .setScale(2, RoundingMode.HALF_UP);
                })
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Calculate discount
     */
    private BigDecimal calculateDiscount(CalculatePriceCommand command, List<PricingRule> rules, BigDecimal subtotal) {
        // Check for discount rules
        BigDecimal discountFromRule = rules.stream()
                .filter(r -> "DISCOUNT".equals(r.getRuleType()))
                .filter(r -> isDiscountApplicable(command, r))
                .findFirst()
                .map(rule -> {
                    BigDecimal discountPercent = extractBigDecimal(rule.getParameters(), "discountPercentage");
                    BigDecimal maxDiscount = extractBigDecimal(rule.getParameters(), "maxDiscountAmount");
                    BigDecimal discount = subtotal.multiply(discountPercent.divide(BigDecimal.valueOf(100)))
                            .setScale(2, RoundingMode.HALF_UP);
                    if (maxDiscount.compareTo(BigDecimal.ZERO) > 0 && discount.compareTo(maxDiscount) > 0) {
                        return maxDiscount;
                    }
                    return discount;
                })
                .orElse(BigDecimal.ZERO);

        return discountFromRule;
    }

    /**
     * Check if discount is applicable
     */
    private boolean isDiscountApplicable(CalculatePriceCommand command, PricingRule rule) {
        // Check promo code
        String discountCode = extractString(rule.getParameters(), "discountCode");
        if (discountCode != null && !discountCode.isBlank()) {
            return discountCode.equals(command.getPromoCode());
        }
        return true;
    }

    /**
     * Extract BigDecimal from parameters map
     */
    private BigDecimal extractBigDecimal(Map<String, Object> parameters, String key) {
        if (parameters == null || !parameters.containsKey(key)) {
            return BigDecimal.ZERO;
        }
        Object value = parameters.get(key);
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String) value);
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Extract String from parameters map
     */
    private String extractString(Map<String, Object> parameters, String key) {
        if (parameters == null || !parameters.containsKey(key)) {
            return null;
        }
        Object value = parameters.get(key);
        return value != null ? value.toString() : null;
    }
}
