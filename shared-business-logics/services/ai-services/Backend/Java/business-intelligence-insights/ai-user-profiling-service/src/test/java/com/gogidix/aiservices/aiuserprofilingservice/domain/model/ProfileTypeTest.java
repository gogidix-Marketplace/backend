package com.gogidix.aiservices.aiuserprofilingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProfileType Enum Tests")
class ProfileTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(ProfileType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(ProfileType value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have BEHAVIORAL enum value")
        void shouldHaveBehavioralValue() {
            assertThat(ProfileType.valueOf("BEHAVIORAL")).isEqualTo(ProfileType.BEHAVIORAL);
        }

        @Test
        @DisplayName("Should have DEMOGRAPHIC enum value")
        void shouldHaveDemographicValue() {
            assertThat(ProfileType.valueOf("DEMOGRAPHIC")).isEqualTo(ProfileType.DEMOGRAPHIC);
        }

        @Test
        @DisplayName("Should have TRANSACTIONAL enum value")
        void shouldHaveTransactionalValue() {
            assertThat(ProfileType.valueOf("TRANSACTIONAL")).isEqualTo(ProfileType.TRANSACTIONAL);
        }

        @Test
        @DisplayName("Should have CUSTOM enum value")
        void shouldHaveCustomValue() {
            assertThat(ProfileType.valueOf("CUSTOM")).isEqualTo(ProfileType.CUSTOM);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            ProfileType[] values = ProfileType.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            ProfileType[] values = ProfileType.values();
            assertThat(values).containsExactly(
                    ProfileType.BEHAVIORAL,
                    ProfileType.DEMOGRAPHIC,
                    ProfileType.TRANSACTIONAL,
                    ProfileType.CUSTOM
            );
        }
    }
}
