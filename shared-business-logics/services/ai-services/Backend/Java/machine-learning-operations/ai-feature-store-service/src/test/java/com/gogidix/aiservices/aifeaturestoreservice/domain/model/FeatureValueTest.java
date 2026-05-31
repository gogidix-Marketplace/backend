package com.gogidix.aiservices.aifeaturestoreservice.domain.model;

import com.gogidix.aiservices.aifeaturestoreservice.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FeatureValue domain model.
 */
@DisplayName("FeatureValue Domain Model Tests")
class FeatureValueTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String FEATURE_NAME = "user_lifetime_value";
    private static final String ENTITY_ID = "user-001";

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create feature value with valid parameters")
        void shouldCreateFeatureValueWithValidParameters() {
            FeatureValue featureValue = new FeatureValue(TENANT_ID, FEATURE_NAME, ENTITY_ID, 1000.0);

            assertNotNull(featureValue.getId());
            assertEquals(TENANT_ID, featureValue.getTenantId());
            assertEquals(FEATURE_NAME, featureValue.getFeatureName());
            assertEquals(ENTITY_ID, featureValue.getEntityId());
            assertEquals(1000.0, featureValue.getValue());
            assertNotNull(featureValue.getCreatedAt());
        }

        @Test
        @DisplayName("Should throw exception when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new FeatureValue(null, FEATURE_NAME, ENTITY_ID, 1000.0));
        }

        @Test
        @DisplayName("Should throw exception when featureName is null")
        void shouldThrowWhenFeatureNameIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new FeatureValue(TENANT_ID, null, ENTITY_ID, 1000.0));
        }

        @Test
        @DisplayName("Should throw exception when entityId is null")
        void shouldThrowWhenEntityIdIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new FeatureValue(TENANT_ID, FEATURE_NAME, null, 1000.0));
        }

        @Test
        @DisplayName("Should throw exception when value is null")
        void shouldThrowWhenValueIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new FeatureValue(TENANT_ID, FEATURE_NAME, ENTITY_ID, null));
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update value")
        void shouldUpdateValue() {
            FeatureValue featureValue = new FeatureValue(TENANT_ID, FEATURE_NAME, ENTITY_ID, 1000.0);

            featureValue.updateValue(2000.0);

            assertEquals(2000.0, featureValue.getValue());
        }

        @Test
        @DisplayName("Should throw exception when updating with null value")
        void shouldThrowWhenUpdatingWithNullValue() {
            FeatureValue featureValue = new FeatureValue(TENANT_ID, FEATURE_NAME, ENTITY_ID, 1000.0);

            assertThrows(NullPointerException.class, () -> featureValue.updateValue(null));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate valid feature value")
        void shouldValidateValidFeatureValue() {
            FeatureValue featureValue = new FeatureValue(TENANT_ID, FEATURE_NAME, ENTITY_ID, 1000.0);

            assertDoesNotThrow(featureValue::validate);
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            FeatureValue featureValue = new FeatureValue("  ", FEATURE_NAME, ENTITY_ID, 1000.0);

            assertThrows(ValidationException.class, featureValue::validate);
        }

        @Test
        @DisplayName("Should throw when featureName is blank")
        void shouldThrowWhenFeatureNameIsBlank() {
            FeatureValue featureValue = new FeatureValue(TENANT_ID, "  ", ENTITY_ID, 1000.0);

            assertThrows(ValidationException.class, featureValue::validate);
        }

        @Test
        @DisplayName("Should throw when entityId is blank")
        void shouldThrowWhenEntityIdIsBlank() {
            FeatureValue featureValue = new FeatureValue(TENANT_ID, FEATURE_NAME, "  ", 1000.0);

            assertThrows(ValidationException.class, featureValue::validate);
        }
    }

    @Nested
    @DisplayName("Value Type Tests")
    class ValueTypeTests {

        @Test
        @DisplayName("Should store numeric value")
        void shouldStoreNumericValue() {
            FeatureValue featureValue = new FeatureValue(TENANT_ID, FEATURE_NAME, ENTITY_ID, 1000.0);

            assertEquals(1000.0, featureValue.getValue());
        }

        @Test
        @DisplayName("Should store string value")
        void shouldStoreStringValue() {
            FeatureValue featureValue = new FeatureValue(TENANT_ID, FEATURE_NAME, ENTITY_ID, "premium");

            assertEquals("premium", featureValue.getValue());
        }

        @Test
        @DisplayName("Should store boolean value")
        void shouldStoreBooleanValue() {
            FeatureValue featureValue = new FeatureValue(TENANT_ID, FEATURE_NAME, ENTITY_ID, true);

            assertEquals(true, featureValue.getValue());
        }
    }
}
