package com.gogidix.centralconfiguration.environmentservice.domain.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EnvironmentVariable Domain Entity Tests")
class EnvironmentVariableTest {

    private static final Long ID = 1L;
    private static final Long ENVIRONMENT_ID = 100L;
    private static final String VARIABLE_KEY = "DATABASE_URL";
    private static final String VARIABLE_VALUE = "jdbc:postgresql://localhost:5432/mydb";
    private static final String DESCRIPTION = "Database connection string";

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            LocalDateTime now = LocalDateTime.now();
            Environment environment = Environment.builder().build();

            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .id(ID)
                    .environmentId(ENVIRONMENT_ID)
                    .environment(environment)
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .isEncrypted(true)
                    .isSensitive(true)
                    .description(DESCRIPTION)
                    .createdAt(now)
                    .build();

            assertThat(variable.getId()).isEqualTo(ID);
            assertThat(variable.getEnvironmentId()).isEqualTo(ENVIRONMENT_ID);
            assertThat(variable.getEnvironment()).isEqualTo(environment);
            assertThat(variable.getVariableKey()).isEqualTo(VARIABLE_KEY);
            assertThat(variable.getVariableValue()).isEqualTo(VARIABLE_VALUE);
            assertThat(variable.getIsEncrypted()).isTrue();
            assertThat(variable.getIsSensitive()).isTrue();
            assertThat(variable.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(variable.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .environmentId(ENVIRONMENT_ID)
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .build();

            assertThat(variable.getEnvironmentId()).isEqualTo(ENVIRONMENT_ID);
            assertThat(variable.getVariableKey()).isEqualTo(VARIABLE_KEY);
            assertThat(variable.getVariableValue()).isEqualTo(VARIABLE_VALUE);
        }

        @Test
        @DisplayName("Should set default values")
        void shouldSetDefaultValues() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .environmentId(ENVIRONMENT_ID)
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .build();

            assertThat(variable.getIsEncrypted()).isFalse();
            assertThat(variable.getIsSensitive()).isFalse();
        }

        @Test
        @DisplayName("Should support method chaining")
        void shouldSupportMethodChaining() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .isEncrypted(true)
                    .build();

