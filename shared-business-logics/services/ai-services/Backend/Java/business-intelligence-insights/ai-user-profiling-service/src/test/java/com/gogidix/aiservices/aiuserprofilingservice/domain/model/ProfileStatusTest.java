package com.gogidix.aiservices.aiuserprofilingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProfileStatus Enum Tests")
class ProfileStatusTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(ProfileStatus.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllValues(ProfileStatus value) {
            assertThat(value).isNotNull();
        }

        @Test
        @DisplayName("Should have DRAFT enum value")
        void shouldHaveDraftValue() {
            assertThat(ProfileStatus.valueOf("DRAFT")).isEqualTo(ProfileStatus.DRAFT);
        }

        @Test
        @DisplayName("Should have ACTIVE enum value")
        void shouldHaveActiveValue() {
            assertThat(ProfileStatus.valueOf("ACTIVE")).isEqualTo(ProfileStatus.ACTIVE);
        }

        @Test
        @DisplayName("Should have INACTIVE enum value")
        void shouldHaveInactiveValue() {
            assertThat(ProfileStatus.valueOf("INACTIVE")).isEqualTo(ProfileStatus.INACTIVE);
        }

        @Test
        @DisplayName("Should have ARCHIVED enum value")
        void shouldHaveArchivedValue() {
            assertThat(ProfileStatus.valueOf("ARCHIVED")).isEqualTo(ProfileStatus.ARCHIVED);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have exactly 4 enum values")
        void shouldHaveExactlyFourValues() {
            ProfileStatus[] values = ProfileStatus.values();
            assertThat(values).hasSize(4);
        }

        @Test
        @DisplayName("Should contain all expected values")
        void shouldContainAllExpectedValues() {
            ProfileStatus[] values = ProfileStatus.values();
            assertThat(values).containsExactly(
                    ProfileStatus.DRAFT,
                    ProfileStatus.ACTIVE,
                    ProfileStatus.INACTIVE,
                    ProfileStatus.ARCHIVED
            );
        }
    }
}
