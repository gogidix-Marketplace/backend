package com.gogidix.dashboard.aggregation.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DataQualityTest {

    @Nested
    @DisplayName("Factory method tests")
    class FactoryTests {
        @Test
        void excellent_createsHighQuality() {
            DataQuality dq = DataQuality.excellent(100);
            assertTrue(dq.isHighQuality());
            assertEquals(DataQuality.QualityLevel.EXCELLENT, dq.getOverallLevel());
            assertEquals(100, dq.getTotalDataPoints());
        }

        @Test
        void good_createsGoodQuality() {
            DataQuality dq = DataQuality.good(100, 5);
            assertTrue(dq.isHighQuality());
        }

        @Test
        void poor_createsPoorQuality() {
            DataQuality dq = DataQuality.poor(100, 50, 20);
            assertTrue(dq.isLow());
        }

        @Test
        void unknown_createsLowestQuality() {
            DataQuality dq = DataQuality.unknown();
            assertEquals(0.0, dq.getOverallScore());
            assertFalse(dq.isHighQuality());
        }
    }

    @Nested
    @DisplayName("Validation tests")
    class ValidationTests {
        @Test
        void constructor_withNegativeCompleteness_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new DataQuality(-0.1, 0.9, 0.9, 0.9, 0.9, 100, 100, 0, LocalDateTime.now()));
        }

        @Test
        void constructor_withCompletenessAboveOne_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new DataQuality(1.1, 0.9, 0.9, 0.9, 0.9, 100, 100, 0, LocalDateTime.now()));
        }

        @Test
        void constructor_withNegativeDataPoints_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new DataQuality(0.9, 0.9, 0.9, 0.9, 0.9, -1, 0, 0, LocalDateTime.now()));
        }

        @Test
        void constructor_withValidExceedingTotal_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new DataQuality(0.9, 0.9, 0.9, 0.9, 0.9, 10, 20, 0, LocalDateTime.now()));
        }

        @Test
        void constructor_withOutliersExceedingTotal_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new DataQuality(0.9, 0.9, 0.9, 0.9, 0.9, 10, 10, 20, LocalDateTime.now()));
        }

        @Test
        void constructor_withNullLastUpdated_throws() {
            assertThrows(NullPointerException.class, () ->
                new DataQuality(0.9, 0.9, 0.9, 0.9, 0.9, 10, 10, 0, null));
        }
    }

    @Nested
    @DisplayName("Quality assessment tests")
    class AssessmentTests {
        @Test
        void getOverallScore_returnsWeightedAverage() {
            DataQuality dq = new DataQuality(1.0, 1.0, 1.0, 1.0, 1.0, 100, 100, 0, LocalDateTime.now());
            assertEquals(1.0, dq.getOverallScore(), 0.001);
        }

        @Test
        void getConfidenceLevel_highDataPoints_returnsHighConfidence() {
            DataQuality dq = DataQuality.excellent(200);
            assertTrue(dq.getConfidenceLevel() > 0.9);
        }

        @Test
        void getConfidenceLevel_lowDataPoints_returnsLowerConfidence() {
            DataQuality dq = new DataQuality(1.0, 1.0, 1.0, 1.0, 1.0, 5, 5, 0, LocalDateTime.now());
            assertTrue(dq.getConfidenceLevel() < 1.0);
        }

        @Test
        void isSuitableForAlerting_highQualityTimelyComplete() {
            DataQuality dq = DataQuality.excellent(100);
            assertTrue(dq.isSuitableForAlerting());
        }

        @Test
        void isSuitableForAlerting_notSuitableWhenLow() {
            DataQuality dq = DataQuality.poor(100, 30, 20);
            assertFalse(dq.isSuitableForAlerting());
        }

        @Test
        void needsImprovement_whenScoresLow() {
            DataQuality dq = DataQuality.poor(100, 30, 20);
            assertTrue(dq.needsImprovement());
        }
    }

    @Nested
    @DisplayName("Dimension analysis tests")
    class DimensionTests {
        @Test
        void getMostProblematicDimension_returnsLowestScore() {
            DataQuality dq = new DataQuality(0.9, 0.5, 0.9, 0.9, 0.9, 100, 100, 0, LocalDateTime.now());
            assertEquals("Accuracy", dq.getMostProblematicDimension());
        }

        @Test
        void getQualityIssues_returnsListOfIssues() {
            DataQuality dq = new DataQuality(0.5, 0.5, 0.5, 0.5, 0.5, 5, 3, 5, LocalDateTime.now());
            assertFalse(dq.getQualityIssues().isEmpty());
        }

        @Test
        void getQualityIssues_noIssuesForExcellent() {
            DataQuality dq = DataQuality.excellent(100);
            assertTrue(dq.getQualityIssues().isEmpty());
        }
    }

    @Nested
    @DisplayName("Mutation tests")
    class MutationTests {
        @Test
        void withNewDataPoint_valid_incrementsTotal() {
            DataQuality dq = DataQuality.excellent(100);
            DataQuality updated = dq.withNewDataPoint();
            assertEquals(101, updated.getTotalDataPoints());
        }

        @Test
        void withNewDataPoint_invalid_incrementsOutliers() {
            DataQuality dq = DataQuality.excellent(100);
            DataQuality updated = dq.withNewDataPoint(false, true);
            assertEquals(1, updated.getOutlierCount());
        }

        @Test
        void withTimeAging_decreasesTimeliness() {
            DataQuality dq = new DataQuality(1.0, 1.0, 1.0, 1.0, 1.0, 100, 100, 0, LocalDateTime.now().minusHours(2));
            DataQuality aged = dq.withTimeAging(LocalDateTime.now());
            assertTrue(aged.getTimeliness() <= dq.getTimeliness());
        }

        @Test
        void combineWith_weightsByDataPoints() {
            DataQuality dq1 = DataQuality.excellent(100);
            DataQuality dq2 = DataQuality.poor(100, 30, 20);
            DataQuality combined = dq1.combineWith(dq2);
            assertEquals(200, combined.getTotalDataPoints());
        }
    }

    @Test
    void getOutlierPercentage_returnsCorrectPercentage() {
        DataQuality dq = DataQuality.good(100, 10);
        assertEquals(10.0, dq.getOutlierPercentage(), 0.01);
    }

    @Test
    void getValidDataPercentage_returnsCorrectPercentage() {
        DataQuality dq = DataQuality.good(100, 5);
        assertEquals(95.0, dq.getValidDataPercentage(), 0.01);
    }

    @Test
    void toString_containsQualityInfo() {
        DataQuality dq = DataQuality.excellent(100);
        assertTrue(dq.toString().contains("EXCELLENT"));
    }

    @Nested
    @DisplayName("QualityLevel tests")
    class QualityLevelTests {
        @Test
        void isSufficientFor_criticalAlerting_onlyExcellent() {
            assertTrue(DataQuality.QualityLevel.EXCELLENT.isSufficientFor(DataQuality.UsageType.CRITICAL_ALERTING));
            assertFalse(DataQuality.QualityLevel.GOOD.isSufficientFor(DataQuality.UsageType.CRITICAL_ALERTING));
        }

        @Test
        void isSufficientFor_exploratory_anyQuality() {
            for (DataQuality.QualityLevel level : DataQuality.QualityLevel.values()) {
                assertTrue(level.isSufficientFor(DataQuality.UsageType.EXPLORATORY_ANALYSIS));
            }
        }
    }
}
