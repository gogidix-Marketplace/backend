package com.gogidix.centralconfiguration.configserver.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class ConfigEnvironmentTest {

    @Nested
    @DisplayName("Enum Values")
    class EnumValues {

        @Test
        @DisplayName("Should have exactly 6 environments")
        void shouldHaveSixEnvironments() {
            assertThat(ConfigEnvironment.values()).hasSize(6);
        }

        @Test
        @DisplayName("Should contain all expected environments")
        void shouldContainAllExpectedEnvironments() {
            assertThat(ConfigEnvironment.values())
                .containsExactly(
                    ConfigEnvironment.DEVELOPMENT,
                    ConfigEnvironment.STAGING,
                    ConfigEnvironment.QA,
                    ConfigEnvironment.UAT,
                    ConfigEnvironment.PRODUCTION,
                    ConfigEnvironment.DR
                );
        }
    }

    @Nested
    @DisplayName("Profile Mapping")
    class ProfileMapping {

        @ParameterizedTest
        @CsvSource({
            "DEVELOPMENT, dev",
            "STAGING, staging",
            "QA, qa",
            "UAT, uat",
            "PRODUCTION, prod",
            "DR, dr"
        })
        @DisplayName("Should map environment to correct profile")
        void shouldMapEnvironmentToCorrectProfile(ConfigEnvironment env, String expectedProfile) {
            assertThat(env.getProfile()).isEqualTo(expectedProfile);
        }
    }

    @Nested
    @DisplayName("From Profile Lookup")
    class FromProfileLookup {

        @ParameterizedTest
        @CsvSource({
            "dev, DEVELOPMENT",
            "staging, STAGING",
            "qa, QA",
            "uat, UAT",
            "prod, PRODUCTION",
            "dr, DR"
        })
        @DisplayName("Should resolve profile string to correct environment")
        void shouldResolveProfileToCorrectEnvironment(String profile, ConfigEnvironment expected) {
            assertThat(ConfigEnvironment.fromProfile(profile)).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {"DEV", "STAGING", "QA", "UAT", "PROD", "DR"})
        @DisplayName("Should handle case-insensitive profile lookup")
        void shouldHandleCaseInsensitiveLookup(String profile) {
            assertThat(ConfigEnvironment.fromProfile(profile)).isNotNull();
        }

        @ParameterizedTest
        @ValueSource(strings = {"unknown", "test", "", "production", "develop"})
        @DisplayName("Should throw IllegalArgumentException for unknown profiles")
        void shouldThrowForUnknownProfiles(String invalidProfile) {
            assertThatThrownBy(() -> ConfigEnvironment.fromProfile(invalidProfile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown profile: " + invalidProfile);
        }

        @Test
        @DisplayName("Should throw for null profile")
        void shouldThrowForNullProfile() {
            assertThatThrownBy(() -> ConfigEnvironment.fromProfile(null))
                .isInstanceOf(Exception.class);
        }
    }
}
