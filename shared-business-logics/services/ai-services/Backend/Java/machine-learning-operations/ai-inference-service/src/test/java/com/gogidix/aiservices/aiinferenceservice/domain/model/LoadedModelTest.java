package com.gogidix.aiservices.aiinferenceservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("LoadedModel Domain Entity Tests")
class LoadedModelTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String MODEL_ID = "model-abc";

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create model with tenantId and modelId")
        void shouldCreateWithTenantIdAndModelId() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model.getId()).isNotNull();
            assertThat(model.getTenantId()).isEqualTo(TENANT_ID);
            assertThat(model.getModelId()).isEqualTo(MODEL_ID);
        }

        @Test
        @DisplayName("Should initialize status as LOADING")
        void shouldInitializeStatusAsLoading() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model.getStatus()).isEqualTo(ModelStatus.LOADING);
        }

        @Test
        @DisplayName("Should set loadedAt timestamp on creation")
        void shouldSetLoadedAtOnCreation() {
            Instant before = Instant.now();
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            Instant after = Instant.now();

            assertThat(model.getLoadedAt()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should set cache expiry to 1 hour from now")
        void shouldSetCacheExpiryToOneHour() {
            Instant before = Instant.now().plusSeconds(3599);
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            Instant after = Instant.now().plusSeconds(3601);

            assertThat(model.getCacheExpiry()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should generate unique ID for each model")
        void shouldGenerateUniqueIdForEachModel() {
            LoadedModel model1 = new LoadedModel(TENANT_ID, MODEL_ID);
            LoadedModel model2 = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model1.getId()).isNotEqualTo(model2.getId());
        }

        @Test
        @DisplayName("Should throw when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThatThrownBy(() -> new LoadedModel(null, MODEL_ID))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("tenantId");
        }

        @Test
        @DisplayName("Should throw when modelId is null")
        void shouldThrowWhenModelIdIsNull() {
            assertThatThrownBy(() -> new LoadedModel(TENANT_ID, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("modelId");
        }
    }

    @Nested
    @DisplayName("Status Management Tests")
    class StatusManagementTests {

        @Test
        @DisplayName("Should mark model as LOADED")
        void shouldMarkAsLoaded() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            model.markAsLoaded();

            assertThat(model.getStatus()).isEqualTo(ModelStatus.LOADED);
        }

        @Test
        @DisplayName("Should mark model as UNLOADED")
        void shouldMarkAsUnloaded() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            model.markAsLoaded();

            model.markAsUnloaded();

            assertThat(model.getStatus()).isEqualTo(ModelStatus.UNLOADED);
        }

        @Test
        @DisplayName("Should mark model as ERROR")
        void shouldMarkAsError() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            model.markAsError();

            assertThat(model.getStatus()).isEqualTo(ModelStatus.ERROR);
        }

        @Test
        @DisplayName("Should support status transition from LOADING to LOADED")
        void shouldSupportLoadingToLoadedTransition() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            assertThat(model.getStatus()).isEqualTo(ModelStatus.LOADING);

            model.markAsLoaded();

            assertThat(model.getStatus()).isEqualTo(ModelStatus.LOADED);
        }

        @Test
        @DisplayName("Should support status transition from LOADED to UNLOADED")
        void shouldSupportLoadedToUnloadedTransition() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            model.markAsLoaded();

            model.markAsUnloaded();

            assertThat(model.getStatus()).isEqualTo(ModelStatus.UNLOADED);
        }

        @Test
        @DisplayName("Should support status transition to ERROR from any state")
        void shouldSupportTransitionToError() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            model.markAsError();

            assertThat(model.getStatus()).isEqualTo(ModelStatus.ERROR);
        }
    }

    @Nested
    @DisplayName("Cache Expiry Tests")
    class CacheExpiryTests {

        @Test
        @DisplayName("Should not be expired immediately after creation")
        void shouldNotBeExpiredImmediately() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model.isExpired()).isFalse();
        }

        @Test
        @DisplayName("Should be expired when cache expiry time has passed")
        void shouldBeExpiredWhenTimeHasPassed() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            model.setCacheExpiry(Instant.now().minusSeconds(1));

            assertThat(model.isExpired()).isTrue();
        }

        @Test
        @DisplayName("Should not be expired when cache expiry time is in future")
        void shouldNotBeExpiredWhenExpiryInFuture() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            model.setCacheExpiry(Instant.now().plusSeconds(3600));

            assertThat(model.isExpired()).isFalse();
        }

        @Test
        @DisplayName("Should check expiry based on current time")
        void shouldCheckExpiryBasedOnCurrentTime() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            Instant cacheExpiry = model.getCacheExpiry();

            // Immediately after creation, should not be expired
            assertThat(Instant.now()).isBefore(cacheExpiry);
            assertThat(model.isExpired()).isFalse();
        }
    }

    @Nested
    @DisplayName("Lifecycle Tests")
    class LifecycleTests {

        @Test
        @DisplayName("Should follow full lifecycle: LOADING -> LOADED -> UNLOADED")
        void shouldFollowFullLifecycle() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model.getStatus()).isEqualTo(ModelStatus.LOADING);

            model.markAsLoaded();
            assertThat(model.getStatus()).isEqualTo(ModelStatus.LOADED);

            model.markAsUnloaded();
            assertThat(model.getStatus()).isEqualTo(ModelStatus.UNLOADED);
        }

        @Test
        @DisplayName("Should follow error lifecycle: LOADING -> ERROR")
        void shouldFollowErrorLifecycle() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model.getStatus()).isEqualTo(ModelStatus.LOADING);

            model.markAsError();
            assertThat(model.getStatus()).isEqualTo(ModelStatus.ERROR);
        }

        @Test
        @DisplayName("Should allow transition from ERROR back to LOADING")
        void shouldAllowTransitionFromErrorToLoading() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            model.markAsError();
            assertThat(model.getStatus()).isEqualTo(ModelStatus.ERROR);

            model.setStatus(ModelStatus.LOADING);
            assertThat(model.getStatus()).isEqualTo(ModelStatus.LOADING);
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should return tenant ID")
        void shouldReturnTenantId() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model.getTenantId()).isEqualTo(TENANT_ID);
        }

        @Test
        @DisplayName("Should return model ID")
        void shouldReturnModelId() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model.getModelId()).isEqualTo(MODEL_ID);
        }

        @Test
        @DisplayName("Should return status")
        void shouldReturnStatus() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model.getStatus()).isEqualTo(ModelStatus.LOADING);
        }

        @Test
        @DisplayName("Should return loaded at timestamp")
        void shouldReturnLoadedAt() {
            Instant before = Instant.now();
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            Instant after = Instant.now();

            assertThat(model.getLoadedAt()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should return cache expiry")
        void shouldReturnCacheExpiry() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            assertThat(model.getCacheExpiry()).isAfter(Instant.now());
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        @Test
        @DisplayName("Should set tenant ID")
        void shouldSetTenantId() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            model.setTenantId("new-tenant");

            assertThat(model.getTenantId()).isEqualTo("new-tenant");
        }

        @Test
        @DisplayName("Should set model ID")
        void shouldSetModelId() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            model.setModelId("new-model");

            assertThat(model.getModelId()).isEqualTo("new-model");
        }

        @Test
        @DisplayName("Should set status")
        void shouldSetStatus() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);

            model.setStatus(ModelStatus.ERROR);

            assertThat(model.getStatus()).isEqualTo(ModelStatus.ERROR);
        }

        @Test
        @DisplayName("Should set loaded at timestamp")
        void shouldSetLoadedAt() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            Instant newTime = Instant.now().minusSeconds(3600);

            model.setLoadedAt(newTime);

            assertThat(model.getLoadedAt()).isEqualTo(newTime);
        }

        @Test
        @DisplayName("Should set cache expiry")
        void shouldSetCacheExpiry() {
            LoadedModel model = new LoadedModel(TENANT_ID, MODEL_ID);
            Instant newExpiry = Instant.now().plusSeconds(7200);

            model.setCacheExpiry(newExpiry);

            assertThat(model.getCacheExpiry()).isEqualTo(newExpiry);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in tenant ID")
        void shouldHandleUnicodeInTenantId() {
            LoadedModel model = new LoadedModel("テナント-123", MODEL_ID);

            assertThat(model.getTenantId()).isEqualTo("テナント-123");
        }

        @Test
        @DisplayName("Should handle unicode in model ID")
        void shouldHandleUnicodeInModelId() {
            LoadedModel model = new LoadedModel(TENANT_ID, "モデル-abc");

            assertThat(model.getModelId()).isEqualTo("モデル-abc");
        }

        @Test
        @DisplayName("Should handle very long IDs")
        void shouldHandleVeryLongIds() {
            String longId = "a".repeat(1000);
            LoadedModel model = new LoadedModel(longId, longId);

            assertThat(model.getTenantId()).hasSize(1000);
            assertThat(model.getModelId()).hasSize(1000);
        }
    }
}
