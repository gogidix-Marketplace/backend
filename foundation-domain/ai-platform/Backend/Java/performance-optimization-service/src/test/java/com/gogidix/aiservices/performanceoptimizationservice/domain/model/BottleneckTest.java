package com.gogidix.aiservices.performanceoptimizationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Bottleneck Record Tests")
class BottleneckTest {

    private static final String COMPONENT = "Database";
    private static final String SEVERITY = "HIGH";
    private static final String DESCRIPTION = "Slow query execution";
    private static final double IMPACT_SCORE = 85.5;

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create bottleneck with all fields")
        void shouldCreateWithAllFields() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.component()).isEqualTo(COMPONENT);
            assertThat(bottleneck.severity()).isEqualTo(SEVERITY);
            assertThat(bottleneck.description()).isEqualTo(DESCRIPTION);
            assertThat(bottleneck.impactScore()).isEqualTo(IMPACT_SCORE);
        }

        @Test
        @DisplayName("Should create bottleneck with null component")
        void shouldCreateWithNullComponent() {
            Bottleneck bottleneck = new Bottleneck(null, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.component()).isNull();
        }

        @Test
        @DisplayName("Should create bottleneck with null severity")
        void shouldCreateWithNullSeverity() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, null, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.severity()).isNull();
        }

        @Test
        @DisplayName("Should create bottleneck with null description")
        void shouldCreateWithNullDescription() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, null, IMPACT_SCORE);

            assertThat(bottleneck.description()).isNull();
        }

        @Test
        @DisplayName("Should create bottleneck with zero impact score")
        void shouldCreateWithZeroImpactScore() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, 0.0);

            assertThat(bottleneck.impactScore()).isZero();
        }

        @Test
        @DisplayName("Should create bottleneck with negative impact score")
        void shouldCreateWithNegativeImpactScore() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, -10.5);

            assertThat(bottleneck.impactScore()).isEqualTo(-10.5);
        }

        @Test
        @DisplayName("Should create bottleneck with maximum impact score")
        void shouldCreateWithMaximumImpactScore() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, 100.0);

            assertThat(bottleneck.impactScore()).isEqualTo(100.0);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal with same values")
        void shouldBeEqualWithSameValues() {
            Bottleneck bottleneck1 = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);
            Bottleneck bottleneck2 = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck1).isEqualTo(bottleneck2);
            assertThat(bottleneck1.hashCode()).isEqualTo(bottleneck2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal with different component")
        void shouldNotBeEqualWithDifferentComponent() {
            Bottleneck bottleneck1 = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);
            Bottleneck bottleneck2 = new Bottleneck("Cache", SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck1).isNotEqualTo(bottleneck2);
        }

        @Test
        @DisplayName("Should not be equal with different severity")
        void shouldNotBeEqualWithDifferentSeverity() {
            Bottleneck bottleneck1 = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);
            Bottleneck bottleneck2 = new Bottleneck(COMPONENT, "MEDIUM", DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck1).isNotEqualTo(bottleneck2);
        }

        @Test
        @DisplayName("Should not be equal with different description")
        void shouldNotBeEqualWithDifferentDescription() {
            Bottleneck bottleneck1 = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);
            Bottleneck bottleneck2 = new Bottleneck(COMPONENT, SEVERITY, "Different description", IMPACT_SCORE);

            assertThat(bottleneck1).isNotEqualTo(bottleneck2);
        }

        @Test
        @DisplayName("Should not be equal with different impact score")
        void shouldNotBeEqualWithDifferentImpactScore() {
            Bottleneck bottleneck1 = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);
            Bottleneck bottleneck2 = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, 50.0);

            assertThat(bottleneck1).isNotEqualTo(bottleneck2);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck).isEqualTo(bottleneck);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck).isNotNull();
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck).isNotEqualTo("not a bottleneck");
        }
    }

    @Nested
    @DisplayName("Component Tests")
    class ComponentTests {

        @ParameterizedTest
        @ValueSource(strings = {"Database", "Cache", "API", "Network", "CPU", "Memory", "Disk", "External Service"})
        @DisplayName("Should accept various component types")
        void shouldAcceptVariousComponentTypes(String component) {
            Bottleneck bottleneck = new Bottleneck(component, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.component()).isEqualTo(component);
        }

        @Test
        @DisplayName("Should accept empty component")
        void shouldAcceptEmptyComponent() {
            Bottleneck bottleneck = new Bottleneck("", SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.component()).isEmpty();
        }

        @Test
        @DisplayName("Should accept component with special characters")
        void shouldAcceptComponentWithSpecialCharacters() {
            Bottleneck bottleneck = new Bottleneck("API-Gateway/v2", SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.component()).isEqualTo("API-Gateway/v2");
        }

        @Test
        @DisplayName("Should accept component with unicode")
        void shouldAcceptComponentWithUnicode() {
            Bottleneck bottleneck = new Bottleneck("数据库", SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.component()).isEqualTo("数据库");
        }
    }

    @Nested
    @DisplayName("Severity Tests")
    class SeverityTests {

        @ParameterizedTest
        @ValueSource(strings = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
        @DisplayName("Should accept standard severity levels")
        void shouldAcceptStandardSeverityLevels(String severity) {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, severity, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.severity()).isEqualTo(severity);
        }

        @Test
        @DisplayName("Should accept custom severity levels")
        void shouldAcceptCustomSeverityLevels() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, "EXTREME", DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.severity()).isEqualTo("EXTREME");
        }

        @Test
        @DisplayName("Should accept empty severity")
        void shouldAcceptEmptySeverity() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, "", DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.severity()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should accept short description")
        void shouldAcceptShortDescription() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, "Slow", IMPACT_SCORE);

            assertThat(bottleneck.description()).isEqualTo("Slow");
        }

        @Test
        @DisplayName("Should accept long description")
        void shouldAcceptLongDescription() {
            String longDescription = "This is a very detailed description of the performance bottleneck ".repeat(5);
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, longDescription, IMPACT_SCORE);

            assertThat(bottleneck.description()).isEqualTo(longDescription);
        }

        @Test
        @DisplayName("Should accept empty description")
        void shouldAcceptEmptyDescription() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, "", IMPACT_SCORE);

            assertThat(bottleneck.description()).isEmpty();
        }

        @Test
        @DisplayName("Should accept description with special characters")
        void shouldAcceptDescriptionWithSpecialCharacters() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, "Error: timeout after 30s!", IMPACT_SCORE);

            assertThat(bottleneck.description()).isEqualTo("Error: timeout after 30s!");
        }

        @Test
        @DisplayName("Should accept description with newlines")
        void shouldAcceptDescriptionWithNewlines() {
            String description = "Multiple issues:\n1. Slow query\n2. Missing index";
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, description, IMPACT_SCORE);

            assertThat(bottleneck.description()).isEqualTo(description);
        }
    }

    @Nested
    @DisplayName("Impact Score Tests")
    class ImpactScoreTests {

        @ParameterizedTest
        @ValueSource(doubles = {0.0, 25.5, 50.0, 75.5, 100.0})
        @DisplayName("Should accept various impact scores")
        void shouldAcceptVariousImpactScores(double score) {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, score);

            assertThat(bottleneck.impactScore()).isEqualTo(score);
        }

        @Test
        @DisplayName("Should accept very small impact score")
        void shouldAcceptVerySmallImpactScore() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, 0.001);

            assertThat(bottleneck.impactScore()).isEqualTo(0.001);
        }

        @Test
        @DisplayName("Should accept negative impact score")
        void shouldAcceptNegativeImpactScore() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, -50.0);

            assertThat(bottleneck.impactScore()).isEqualTo(-50.0);
        }

        @Test
        @DisplayName("Should accept very high impact score")
        void shouldAcceptVeryHighImpactScore() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, 999.99);

            assertThat(bottleneck.impactScore()).isEqualTo(999.99);
        }

        @Test
        @DisplayName("Should handle floating point precision")
        void shouldHandleFloatingPointPrecision() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, 85.55555555);

            assertThat(bottleneck.impactScore()).isEqualTo(85.55555555);
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should represent database bottleneck")
        void shouldRepresentDatabaseBottleneck() {
            Bottleneck bottleneck = new Bottleneck("Database", "HIGH", "N+1 query problem", 90.0);

            assertThat(bottleneck.component()).isEqualTo("Database");
            assertThat(bottleneck.severity()).isEqualTo("HIGH");
            assertThat(bottleneck.impactScore()).isGreaterThanOrEqualTo(80.0);
        }

        @Test
        @DisplayName("Should represent cache bottleneck")
        void shouldRepresentCacheBottleneck() {
            Bottleneck bottleneck = new Bottleneck("Cache", "MEDIUM", "Low hit ratio", 60.0);

            assertThat(bottleneck.component()).isEqualTo("Cache");
            assertThat(bottleneck.severity()).isEqualTo("MEDIUM");
        }

        @Test
        @DisplayName("Should represent API bottleneck")
        void shouldRepresentApiBottleneck() {
            Bottleneck bottleneck = new Bottleneck("API", "CRITICAL", "Timeout errors", 95.0);

            assertThat(bottleneck.component()).isEqualTo("API");
            assertThat(bottleneck.severity()).isEqualTo("CRITICAL");
            assertThat(bottleneck.impactScore()).isGreaterThan(90.0);
        }

        @Test
        @DisplayName("Should represent network bottleneck")
        void shouldRepresentNetworkBottleneck() {
            Bottleneck bottleneck = new Bottleneck("Network", "LOW", "High latency", 40.0);

            assertThat(bottleneck.component()).isEqualTo("Network");
            assertThat(bottleneck.severity()).isEqualTo("LOW");
        }

        @Test
        @DisplayName("Should represent memory bottleneck")
        void shouldRepresentMemoryBottleneck() {
            Bottleneck bottleneck = new Bottleneck("Memory", "HIGH", "Memory leak detected", 85.0);

            assertThat(bottleneck.component()).isEqualTo("Memory");
            assertThat(bottleneck.impactScore()).isGreaterThan(80.0);
        }

        @Test
        @DisplayName("Should represent CPU bottleneck")
        void shouldRepresentCpuBottleneck() {
            Bottleneck bottleneck = new Bottleneck("CPU", "MEDIUM", "High utilization", 70.0);

            assertThat(bottleneck.component()).isEqualTo("CPU");
            assertThat(bottleneck.impactScore()).isBetween(60.0, 80.0);
        }
    }

    @Nested
    @DisplayName("Record Methods Tests")
    class RecordMethodsTests {

        @Test
        @DisplayName("Should implement toString")
        void shouldImplementToString() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            String toString = bottleneck.toString();

            assertThat(toString).contains(COMPONENT);
            assertThat(toString).contains(SEVERITY);
            assertThat(toString).contains(DESCRIPTION);
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            int hashCode1 = bottleneck.hashCode();
            int hashCode2 = bottleneck.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        @DisplayName("Should have different hashCode for different values")
        void shouldHaveDifferentHashCodeForDifferentValues() {
            Bottleneck bottleneck1 = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, IMPACT_SCORE);
            Bottleneck bottleneck2 = new Bottleneck("Different", SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck1.hashCode()).isNotEqualTo(bottleneck2.hashCode());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle all null fields")
        void shouldHandleAllNullFields() {
            Bottleneck bottleneck = new Bottleneck(null, null, null, 0.0);

            assertThat(bottleneck.component()).isNull();
            assertThat(bottleneck.severity()).isNull();
            assertThat(bottleneck.description()).isNull();
            assertThat(bottleneck.impactScore()).isZero();
        }

        @Test
        @DisplayName("Should handle very long component name")
        void shouldHandleVeryLongComponentName() {
            String longComponent = "Component-" + "X".repeat(200);
            Bottleneck bottleneck = new Bottleneck(longComponent, SEVERITY, DESCRIPTION, IMPACT_SCORE);

            assertThat(bottleneck.component()).isEqualTo(longComponent);
        }

        @Test
        @DisplayName("Should handle infinity impact score")
        void shouldHandleInfinityImpactScore() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, Double.POSITIVE_INFINITY);

            assertThat(bottleneck.impactScore()).isPositive().isInfinite();
        }

        @Test
        @DisplayName("Should handle NaN impact score")
        void shouldHandleNaNImpactScore() {
            Bottleneck bottleneck = new Bottleneck(COMPONENT, SEVERITY, DESCRIPTION, Double.NaN);

            assertThat(bottleneck.impactScore()).isNaN();
        }
    }
}
