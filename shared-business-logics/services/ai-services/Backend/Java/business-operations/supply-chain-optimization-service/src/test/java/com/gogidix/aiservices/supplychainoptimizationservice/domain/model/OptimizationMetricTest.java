package com.gogidix.aiservices.supplychainoptimizationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OptimizationMetric Domain Model Tests")
class OptimizationMetricTest {

    @Nested
    @DisplayName("OptimizationMetric Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create metric with valid parameters")
        void shouldCreateMetric() {
            OptimizationMetric metric = OptimizationMetric.builder()
                    .metricName("Cost")
                    .currentValue(1000)
                    .optimizedValue(850)
                    .unit("USD")
                    .direction(OptimizationMetric.ImprovementDirection.LOWER_IS_BETTER)
                    .build();

            assertThat(metric).isNotNull();
            assertThat(metric.getMetricName()).isEqualTo("Cost");
            assertThat(metric.getCurrentValue()).isEqualTo(1000);
            assertThat(metric.getOptimizedValue()).isEqualTo(850);
            assertThat(metric.getImprovement()).isEqualTo(150);
        }

        @Test
        @DisplayName("Should calculate positive improvement for lower-is-better")
        void shouldCalculatePositiveImprovementForLowerIsBetter() {
            OptimizationMetric metric = OptimizationMetric.builder()
                    .metricName("Cost")
                    .currentValue(1000)
                    .optimizedValue(800)
                    .direction(OptimizationMetric.ImprovementDirection.LOWER_IS_BETTER)
                    .build();

            assertThat(metric.isPositiveImprovement()).isTrue();
            assertThat(metric.getImprovement()).isEqualTo(200);
        }

        @Test
        @DisplayName("Should calculate positive improvement for higher-is-better")
        void shouldCalculatePositiveImprovementForHigherIsBetter() {
            OptimizationMetric metric = OptimizationMetric.builder()
                    .metricName("Efficiency")
                    .currentValue(0.75)
                    .optimizedValue(0.90)
                    .direction(OptimizationMetric.ImprovementDirection.HIGHER_IS_BETTER)
                    .build();

            assertThat(metric.isPositiveImprovement()).isTrue();
            assertThat(metric.getImprovement()).isEqualTo(0.15);
        }

        @Test
        @DisplayName("Should calculate improvement percentage")
        void shouldCalculateImprovementPercentage() {
            OptimizationMetric metric = OptimizationMetric.builder()
                    .metricName("Cost")
                    .currentValue(1000)
                    .optimizedValue(850)
                    .direction(OptimizationMetric.ImprovementDirection.LOWER_IS_BETTER)
                    .build();

            assertThat(metric.getImprovementPercentage()).isEqualTo(15.0);
        }
    }
}
