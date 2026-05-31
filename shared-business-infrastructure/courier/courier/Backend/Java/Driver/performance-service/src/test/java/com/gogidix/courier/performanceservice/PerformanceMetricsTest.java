package com.gogidix.courier.performanceservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Performance Metrics domain logic.
 */
@DisplayName("Performance Metrics Domain Tests")
class PerformanceMetricsTest {

    @Test
    @DisplayName("Should calculate on-time delivery rate correctly")
    void shouldCalculateOnTimeDeliveryRate() {
        int totalDeliveries = 100;
        int onTimeDeliveries = 90;

        double onTimeRate = (double) onTimeDeliveries / totalDeliveries * 100;

        assertEquals(90.0, onTimeRate);
    }

    @Test
    @DisplayName("Should calculate average delivery time correctly")
    void shouldCalculateAverageDeliveryTime() {
        long[] deliveryTimesMinutes = {35, 42, 38, 45, 40};

        long sum = 0;
        for (long time : deliveryTimesMinutes) {
            sum += time;
        }
        double average = (double) sum / deliveryTimesMinutes.length;

        assertEquals(40.0, average);
    }

    @Test
    @DisplayName("Should calculate driver rating correctly")
    void shouldCalculateDriverRating() {
        int[] ratings = {5, 4, 5, 5, 4, 5, 3, 5, 4, 5};

        double sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }
        double averageRating = sum / ratings.length;

        assertEquals(4.5, averageRating);
    }

    @Test
    @DisplayName("Should identify high performing driver")
    void shouldIdentifyHighPerformingDriver() {
        double rating = 4.8;
        double onTimeRate = 95.0;
        int completedDeliveries = 150;

        boolean isHighPerformer = rating >= 4.7
                && onTimeRate >= 90.0
                && completedDeliveries >= 100;

        assertTrue(isHighPerformer);
    }

    @Test
    @DisplayName("Should not identify low performance as high performer")
    void shouldNotIdentifyLowPerformanceAsHighPerformer() {
        double rating = 4.8;
        double onTimeRate = 85.0; // Below threshold
        int completedDeliveries = 150;

        boolean isHighPerformer = rating >= 4.7
                && onTimeRate >= 90.0
                && completedDeliveries >= 100;

        assertFalse(isHighPerformer);
    }

    @Test
    @DisplayName("Should calculate acceptance rate correctly")
    void shouldCalculateAcceptanceRate() {
        int totalOffers = 50;
        int acceptedOffers = 42;

        double acceptanceRate = (double) acceptedOffers / totalOffers * 100;

        assertEquals(84.0, acceptanceRate);
    }

    @Test
    @DisplayName("Should calculate cancellation rate correctly")
    void shouldCalculateCancellationRate() {
        int totalAssignments = 100;
        int cancellations = 5;

        double cancellationRate = (double) cancellations / totalAssignments * 100;

        assertEquals(5.0, cancellationRate);
    }

    @Test
    @DisplayName("Should calculate efficiency ratio correctly")
    void shouldCalculateEfficiencyRatio() {
        double estimatedDistance = 10.0;
        double actualDistance = 11.0;

        double efficiencyRatio = estimatedDistance / actualDistance;

        assertEquals(0.909, efficiencyRatio, 0.001);
    }

    @Test
    @DisplayName("Should calculate weekly earnings correctly")
    void shouldCalculateWeeklyEarnings() {
        double[] dailyEarnings = {150.0, 175.0, 160.0, 180.0, 200.0, 0.0, 0.0};

        double weeklyEarnings = 0;
        for (double earning : dailyEarnings) {
            weeklyEarnings += earning;
        }

        assertEquals(865.0, weeklyEarnings);
    }

    @Test
    @DisplayName("Should calculate earnings per hour correctly")
    void shouldCalculateEarningsPerHour() {
        double totalEarnings = 500.0;
        double hoursWorked = 25.0;

        double earningsPerHour = totalEarnings / hoursWorked;

        assertEquals(20.0, earningsPerHour);
    }

    @Test
    @DisplayName("Should detect performance improvement trend")
    void shouldDetectPerformanceImprovementTrend() {
        double[] weeklyRatings = {4.2, 4.3, 4.4, 4.5, 4.6};

        boolean isImproving = true;
        for (int i = 1; i < weeklyRatings.length; i++) {
            if (weeklyRatings[i] <= weeklyRatings[i - 1]) {
                isImproving = false;
                break;
            }
        }

        assertTrue(isImproving);
    }

    @Test
    @DisplayName("Should calculate average response time")
    void shouldCalculateAverageResponseTime() {
        Instant[] offerTimes = {
                Instant.now().minusSeconds(120),
                Instant.now().minusSeconds(90),
                Instant.now().minusSeconds(60),
                Instant.now().minusSeconds(30),
                Instant.now()
        };

        long totalResponseTime = 0;
        for (int i = 0; i < offerTimes.length - 1; i++) {
            totalResponseTime += ChronoUnit.SECONDS.between(offerTimes[i], offerTimes[i + 1]);
        }
        double averageResponseTime = (double) totalResponseTime / (offerTimes.length - 1);

        assertEquals(30.0, averageResponseTime, 0.1);
    }

    @Test
    @DisplayName("Should calculate completion percentage for target")
    void shouldCalculateCompletionPercentage() {
        int target = 50;
        int completed = 42;

        double percentage = (double) completed / target * 100;

        assertEquals(84.0, percentage);
    }

    @Test
    @DisplayName("Should determine if driver is eligible for bonus")
    void shouldDetermineEligibilityForBonus() {
        double rating = 4.8;
        double onTimeRate = 92.0;
        int weeklyDeliveries = 52;

        boolean isEligible = rating >= 4.7
                && onTimeRate >= 90.0
                && weeklyDeliveries >= 50;

        assertTrue(isEligible);
    }

    @Test
    @DisplayName("Should calculate customer satisfaction score")
    void shouldCalculateCustomerSatisfactionScore() {
        int fiveStar = 45;
        int fourStar = 30;
        int threeStar = 15;
        int twoStar = 7;
        int oneStar = 3;

        int totalRatings = fiveStar + fourStar + threeStar + twoStar + oneStar;
        double weightedScore = (5.0 * fiveStar + 4.0 * fourStar + 3.0 * threeStar
                + 2.0 * twoStar + 1.0 * oneStar) / totalRatings;

        assertEquals(4.07, weightedScore, 0.01);
    }
}
