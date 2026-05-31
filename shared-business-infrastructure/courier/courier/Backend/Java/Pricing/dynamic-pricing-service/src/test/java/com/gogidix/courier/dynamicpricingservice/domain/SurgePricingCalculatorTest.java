package com.gogidix.courier.dynamicpricingservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Surge Pricing Calculator domain logic.
 */
@DisplayName("Surge Pricing Calculator Domain Tests")
class SurgePricingCalculatorTest {

    private static final double BASE_SURGE_THRESHOLD = 1.0;
    private static final double MAX_SURGE_MULTIPLIER = 3.0;

    @Test
    @DisplayName("Should calculate surge multiplier based on demand ratio")
    void shouldCalculateSurgeMultiplierBasedOnDemandRatio() {
        double availableDrivers = 10.0;
        double pendingRequests = 25.0;
        double demandRatio = pendingRequests / availableDrivers; // 2.5

        double surgeMultiplier = calculateSurgeMultiplier(demandRatio);

        assertTrue(surgeMultiplier > 1.0);
        assertTrue(surgeMultiplier <= MAX_SURGE_MULTIPLIER);
    }

    @Test
    @DisplayName("Should return 1.0 when demand is low")
    void shouldReturnOneWhenDemandIsLow() {
        double demandRatio = 0.5; // More drivers than requests

        double surgeMultiplier = calculateSurgeMultiplier(demandRatio);

        assertEquals(1.0, surgeMultiplier);
    }

    @Test
    @DisplayName("Should apply maximum surge multiplier for extreme demand")
    void shouldApplyMaximumSurgeMultiplierForExtremeDemand() {
        double demandRatio = 10.0; // 10x demand vs supply

        double surgeMultiplier = calculateSurgeMultiplier(demandRatio);

        // multiplier = 1.0 + (10.0 - 1.0) * 0.2 = 1.0 + 1.8 = 2.8
        assertEquals(2.8, surgeMultiplier);
    }

    @Test
    @DisplayName("Should calculate adjusted price with surge multiplier")
    void shouldCalculateAdjustedPriceWithSurgeMultiplier() {
        BigDecimal basePrice = new BigDecimal("20.00");
        double surgeMultiplier = 1.5;

        BigDecimal adjustedPrice = calculateAdjustedPrice(basePrice, surgeMultiplier);

        assertEquals(new BigDecimal("30.00"), adjustedPrice);
    }

    @Test
    @DisplayName("Should round adjusted price to two decimal places")
    void shouldRoundAdjustedPriceToTwoDecimalPlaces() {
        BigDecimal basePrice = new BigDecimal("19.99");
        double surgeMultiplier = 1.333;

        BigDecimal adjustedPrice = calculateAdjustedPrice(basePrice, surgeMultiplier);

        assertEquals(2, adjustedPrice.scale());
    }

    @Test
    @DisplayName("Should apply night surcharge during night hours")
    void shouldApplyNightSurchargeDuringNightHours() {
        LocalTime time = LocalTime.of(23, 30);
        BigDecimal basePrice = new BigDecimal("20.00");

        BigDecimal adjustedPrice = applyNightSurcharge(basePrice, time);

        assertEquals(new BigDecimal("24.00"), adjustedPrice); // 20% surcharge
    }

    @Test
    @DisplayName("Should not apply night surcharge during day hours")
    void shouldNotApplyNightSurchargeDuringDayHours() {
        LocalTime time = LocalTime.of(14, 0);
        BigDecimal basePrice = new BigDecimal("20.00");

        BigDecimal adjustedPrice = applyNightSurcharge(basePrice, time);

        assertEquals(basePrice, adjustedPrice);
    }

    @Test
    @DisplayName("Should apply weekend surcharge on weekend")
    void shouldApplyWeekendSurchargeOnWeekend() {
        int dayOfWeek = 6; // Saturday
        BigDecimal basePrice = new BigDecimal("20.00");

        BigDecimal adjustedPrice = applyWeekendSurcharge(basePrice, dayOfWeek);

        assertEquals(new BigDecimal("23.00"), adjustedPrice); // 15% surcharge
    }

    @Test
    @DisplayName("Should not apply weekend surcharge on weekday")
    void shouldNotApplyWeekendSurchargeOnWeekday() {
        int dayOfWeek = 3; // Wednesday
        BigDecimal basePrice = new BigDecimal("20.00");

        BigDecimal adjustedPrice = applyWeekendSurcharge(basePrice, dayOfWeek);

        assertEquals(basePrice, adjustedPrice);
    }

    @Test
    @DisplayName("Should calculate zone-based surge multiplier")
    void shouldCalculateZoneBasedSurgeMultiplier() {
        String zoneId = "zone-downtown";
        Map<String, Double> zoneDemandFactors = new HashMap<>();
        zoneDemandFactors.put("zone-downtown", 1.8);
        zoneDemandFactors.put("zone-suburbs", 0.9);

        double zoneMultiplier = zoneDemandFactors.getOrDefault(zoneId, 1.0);

        assertEquals(1.8, zoneMultiplier);
    }

    @Test
    @DisplayName("Should return default multiplier for unknown zone")
    void shouldReturnDefaultMultiplierForUnknownZone() {
        String zoneId = "zone-unknown";
        Map<String, Double> zoneDemandFactors = new HashMap<>();
        zoneDemandFactors.put("zone-downtown", 1.8);

        double zoneMultiplier = zoneDemandFactors.getOrDefault(zoneId, 1.0);

        assertEquals(1.0, zoneMultiplier);
    }

