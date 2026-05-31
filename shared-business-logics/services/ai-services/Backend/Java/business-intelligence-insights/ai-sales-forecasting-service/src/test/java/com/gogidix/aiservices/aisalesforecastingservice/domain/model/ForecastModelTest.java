package com.gogidix.aiservices.aisalesforecastingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ForecastModel Domain Model Tests")
class ForecastModelTest {

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build ForecastModel with required fields")
        void shouldBuildWithRequiredFields() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile.getForecastModelId()).isEqualTo("cust-123");
            assertThat(profile.getTenantId()).isEqualTo("tenant-456");
        }

        @Test
        @DisplayName("Should build ForecastModel with all fields")
        void shouldBuildWithAllFields() {
            Instant now = Instant.now();
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("preferredCategory", "electronics");

            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .email("john@example.com")
                    .firstName("John")
                    .lastName("Doe")
                    .age(30)
                    .gender("M")
                    .country("USA")
                    .city("New York")
                    .registeredAt(now)
                    .lastActivityAt(now)
                    .totalPurchases(15)
                    .totalSpent(2500.0)
                    .averageOrderValue(166.67)
                    .loyaltyTier("GOLD")
                    .customAttributes(attributes)
                    .build();

            assertThat(profile.getForecastModelId()).isEqualTo("cust-123");
            assertThat(profile.getTenantId()).isEqualTo("tenant-456");
            assertThat(profile.getEmail()).isEqualTo("john@example.com");
            assertThat(profile.getFirstName()).isEqualTo("John");
            assertThat(profile.getLastName()).isEqualTo("Doe");
            assertThat(profile.getAge()).isEqualTo(30);
            assertThat(profile.getGender()).isEqualTo("M");
            assertThat(profile.getCountry()).isEqualTo("USA");
            assertThat(profile.getCity()).isEqualTo("New York");
            assertThat(profile.getRegisteredAt()).isEqualTo(now);
            assertThat(profile.getLastActivityAt()).isEqualTo(now);
            assertThat(profile.getTotalPurchases()).isEqualTo(15);
            assertThat(profile.getTotalSpent()).isEqualTo(2500.0);
            assertThat(profile.getAverageOrderValue()).isEqualTo(166.67);
            assertThat(profile.getLoyaltyTier()).isEqualTo("GOLD");
            assertThat(profile.getCustomAttributes()).isEqualTo(attributes);
        }

        @Test
        @DisplayName("Should throw exception when customerId is null")
        void shouldThrowWhenForecastModelIdIsNull() {
            assertThatThrownBy(() -> ForecastModel.builder()
                    .customerId(null)
                    .tenantId("tenant-456")
                    .build())
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("customerId is required");
        }

        @Test
        @DisplayName("Should throw exception when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThatThrownBy(() -> ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId(null)
                    .build())
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("tenantId is required");
        }
    }

    @Nested
    @DisplayName("Getter Methods Tests")
    class GetterTests {

        @Test
        @DisplayName("Should return customerId")
        void shouldReturnForecastModelId() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile.getForecastModelId()).isEqualTo("cust-123");
        }

        @Test
        @DisplayName("Should return tenantId")
        void shouldReturnTenantId() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile.getTenantId()).isEqualTo("tenant-456");
        }

        @Test
        @DisplayName("Should return email when present")
        void shouldReturnEmailWhenPresent() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .email("john@example.com")
                    .build();

            assertThat(profile.getEmail()).isEqualTo("john@example.com");
        }

        @Test
        @DisplayName("Should return null for email when not set")
        void shouldReturnNullWhenEmailNotSet() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile.getEmail()).isNull();
        }

        @Test
        @DisplayName("Should return full name when both first and last name are present")
        void shouldReturnFullName() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .firstName("John")
                    .lastName("Doe")
                    .build();

            assertThat(profile.getFullName()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("Should return first name only when last name is missing")
        void shouldReturnFirstNameOnly() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .firstName("John")
                    .build();

            assertThat(profile.getFullName()).isEqualTo("John");
        }

        @Test
        @DisplayName("Should return last name only when first name is missing")
        void shouldReturnLastNameOnly() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .lastName("Doe")
                    .build();

            assertThat(profile.getFullName()).isEqualTo("Doe");
        }

        @Test
        @DisplayName("Should return email when both names are missing")
        void shouldReturnEmailWhenNamesMissing() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .email("john@example.com")
                    .build();

            assertThat(profile.getFullName()).isEqualTo("john@example.com");
        }

        @Test
        @DisplayName("Should return null when all name fields and email are missing")
        void shouldReturnNullWhenAllNamesMissing() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile.getFullName()).isNull();
        }

        @Test
        @DisplayName("Should return custom attribute when present")
        void shouldReturnCustomAttributeWhenPresent() {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("preferredCategory", "electronics");
            attributes.put("discountPercent", 15);

            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .customAttributes(attributes)
                    .build();

            assertThat(profile.getCustomAttribute("preferredCategory")).isEqualTo("electronics");
            assertThat(profile.getCustomAttribute("discountPercent")).isEqualTo(15);
        }

        @Test
        @DisplayName("Should return null when custom attribute not found")
        void shouldReturnNullWhenAttributeNotFound() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile.getCustomAttribute("nonexistent")).isNull();
        }

        @Test
        @DisplayName("Should return null when custom attributes map is null")
        void shouldReturnNullWhenCustomAttributesNull() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile.getCustomAttributes()).isNull();
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should return true when customer is active (activity within 30 days)")
        void shouldReturnTrueWhenActive() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .lastActivityAt(Instant.now())
                    .build();

            assertThat(profile.isActive()).isTrue();
        }

        @Test
        @DisplayName("Should return true when activity was 29 days ago")
        void shouldReturnTrueWhenActivityWas29DaysAgo() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .lastActivityAt(Instant.now().minus(29, java.time.temporal.ChronoUnit.DAYS))
                    .build();

            assertThat(profile.isActive()).isTrue();
        }

        @Test
        @DisplayName("Should return false when customer is inactive (activity older than 30 days)")
        void shouldReturnFalseWhenInactive() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .lastActivityAt(Instant.now().minus(31, java.time.temporal.ChronoUnit.DAYS))
                    .build();

            assertThat(profile.isActive()).isFalse();
        }

        @Test
        @DisplayName("Should return false when lastActivityAt is null")
        void shouldReturnFalseWhenLastActivityNull() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile.isActive()).isFalse();
        }

        @ParameterizedTest
        @CsvSource({
                "GOLD, 0, true",
                "PLATINUM, 0, true",
                "SILVER, 0, false",
                "BRONZE, 0, false",
                "null, 15000, true",
                "SILVER, 10000, true",
                "SILVER, 9999, false"
        })
        @DisplayName("Should correctly determine VIP status")
        void shouldDetermineVipStatus(String loyaltyTier, double totalSpent, boolean expected) {
            ForecastModel.Builder builder = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .totalSpent(totalSpent);

            if (!"null".equals(loyaltyTier)) {
                builder.loyaltyTier(loyaltyTier);
            }

            ForecastModel profile = builder.build();

            assertThat(profile.isVip()).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should be equal when customerId and tenantId match")
        void shouldBeEqualWhenIdsMatch() {
            ForecastModel profile1 = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            ForecastModel profile2 = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile1).isEqualTo(profile2);
            assertThat(profile1.hashCode()).isEqualTo(profile2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when customerId differs")
        void shouldNotBeEqualWhenForecastModelIdDiffers() {
            ForecastModel profile1 = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            ForecastModel profile2 = ForecastModel.builder()
                    .customerId("cust-789")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile1).isNotEqualTo(profile2);
        }

        @Test
        @DisplayName("Should not be equal when tenantId differs")
        void shouldNotBeEqualWhenTenantIdDiffers() {
            ForecastModel profile1 = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            ForecastModel profile2 = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-789")
                    .build();

            assertThat(profile1).isNotEqualTo(profile2);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile).isEqualTo(profile);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .build();

            assertThat(profile).isNotEqualTo("string");
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should contain relevant fields in toString")
        void shouldContainRelevantFields() {
            ForecastModel profile = ForecastModel.builder()
                    .customerId("cust-123")
                    .tenantId("tenant-456")
                    .email("john@example.com")
                    .loyaltyTier("GOLD")
                    .totalPurchases(15)
                    .totalSpent(2500.0)
                    .build();

            String str = profile.toString();

            assertThat(str).contains("cust-123");
            assertThat(str).contains("tenant-456");
            assertThat(str).contains("john@example.com");
            assertThat(str).contains("GOLD");
            assertThat(str).contains("15");
            assertThat(str).contains("2500.0");
        }
    }
}
