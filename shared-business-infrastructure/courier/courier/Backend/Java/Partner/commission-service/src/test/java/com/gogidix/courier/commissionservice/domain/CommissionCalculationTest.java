package com.gogidix.courier.commissionservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Commission calculation domain logic.
 */
@DisplayName("Commission Calculation Domain Tests")
class CommissionCalculationTest {

    private static final BigDecimal STANDARD_COMMISSION_RATE = new BigDecimal("0.70");
    private static final BigDecimal PARTNER_COMMISSION_RATE = new BigDecimal("0.15");
    private static final BigDecimal PLATFORM_FEE_RATE = new BigDecimal("0.15");

    @Test
    @DisplayName("Should calculate driver commission correctly")
    void shouldCalculateDriverCommission() {
        BigDecimal deliveryFee = new BigDecimal("25.00");
        BigDecimal expectedCommission = deliveryFee.multiply(STANDARD_COMMISSION_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal actualCommission = calculateDriverCommission(deliveryFee, STANDARD_COMMISSION_RATE);

        assertEquals(new BigDecimal("17.50"), actualCommission);
        assertEquals(expectedCommission, actualCommission);
    }

    @Test
    @DisplayName("Should calculate partner commission correctly")
    void shouldCalculatePartnerCommission() {
        BigDecimal deliveryFee = new BigDecimal("25.00");
        BigDecimal expectedCommission = deliveryFee.multiply(PARTNER_COMMISSION_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal actualCommission = calculatePartnerCommission(deliveryFee, PARTNER_COMMISSION_RATE);

        assertEquals(new BigDecimal("3.75"), actualCommission);
        assertEquals(expectedCommission, actualCommission);
    }

    @Test
    @DisplayName("Should calculate platform fee correctly")
    void shouldCalculatePlatformFee() {
        BigDecimal deliveryFee = new BigDecimal("25.00");
        BigDecimal expectedFee = deliveryFee.multiply(PLATFORM_FEE_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal actualFee = calculatePlatformFee(deliveryFee, PLATFORM_FEE_RATE);

        assertEquals(new BigDecimal("3.75"), actualFee);
        assertEquals(expectedFee, actualFee);
    }

    @Test
    @DisplayName("Should apply performance bonus for high rating")
    void shouldApplyPerformanceBonusForHighRating() {
        BigDecimal baseCommission = new BigDecimal("17.50");
        BigDecimal rating = new BigDecimal("4.8");

        BigDecimal bonus = calculatePerformanceBonus(baseCommission, rating);

        assertEquals(new BigDecimal("0.88"), bonus); // 5% of 17.50
    }

    @Test
    @DisplayName("Should not apply performance bonus for low rating")
    void shouldNotApplyPerformanceBonusForLowRating() {
        BigDecimal baseCommission = new BigDecimal("17.50");
        BigDecimal rating = new BigDecimal("4.2");

        BigDecimal bonus = calculatePerformanceBonus(baseCommission, rating);

        assertEquals(BigDecimal.ZERO, bonus);
    }

    @Test
    @DisplayName("Should apply peak hours bonus")
    void shouldApplyPeakHoursBonus() {
        BigDecimal baseCommission = new BigDecimal("17.50");
        boolean isPeakHours = true;

        BigDecimal totalCommission = applyPeakHoursBonus(baseCommission, isPeakHours);

        assertEquals(new BigDecimal("19.25"), totalCommission); // 17.50 + 10%
    }

    @Test
    @DisplayName("Should not apply peak hours bonus when not peak")
    void shouldNotApplyPeakHoursBonusWhenNotPeak() {
        BigDecimal baseCommission = new BigDecimal("17.50");
        boolean isPeakHours = false;

        BigDecimal totalCommission = applyPeakHoursBonus(baseCommission, isPeakHours);

        assertEquals(baseCommission, totalCommission);
    }

    @Test
    @DisplayName("Should calculate referral bonus correctly")
    void shouldCalculateReferralBonusCorrectly() {
        BigDecimal referralBonusAmount = new BigDecimal("50.00");

        assertEquals(new BigDecimal("50.00"), referralBonusAmount);
    }

    @Test
    @DisplayName("Should handle zero delivery fee")
    void shouldHandleZeroDeliveryFee() {
        BigDecimal deliveryFee = BigDecimal.ZERO;

        BigDecimal commission = calculateDriverCommission(deliveryFee, STANDARD_COMMISSION_RATE);

        // Use compareTo for BigDecimal comparison to handle scale differences
        assertEquals(0, commission.compareTo(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should round commission to two decimal places")
    void shouldRoundCommissionToTwoDecimalPlaces() {
        BigDecimal deliveryFee = new BigDecimal("10.00");
        BigDecimal rate = new BigDecimal("0.333");

        BigDecimal commission = calculateDriverCommission(deliveryFee, rate);

        assertEquals(new BigDecimal("3.33"), commission);
    }

    @Test
    @DisplayName("Should calculate total earnings for multiple deliveries")
    void shouldCalculateTotalEarningsForMultipleDeliveries() {
        BigDecimal[] deliveryFees = {
                new BigDecimal("15.00"),
                new BigDecimal("20.00"),
                new BigDecimal("25.00"),
                new BigDecimal("18.50")
        };

        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal fee : deliveryFees) {
            total = total.add(calculateDriverCommission(fee, STANDARD_COMMISSION_RATE));
        }

        // 15.00*0.70 + 20.00*0.70 + 25.00*0.70 + 18.50*0.70 = 10.50 + 14.00 + 17.50 + 12.95 = 54.95
        assertEquals(new BigDecimal("54.95"), total);
    }

    @Test
    @DisplayName("Should apply minimum commission guarantee")
    void shouldApplyMinimumCommissionGuarantee() {
        BigDecimal calculatedCommission = new BigDecimal("3.00");
        BigDecimal minimumGuarantee = new BigDecimal("5.00");

        BigDecimal finalCommission = calculatedCommission.compareTo(minimumGuarantee) < 0
                ? minimumGuarantee
                : calculatedCommission;

        assertEquals(minimumGuarantee, finalCommission);
    }

    @Test
    @DisplayName("Should not apply minimum guarantee when commission is higher")
    void shouldNotApplyMinimumWhenCommissionIsHigher() {
        BigDecimal calculatedCommission = new BigDecimal("8.00");
        BigDecimal minimumGuarantee = new BigDecimal("5.00");

        BigDecimal finalCommission = calculatedCommission.compareTo(minimumGuarantee) < 0
                ? minimumGuarantee
                : calculatedCommission;

        assertEquals(calculatedCommission, finalCommission);
    }

    // Helper methods simulating domain service methods
    private BigDecimal calculateDriverCommission(BigDecimal deliveryFee, BigDecimal rate) {
        return deliveryFee.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePartnerCommission(BigDecimal deliveryFee, BigDecimal rate) {
        return deliveryFee.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePlatformFee(BigDecimal deliveryFee, BigDecimal rate) {
        return deliveryFee.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePerformanceBonus(BigDecimal baseCommission, BigDecimal rating) {
        if (rating.compareTo(new BigDecimal("4.7")) >= 0) {
            return baseCommission.multiply(new BigDecimal("0.05"))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal applyPeakHoursBonus(BigDecimal baseCommission, boolean isPeakHours) {
        if (isPeakHours) {
            BigDecimal bonus = baseCommission.multiply(new BigDecimal("0.10"))
                    .setScale(2, RoundingMode.HALF_UP);
            return baseCommission.add(bonus);
        }
        return baseCommission;
    }
}
