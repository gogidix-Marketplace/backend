package com.gogidix.centralconfiguration.environmentservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Environment Domain Entity Tests")
class EnvironmentTest {

    private static final Long ID = 1L;
    private static final String TENANT_ID = "tenant-001";
    private static final String ENVIRONMENT_NAME = "production";
    private static final String DISPLAY_NAME = "Production Environment";
    private static final String DESCRIPTION = "Production environment configuration";
    private static final String CONFIG_BASE_URI = "https://config.example.com";
    private static final Integer PRIORITY = 100;
    private static final String CREATED_BY = "admin";

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            LocalDateTime now = LocalDateTime.now();
            List<EnvironmentVariable> variables = List.of(EnvironmentVariable.builder().build());

            Environment environment = Environment.builder()
                    .id(ID)
                    .tenantId(TENANT_ID)
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .description(DESCRIPTION)
                    .environmentType(EnvironmentType.PRODUCTION)
                    .isActive(true)
                    .configBaseUri(CONFIG_BASE_URI)
                    .priority(PRIORITY)
                    .createdBy(CREATED_BY)
                    .createdAt(now)
                    .updatedAt(now)
                    .variables(variables)
                    .build();

            assertThat(environment.getId()).isEqualTo(ID);
            assertThat(environment.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(environment.getEnvironmentName()).isEqualTo(ENVIRONMENT_NAME);
            assertThat(environment.getDisplayName()).isEqualTo(DISPLAY_NAME);
            assertThat(environment.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.PRODUCTION);
            assertThat(environment.getIsActive()).isTrue();
            assertThat(environment.getConfigBaseUri()).isEqualTo(CONFIG_BASE_URI);
            assertThat(environment.getPriority()).isEqualTo(PRIORITY);
            assertThat(environment.getCreatedBy()).isEqualTo(CREATED_BY);
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            Environment environment = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .build();

            assertThat(environment.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(environment.getEnvironmentName()).isEqualTo(ENVIRONMENT_NAME);
            assertThat(environment.getDisplayName()).isEqualTo(DISPLAY_NAME);
        }

        @Test
        @DisplayName("Should set default values")
        void shouldSetDefaultValues() {
            Environment environment = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.DEVELOPMENT);
            assertThat(environment.getIsActive()).isTrue();
            assertThat(environment.getPriority()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should support method chaining")
        void shouldSupportMethodChaining() {
            Environment environment = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .environmentType(EnvironmentType.PRODUCTION)
                    .priority(50)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.PRODUCTION);
            assertThat(environment.getPriority()).isEqualTo(50);
        }
    }

    @Nested
    @DisplayName("TenantId Tests")
    class TenantIdTests {

        @Test
        @DisplayName("Should set and get tenant ID")
        void shouldSetAndGetTenantId() {
            Environment environment = Environment.builder()
                    .tenantId(TENANT_ID)
                    .build();

            assertThat(environment.getTenantId()).isEqualTo(TENANT_ID);
        }

        @ParameterizedTest
        @ValueSource(strings = {"tenant-001", "tenant-002", "org-123", "customer-456"})
        @DisplayName("Should accept various tenant IDs")
        void shouldAcceptVariousTenantIds(String tenantId) {
            Environment environment = Environment.builder()
                    .tenantId(tenantId)
                    .build();

            assertThat(environment.getTenantId()).isEqualTo(tenantId);
        }
    }

    @Nested
    @DisplayName("EnvironmentName Tests")
    class EnvironmentNameTests {

        @Test
        @DisplayName("Should set and get environment name")
        void shouldSetAndGetEnvironmentName() {
            Environment environment = Environment.builder()
                    .environmentName(ENVIRONMENT_NAME)
                    .build();

            assertThat(environment.getEnvironmentName()).isEqualTo(ENVIRONMENT_NAME);
        }

        @ParameterizedTest
        @ValueSource(strings = {"development", "staging", "qa", "uat", "production", "dr"})
        @DisplayName("Should accept various environment names")
        void shouldAcceptVariousEnvironmentNames(String name) {
            Environment environment = Environment.builder()
                    .environmentName(name)
                    .build();

            assertThat(environment.getEnvironmentName()).isEqualTo(name);
        }
    }

    @Nested
    @DisplayName("DisplayName Tests")
    class DisplayNameTests {

        @Test
        @DisplayName("Should set and get display name")
        void shouldSetAndGetDisplayName() {
            Environment environment = Environment.builder()
                    .displayName(DISPLAY_NAME)
                    .build();

            assertThat(environment.getDisplayName()).isEqualTo(DISPLAY_NAME);
        }

