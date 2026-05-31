package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BasketStatus Enum Tests")
class BasketStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(BasketStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(BasketStatus value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have DRAFT enum value")
        void shouldHaveDraftValue() {
            assertThat(BasketStatus.valueOf("DRAFT")).isEqualTo(BasketStatus.DRAFT);
        }

        @Test
        @DisplayName("Should have ACTIVE enum value")
        void shouldHaveActiveValue() {
            assertThat(BasketStatus.valueOf("ACTIVE")).isEqualTo(BasketStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should have INACTIVE enum value")
        void shouldHaveInactiveValue() {
            assertThat(BasketStatus.valueOf("INACTIVE")).isEqualTo(BasketStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should have ARCHIVED enum value")
        void shouldHaveArchivedValue() {
            assertThat(BasketStatus.valueOf("ARCHIVED")).isEqualTo(BasketStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            BasketStatus[] values = BasketStatus.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            BasketStatus[] values = BasketStatus.values();
            assertThat(values).containsExactly(
                    BasketStatus.DRAFT,
                    BasketStatus.ACTIVE,
                    BasketStatus.INACTIVE,
                    BasketStatus.ARCHIVED
            );
        }
    }
}
