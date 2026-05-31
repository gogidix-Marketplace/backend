package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BasketType Enum Tests")
class BasketTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(BasketType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(BasketType value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have BEHAVIORAL enum value")
        void shouldHaveBehavioralValue() {
            assertThat(BasketType.valueOf("BEHAVIORAL")).isEqualTo(BasketType.BEHAVIORAL);
        }

        @Test
        @DisplayName("Should have DEMOGRAPHIC enum value")
        void shouldHaveDemographicValue() {
            assertThat(BasketType.valueOf("DEMOGRAPHIC")).isEqualTo(BasketType.DEMOGRAPHIC);
        }

        @Test
        @DisplayName("Should have TRANSACTIONAL enum value")
        void shouldHaveTransactionalValue() {
            assertThat(BasketType.valueOf("TRANSACTIONAL")).isEqualTo(BasketType.TRANSACTIONAL);
        }

        @Test
        @DisplayName("Should have CUSTOM enum value")
        void shouldHaveCustomValue() {
            assertThat(BasketType.valueOf("CUSTOM")).isEqualTo(BasketType.CUSTOM);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            BasketType[] values = BasketType.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            BasketType[] values = BasketType.values();
            assertThat(values).containsExactly(
                    BasketType.BEHAVIORAL,
                    BasketType.DEMOGRAPHIC,
                    BasketType.TRANSACTIONAL,
                    BasketType.CUSTOM
            );
        }
    }
}
