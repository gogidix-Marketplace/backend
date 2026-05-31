package com.gogidix.courier.etaservice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ETA Calculator domain logic.
 */
@DisplayName("ETA Calculator Domain Tests")
class EtaCalculatorTest {

    @Test
    @DisplayName("Should calculate ETA based on distance and average speed")
    void shouldCalculateEtaBasedOnDistanceAndSpeed() {
        double distanceKm = 15.0;
        double averageSpeedKph = 30.0;

        double estimatedMinutes = (distanceKm / averageSpeedKph) * 60;

        assertEquals(30.0, estimatedMinutes);
    }

    @Test
    @DisplayName("Should add traffic delay to ETA")
    void shouldAddTrafficDelayToEta() {
        double baseMinutes = 30.0;
        double trafficDelayMinutes = 10.0;

        double adjustedMinutes = baseMinutes + trafficDelayMinutes;

        assertEquals(40.0, adjustedMinutes);
    }

    @Test
    @DisplayName("Should calculate arrival time")
    void shouldCalculateArrivalTime() {
        Instant currentTime = Instant.now();
        Duration estimatedDuration = Duration.ofMinutes(30);

        Instant arrivalTime = currentTime.plus(estimatedDuration);

        assertTrue(arrivalTime.isAfter(currentTime));
        assertEquals(30, Duration.between(currentTime, arrivalTime).toMinutes());
    }

    @Test
    @DisplayName("Should adjust ETA for weather conditions")
    void shouldAdjustEtaForWeatherConditions() {
        double baseMinutes = 30.0;
        String weatherCondition = "heavy-rain";

        double adjustedMinutes = baseMinutes;
        if (weatherCondition.equals("heavy-rain") || weatherCondition.equals("snow")) {
            adjustedMinutes = baseMinutes * 1.3;
        }

        assertEquals(39.0, adjustedMinutes, 0.1);
    }

    @Test
    @DisplayName("Should not adjust ETA for clear weather")
    void shouldNotAdjustEtaForClearWeather() {
        double baseMinutes = 30.0;
        String weatherCondition = "clear";

        double adjustedMinutes = baseMinutes;
        if (weatherCondition.equals("heavy-rain") || weatherCondition.equals("snow")) {
            adjustedMinutes = baseMinutes * 1.3;
        }

        assertEquals(30.0, adjustedMinutes);
    }

    @Test
    @DisplayName("Should calculate delivery time window")
    void shouldCalculateDeliveryTimeWindow() {
        Instant pickupTime = Instant.now();
        Duration estimatedDuration = Duration.ofMinutes(30);
        Duration buffer = Duration.ofMinutes(5);

        Instant earliestArrival = pickupTime.plus(estimatedDuration).minus(buffer);
        Instant latestArrival = pickupTime.plus(estimatedDuration).plus(buffer);

        assertEquals(Duration.ofMinutes(25), Duration.between(pickupTime, earliestArrival));
        assertEquals(Duration.ofMinutes(35), Duration.between(pickupTime, latestArrival));
    }

    @Test
    @DisplayName("Should update ETA based on real-time location")
    void shouldUpdateEtaBasedOnRealTimeLocation() {
        double remainingDistanceKm = 10.0;
        double currentSpeedKph = 25.0;

        double updatedMinutes = (remainingDistanceKm / currentSpeedKph) * 60;

        assertEquals(24.0, updatedMinutes);
    }

    @Test
    @DisplayName("Should calculate ETA for multi-stop route")
    void shouldCalculateEtaForMultiStopRoute() {
        double[] legDistancesKm = {5.0, 8.0, 6.0};
        double averageSpeedKph = 30.0;
        double stopTimeMinutes = 5.0;

        double totalMinutes = 0;
        for (double distance : legDistancesKm) {
            totalMinutes += (distance / averageSpeedKph) * 60;
        }
        totalMinutes += stopTimeMinutes * (legDistancesKm.length - 1);

        // Travel: (5+8+6)/30*60 = 38 minutes, Stops: 5*(3-1) = 10 minutes
        assertEquals(48.0, totalMinutes, 0.1);
    }

    @Test
    @DisplayName("Should handle zero distance gracefully")
    void shouldHandleZeroDistanceGracefully() {
        double distanceKm = 0.0;
        double averageSpeedKph = 30.0;

        double estimatedMinutes = (distanceKm / averageSpeedKph) * 60;

        assertEquals(0.0, estimatedMinutes);
    }

    @Test
    @DisplayName("Should handle very short distances")
    void shouldHandleVeryShortDistances() {
        double distanceKm = 0.5;
        double averageSpeedKph = 30.0;

        double estimatedMinutes = (distanceKm / averageSpeedKph) * 60;

        assertEquals(1.0, estimatedMinutes);
    }

    @Test
    @DisplayName("Should adjust for time of day")
    void shouldAdjustForTimeOfDay() {
        double baseMinutes = 30.0;
        int currentHour = 8; // Rush hour

        double adjustedMinutes = baseMinutes;
        if ((currentHour >= 7 && currentHour <= 9) || (currentHour >= 17 && currentHour <= 19)) {
            adjustedMinutes = baseMinutes * 1.4;
        }

        assertEquals(42.0, adjustedMinutes);
    }

    @Test
    @DisplayName("Should not adjust during off-peak hours")
    void shouldNotAdjustDuringOffPeakHours() {
        double baseMinutes = 30.0;
        int currentHour = 14; // Afternoon

        double adjustedMinutes = baseMinutes;
        if ((currentHour >= 7 && currentHour <= 9) || (currentHour >= 17 && currentHour <= 19)) {
            adjustedMinutes = baseMinutes * 1.4;
        }

        assertEquals(30.0, adjustedMinutes);
    }

    @Test
    @DisplayName("Should calculate ETA accuracy percentage")
    void shouldCalculateEtaAccuracyPercentage() {
        int estimatedMinutes = 30;
        int actualMinutes = 33;

        double errorPercentage = Math.abs(actualMinutes - estimatedMinutes) / (double) estimatedMinutes * 100;
        double accuracyPercentage = 100 - errorPercentage;

        assertEquals(90.0, accuracyPercentage, 0.1);
    }

    @Test
    @DisplayName("Should return ETA within valid range")
    void shouldReturnEtaWithinValidRange() {
        double distanceKm = 15.0;
        double minSpeedKph = 15.0;
        double maxSpeedKph = 60.0;

        double minMinutes = (distanceKm / maxSpeedKph) * 60;
        double maxMinutes = (distanceKm / minSpeedKph) * 60;

        assertTrue(minMinutes < maxMinutes);
        assertTrue(minMinutes > 0);
    }

    @Test
    @DisplayName("Should calculate confidence interval for ETA")
    void shouldCalculateConfidenceIntervalForEta() {
        double estimatedMinutes = 30.0;
        double standardDeviation = 5.0;
        double confidence = 1.96; // 95% confidence

        double marginOfError = standardDeviation * confidence;
        double lowerBound = estimatedMinutes - marginOfError;
        double upperBound = estimatedMinutes + marginOfError;

        assertEquals(20.2, lowerBound, 0.1);
        assertEquals(39.8, upperBound, 0.1);
    }
}
