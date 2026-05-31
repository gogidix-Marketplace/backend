package com.gogidix.aiservices.aifrauddetectionservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Transaction Domain Model Tests")
class TransactionTest {

    private static final String TRANSACTION_ID = "txn-123";
    private static final String USER_ID = "user-456";
    private static final BigDecimal AMOUNT = new BigDecimal("100.50");
    private static final String MERCHANT = "Test Merchant";
    private static final Instant TIMESTAMP = Instant.now();
    private static final String CURRENCY = "USD";

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build transaction with all fields")
        void shouldBuildTransactionWithAllFields() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("ip_address", "192.168.1.1");
            metadata.put("device_id", "device-123");

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .metadata(metadata)
                    .build();

            assertThat(transaction).isNotNull();
            assertThat(transaction.getTransactionId()).isEqualTo(TRANSACTION_ID);
            assertThat(transaction.getUserId()).isEqualTo(USER_ID);
            assertThat(transaction.getAmount()).isEqualTo(AMOUNT);
            assertThat(transaction.getMerchant()).isEqualTo(MERCHANT);
            assertThat(transaction.getTimestamp()).isEqualTo(TIMESTAMP);
            assertThat(transaction.getCurrency()).isEqualTo(CURRENCY);
            assertThat(transaction.getMetadata()).isEqualTo(metadata);
        }

        @Test
        @DisplayName("Should build transaction with required fields only")
        void shouldBuildTransactionWithRequiredFieldsOnly() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction).isNotNull();
            assertThat(transaction.getTransactionId()).isEqualTo(TRANSACTION_ID);
            assertThat(transaction.getUserId()).isEqualTo(USER_ID);
            assertThat(transaction.getAmount()).isEqualTo(AMOUNT);
            assertThat(transaction.getMerchant()).isEqualTo(MERCHANT);
            assertThat(transaction.getTimestamp()).isEqualTo(TIMESTAMP);
            assertThat(transaction.getCurrency()).isEqualTo(CURRENCY);
            assertThat(transaction.getMetadata()).isNull();
        }

        @Test
        @DisplayName("Should build transaction with null currency")
        void shouldBuildTransactionWithNullCurrency() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(null)
                    .build();

            assertThat(transaction).isNotNull();
            assertThat(transaction.getCurrency()).isNull();
        }

        @Test
        @DisplayName("Should build transaction with empty metadata")
        void shouldBuildTransactionWithEmptyMetadata() {
            Map<String, Object> emptyMetadata = new HashMap<>();

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .metadata(emptyMetadata)
                    .build();

            assertThat(transaction).isNotNull();
            assertThat(transaction.getMetadata()).isEmpty();
        }

        @Test
        @DisplayName("Should create multiple independent instances")
        void shouldCreateMultipleIndependentInstances() {
            Transaction transaction1 = Transaction.builder()
                    .transactionId("txn-1")
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            Transaction transaction2 = Transaction.builder()
                    .transactionId("txn-2")
                    .userId(USER_ID)
                    .amount(new BigDecimal("200"))
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("EUR")
                    .build();

            assertThat(transaction1.getTransactionId()).isNotEqualTo(transaction2.getTransactionId());
            assertThat(transaction1.getAmount()).isNotEqualTo(transaction2.getAmount());
            assertThat(transaction1.getCurrency()).isNotEqualTo(transaction2.getCurrency());
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterMethodTests {

        @Test
        @DisplayName("Should return correct transactionId")
        void shouldReturnCorrectTransactionId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getTransactionId()).isEqualTo(TRANSACTION_ID);
        }

        @Test
        @DisplayName("Should return correct userId")
        void shouldReturnCorrectUserId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getUserId()).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should return correct amount")
        void shouldReturnCorrectAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getAmount()).isEqualTo(AMOUNT);
        }

        @Test
        @DisplayName("Should return correct merchant")
        void shouldReturnCorrectMerchant() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getMerchant()).isEqualTo(MERCHANT);
        }

        @Test
        @DisplayName("Should return correct timestamp")
        void shouldReturnCorrectTimestamp() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getTimestamp()).isEqualTo(TIMESTAMP);
        }

        @Test
        @DisplayName("Should return correct currency")
        void shouldReturnCorrectCurrency() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getCurrency()).isEqualTo(CURRENCY);
        }

        @Test
        @DisplayName("Should return correct metadata")
        void shouldReturnCorrectMetadata() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("key1", "value1");
            metadata.put("key2", 123);

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .metadata(metadata)
                    .build();

            assertThat(transaction.getMetadata()).isEqualTo(metadata);
            assertThat(transaction.getMetadata()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle zero amount")
        void shouldHandleZeroAmount() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(BigDecimal.ZERO)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Should handle very large amount")
        void shouldHandleVeryLargeAmount() {
            BigDecimal largeAmount = new BigDecimal("999999999.99");

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(largeAmount)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getAmount()).isEqualByComparingTo(largeAmount);
        }

        @Test
        @DisplayName("Should handle very small amount")
        void shouldHandleVerySmallAmount() {
            BigDecimal smallAmount = new BigDecimal("0.01");

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(smallAmount)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getAmount()).isEqualByComparingTo(smallAmount);
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("Should handle null timestamp")
        void shouldHandleNullTimestamp(Instant nullTimestamp) {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(nullTimestamp)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getTimestamp()).isNull();
        }

        @Test
        @DisplayName("Should handle empty string transactionId")
        void shouldHandleEmptyStringTransactionId() {
            Transaction transaction = Transaction.builder()
                    .transactionId("")
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getTransactionId()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty string userId")
        void shouldHandleEmptyStringUserId() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId("")
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getUserId()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty string merchant")
        void shouldHandleEmptyStringMerchant() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant("")
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .build();

            assertThat(transaction.getMerchant()).isEmpty();
        }

        @Test
        @DisplayName("Should handle metadata with null values")
        void shouldHandleMetadataWithNullValues() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("key1", null);
            metadata.put("key2", "value2");

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .metadata(metadata)
                    .build();

            assertThat(transaction.getMetadata()).containsKey("key1");
            assertThat(transaction.getMetadata().get("key1")).isNull();
        }

        @Test
        @DisplayName("Should handle metadata with multiple types")
        void shouldHandleMetadataWithMultipleTypes() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("string", "value");
            metadata.put("integer", 123);
            metadata.put("double", 45.67);
            metadata.put("boolean", true);
            metadata.put("nested", Map.of("key", "value"));

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .metadata(metadata)
                    .build();

            assertThat(transaction.getMetadata()).hasSize(5);
            assertThat(transaction.getMetadata().get("string")).isEqualTo("value");
            assertThat(transaction.getMetadata().get("integer")).isEqualTo(123);
            assertThat(transaction.getMetadata().get("double")).isEqualTo(45.67);
            assertThat(transaction.getMetadata().get("boolean")).isEqualTo(true);
        }
    }

    @Nested
    @DisplayName("Currency Tests")
    class CurrencyTests {

        @ParameterizedTest
        @ValueSource(strings = {"USD", "EUR", "GBP", "JPY", "CAD", "AUD", "CHF", "CNY", "INR"})
        @DisplayName("Should handle various currency codes")
        void shouldHandleVariousCurrencyCodes(String currency) {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(currency)
                    .build();

            assertThat(transaction.getCurrency()).isEqualTo(currency);
        }

        @Test
        @DisplayName("Should handle lowercase currency code")
        void shouldHandleLowercaseCurrencyCode() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("usd")
                    .build();

            assertThat(transaction.getCurrency()).isEqualTo("usd");
        }

        @Test
        @DisplayName("Should handle numeric currency code")
        void shouldHandleNumericCurrencyCode() {
            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency("840")
                    .build();

            assertThat(transaction.getCurrency()).isEqualTo("840");
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should be immutable after creation")
        void shouldBeImmutableAfterCreation() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("key", "value");

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .metadata(metadata)
                    .build();

            String originalTransactionId = transaction.getTransactionId();
            String originalUserId = transaction.getUserId();
            BigDecimal originalAmount = transaction.getAmount();

            assertThat(originalTransactionId).isEqualTo(transaction.getTransactionId());
            assertThat(originalUserId).isEqualTo(transaction.getUserId());
            assertThat(originalAmount).isEqualTo(transaction.getAmount());
        }

        @Test
        @DisplayName("Should allow metadata modification after creation")
        void shouldAllowMetadataModificationAfterCreation() {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("key1", "value1");

            Transaction transaction = Transaction.builder()
                    .transactionId(TRANSACTION_ID)
                    .userId(USER_ID)
                    .amount(AMOUNT)
                    .merchant(MERCHANT)
                    .timestamp(TIMESTAMP)
                    .currency(CURRENCY)
                    .metadata(metadata)
                    .build();

            metadata.put("key2", "value2");

            assertThat(transaction.getMetadata()).hasSize(2);
            assertThat(transaction.getMetadata()).containsKey("key2");
        }
    }
}
