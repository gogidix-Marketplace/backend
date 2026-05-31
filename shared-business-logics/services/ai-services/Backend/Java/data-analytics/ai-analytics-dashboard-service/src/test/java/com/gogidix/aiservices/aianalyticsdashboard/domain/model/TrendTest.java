package com.gogidix.aiservices.aianalyticsdashboard.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Trend Enum Tests")
class TrendTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(Trend.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(Trend value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have UP trend")
        void shouldHaveUpTrend() {
            assertThat(Trend.UP).isNotNull();
        }

        @Test
        @DisplayName("Should have DOWN trend")
        void shouldHaveDownTrend() {
            assertThat(Trend.DOWN).isNotNull();
        }

        @Test
        @DisplayName("Should have STABLE trend")
        void shouldHaveStableTrend() {
            assertThat(Trend.STABLE).isNotNull();
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have 3 trend types")
        void shouldHaveThreeTypes() {
            assertThat(Trend.values()).hasSize(3);
        }

        @Test
        @DisplayName("Enum values should be consistent")
        void shouldBeConsistent() {
            Trend up1 = Trend.UP;
            Trend up2 = Trend.valueOf("UP");
            assertThat(up1).isEqualTo(up2);
        }
    }
}