        @Test
        @DisplayName("Should accept null display name")
        void shouldAcceptNullDisplayName() {
            Environment environment = Environment.builder()
                    .displayName(null)
                    .build();

            assertThat(environment.getDisplayName()).isNull();
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should set and get description")
        void shouldSetAndGetDescription() {
            Environment environment = Environment.builder()
                    .description(DESCRIPTION)
                    .build();

            assertThat(environment.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        @DisplayName("Should accept null description")
        void shouldAcceptNullDescription() {
            Environment environment = Environment.builder()
                    .description(null)
                    .build();

            assertThat(environment.getDescription()).isNull();
        }

        @Test
        @DisplayName("Should accept long description")
        void shouldAcceptLongDescription() {
            String longDescription = "Detailed description ".repeat(20);
            Environment environment = Environment.builder()
                    .description(longDescription)
                    .build();

            assertThat(environment.getDescription()).isEqualTo(longDescription);
        }
    }

    @Nested
    @DisplayName("EnvironmentType Tests")
    class EnvironmentTypeTests {

        @ParameterizedTest
        @EnumSource(EnvironmentType.class)
        @DisplayName("Should accept all environment types")
        void shouldAcceptAllEnvironmentTypes(EnvironmentType type) {
            Environment environment = Environment.builder()
                    .environmentType(type)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(type);
        }

        @Test
        @DisplayName("Should default to DEVELOPMENT")
        void shouldDefaultToDEVELOPMENT() {
            Environment environment = Environment.builder()
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.DEVELOPMENT);
        }
    }

    @Nested
    @DisplayName("IsActive Tests")
    class IsActiveTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        @DisplayName("Should set active status")
        void shouldSetActiveStatus(boolean active) {
            Environment environment = Environment.builder()
                    .isActive(active)
                    .build();

            assertThat(environment.getIsActive()).isEqualTo(active);
        }

        @Test
        @DisplayName("Should default to active")
        void shouldDefaultToActive() {
            Environment environment = Environment.builder()
                    .build();

            assertThat(environment.getIsActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("Activation Tests")
    class ActivationTests {

        @Test
        @DisplayName("Should activate environment")
        void shouldActivateEnvironment() {
            Environment environment = Environment.builder()
                    .isActive(false)
                    .build();

            environment.activate();

            assertThat(environment.getIsActive()).isTrue();
        }

        @Test
        @DisplayName("Should deactivate environment")
        void shouldDeactivateEnvironment() {
            Environment environment = Environment.builder()
                    .isActive(true)
                    .build();

            environment.deactivate();

            assertThat(environment.getIsActive()).isFalse();
        }

        @Test
        @DisplayName("Should allow reactivation")
        void shouldAllowReactivation() {
            Environment environment = Environment.builder()
                    .isActive(true)
                    .build();

            environment.deactivate();
            assertThat(environment.getIsActive()).isFalse();

            environment.activate();
            assertThat(environment.getIsActive()).isTrue();
        }
    }

    @Nested
    @DisplayName("ConfigBaseUri Tests")
    class ConfigBaseUriTests {

        @Test
        @DisplayName("Should set and get config base URI")
        void shouldSetAndGetConfigBaseUri() {
            Environment environment = Environment.builder()
                    .configBaseUri(CONFIG_BASE_URI)
                    .build();

            assertThat(environment.getConfigBaseUri()).isEqualTo(CONFIG_BASE_URI);
        }

        @Test
        @DisplayName("Should accept null config base URI")
        void shouldAcceptNullConfigBaseUri() {
            Environment environment = Environment.builder()
                    .configBaseUri(null)
                    .build();

            assertThat(environment.getConfigBaseUri()).isNull();
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "https://config.example.com",
                "http://localhost:8080",
                "https://api.company.com/config"
        })
        @DisplayName("Should accept various URI formats")
        void shouldAcceptVariousUriFormats(String uri) {
            Environment environment = Environment.builder()
                    .configBaseUri(uri)
                    .build();

            assertThat(environment.getConfigBaseUri()).isEqualTo(uri);
        }
    }

    @Nested
    @DisplayName("Priority Tests")
    class PriorityTests {

        @Test
        @DisplayName("Should set and get priority")
        void shouldSetAndGetPriority() {
            Environment environment = Environment.builder()
                    .priority(PRIORITY)
                    .build();

            assertThat(environment.getPriority()).isEqualTo(PRIORITY);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 50, 100, 200, 1000})
        @DisplayName("Should accept various priority values")
        void shouldAcceptVariousPriorityValues(int priority) {
            Environment environment = Environment.builder()
                    .priority(priority)
                    .build();

            assertThat(environment.getPriority()).isEqualTo(priority);
        }

        @Test
        @DisplayName("Should default to priority 100")
        void shouldDefaultToPriority100() {
            Environment environment = Environment.builder()
                    .build();

            assertThat(environment.getPriority()).isEqualTo(100);
        }
    }

    @Nested
    @DisplayName("Variables Relationship Tests")
    class VariablesRelationshipTests {

        @Test
        @DisplayName("Should set variables list")
        void shouldSetVariablesList() {
            List<EnvironmentVariable> variables = List.of(
                    EnvironmentVariable.builder().variableKey("KEY1").build(),
                    EnvironmentVariable.builder().variableKey("KEY2").build()
            );

            Environment environment = Environment.builder()
                    .variables(variables)
                    .build();

            assertThat(environment.getVariables()).hasSize(2);
        }

        @Test
        @DisplayName("Should accept null variables")
        void shouldAcceptNullVariables() {
            Environment environment = Environment.builder()
                    .variables(null)
                    .build();

            assertThat(environment.getVariables()).isNull();
        }

        @Test
        @DisplayName("Should accept empty variables")
        void shouldAcceptEmptyVariables() {
            Environment environment = Environment.builder()
                    .variables(List.of())
                    .build();

            assertThat(environment.getVariables()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            Environment env1 = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .build();

            Environment env2 = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .build();

            assertThat(env1).isEqualTo(env2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            Environment env1 = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .build();

            Environment env2 = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .build();

            assertThat(env1.hashCode()).isEqualTo(env2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            Environment environment = Environment.builder()
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .build();

            String toString = environment.toString();

            assertThat(toString).contains(ENVIRONMENT_NAME);
        }

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            Environment environment = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName(ENVIRONMENT_NAME)
                    .displayName(DISPLAY_NAME)
                    .build();

            environment.setDisplayName("Updated Display Name");
            environment.setIsActive(false);

            assertThat(environment.getDisplayName()).isEqualTo("Updated Display Name");
            assertThat(environment.getIsActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should represent development environment")
        void shouldRepresentDevelopmentEnvironment() {
            Environment environment = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName("dev")
                    .displayName("Development")
                    .environmentType(EnvironmentType.DEVELOPMENT)
                    .isActive(true)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.DEVELOPMENT);
            assertThat(environment.getIsActive()).isTrue();
        }

        @Test
        @DisplayName("Should represent production environment")
        void shouldRepresentProductionEnvironment() {
            Environment environment = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName("prod")
                    .displayName("Production")
                    .environmentType(EnvironmentType.PRODUCTION)
                    .priority(10)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.PRODUCTION);
            assertThat(environment.getPriority()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should represent staging environment")
        void shouldRepresentStagingEnvironment() {
            Environment environment = Environment.builder()
                    .tenantId(TENANT_ID)
                    .environmentName("staging")
                    .displayName("Staging")
                    .environmentType(EnvironmentType.STAGING)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.STAGING);
        }
    }

    @Nested
    @DisplayName("Lifecycle Tests")
    class LifecycleTests {

        @Test
        @DisplayName("Should follow activation lifecycle")
        void shouldFollowActivationLifecycle() {
            Environment environment = Environment.builder()
                    .isActive(true)
                    .build();

            assertThat(environment.getIsActive()).isTrue();

            environment.deactivate();
            assertThat(environment.getIsActive()).isFalse();

            environment.activate();
            assertThat(environment.getIsActive()).isTrue();
        }

        @Test
        @DisplayName("Should support deactivation")
        void shouldSupportDeactivation() {
            Environment environment = Environment.builder()
                    .isActive(true)
                    .build();

            environment.deactivate();

            assertThat(environment.getIsActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Environment Type Mapping Tests")
    class EnvironmentTypeMappingTests {

        @Test
        @DisplayName("Development uses DEVELOPMENT type")
        void developmentUsesDEVELOPMENTType() {
            Environment environment = Environment.builder()
                    .environmentName("dev")
                    .environmentType(EnvironmentType.DEVELOPMENT)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.DEVELOPMENT);
        }

        @Test
        @DisplayName("Production uses PRODUCTION type")
        void productionUsesPRODUCTIONType() {
            Environment environment = Environment.builder()
                    .environmentName("prod")
                    .environmentType(EnvironmentType.PRODUCTION)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.PRODUCTION);
        }

        @Test
        @DisplayName("Staging uses STAGING type")
        void stagingUsesSTAGINGType() {
            Environment environment = Environment.builder()
                    .environmentName("staging")
                    .environmentType(EnvironmentType.STAGING)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.STAGING);
        }
    }

    @Nested
    @DisplayName("Priority Ordering Tests")
    class PriorityOrderingTests {

        @Test
        @DisplayName("Should support priority ordering")
        void shouldSupportPriorityOrdering() {
            Environment env1 = Environment.builder().priority(10).build();
            Environment env2 = Environment.builder().priority(50).build();
            Environment env3 = Environment.builder().priority(100).build();

            assertThat(env1.getPriority()).isLessThan(env2.getPriority());
            assertThat(env2.getPriority()).isLessThan(env3.getPriority());
        }

        @Test
        @DisplayName("Production should have higher priority")
        void productionShouldHaveHigherPriority() {
            Environment prod = Environment.builder()
                    .environmentType(EnvironmentType.PRODUCTION)
                    .priority(10)
                    .build();

            Environment dev = Environment.builder()
                    .environmentType(EnvironmentType.DEVELOPMENT)
                    .priority(100)
                    .build();

            assertThat(prod.getPriority()).isLessThan(dev.getPriority());
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get id")
        void shouldGetId() {
            Environment environment = Environment.builder()
                    .id(ID)
                    .build();

            assertThat(environment.getId()).isEqualTo(ID);
        }

        @Test
        @DisplayName("Should get tenant ID")
        void shouldGetTenantId() {
            Environment environment = Environment.builder()
                    .tenantId(TENANT_ID)
                    .build();

            assertThat(environment.getTenantId()).isEqualTo(TENANT_ID);
        }

        @Test
        @DisplayName("Should get environment name")
        void shouldGetEnvironmentName() {
            Environment environment = Environment.builder()
                    .environmentName(ENVIRONMENT_NAME)
                    .build();

            assertThat(environment.getEnvironmentName()).isEqualTo(ENVIRONMENT_NAME);
        }

        @Test
        @DisplayName("Should get display name")
        void shouldGetDisplayName() {
            Environment environment = Environment.builder()
                    .displayName(DISPLAY_NAME)
                    .build();

            assertThat(environment.getDisplayName()).isEqualTo(DISPLAY_NAME);
        }

        @Test
        @DisplayName("Should get description")
        void shouldGetDescription() {
            Environment environment = Environment.builder()
                    .description(DESCRIPTION)
                    .build();

            assertThat(environment.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        @DisplayName("Should get environment type")
        void shouldGetEnvironmentType() {
            Environment environment = Environment.builder()
                    .environmentType(EnvironmentType.PRODUCTION)
                    .build();

            assertThat(environment.getEnvironmentType()).isEqualTo(EnvironmentType.PRODUCTION);
        }

        @Test
        @DisplayName("Should get is active")
        void shouldGetIsActive() {
            Environment environment = Environment.builder()
                    .isActive(true)
                    .build();

            assertThat(environment.getIsActive()).isTrue();
        }

        @Test
        @DisplayName("Should get config base URI")
        void shouldGetConfigBaseUri() {
            Environment environment = Environment.builder()
                    .configBaseUri(CONFIG_BASE_URI)
                    .build();

            assertThat(environment.getConfigBaseUri()).isEqualTo(CONFIG_BASE_URI);
        }

        @Test
        @DisplayName("Should get priority")
        void shouldGetPriority() {
            Environment environment = Environment.builder()
                    .priority(PRIORITY)
                    .build();

            assertThat(environment.getPriority()).isEqualTo(PRIORITY);
        }

        @Test
        @DisplayName("Should get created by")
        void shouldGetCreatedBy() {
            Environment environment = Environment.builder()
                    .createdBy(CREATED_BY)
                    .build();

            assertThat(environment.getCreatedBy()).isEqualTo(CREATED_BY);
        }

        @Test
        @DisplayName("Should get created at")
        void shouldGetCreatedAt() {
            LocalDateTime now = LocalDateTime.now();
            Environment environment = Environment.builder()
                    .createdAt(now)
                    .build();

            assertThat(environment.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should get updated at")
        void shouldGetUpdatedAt() {
            LocalDateTime now = LocalDateTime.now();
            Environment environment = Environment.builder()
                    .updatedAt(now)
                    .build();

            assertThat(environment.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should get variables")
        void shouldGetVariables() {
            List<EnvironmentVariable> variables = List.of(EnvironmentVariable.builder().build());
            Environment environment = Environment.builder()
                    .variables(variables)
                    .build();

            assertThat(environment.getVariables()).isEqualTo(variables);
        }
    }
}
