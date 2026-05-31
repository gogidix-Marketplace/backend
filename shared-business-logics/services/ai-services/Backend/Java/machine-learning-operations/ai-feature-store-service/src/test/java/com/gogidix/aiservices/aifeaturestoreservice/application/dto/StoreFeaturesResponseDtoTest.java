package com.gogidix.aiservices.aifeaturestoreservice.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StoreFeaturesResponseDto Tests")
class StoreFeaturesResponseDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create response with all fields")
        void shouldCreateWithAllFields() {
            Instant now = Instant.now();
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0.0",
                    100,
                    now
            );

            assertThat(response.featureVersion()).isEqualTo("v1.0.0");
            assertThat(response.storedCount()).isEqualTo(100);
            assertThat(response.timestamp()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should create response with minimal data")
        void shouldCreateWithMinimalData() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    1,
                    Instant.now()
            );

            assertThat(response.featureVersion()).isEqualTo("v1.0");
            assertThat(response.storedCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should accept zero stored count")
        void shouldAcceptZeroStoredCount() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    0,
                    Instant.now()
            );

            assertThat(response.storedCount()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Feature Version Tests")
    class FeatureVersionTests {

        @Test
        @DisplayName("Should store semantic version")
        void shouldStoreSemanticVersion() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "1.2.3-beta",
                    10,
                    Instant.now()
            );

            assertThat(response.featureVersion()).isEqualTo("1.2.3-beta");
        }

        @Test
        @DisplayName("Should store custom version format")
        void shouldStoreCustomVersionFormat() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "20240315-001",
                    5,
                    Instant.now()
            );

            assertThat(response.featureVersion()).isEqualTo("20240315-001");
        }

        @Test
        @DisplayName("Should store UUID as version")
        void shouldStoreUuidAsVersion() {
            String uuid = java.util.UUID.randomUUID().toString();
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    uuid,
                    1,
                    Instant.now()
            );

            assertThat(response.featureVersion()).isEqualTo(uuid);
        }
    }

    @Nested
    @DisplayName("Stored Count Tests")
    class StoredCountTests {

        @Test
        @DisplayName("Should represent single feature storage")
        void shouldRepresentSingleFeatureStorage() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    1,
                    Instant.now()
            );

            assertThat(response.storedCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should represent batch feature storage")
        void shouldRepresentBatchFeatureStorage() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    1000,
                    Instant.now()
            );

            assertThat(response.storedCount()).isEqualTo(1000);
        }

        @Test
        @DisplayName("Should represent large batch storage")
        void shouldRepresentLargeBatchStorage() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    100000,
                    Instant.now()
            );

            assertThat(response.storedCount()).isEqualTo(100000);
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should store current timestamp")
        void shouldStoreCurrentTimestamp() {
            Instant before = Instant.now();
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    10,
                    Instant.now()
            );
            Instant after = Instant.now();

            assertThat(response.timestamp()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should store past timestamp")
        void shouldStorePastTimestamp() {
            Instant past = Instant.now().minusSeconds(3600);
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    10,
                    past
            );

            assertThat(response.timestamp()).isEqualTo(past);
        }

        @Test
        @DisplayName("Should store epoch timestamp")
        void shouldStoreEpochTimestamp() {
            Instant epoch = Instant.EPOCH;
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v0.1",
                    0,
                    epoch
            );

            assertThat(response.timestamp()).isEqualTo(epoch);
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            Instant timestamp = Instant.now();
            StoreFeaturesResponseDto response1 = new StoreFeaturesResponseDto(
                    "v1.0", 10, timestamp
            );
            StoreFeaturesResponseDto response2 = new StoreFeaturesResponseDto(
                    "v1.0", 10, timestamp
            );

            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when versions differ")
        void shouldNotBeEqualWhenVersionsDiffer() {
            Instant timestamp = Instant.now();
            StoreFeaturesResponseDto response1 = new StoreFeaturesResponseDto(
                    "v1.0", 10, timestamp
            );
            StoreFeaturesResponseDto response2 = new StoreFeaturesResponseDto(
                    "v2.0", 10, timestamp
            );

            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Should not be equal when counts differ")
        void shouldNotBeEqualWhenCountsDiffer() {
            Instant timestamp = Instant.now();
            StoreFeaturesResponseDto response1 = new StoreFeaturesResponseDto(
                    "v1.0", 10, timestamp
            );
            StoreFeaturesResponseDto response2 = new StoreFeaturesResponseDto(
                    "v1.0", 20, timestamp
            );

            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Should not be equal when timestamps differ")
        void shouldNotBeEqualWhenTimestampsDiffer() {
            Instant timestamp1 = Instant.now();
            Instant timestamp2 = timestamp1.plusSeconds(1);
            StoreFeaturesResponseDto response1 = new StoreFeaturesResponseDto(
                    "v1.0", 10, timestamp1
            );
            StoreFeaturesResponseDto response2 = new StoreFeaturesResponseDto(
                    "v1.0", 10, timestamp2
            );

            assertThat(response1).isNotEqualTo(response2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should contain version in toString")
        void shouldContainVersionInToString() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0.0",
                    10,
                    Instant.now()
            );

            assertThat(response.toString()).contains("v1.0.0");
        }

        @Test
        @DisplayName("Should contain count in toString")
        void shouldContainCountInToString() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    100,
                    Instant.now()
            );

            assertThat(response.toString()).contains("100");
        }
    }

    @Nested
    @DisplayName("Component Access Tests")
    class ComponentAccessTests {

        @Test
        @DisplayName("Should access featureVersion component")
        void shouldAccessFeatureVersionComponent() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v2.5.1",
                    50,
                    Instant.now()
            );

            assertThat(response.featureVersion()).isEqualTo("v2.5.1");
        }

        @Test
        @DisplayName("Should access storedCount component")
        void shouldAccessStoredCountComponent() {
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    999,
                    Instant.now()
            );

            assertThat(response.storedCount()).isEqualTo(999);
        }

        @Test
        @DisplayName("Should access timestamp component")
        void shouldAccessTimestampComponent() {
            Instant now = Instant.now();
            StoreFeaturesResponseDto response = new StoreFeaturesResponseDto(
                    "v1.0",
                    10,
                    now
            );

            assertThat(response.timestamp()).isEqualTo(now);
        }
    }
}
