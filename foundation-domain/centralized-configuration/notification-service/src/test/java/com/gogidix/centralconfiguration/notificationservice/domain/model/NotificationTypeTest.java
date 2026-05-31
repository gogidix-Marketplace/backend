package com.gogidix.centralconfiguration.notificationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationType Enum Tests")
class NotificationTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(NotificationType.class)
        @DisplayName("Should have all enum values")
        void shouldHaveAllEnumValues(NotificationType type) {
            assertThat(type).isNotNull();
        }

        @Test
        @DisplayName("Should have CONFIG_CREATED type")
        void shouldHaveCONFIG_CREATEDType() {
            assertThat(NotificationType.valueOf("CONFIG_CREATED")).isEqualTo(NotificationType.CONFIG_CREATED);
        }

        @Test
        @DisplayName("Should have CONFIG_UPDATED type")
        void shouldHaveCONFIG_UPDATEDType() {
            assertThat(NotificationType.valueOf("CONFIG_UPDATED")).isEqualTo(NotificationType.CONFIG_UPDATED);
        }

        @Test
        @DisplayName("Should have CONFIG_DELETED type")
        void shouldHaveCONFIG_DELETEDType() {
            assertThat(NotificationType.valueOf("CONFIG_DELETED")).isEqualTo(NotificationType.CONFIG_DELETED);
        }

        @Test
        @DisplayName("Should have FEATURE_FLAG_TOGGLED type")
        void shouldHaveFEATURE_FLAG_TOGGLEDType() {
            assertThat(NotificationType.valueOf("FEATURE_FLAG_TOGGLED")).isEqualTo(NotificationType.FEATURE_FLAG_TOGGLED);
        }

        @Test
        @DisplayName("Should have ENVIRONMENT_CHANGED type")
        void shouldHaveENVIRONMENT_CHANGEDType() {
            assertThat(NotificationType.valueOf("ENVIRONMENT_CHANGED")).isEqualTo(NotificationType.ENVIRONMENT_CHANGED);
        }

        @Test
        @DisplayName("Should have SECURITY_ALERT type")
        void shouldHaveSECURITY_ALERTType() {
            assertThat(NotificationType.valueOf("SECURITY_ALERT")).isEqualTo(NotificationType.SECURITY_ALERT);
        }

        @Test
        @DisplayName("Should have SYSTEM_ALERT type")
        void shouldHaveSYSTEM_ALERTType() {
            assertThat(NotificationType.valueOf("SYSTEM_ALERT")).isEqualTo(NotificationType.SYSTEM_ALERT);
        }

        @Test
        @DisplayName("Should have 7 notification types")
        void shouldHave7NotificationTypes() {
            assertThat(NotificationType.values()).hasSize(7);
        }
    }

    @Nested
    @DisplayName("Code Tests")
    class CodeTests {

        @Test
        @DisplayName("CONFIG_CREATED has code 'CONFIG_CREATED'")
        void configCreatedHasCode() {
            assertThat(NotificationType.CONFIG_CREATED.getCode()).isEqualTo("CONFIG_CREATED");
        }

        @Test
        @DisplayName("CONFIG_UPDATED has code 'CONFIG_UPDATED'")
        void configUpdatedHasCode() {
            assertThat(NotificationType.CONFIG_UPDATED.getCode()).isEqualTo("CONFIG_UPDATED");
        }

        @Test
        @DisplayName("CONFIG_DELETED has code 'CONFIG_DELETED'")
        void configDeletedHasCode() {
            assertThat(NotificationType.CONFIG_DELETED.getCode()).isEqualTo("CONFIG_DELETED");
        }

        @Test
        @DisplayName("FEATURE_FLAG_TOGGLED has code 'FEATURE_FLAG_TOGGLED'")
        void featureFlagToggledHasCode() {
            assertThat(NotificationType.FEATURE_FLAG_TOGGLED.getCode()).isEqualTo("FEATURE_FLAG_TOGGLED");
        }

        @Test
        @DisplayName("ENVIRONMENT_CHANGED has code 'ENVIRONMENT_CHANGED'")
        void environmentChangedHasCode() {
            assertThat(NotificationType.ENVIRONMENT_CHANGED.getCode()).isEqualTo("ENVIRONMENT_CHANGED");
        }

        @Test
        @DisplayName("SECURITY_ALERT has code 'SECURITY_ALERT'")
        void securityAlertHasCode() {
            assertThat(NotificationType.SECURITY_ALERT.getCode()).isEqualTo("SECURITY_ALERT");
        }

        @Test
        @DisplayName("SYSTEM_ALERT has code 'SYSTEM_ALERT'")
        void systemAlertHasCode() {
            assertThat(NotificationType.SYSTEM_ALERT.getCode()).isEqualTo("SYSTEM_ALERT");
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("All types have descriptions")
        void allTypesHaveDescriptions() {
            for (NotificationType type : NotificationType.values()) {
                assertThat(type.getDescription()).isNotNull();
                assertThat(type.getDescription()).isNotEmpty();
            }
        }

        @Test
        @DisplayName("CONFIG_CREATED description mentions created")
        void configCreatedDescriptionMentionsCreated() {
            assertThat(NotificationType.CONFIG_CREATED.getDescription()).containsIgnoringCase("created");
        }

        @Test
        @DisplayName("CONFIG_UPDATED description mentions updated")
        void configUpdatedDescriptionMentionsUpdated() {
            assertThat(NotificationType.CONFIG_UPDATED.getDescription()).containsIgnoringCase("updated");
        }

        @Test
        @DisplayName("CONFIG_DELETED description mentions deleted")
        void configDeletedDescriptionMentionsDeleted() {
            assertThat(NotificationType.CONFIG_DELETED.getDescription()).containsIgnoringCase("deleted");
        }

        @Test
        @DisplayName("SECURITY_ALERT description mentions security")
        void securityAlertDescriptionMentionsSecurity() {
            assertThat(NotificationType.SECURITY_ALERT.getDescription()).containsIgnoringCase("security");
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("CONFIG_CREATED for new configuration")
        void configCreatedForNewConfiguration() {
            NotificationType type = NotificationType.CONFIG_CREATED;
            assertThat(type).isEqualTo(NotificationType.CONFIG_CREATED);
        }

        @Test
        @DisplayName("CONFIG_UPDATED for configuration changes")
        void configUpdatedForConfigurationChanges() {
            NotificationType type = NotificationType.CONFIG_UPDATED;
            assertThat(type).isEqualTo(NotificationType.CONFIG_UPDATED);
        }

        @Test
        @DisplayName("CONFIG_DELETED for configuration removal")
        void configDeletedForConfigurationRemoval() {
            NotificationType type = NotificationType.CONFIG_DELETED;
            assertThat(type).isEqualTo(NotificationType.CONFIG_DELETED);
        }

        @Test
        @DisplayName("FEATURE_FLAG_TOGGLED for flag changes")
        void featureFlagToggledForFlagChanges() {
            NotificationType type = NotificationType.FEATURE_FLAG_TOGGLED;
            assertThat(type).isEqualTo(NotificationType.FEATURE_FLAG_TOGGLED);
        }

        @Test
        @DisplayName("ENVIRONMENT_CHANGED for environment updates")
        void environmentChangedForEnvironmentUpdates() {
            NotificationType type = NotificationType.ENVIRONMENT_CHANGED;
            assertThat(type).isEqualTo(NotificationType.ENVIRONMENT_CHANGED);
        }

        @Test
        @DisplayName("SECURITY_ALERT for security events")
        void securityAlertForSecurityEvents() {
            NotificationType type = NotificationType.SECURITY_ALERT;
            assertThat(type).isEqualTo(NotificationType.SECURITY_ALERT);
        }

        @Test
        @DisplayName("SYSTEM_ALERT for system events")
        void systemAlertForSystemEvents() {
            NotificationType type = NotificationType.SYSTEM_ALERT;
            assertThat(type).isEqualTo(NotificationType.SYSTEM_ALERT);
        }
    }

    @Nested
    @DisplayName("Enum Properties Tests")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have consistent enum names")
        void shouldHaveConsistentEnumNames() {
            for (NotificationType type : NotificationType.values()) {
                String name = type.name();
                assertThat(name).isNotNull();
                assertThat(name).isUpperCase();
                assertThat(name).doesNotContain(" ");
            }
        }

        @Test
        @DisplayName("Should have unique enum values")
        void shouldHaveUniqueEnumValues() {
            long uniqueCount = java.util.Arrays.stream(NotificationType.values())
                    .map(NotificationType::name)
                    .distinct()
                    .count();

            assertThat(uniqueCount).isEqualTo(NotificationType.values().length);
        }

        @Test
        @DisplayName("Should have unique codes")
        void shouldHaveUniqueCodes() {
            long uniqueCodes = java.util.Arrays.stream(NotificationType.values())
                    .map(NotificationType::getCode)
                    .distinct()
                    .count();

            assertThat(uniqueCodes).isEqualTo(NotificationType.values().length);
        }
    }

    @Nested
    @DisplayName("Notification Category Tests")
    class NotificationCategoryTests {

        @Test
        @DisplayName("Configuration related notifications exist")
        void configurationRelatedNotificationsExist() {
            assertThat(NotificationType.CONFIG_CREATED).isNotNull();
            assertThat(NotificationType.CONFIG_UPDATED).isNotNull();
            assertThat(NotificationType.CONFIG_DELETED).isNotNull();
        }

        @Test
        @DisplayName("Feature flag notifications exist")
        void featureFlagNotificationsExist() {
            assertThat(NotificationType.FEATURE_FLAG_TOGGLED).isNotNull();
        }

        @Test
        @DisplayName("Alert type notifications exist")
        void alertTypeNotificationsExist() {
            assertThat(NotificationType.SECURITY_ALERT).isNotNull();
            assertThat(NotificationType.SYSTEM_ALERT).isNotNull();
        }

        @Test
        @DisplayName("Environment notifications exist")
        void environmentNotificationsExist() {
            assertThat(NotificationType.ENVIRONMENT_CHANGED).isNotNull();
        }
    }
}