            assertThat(variable.getIsEncrypted()).isTrue();
        }
    }

    @Nested
    @DisplayName("VariableKey Tests")
    class VariableKeyTests {

        @Test
        @DisplayName("Should set and get variable key")
        void shouldSetAndGetVariableKey() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey(VARIABLE_KEY)
                    .build();

            assertThat(variable.getVariableKey()).isEqualTo(VARIABLE_KEY);
        }

        @ParameterizedTest
        @ValueSource(strings = {"DATABASE_URL", "API_KEY", "USERNAME", "PASSWORD", "TIMEOUT"})
        @DisplayName("Should accept various variable keys")
        void shouldAcceptVariousVariableKeys(String key) {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey(key)
                    .build();

            assertThat(variable.getVariableKey()).isEqualTo(key);
        }

        @Test
        @DisplayName("Should accept key with special characters")
        void shouldAcceptKeyWithSpecialCharacters() {
            String key = "DB.CONNECTION.STRING";
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey(key)
                    .build();

            assertThat(variable.getVariableKey()).isEqualTo(key);
        }
    }

    @Nested
    @DisplayName("VariableValue Tests")
    class VariableValueTests {

        @Test
        @DisplayName("Should set and get variable value")
        void shouldSetAndGetVariableValue() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableValue(VARIABLE_VALUE)
                    .build();

            assertThat(variable.getVariableValue()).isEqualTo(VARIABLE_VALUE);
        }

        @Test
        @DisplayName("Should accept empty value")
        void shouldAcceptEmptyValue() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableValue("")
                    .build();

            assertThat(variable.getVariableValue()).isEmpty();
        }

        @Test
        @DisplayName("Should accept null value")
        void shouldAcceptNullValue() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableValue(null)
                    .build();

            assertThat(variable.getVariableValue()).isNull();
        }

        @Test
        @DisplayName("Should accept long value")
        void shouldAcceptLongValue() {
            String longValue = "x".repeat(1000);
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableValue(longValue)
                    .build();

            assertThat(variable.getVariableValue()).hasSize(1000);
        }

        @Test
        @DisplayName("Should accept URL value")
        void shouldAcceptUrlValue() {
            String urlValue = "jdbc:postgresql://localhost:5432/mydb";
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableValue(urlValue)
                    .build();

            assertThat(variable.getVariableValue()).isEqualTo(urlValue);
        }

        @Test
        @DisplayName("Should accept JSON value")
        void shouldAcceptJsonValue() {
            String jsonValue = "{\"host\":\"localhost\",\"port\":5432}";
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableValue(jsonValue)
                    .build();

            assertThat(variable.getVariableValue()).isEqualTo(jsonValue);
        }
    }

    @Nested
    @DisplayName("Encrypted Tests")
    class EncryptedTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        @DisplayName("Should set encrypted flag")
        void shouldSetEncryptedFlag(boolean encrypted) {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .isEncrypted(encrypted)
                    .build();

            assertThat(variable.getIsEncrypted()).isEqualTo(encrypted);
        }

        @Test
        @DisplayName("Should default to not encrypted")
        void shouldDefaultToNotEncrypted() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .build();

            assertThat(variable.getIsEncrypted()).isFalse();
        }

        @Test
        @DisplayName("Should mark sensitive data as encrypted")
        void shouldMarkSensitiveDataAsEncrypted() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("API_KEY")
                    .isEncrypted(true)
                    .build();

            assertThat(variable.getIsEncrypted()).isTrue();
        }
    }

    @Nested
    @DisplayName("Sensitive Tests")
    class SensitiveTests {

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        @DisplayName("Should set sensitive flag")
        void shouldSetSensitiveFlag(boolean sensitive) {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .isSensitive(sensitive)
                    .build();

            assertThat(variable.getIsSensitive()).isEqualTo(sensitive);
        }

        @Test
        @DisplayName("Should default to not sensitive")
        void shouldDefaultToNotSensitive() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .build();

            assertThat(variable.getIsSensitive()).isFalse();
        }

        @Test
        @DisplayName("Should mark password as sensitive")
        void shouldMarkPasswordAsSensitive() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("PASSWORD")
                    .isSensitive(true)
                    .build();

            assertThat(variable.getIsSensitive()).isTrue();
        }

        @Test
        @DisplayName("Should mark API key as sensitive")
        void shouldMarkApiKeyAsSensitive() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("API_KEY")
                    .isSensitive(true)
                    .build();

            assertThat(variable.getIsSensitive()).isTrue();
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should set and get description")
        void shouldSetAndGetDescription() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .description(DESCRIPTION)
                    .build();

            assertThat(variable.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        @DisplayName("Should accept null description")
        void shouldAcceptNullDescription() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .description(null)
                    .build();

            assertThat(variable.getDescription()).isNull();
        }

        @Test
        @DisplayName("Should accept empty description")
        void shouldAcceptEmptyDescription() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .description("")
                    .build();

            assertThat(variable.getDescription()).isEmpty();
        }

        @Test
        @DisplayName("Should accept long description")
        void shouldAcceptLongDescription() {
            String longDescription = "Detailed description ".repeat(20);
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .description(longDescription)
                    .build();

            assertThat(variable.getDescription()).isEqualTo(longDescription);
        }
    }

    @Nested
    @DisplayName("Environment Relationship Tests")
    class EnvironmentRelationshipTests {

        @Test
        @DisplayName("Should set environment")
        void shouldSetEnvironment() {
            Environment environment = Environment.builder()
                    .id(1L)
                    .environmentName("production")
                    .build();

            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .environment(environment)
                    .build();

            assertThat(variable.getEnvironment()).isEqualTo(environment);
        }

        @Test
        @DisplayName("Should accept null environment")
        void shouldAcceptNullEnvironment() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .environment(null)
                    .build();

            assertThat(variable.getEnvironment()).isNull();
        }

        @Test
        @DisplayName("Should set environment ID")
        void shouldSetEnvironmentId() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .environmentId(ENVIRONMENT_ID)
                    .build();

            assertThat(variable.getEnvironmentId()).isEqualTo(ENVIRONMENT_ID);
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            EnvironmentVariable variable1 = EnvironmentVariable.builder()
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .build();

            EnvironmentVariable variable2 = EnvironmentVariable.builder()
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .build();

            assertThat(variable1).isEqualTo(variable2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            EnvironmentVariable variable1 = EnvironmentVariable.builder()
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .build();

            EnvironmentVariable variable2 = EnvironmentVariable.builder()
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .build();

            assertThat(variable1.hashCode()).isEqualTo(variable2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .build();

            String toString = variable.toString();

            assertThat(toString).contains(VARIABLE_KEY);
        }

        @Test
        @DisplayName("Should generate getters and setters")
        void shouldGenerateGettersAndSetters() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey(VARIABLE_KEY)
                    .variableValue(VARIABLE_VALUE)
                    .build();

            assertThat(variable.getVariableKey()).isEqualTo(VARIABLE_KEY);
            assertThat(variable.getVariableValue()).isEqualTo(VARIABLE_VALUE);

            variable.setVariableKey("NEW_KEY");
            variable.setVariableValue("NEW_VALUE");

            assertThat(variable.getVariableKey()).isEqualTo("NEW_KEY");
            assertThat(variable.getVariableValue()).isEqualTo("NEW_VALUE");
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with no-args constructor")
        void shouldCreateWithNoArgsConstructor() {
            EnvironmentVariable variable = new EnvironmentVariable();

            assertThat(variable).isNotNull();
            assertThat(variable.getIsEncrypted()).isFalse();
            assertThat(variable.getIsSensitive()).isFalse();
        }

        @Test
        @DisplayName("Should create with all-args constructor")
        void shouldCreateWithAllArgsConstructor() {
            Environment environment = Environment.builder().build();
            LocalDateTime now = LocalDateTime.now();

            EnvironmentVariable variable = new EnvironmentVariable();
            variable.setId(ID);
            variable.setEnvironmentId(ENVIRONMENT_ID);
            variable.setEnvironment(environment);
            variable.setVariableKey(VARIABLE_KEY);
            variable.setVariableValue(VARIABLE_VALUE);
            variable.setIsEncrypted(true);
            variable.setIsSensitive(true);
            variable.setDescription(DESCRIPTION);
            variable.setCreatedAt(now);

            assertThat(variable.getId()).isEqualTo(ID);
            assertThat(variable.getVariableKey()).isEqualTo(VARIABLE_KEY);
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should represent database configuration")
        void shouldRepresentDatabaseConfiguration() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("DATABASE_URL")
                    .variableValue("jdbc:postgresql://localhost:5432/mydb")
                    .isSensitive(true)
                    .description("Database connection string")
                    .build();

            assertThat(variable.getVariableKey()).isEqualTo("DATABASE_URL");
            assertThat(variable.getIsSensitive()).isTrue();
        }

        @Test
        @DisplayName("Should represent API configuration")
        void shouldRepresentApiConfiguration() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("API_TIMEOUT")
                    .variableValue("30000")
                    .isSensitive(false)
                    .build();

            assertThat(variable.getVariableKey()).isEqualTo("API_TIMEOUT");
            assertThat(variable.getIsSensitive()).isFalse();
        }

        @Test
        @DisplayName("Should represent encrypted secret")
        void shouldRepresentEncryptedSecret() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("ENCRYPTION_KEY")
                    .variableValue("secret-key-value")
                    .isEncrypted(true)
                    .isSensitive(true)
                    .build();

            assertThat(variable.getIsEncrypted()).isTrue();
            assertThat(variable.getIsSensitive()).isTrue();
        }

        @Test
        @DisplayName("Should represent feature flag")
        void shouldRepresentFeatureFlag() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("FEATURE_ENABLED")
                    .variableValue("true")
                    .isEncrypted(false)
                    .isSensitive(false)
                    .build();

            assertThat(variable.getVariableKey()).isEqualTo("FEATURE_ENABLED");
            assertThat(variable.getVariableValue()).isEqualTo("true");
        }
    }

    @Nested
    @DisplayName("Security Tests")
    class SecurityTests {

        @Test
        @DisplayName("Should mark password as both encrypted and sensitive")
        void shouldMarkPasswordAsBothEncryptedAndSensitive() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("DB_PASSWORD")
                    .variableValue("secret123")
                    .isEncrypted(true)
                    .isSensitive(true)
                    .build();

            assertThat(variable.getIsEncrypted()).isTrue();
            assertThat(variable.getIsSensitive()).isTrue();
        }

        @Test
        @DisplayName("Should mark API key as sensitive")
        void shouldMarkApiKeyAsSensitive() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("STRIPE_API_KEY")
                    .variableValue("sk_test_12345")
                    .isSensitive(true)
                    .build();

            assertThat(variable.getIsSensitive()).isTrue();
        }

        @Test
        @DisplayName("Public configuration is not sensitive")
        void publicConfigurationIsNotSensitive() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("LOG_LEVEL")
                    .variableValue("INFO")
                    .isSensitive(false)
                    .build();

            assertThat(variable.getIsSensitive()).isFalse();
            assertThat(variable.getIsEncrypted()).isFalse();
        }
    }

    @Nested
    @DisplayName("Variable Types Tests")
    class VariableTypesTests {

        @Test
        @DisplayName("Should handle string value")
        void shouldHandleStringValue() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("STRING_VAR")
                    .variableValue("text value")
                    .build();

            assertThat(variable.getVariableValue()).isEqualTo("text value");
        }

        @Test
        @DisplayName("Should handle numeric value")
        void shouldHandleNumericValue() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("PORT")
                    .variableValue("5432")
                    .build();

            assertThat(variable.getVariableValue()).isEqualTo("5432");
        }

        @Test
        @DisplayName("Should handle boolean value")
        void shouldHandleBooleanValue() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("DEBUG")
                    .variableValue("true")
                    .build();

            assertThat(variable.getVariableValue()).isEqualTo("true");
        }

        @Test
        @DisplayName("Should handle JSON configuration")
        void shouldHandleJsonConfiguration() {
            String json = "{\"timeout\":30,\"retries\":3}";
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey("CONFIG")
                    .variableValue(json)
                    .build();

            assertThat(variable.getVariableValue()).isEqualTo(json);
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should set created timestamp")
        void shouldSetCreatedTimestamp() {
            LocalDateTime now = LocalDateTime.now();
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .createdAt(now)
                    .build();

            assertThat(variable.getCreatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should accept null created timestamp")
        void shouldAcceptNullCreatedTimestamp() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .createdAt(null)
                    .build();

            assertThat(variable.getCreatedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get id")
        void shouldGetId() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .id(ID)
                    .build();

            assertThat(variable.getId()).isEqualTo(ID);
        }

        @Test
        @DisplayName("Should get environment id")
        void shouldGetEnvironmentId() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .environmentId(ENVIRONMENT_ID)
                    .build();

            assertThat(variable.getEnvironmentId()).isEqualTo(ENVIRONMENT_ID);
        }

        @Test
        @DisplayName("Should get variable key")
        void shouldGetVariableKey() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableKey(VARIABLE_KEY)
                    .build();

            assertThat(variable.getVariableKey()).isEqualTo(VARIABLE_KEY);
        }

        @Test
        @DisplayName("Should get variable value")
        void shouldGetVariableValue() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .variableValue(VARIABLE_VALUE)
                    .build();

            assertThat(variable.getVariableValue()).isEqualTo(VARIABLE_VALUE);
        }

        @Test
        @DisplayName("Should get is encrypted")
        void shouldGetIsEncrypted() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .isEncrypted(true)
                    .build();

            assertThat(variable.getIsEncrypted()).isTrue();
        }

        @Test
        @DisplayName("Should get is sensitive")
        void shouldGetIsSensitive() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .isSensitive(true)
                    .build();

            assertThat(variable.getIsSensitive()).isTrue();
        }

        @Test
        @DisplayName("Should get description")
        void shouldGetDescription() {
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .description(DESCRIPTION)
                    .build();

            assertThat(variable.getDescription()).isEqualTo(DESCRIPTION);
        }

        @Test
        @DisplayName("Should get created at")
        void shouldGetCreatedAt() {
            LocalDateTime now = LocalDateTime.now();
            EnvironmentVariable variable = EnvironmentVariable.builder()
                    .createdAt(now)
                    .build();

            assertThat(variable.getCreatedAt()).isEqualTo(now);
        }
    }
}
