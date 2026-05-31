package com.gogidix.aiservices.aifrauddetectionservice.application.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for AnalyzeTransactionRequest DTO.
 * Tests validation annotations, getters, setters, and builder pattern.
 */
@DisplayName("AnalyzeTransactionRequest DTO Tests")
class AnalyzeTransactionRequestTest {

    private ValidatorFactory validatorFactory;
    private Validator validator;

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

    @Nested
    @DisplayName("Getters and Setters")
    class GettersAndSettersTests {

        @Test
        @DisplayName("Should get and set transaction ID")
        void shouldGetAndSetTransactionId() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            request.setTransactionId("txn-123");

            assertThat(request.getTransactionId()).isEqualTo("txn-123");
        }

        @Test
        @DisplayName("Should get and set user ID")
        void shouldGetAndSetUserId() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            request.setUserId("user-456");

            assertThat(request.getUserId()).isEqualTo("user-456");
        }

        @Test
        @DisplayName("Should get and set amount")
        void shouldGetAndSetAmount() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            BigDecimal amount = new BigDecimal("100.50");
            request.setAmount(amount);

            assertThat(request.getAmount()).isEqualByComparingTo("100.50");
        }

        @Test
        @DisplayName("Should get and set merchant")
        void shouldGetAndSetMerchant() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            request.setMerchant("Amazon");

            assertThat(request.getMerchant()).isEqualTo("Amazon");
        }

        @Test
        @DisplayName("Should get and set timestamp")
        void shouldGetAndSetTimestamp() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            Instant now = Instant.now();
            request.setTimestamp(now);

            assertThat(request.getTimestamp()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should get and set currency")
        void shouldGetAndSetCurrency() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            request.setCurrency("USD");

            assertThat(request.getCurrency()).isEqualTo("USD");
        }

        @Test
        @DisplayName("Should get and set metadata")
        void shouldGetAndSetMetadata() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            Map<String, Object> metadata = Map.of("key1", "value1", "key2", 123);
            request.setMetadata(metadata);

            assertThat(request.getMetadata()).hasSize(2);
            assertThat(request.getMetadata().get("key1")).isEqualTo("value1");
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should build request with all fields")
        void shouldBuildRequestWithAllFields() {
            Instant now = Instant.now();
            Map<String, Object> metadata = Map.of("key", "value");

            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("txn-123")
                    .userId("user-456")
                    .amount(new BigDecimal("100.50"))
                    .merchant("Amazon")
                    .timestamp(now)
                    .currency("USD")
                    .metadata(metadata)
                    .build();

            assertThat(request.getTransactionId()).isEqualTo("txn-123");
            assertThat(request.getUserId()).isEqualTo("user-456");
            assertThat(request.getAmount()).isEqualByComparingTo("100.50");
            assertThat(request.getMerchant()).isEqualTo("Amazon");
            assertThat(request.getTimestamp()).isEqualTo(now);
            assertThat(request.getCurrency()).isEqualTo("USD");
            assertThat(request.getMetadata()).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should build request with partial fields")
        void shouldBuildRequestWithPartialFields() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("txn-123")
                    .userId("user-456")
                    .amount(new BigDecimal("50.00"))
                    .merchant("Store")
                    .build();

            assertThat(request.getTransactionId()).isEqualTo("txn-123");
            assertThat(request.getUserId()).isEqualTo("user-456");
            assertThat(request.getMerchant()).isEqualTo("Store");
        }

        @Test
        @DisplayName("Should allow chaining builder methods")
        void shouldAllowChainingBuilderMethods() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("txn-1")
                    .userId("user-1")
                    .amount(new BigDecimal("10"))
                    .merchant("Shop")
                    .currency("EUR")
                    .build();

            assertThat(request.getTransactionId()).isEqualTo("txn-1");
            assertThat(request.getCurrency()).isEqualTo("EUR");
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("Should validate blank transaction ID")
        void shouldValidateBlankTransactionId() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("")
                    .userId("user-123")
                    .amount(new BigDecimal("100"))
                    .merchant("Store")
                    .build();

            Set<ConstraintViolation<AnalyzeTransactionRequest>> violations = validator.validate(request);

            assertThat(violations)
                    .anyMatch(v -> v.getPropertyPath().toString().equals("transactionId") &&
                            v.getMessage().contains("required"));
        }

        @Test
        @DisplayName("Should validate null transaction ID")
        void shouldValidateNullTransactionId() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId(null)
                    .userId("user-123")
                    .amount(new BigDecimal("100"))
                    .merchant("Store")
                    .build();

            Set<ConstraintViolation<AnalyzeTransactionRequest>> violations = validator.validate(request);

            assertThat(violations)
                    .anyMatch(v -> v.getPropertyPath().toString().equals("transactionId"));
        }

        @Test
        @DisplayName("Should validate blank user ID")
        void shouldValidateBlankUserId() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("txn-123")
                    .userId("")
                    .amount(new BigDecimal("100"))
                    .merchant("Store")
                    .build();

            Set<ConstraintViolation<AnalyzeTransactionRequest>> violations = validator.validate(request);

            assertThat(violations)
                    .anyMatch(v -> v.getPropertyPath().toString().equals("userId"));
        }

        @Test
        @DisplayName("Should validate null amount")
        void shouldValidateNullAmount() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("txn-123")
                    .userId("user-123")
                    .amount(null)
                    .merchant("Store")
                    .build();

            Set<ConstraintViolation<AnalyzeTransactionRequest>> violations = validator.validate(request);

            assertThat(violations)
                    .anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
        }

        @Test
        @DisplayName("Should validate negative amount")
        void shouldValidateNegativeAmount() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("txn-123")
                    .userId("user-123")
                    .amount(new BigDecimal("-10"))
                    .merchant("Store")
                    .build();

            Set<ConstraintViolation<AnalyzeTransactionRequest>> violations = validator.validate(request);

            assertThat(violations)
                    .anyMatch(v -> v.getPropertyPath().toString().equals("amount") &&
                            v.getMessage().contains("positive"));
        }

        @Test
        @DisplayName("Should validate zero amount")
        void shouldValidateZeroAmount() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("txn-123")
                    .userId("user-123")
                    .amount(BigDecimal.ZERO)
                    .merchant("Store")
                    .build();

            Set<ConstraintViolation<AnalyzeTransactionRequest>> violations = validator.validate(request);

            assertThat(violations)
                    .anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
        }

        @Test
        @DisplayName("Should validate blank merchant")
        void shouldValidateBlankMerchant() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("txn-123")
                    .userId("user-123")
                    .amount(new BigDecimal("100"))
                    .merchant("")
                    .build();

            Set<ConstraintViolation<AnalyzeTransactionRequest>> violations = validator.validate(request);

            assertThat(violations)
                    .anyMatch(v -> v.getPropertyPath().toString().equals("merchant"));
        }

        @Test
        @DisplayName("Should pass validation with valid request")
        void shouldPassValidationWithValidRequest() {
            AnalyzeTransactionRequest request = AnalyzeTransactionRequest.builder()
                    .transactionId("txn-123")
                    .userId("user-123")
                    .amount(new BigDecimal("100.50"))
                    .merchant("Amazon")
                    .build();

            Set<ConstraintViolation<AnalyzeTransactionRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Boundary Values")
    class BoundaryValueTests {

        @Test
        @DisplayName("Should handle very small positive amount")
        void shouldHandleVerySmallPositiveAmount() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            request.setAmount(new BigDecimal("0.01"));

            assertThat(request.getAmount()).isEqualByComparingTo("0.01");
        }

        @Test
        @DisplayName("Should handle very large amount")
        void shouldHandleVeryLargeAmount() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            BigDecimal largeAmount = new BigDecimal("999999999.99");
            request.setAmount(largeAmount);

            assertThat(request.getAmount()).isEqualByComparingTo("999999999.99");
        }

        @Test
        @DisplayName("Should handle amount with many decimal places")
        void shouldHandleAmountWithManyDecimalPlaces() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            BigDecimal preciseAmount = new BigDecimal("100.123456789");
            request.setAmount(preciseAmount);

            assertThat(request.getAmount()).isEqualByComparingTo("100.123456789");
        }
    }

    @Nested
    @DisplayName("Null Handling")
    class NullHandlingTests {

        @Test
        @DisplayName("Should default to null for optional fields")
        void shouldDefaultToNullForOptionalFields() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();

            assertThat(request.getTimestamp()).isNull();
            assertThat(request.getCurrency()).isNull();
            assertThat(request.getMetadata()).isNull();
        }

        @Test
        @DisplayName("Should allow setting null for optional fields")
        void shouldAllowSettingNullForOptionalFields() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            request.setTimestamp(null);
            request.setCurrency(null);
            request.setMetadata(null);

            assertThat(request.getTimestamp()).isNull();
            assertThat(request.getCurrency()).isNull();
            assertThat(request.getMetadata()).isNull();
        }
    }

    @Nested
    @DisplayName("Metadata Handling")
    class MetadataHandlingTests {

        @Test
        @DisplayName("Should handle empty metadata")
        void shouldHandleEmptyMetadata() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            request.setMetadata(Map.of());

            assertThat(request.getMetadata()).isEmpty();
        }

        @Test
        @DisplayName("Should handle metadata with various value types")
        void shouldHandleMetadataWithVariousValueTypes() {
            AnalyzeTransactionRequest request = new AnalyzeTransactionRequest();
            // Use HashMap instead of Map.of() because Map.of() doesn't allow null values
            Map<String, Object> metadata = new java.util.HashMap<>();
            metadata.put("string", "value");
            metadata.put("number", 123);
            metadata.put("boolean", true);
            metadata.put("null", null);
            request.setMetadata(metadata);

            assertThat(request.getMetadata()).hasSize(4);
            assertThat(request.getMetadata().get("string")).isEqualTo("value");
            assertThat(request.getMetadata().get("number")).isEqualTo(123);
            assertThat(request.getMetadata().get("boolean")).isEqualTo(true);
            assertThat(request.getMetadata().get("null")).isNull();
        }
    }
}
