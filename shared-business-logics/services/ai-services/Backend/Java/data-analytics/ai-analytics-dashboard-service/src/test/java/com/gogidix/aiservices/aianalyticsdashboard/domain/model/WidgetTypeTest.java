package com.gogidix.aiservices.aianalyticsdashboard.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("WidgetType Enum Tests")
class WidgetTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(WidgetType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(WidgetType value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have CHART type")
        void shouldHaveChartType() {
            assertThat(WidgetType.CHART).isNotNull();
        }

        @Test
        @DisplayName("Should have METRIC type")
        void shouldHaveMetricType() {
            assertThat(WidgetType.METRIC).isNotNull();
        }

        @Test
        @DisplayName("Should have TABLE type")
        void shouldHaveTableType() {
            assertThat(WidgetType.TABLE).isNotNull();
        }

        @Test
        @DisplayName("Should have GAUGE type")
        void shouldHaveGaugeType() {
            assertThat(WidgetType.GAUGE).isNotNull();
        }

        @Test
        @DisplayName("Should have MAP type")
        void shouldHaveMapType() {
            assertThat(WidgetType.MAP).isNotNull();
        }

        @Test
        @DisplayName("Should have FUNNEL type")
        void shouldHaveFunnelType() {
            assertThat(WidgetType.FUNNEL).isNotNull();
        }

        @Test
        @DisplayName("Should have HEATMAP type")
        void shouldHaveHeatmapType() {
            assertThat(WidgetType.HEATMAP).isNotNull();
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have 7 widget types")
        void shouldHaveSevenTypes() {
            assertThat(WidgetType.values()).hasSize(7);
        }

        @Test
        @DisplayName("Enum values should be consistent")
        void shouldBeConsistent() {
            WidgetType chart1 = WidgetType.CHART;
            WidgetType chart2 = WidgetType.valueOf("CHART");
            assertThat(chart1).isEqualTo(chart2);
        }
    }
}
