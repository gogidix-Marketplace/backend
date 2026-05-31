package com.gogidix.aiservices.aisecurityservice.application.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DecryptionRequest DTO Tests")
class DecryptionRequestTest {

    private Validator validator;
    private ValidatorFactory validatorFactory;

    @BeforeEach
    void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    void tearDown() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    private static final String VALID_ENCRYPTED_DATA = "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=";
    private static final String VALID_KEY_ID = "key-123";

    @Nested
    @DisplayName("EncryptedData Field Tests")
    class EncryptedDataFieldTests {

        @Test
        @DisplayName("Should set and get encrypted data")
        void shouldSetAndGetEncryptedData() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);

            assertThat(request.getEncryptedData()).isEqualTo(VALID_ENCRYPTED_DATA);
        }

        @Test
        @DisplayName("Should accept non-empty encrypted data")
        void shouldAcceptNonEmptyEncryptedData() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should reject empty encrypted data")
        void shouldRejectEmptyEncryptedData() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("");
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
            assertThat(violations).anyMatch(v -> v.getMessage().contains("NotBlank"));
        }

        @Test
        @DisplayName("Should reject null encrypted data")
        void shouldRejectNullEncryptedData() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(null);
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
            assertThat(violations).anyMatch(v -> v.getMessage().contains("NotBlank"));
        }

        @Test
        @DisplayName("Should reject blank encrypted data with whitespace")
        void shouldRejectBlankEncryptedDataWithWhitespace() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("   ");
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=",
                "SGVsbG8gV29ybGQ=",
                "YW55IGNhcm5hbCBwbGVhc3VyZQ==",
                "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8="
        })
        @DisplayName("Should accept various Base64 encoded data")
        void shouldAcceptVariousBase64EncodedData(String encryptedData) {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(encryptedData);
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should accept short encrypted data")
        void shouldAcceptShortEncryptedData() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("AA==");
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should accept long encrypted data")
        void shouldAcceptLongEncryptedData() {
            String longData = "U2FsdGVkX1".repeat(100);
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(longData);
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("KeyId Field Tests")
    class KeyIdFieldTests {

        @Test
        @DisplayName("Should set and get key ID")
        void shouldSetAndGetKeyId() {
            DecryptionRequest request = new DecryptionRequest();
            request.setKeyId(VALID_KEY_ID);

            assertThat(request.getKeyId()).isEqualTo(VALID_KEY_ID);
        }

        @Test
        @DisplayName("Should accept non-empty key ID")
        void shouldAcceptNonEmptyKeyId() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should reject empty key ID")
        void shouldRejectEmptyKeyId() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId("");

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
            assertThat(violations).anyMatch(v -> v.getMessage().contains("NotBlank"));
        }

        @Test
        @DisplayName("Should reject null key ID")
        void shouldRejectNullKeyId() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(null);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
            assertThat(violations).anyMatch(v -> v.getMessage().contains("NotBlank"));
        }

        @Test
        @DisplayName("Should reject blank key ID with whitespace")
        void shouldRejectBlankKeyIdWithWhitespace() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId("   ");

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "key-001",
                "key-abc",
                "key-550e8400-e29b-41d4-a716-446655440000",
                "key-x9y8z7"
        })
        @DisplayName("Should accept various key ID formats")
        void shouldAcceptVariousKeyIdFormats(String keyId) {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(keyId);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should accept UUID format key ID")
        void shouldAcceptUuidFormatKeyId() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId("550e8400-e29b-41d4-a716-446655440000");

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should accept short key ID")
        void shouldAcceptShortKeyId() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId("k1");

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should accept long key ID")
        void shouldAcceptLongKeyId() {
            String longKeyId = "key-" + "x".repeat(200);
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(longKeyId);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate getters")
        void shouldGenerateGetters() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(VALID_KEY_ID);

            assertThat(request.getEncryptedData()).isEqualTo(VALID_ENCRYPTED_DATA);
            assertThat(request.getKeyId()).isEqualTo(VALID_KEY_ID);
        }

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            DecryptionRequest request = new DecryptionRequest();

            request.setEncryptedData("U2FsdGVkX1+AAA");
            request.setKeyId("key-001");

            assertThat(request.getEncryptedData()).isEqualTo("U2FsdGVkX1+AAA");
            assertThat(request.getKeyId()).isEqualTo("key-001");

            request.setEncryptedData("U2FsdGVkX1+BBB");
            request.setKeyId("key-002");

            assertThat(request.getEncryptedData()).isEqualTo("U2FsdGVkX1+BBB");
            assertThat(request.getKeyId()).isEqualTo("key-002");
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(VALID_KEY_ID);

            String toString = request.toString();

            assertThat(toString).contains(VALID_ENCRYPTED_DATA);
            assertThat(toString).contains(VALID_KEY_ID);
        }

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            DecryptionRequest request1 = new DecryptionRequest();
            request1.setEncryptedData(VALID_ENCRYPTED_DATA);
            request1.setKeyId(VALID_KEY_ID);

            DecryptionRequest request2 = new DecryptionRequest();
            request2.setEncryptedData(VALID_ENCRYPTED_DATA);
            request2.setKeyId(VALID_KEY_ID);

            assertThat(request1).isEqualTo(request2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            DecryptionRequest request1 = new DecryptionRequest();
            request1.setEncryptedData(VALID_ENCRYPTED_DATA);
            request1.setKeyId(VALID_KEY_ID);

            DecryptionRequest request2 = new DecryptionRequest();
            request2.setEncryptedData(VALID_ENCRYPTED_DATA);
            request2.setKeyId(VALID_KEY_ID);

            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal with different encrypted data")
        void shouldNotBeEqualWithDifferentEncryptedData() {
            DecryptionRequest request1 = new DecryptionRequest();
            request1.setEncryptedData("U2FsdGVkX1+AAA");
            request1.setKeyId(VALID_KEY_ID);

            DecryptionRequest request2 = new DecryptionRequest();
            request2.setEncryptedData("U2FsdGVkX1+BBB");
            request2.setKeyId(VALID_KEY_ID);

            assertThat(request1).isNotEqualTo(request2);
        }

        @Test
        @DisplayName("Should not be equal with different key ID")
        void shouldNotBeEqualWithDifferentKeyId() {
            DecryptionRequest request1 = new DecryptionRequest();
            request1.setEncryptedData(VALID_ENCRYPTED_DATA);
            request1.setKeyId("key-001");

            DecryptionRequest request2 = new DecryptionRequest();
            request2.setEncryptedData(VALID_ENCRYPTED_DATA);
            request2.setKeyId("key-002");

            assertThat(request1).isNotEqualTo(request2);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create with no-args constructor")
        void shouldCreateWithNoArgsConstructor() {
            DecryptionRequest request = new DecryptionRequest();

            assertThat(request).isNotNull();
        }

        @Test
        @DisplayName("Should allow setting fields after construction")
        void shouldAllowSettingFieldsAfterConstruction() {
            DecryptionRequest request = new DecryptionRequest();

            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(VALID_KEY_ID);

            assertThat(request.getEncryptedData()).isEqualTo(VALID_ENCRYPTED_DATA);
            assertThat(request.getKeyId()).isEqualTo(VALID_KEY_ID);
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should pass validation with valid data")
        void shouldPassValidationWithValidData() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should fail validation with blank encrypted data")
        void shouldFailValidationWithBlankEncryptedData() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("");
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("Should fail validation with null encrypted data")
        void shouldFailValidationWithNullEncryptedData() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(null);
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("Should fail validation with blank key ID")
        void shouldFailValidationWithBlankKeyId() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId("");

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("Should fail validation with null key ID")
        void shouldFailValidationWithNullKeyId() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(null);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("Should fail validation with both fields null")
        void shouldFailValidationWithBothFieldsNull() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(null);
            request.setKeyId(null);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(2);
        }

        @Test
        @DisplayName("Should validate encrypted data field")
        void shouldValidateEncryptedDataField() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("   ");
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().contains("encryptedData"));
        }

        @Test
        @DisplayName("Should validate key ID field")
        void shouldValidateKeyIdField() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId("   ");

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isNotEmpty();
            assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().contains("keyId"));
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should support AES256 decryption request")
        void shouldSupportAES256DecryptionRequest() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("U2FsdGVkX1+AAA");
            request.setKeyId("aes-key-001");

            assertThat(request.getEncryptedData()).isNotNull();
            assertThat(request.getKeyId()).isNotNull();
        }

        @Test
        @DisplayName("Should support RSA2048 decryption request")
        void shouldSupportRSA2048DecryptionRequest() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("RSA2048EncryptedData");
            request.setKeyId("rsa-key-001");

            assertThat(request.getEncryptedData()).isNotNull();
            assertThat(request.getKeyId()).isNotNull();
        }

        @Test
        @DisplayName("Should support RSA4096 decryption request")
        void shouldSupportRSA4096DecryptionRequest() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("RSA4096EncryptedData");
            request.setKeyId("rsa4096-key-001");

            assertThat(request.getEncryptedData()).isNotNull();
            assertThat(request.getKeyId()).isNotNull();
        }

        @Test
        @DisplayName("Should support key rotation scenario")
        void shouldSupportKeyRotationScenario() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("Data encrypted with old key");
            request.setKeyId("old-key-id");

            assertThat(request.getKeyId()).isEqualTo("old-key-id");
        }

        @Test
        @DisplayName("Should support multiple key scenario")
        void shouldSupportMultipleKeyScenario() {
            DecryptionRequest request1 = new DecryptionRequest();
            request1.setEncryptedData("Data1");
            request1.setKeyId("key-001");

            DecryptionRequest request2 = new DecryptionRequest();
            request2.setEncryptedData("Data2");
            request2.setKeyId("key-002");

            assertThat(request1.getKeyId()).isNotEqualTo(request2.getKeyId());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle encrypted data with special characters")
        void shouldHandleEncryptedDataWithSpecialCharacters() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("U2FsdGVkX1+/@#$%");
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should handle key ID with special characters")
        void shouldHandleKeyIdWithSpecialCharacters() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId("key-001_v2.0@prod");

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should handle unicode in encrypted data")
        void shouldHandleUnicodeInEncryptedData() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("U2FsdGVkX1+🔒🔑");
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should handle unicode in key ID")
        void shouldHandleUnicodeInKeyId() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId("key-鍵-001");

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should handle very long encrypted data")
        void shouldHandleVeryLongEncryptedData() {
            String longData = "ENCRYPTED".repeat(10000);
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(longData);
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should handle very long key ID")
        void shouldHandleVeryLongKeyId() {
            String longKeyId = "key-" + "x".repeat(500);
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(longKeyId);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Field Combination Tests")
    class FieldCombinationTests {

        @Test
        @DisplayName("Should require both fields for valid request")
        void shouldRequireBothFieldsForValidRequest() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should fail when only encrypted data is set")
        void shouldFailWhenOnlyEncryptedDataIsSet() {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(VALID_ENCRYPTED_DATA);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
        }

        @Test
        @DisplayName("Should fail when only key ID is set")
        void shouldFailWhenOnlyKeyIdIsSet() {
            DecryptionRequest request = new DecryptionRequest();
            request.setKeyId(VALID_KEY_ID);

            Set<ConstraintViolation<DecryptionRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
        }
    }
}
