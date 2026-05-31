package com.gogidix.aiservices.aiinferenceservice.application.dto;

import com.gogidix.aiservices.aiinferenceservice.domain.model.ModelStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ModelStatusResponseDto Tests")
class ModelStatusResponseDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @ParameterizedTest
        @EnumSource(ModelStatus.class)
        @DisplayName("Should create with all ModelStatus values")
        void shouldCreateWithAllStatusValues(ModelStatus status) {
            Instant now = Instant.now();

            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    status,
                    now.minusSeconds(3600),
                    now.plusSeconds(3600)
            );

            assertThat(dto.modelId()).isEqualTo("model-123");
            assertThat(dto.status()).isEqualTo(status);
            assertThat(dto.loadedAt()).isNotNull();
            assertThat(dto.cacheExpiry()).isNotNull();
        }

        @Test
        @DisplayName("Should create with LOADED status")
        void shouldCreateWithLoadedStatus() {
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    Instant.now().minusSeconds(60),
                    Instant.now().plusSeconds(3540)
            );

            assertThat(dto.status()).isEqualTo(ModelStatus.LOADED);
        }

        @Test
        @DisplayName("Should create with LOADING status")
        void shouldCreateWithLoadingStatus() {
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADING,
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );

            assertThat(dto.status()).isEqualTo(ModelStatus.LOADING);
        }

        @Test
        @DisplayName("Should create with UNLOADED status")
        void shouldCreateWithUnloadedStatus() {
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.UNLOADED,
                    Instant.now().minusSeconds(7200),
                    Instant.now()
            );

            assertThat(dto.status()).isEqualTo(ModelStatus.UNLOADED);
        }

        @Test
        @DisplayName("Should create with ERROR status")
        void shouldCreateWithErrorStatus() {
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.ERROR,
                    Instant.now().minusSeconds(60),
                    Instant.now()
            );

            assertThat(dto.status()).isEqualTo(ModelStatus.ERROR);
        }
    }

    @Nested
    @DisplayName("Model ID Tests")
    class ModelIdTests {

        @Test
        @DisplayName("Should store model ID")
        void shouldStoreModelId() {
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-abc-123",
                    ModelStatus.LOADED,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.modelId()).isEqualTo("model-abc-123");
        }

        @Test
        @DisplayName("Should accept UUID as model ID")
        void shouldAcceptUuidAsModelId() {
            String uuid = java.util.UUID.randomUUID().toString();
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    uuid,
                    ModelStatus.LOADED,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.modelId()).isEqualTo(uuid);
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should store loadedAt timestamp")
        void shouldStoreLoadedAtTimestamp() {
            Instant loadedAt = Instant.now().minusSeconds(300);
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    loadedAt,
                    Instant.now()
            );

            assertThat(dto.loadedAt()).isEqualTo(loadedAt);
        }

        @Test
        @DisplayName("Should store cacheExpiry timestamp")
        void shouldStoreCacheExpiryTimestamp() {
            Instant cacheExpiry = Instant.now().plusSeconds(3600);
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    Instant.now(),
                    cacheExpiry
            );

            assertThat(dto.cacheExpiry()).isEqualTo(cacheExpiry);
        }

        @Test
        @DisplayName("Should allow same loadedAt and cacheExpiry")
        void shouldAllowSameLoadedAtAndCacheExpiry() {
            Instant now = Instant.now();
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.ERROR,
                    now,
                    now
            );

            assertThat(dto.loadedAt()).isEqualTo(dto.cacheExpiry());
        }

        @Test
        @DisplayName("Should represent model loaded 5 minutes ago")
        void shouldRepresentModelLoaded5MinutesAgo() {
            Instant fiveMinutesAgo = Instant.now().minusSeconds(300);
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    fiveMinutesAgo,
                    Instant.now().plusSeconds(3300)
            );

            assertThat(dto.loadedAt()).isBefore(Instant.now());
        }

        @Test
        @DisplayName("Should represent model expiring in 1 hour")
        void shouldRepresentModelExpiringIn1Hour() {
            Instant oneHourLater = Instant.now().plusSeconds(3600);
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    Instant.now(),
                    oneHourLater
            );

            assertThat(dto.cacheExpiry()).isAfter(Instant.now());
        }
    }

    @Nested
    @DisplayName("ModelStatus Lifecycle Tests")
    class ModelStatusLifecycleTests {

        @Test
        @DisplayName("Should represent LOADING -> LOADED transition")
        void shouldRepresentLoadingToLoadedTransition() {
            ModelStatusResponseDto loadingDto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADING,
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );

            ModelStatusResponseDto loadedDto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    loadingDto.loadedAt(),
                    Instant.now().plusSeconds(3600)
            );

            assertThat(loadingDto.status()).isEqualTo(ModelStatus.LOADING);
            assertThat(loadedDto.status()).isEqualTo(ModelStatus.LOADED);
        }

        @Test
        @DisplayName("Should represent LOADED -> UNLOADED transition")
        void shouldRepresentLoadedToUnloadedTransition() {
            ModelStatusResponseDto loadedDto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    Instant.now().minusSeconds(3600),
                    Instant.now()
            );

            ModelStatusResponseDto unloadedDto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.UNLOADED,
                    loadedDto.loadedAt(),
                    Instant.now()
            );

            assertThat(loadedDto.status()).isEqualTo(ModelStatus.LOADED);
            assertThat(unloadedDto.status()).isEqualTo(ModelStatus.UNLOADED);
        }

        @Test
        @DisplayName("Should represent any status -> ERROR transition")
        void shouldRepresentAnyToErrorTransition() {
            ModelStatusResponseDto errorDto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.ERROR,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(errorDto.status()).isEqualTo(ModelStatus.ERROR);
        }
    }

    @Nested
    @DisplayName("Cache Status Tests")
    class CacheStatusTests {

        @Test
        @DisplayName("Should represent fresh model (not expired)")
        void shouldRepresentFreshModel() {
            Instant now = Instant.now();
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    now.minusSeconds(60),
                    now.plusSeconds(3540)
            );

            assertThat(dto.cacheExpiry()).isAfter(now);
            assertThat(dto.status()).isEqualTo(ModelStatus.LOADED);
        }

        @Test
        @DisplayName("Should represent expired model")
        void shouldRepresentExpiredModel() {
            Instant now = Instant.now();
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.UNLOADED,
                    now.minusSeconds(7200),
                    now.minusSeconds(1)
            );

            assertThat(dto.cacheExpiry()).isBefore(now);
            assertThat(dto.status()).isEqualTo(ModelStatus.UNLOADED);
        }

        @Test
        @DisplayName("Should represent model about to expire")
        void shouldRepresentModelAboutToExpire() {
            Instant now = Instant.now();
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    now.minusSeconds(3599),
                    now.plusSeconds(1)
            );

            assertThat(dto.cacheExpiry()).isAfter(now);
            assertThat(dto.cacheExpiry()).isBefore(now.plusSeconds(10));
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            Instant now = Instant.now();

            ModelStatusResponseDto dto1 = new ModelStatusResponseDto(
                    "model-1", ModelStatus.LOADED, now, now.plusSeconds(3600)
            );
            ModelStatusResponseDto dto2 = new ModelStatusResponseDto(
                    "model-1", ModelStatus.LOADED, now, now.plusSeconds(3600)
            );

            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when model IDs differ")
        void shouldNotBeEqualWhenModelIdsDiffer() {
            Instant now = Instant.now();

            ModelStatusResponseDto dto1 = new ModelStatusResponseDto(
                    "model-1", ModelStatus.LOADED, now, now.plusSeconds(3600)
            );
            ModelStatusResponseDto dto2 = new ModelStatusResponseDto(
                    "model-2", ModelStatus.LOADED, now, now.plusSeconds(3600)
            );

            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("Should not be equal when status differs")
        void shouldNotBeEqualWhenStatusDiffers() {
            Instant now = Instant.now();

            ModelStatusResponseDto dto1 = new ModelStatusResponseDto(
                    "model-1", ModelStatus.LOADED, now, now.plusSeconds(3600)
            );
            ModelStatusResponseDto dto2 = new ModelStatusResponseDto(
                    "model-1", ModelStatus.LOADING, now, now.plusSeconds(3600)
            );

            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("Should not be equal when loadedAt differs")
        void shouldNotBeEqualWhenLoadedAtDiffers() {
            Instant now = Instant.now();

            ModelStatusResponseDto dto1 = new ModelStatusResponseDto(
                    "model-1", ModelStatus.LOADED, now.minusSeconds(60), now.plusSeconds(3600)
            );
            ModelStatusResponseDto dto2 = new ModelStatusResponseDto(
                    "model-1", ModelStatus.LOADED, now.minusSeconds(30), now.plusSeconds(3600)
            );

            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("Should not be equal when cacheExpiry differs")
        void shouldNotBeEqualWhenCacheExpiryDiffers() {
            Instant now = Instant.now();

            ModelStatusResponseDto dto1 = new ModelStatusResponseDto(
                    "model-1", ModelStatus.LOADED, now, now.plusSeconds(3600)
            );
            ModelStatusResponseDto dto2 = new ModelStatusResponseDto(
                    "model-1", ModelStatus.LOADED, now, now.plusSeconds(7200)
            );

            assertThat(dto1).isNotEqualTo(dto2);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in model ID")
        void shouldHandleUnicodeInModelId() {
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "モデル-123",
                    ModelStatus.LOADED,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.modelId()).isEqualTo("モデル-123");
        }

        @Test
        @DisplayName("Should handle epoch timestamps")
        void shouldHandleEpochTimestamps() {
            Instant epoch = Instant.EPOCH;

            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.UNLOADED,
                    epoch,
                    epoch
            );

            assertThat(dto.loadedAt()).isEqualTo(epoch);
            assertThat(dto.cacheExpiry()).isEqualTo(epoch);
        }
    }

    @Nested
    @DisplayName("Component Access Tests")
    class ComponentAccessTests {

        @Test
        @DisplayName("Should access modelId component")
        void shouldAccessModelIdComponent() {
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-xyz",
                    ModelStatus.LOADED,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.modelId()).isEqualTo("model-xyz");
        }

        @Test
        @DisplayName("Should access status component")
        void shouldAccessStatusComponent() {
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.ERROR,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.status()).isEqualTo(ModelStatus.ERROR);
        }

        @Test
        @DisplayName("Should access loadedAt component")
        void shouldAccessLoadedAtComponent() {
            Instant loadedAt = Instant.now().minusSeconds(120);
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    loadedAt,
                    Instant.now()
            );

            assertThat(dto.loadedAt()).isEqualTo(loadedAt);
        }

        @Test
        @DisplayName("Should access cacheExpiry component")
        void shouldAccessCacheExpiryComponent() {
            Instant cacheExpiry = Instant.now().plusSeconds(7200);
            ModelStatusResponseDto dto = new ModelStatusResponseDto(
                    "model-123",
                    ModelStatus.LOADED,
                    Instant.now(),
                    cacheExpiry
            );

            assertThat(dto.cacheExpiry()).isEqualTo(cacheExpiry);
        }
    }
}
