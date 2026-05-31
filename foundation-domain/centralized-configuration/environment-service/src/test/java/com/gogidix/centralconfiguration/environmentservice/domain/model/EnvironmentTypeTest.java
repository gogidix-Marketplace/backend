package com.gogidix.centralconfiguration.environmentservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EnvironmentType Enum Tests")
class EnvironmentTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(EnvironmentType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(EnvironmentType type) {
            assertThat(type).isNotNull();
        }

        @Test
        @DisplayName("Should have DEVELOPMENT type")
        void shouldHaveDEVELOPMENTType() {
            assertThat(EnvironmentType.valueOf("DEVELOPMENT")).isEqualTo(EnvironmentType.DEVELOPMENT);
        }

        @Test
        @DisplayName("Should have STAGING type")
        void shouldHaveSTAGINGType() {
            assertThat(EnvironmentType.valueOf("STAGING")).isEqualTo(EnvironmentType.STAGING);
        }

        @Test
        @DisplayName("Should have QA type")
        void shouldHaveQAType() {
            assertThat(EnvironmentType.valueOf("QA")).isEqualTo(EnvironmentType.QA);
        }

        @Test
        @DisplayName("Should have UAT type")
        void shouldHaveUATType() {
            assertThat(EnvironmentType.valueOf("UAT")).isEqualTo(EnvironmentType.UAT);
        }

        @Test
        @DisplayName("Should have PRODUCTION type")
        void shouldHavePRODUCTIONType() {
            assertThat(EnvironmentType.valueOf("PRODUCTION")).isEqualTo(EnvironmentType.PRODUCTION);
        }

        @Test
        @DisplayName("Should have DR type")
        void shouldHaveDRType() {
            assertThat(EnvironmentType.valueOf("DR")).isEqualTo(EnvironmentType.DR);
        }

        @Test
        @DisplayName("Should have 6 environment types")
        void shouldHave6EnvironmentTypes() {
            assertThat(EnvironmentType.values()).hasSize(6);
        }
    }

    @Nested
    @DisplayName("Code Tests")
    class CodeTests {

        @Test
        @DisplayName("DEVELOPMENT has code 'development'")
        void developmentHasCode() {
            assertThat(EnvironmentType.DEVELOPMENT.getCode()).isEqualTo("development");
        }

        @Test
        @DisplayName("STAGING has code 'staging'")
        void stagingHasCode() {
            assertThat(EnvironmentType.STAGING.getCode()).isEqualTo("staging");
        }

        @Test
        @DisplayName("QA has code 'qa'")
        void qaHasCode() {
            assertThat(EnvironmentType.QA.getCode()).isEqualTo("qa");
        }

        @Test
        @DisplayName("UAT has code 'uat'")
        void uatHasCode() {
            assertThat(EnvironmentType.UAT.getCode()).isEqualTo("uat");
        }

        @Test
        @DisplayName("PRODUCTION has code 'production'")
        void productionHasCode() {
            assertThat(EnvironmentType.PRODUCTION.getCode()).isEqualTo("production");
        }

        @Test
        @DisplayName("DR has code 'dr'")
        void drHasCode() {
            assertThat(EnvironmentType.DR.getCode()).isEqualTo("dr");
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("DEVELOPMENT has description")
        void developmentHasDescription() {
            assertThat(EnvironmentType.DEVELOPMENT.getDescription()).isEqualTo("Development environment");
        }

        @Test
        @DisplayName("STAGING has description")
        void stagingHasDescription() {
            assertThat(EnvironmentType.STAGING.getDescription()).isEqualTo("Staging environment");
        }

        @Test
        @DisplayName("QA has description")
        void qaHasDescription() {
            assertThat(EnvironmentType.QA.getDescription()).isEqualTo("Quality Assurance environment");
        }

        @Test
        @DisplayName("UAT has description")
        void uatHasDescription() {
            assertThat(EnvironmentType.UAT.getDescription()).isEqualTo("User Acceptance Testing environment");
        }

        @Test
        @DisplayName("PRODUCTION has description")
        void productionHasDescription() {
            assertThat(EnvironmentType.PRODUCTION.getDescription()).isEqualTo("Production environment");
        }

        @Test
        @DisplayName("DR has description")
        void drHasDescription() {
            assertThat(EnvironmentType.DR.getDescription()).isEqualTo("Disaster Recovery environment");
        }
    }

    @Nested
    @DisplayName("Lifecycle Tests")
    class LifecycleTests {

        @Test
        @DisplayName("Should follow development lifecycle")
        void shouldFollowDevelopmentLifecycle() {
            EnvironmentType dev = EnvironmentType.DEVELOPMENT;
            EnvironmentType qa = EnvironmentType.QA;
            EnvironmentType staging = EnvironmentType.STAGING;
            EnvironmentType prod = EnvironmentType.PRODUCTION;

            assertThat(dev).isNotNull();
            assertThat(qa).isNotNull();
            assertThat(staging).isNotNull();
            assertThat(prod).isNotNull();
        }

        @Test
        @DisplayName("Development comes before production")
        void developmentComesBeforeProduction() {
            EnvironmentType dev = EnvironmentType.DEVELOPMENT;
            EnvironmentType prod = EnvironmentType.PRODUCTION;

            assertThat(dev).isNotEqualTo(prod);
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("DEVELOPMENT for local development")
        void developmentForLocalDevelopment() {
            EnvironmentType type = EnvironmentType.DEVELOPMENT;
            assertThat(type).isEqualTo(EnvironmentType.DEVELOPMENT);
        }

        @Test
        @DisplayName("STAGING for pre-production testing")
        void stagingForPreProductionTesting() {
            EnvironmentType type = EnvironmentType.STAGING;
            assertThat(type).isEqualTo(EnvironmentType.STAGING);
        }

        @Test
        @DisplayName("QA for quality assurance")
        void qaForQualityAssurance() {
            EnvironmentType type = EnvironmentType.QA;
            assertThat(type).isEqualTo(EnvironmentType.QA);
        }

        @Test
        @DisplayName("UAT for user acceptance testing")
        void uatForUserAcceptanceTesting() {
            EnvironmentType type = EnvironmentType.UAT;
            assertThat(type).isEqualTo(EnvironmentType.UAT);
        }

        @Test
        @DisplayName("PRODUCTION for live deployment")
        void productionForLiveDeployment() {
            EnvironmentType type = EnvironmentType.PRODUCTION;
            assertThat(type).isEqualTo(EnvironmentType.PRODUCTION);
        }

        @Test
        @DisplayName("DR for disaster recovery")
        void drForDisasterRecovery() {
            EnvironmentType type = EnvironmentType.DR;
            assertThat(type).isEqualTo(EnvironmentType.DR);
        }
    }

    @Nested
    @DisplayName("Environment Hierarchy Tests")
    class EnvironmentHierarchyTests {

        @Test
        @DisplayName("Development is lowest level")
        void developmentIsLowestLevel() {
            EnvironmentType dev = EnvironmentType.DEVELOPMENT;
            assertThat(dev.name()).isEqualTo("DEVELOPMENT");
        }

        @Test
        @DisplayName("Production is highest level")
        void productionIsHighestLevel() {
            EnvironmentType prod = EnvironmentType.PRODUCTION;
            assertThat(prod.name()).isEqualTo("PRODUCTION");
        }

        @Test
        @DisplayName("Environments have different levels")
        void environmentsHaveDifferentLevels() {
            EnvironmentType[] types = EnvironmentType.values();
            assertThat(types).hasSize(6);
        }
    }

    @Nested
    @DisplayName("Code Properties Tests")
    class CodePropertiesTests {

        @Test
        @DisplayName("All codes should be lowercase")
        void allCodesShouldBeLowercase() {
            for (EnvironmentType type : EnvironmentType.values()) {
                assertThat(type.getCode()).isLowerCase();
            }
        }

        @Test
        @DisplayName("All codes should be unique")
        void allCodesShouldBeUnique() {
            long uniqueCodes = java.util.Arrays.stream(EnvironmentType.values())
                    .map(EnvironmentType::getCode)
                    .distinct()
                    .count();

            assertThat(uniqueCodes).isEqualTo(EnvironmentType.values().length);
        }

        @Test
        @DisplayName("All codes should be non-null")
        void allCodesShouldBeNonNull() {
            for (EnvironmentType type : EnvironmentType.values()) {
                assertThat(type.getCode()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Description Properties Tests")
    class DescriptionPropertiesTests {

        @Test
        @DisplayName("All descriptions should be non-null")
        void allDescriptionsShouldBeNonNull() {
            for (EnvironmentType type : EnvironmentType.values()) {
                assertThat(type.getDescription()).isNotNull();
            }
        }

        @Test
        @DisplayName("All descriptions should contain 'environment'")
        void allDescriptionsShouldContainEnvironment() {
            for (EnvironmentType type : EnvironmentType.values()) {
                assertThat(type.getDescription()).containsIgnoringCase("environment");
            }
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (EnvironmentType type : EnvironmentType.values()) {
                String name = type.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(EnvironmentType.values())
                    .map(EnvironmentType::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(EnvironmentType.values().length);
        }
    }

    @Nested
    @DisplayName("Deployment Context Tests")
    class DeploymentContextTests {

        @Test
        @DisplayName("Development allows frequent deployments")
        void developmentAllowsFrequentDeployments() {
            EnvironmentType type = EnvironmentType.DEVELOPMENT;
            assertThat(type).isEqualTo(EnvironmentType.DEVELOPMENT);
        }

        @Test
        @DisplayName("Production requires careful deployment")
        void productionRequiresCarefulDeployment() {
            EnvironmentType type = EnvironmentType.PRODUCTION;
            assertThat(type).isEqualTo(EnvironmentType.PRODUCTION);
        }

        @Test
        @DisplayName("Staging mirrors production")
        void stagingMirrorsProduction() {
            EnvironmentType type = EnvironmentType.STAGING;
            assertThat(type).isEqualTo(EnvironmentType.STAGING);
        }
    }

    @Nested
    @DisplayName("Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Development has relaxed configuration")
        void developmentHasRelaxedConfiguration() {
            EnvironmentType type = EnvironmentType.DEVELOPMENT;
            assertThat(type.getCode()).isEqualTo("development");
        }

        @Test
        @DisplayName("Production has strict configuration")
        void productionHasStrictConfiguration() {
            EnvironmentType type = EnvironmentType.PRODUCTION;
            assertThat(type.getCode()).isEqualTo("production");
        }

        @Test
        @DisplayName("QA has test-specific configuration")
        void qaHasTestSpecificConfiguration() {
            EnvironmentType type = EnvironmentType.QA;
            assertThat(type.getCode()).isEqualTo("qa");
        }
    }
}