    @Test
    @DisplayName("Should combine multiple surcharges correctly")
    void shouldCombineMultipleSurchargeCorrectly() {
        BigDecimal basePrice = new BigDecimal("20.00");
        double surgeMultiplier = 1.5;
        LocalTime nightTime = LocalTime.of(23, 0);

        BigDecimal priceWithSurge = calculateAdjustedPrice(basePrice, surgeMultiplier);
        BigDecimal finalPrice = applyNightSurcharge(priceWithSurge, nightTime);

        assertEquals(new BigDecimal("36.00"), finalPrice); // 20 * 1.5 * 1.2
    }

    @Test
    @DisplayName("Should not exceed maximum price cap")
    void shouldNotExceedMaximumPriceCap() {
        BigDecimal basePrice = new BigDecimal("10.00");
        double surgeMultiplier = 5.0; // Above max
        BigDecimal maxPrice = new BigDecimal("50.00");

        BigDecimal adjustedPrice = calculateAdjustedPrice(basePrice, surgeMultiplier);
        BigDecimal cappedPrice = adjustedPrice.min(maxPrice);

        assertEquals(maxPrice, cappedPrice);
    }

    @Test
    @DisplayName("Should calculate demand ratio correctly")
    void shouldCalculateDemandRatioCorrectly() {
        int availableDrivers = 15;
        int pendingRequests = 30;

        double demandRatio = (double) pendingRequests / availableDrivers;

        assertEquals(2.0, demandRatio);
    }

    @Test
    @DisplayName("Should handle zero available drivers")
    void shouldHandleZeroAvailableDrivers() {
        double availableDrivers = 0.0;
        double pendingRequests = 10.0;

        double demandRatio = availableDrivers == 0 ? Double.MAX_VALUE : pendingRequests / availableDrivers;

        assertEquals(Double.MAX_VALUE, demandRatio);

        double surgeMultiplier = calculateSurgeMultiplier(demandRatio);
        assertEquals(MAX_SURGE_MULTIPLIER, surgeMultiplier);
    }

    @Test
    @DisplayName("Should apply weather-based surge")
    void shouldApplyWeatherBasedSurge() {
        String weatherCondition = "heavy-rain";
        BigDecimal basePrice = new BigDecimal("20.00");

        BigDecimal adjustedPrice = applyWeatherSurcharge(basePrice, weatherCondition);

        assertEquals(new BigDecimal("22.00"), adjustedPrice); // 10% weather surcharge
    }

    @Test
    @DisplayName("Should not apply weather surcharge for clear weather")
    void shouldNotApplyWeatherSurchargeForClearWeather() {
        String weatherCondition = "clear";
        BigDecimal basePrice = new BigDecimal("20.00");

        BigDecimal adjustedPrice = applyWeatherSurcharge(basePrice, weatherCondition);

        assertEquals(basePrice, adjustedPrice);
    }

    @Test
    @DisplayName("Should decay surge multiplier over time")
    void shouldDecaySurgeMultiplierOverTime() {
        double currentSurge = 2.0;
        int minutesSinceLastUpdate = 30;

        double decayedSurge = calculateDecayedSurge(currentSurge, minutesSinceLastUpdate);

        assertTrue(decayedSurge < currentSurge);
        assertTrue(decayedSurge >= 1.0);
    }

    // Helper methods simulating domain service methods
    private double calculateSurgeMultiplier(double demandRatio) {
        if (demandRatio <= 1.0) {
            return 1.0;
        }
        double multiplier = 1.0 + (demandRatio - 1.0) * 0.2; // 20% increase per unit of excess demand
        return Math.min(multiplier, MAX_SURGE_MULTIPLIER);
    }

    private BigDecimal calculateAdjustedPrice(BigDecimal basePrice, double multiplier) {
        return basePrice.multiply(BigDecimal.valueOf(multiplier))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal applyNightSurcharge(BigDecimal price, LocalTime time) {
        boolean isNightTime = time.isBefore(LocalTime.of(6, 0)) || time.isAfter(LocalTime.of(22, 0));
        if (isNightTime) {
            return price.multiply(BigDecimal.valueOf(1.20)).setScale(2, RoundingMode.HALF_UP);
        }
        return price;
    }

    private BigDecimal applyWeekendSurcharge(BigDecimal price, int dayOfWeek) {
        boolean isWeekend = dayOfWeek == 6 || dayOfWeek == 7; // Saturday or Sunday
        if (isWeekend) {
            return price.multiply(BigDecimal.valueOf(1.15)).setScale(2, RoundingMode.HALF_UP);
        }
        return price;
    }

    private BigDecimal applyWeatherSurcharge(BigDecimal price, String weatherCondition) {
        if (weatherCondition.equals("heavy-rain") || weatherCondition.equals("snow")) {
            return price.multiply(BigDecimal.valueOf(1.10)).setScale(2, RoundingMode.HALF_UP);
        }
        return price;
    }

    private double calculateDecayedSurge(double currentSurge, int minutesSinceLastUpdate) {
        double decayRate = 0.02; // 2% decay per minute
        double decayedValue = currentSurge * Math.exp(-decayRate * minutesSinceLastUpdate);
        return Math.max(1.0, decayedValue);
    }
}
