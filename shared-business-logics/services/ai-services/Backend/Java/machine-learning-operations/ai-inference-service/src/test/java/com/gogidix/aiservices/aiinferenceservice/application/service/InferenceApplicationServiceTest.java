package com.gogidix.aiservices.aiinferenceservice.application.service;

import com.gogidix.aiservices.aiinferenceservice.application.dto.InferenceRequestDto;
import com.gogidix.aiservices.aiinferenceservice.application.dto.InferenceResponseDto;
import com.gogidix.aiservices.aiinferenceservice.domain.model.InferenceResult;
import com.gogidix.aiservices.aiinferenceservice.domain.model.LoadedModel;
import com.gogidix.aiservices.aiinferenceservice.domain.port.out.InferenceResultRepositoryPort;
import com.gogidix.aiservices.aiinferenceservice.domain.port.out.LoadedModelRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for InferenceApplicationService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("InferenceApplicationService Tests")
class InferenceApplicationServiceTest {

    @Mock
    private InferenceResultRepositoryPort resultRepository;

    @Mock
    private LoadedModelRepositoryPort loadedModelRepository;

    private InferenceApplicationService service;

    @BeforeEach
    void setUp() {
        service = new InferenceApplicationService(resultRepository, loadedModelRepository);
    }

    @Nested
    @DisplayName("Run Inference Tests")
    class RunInferenceTests {

        @Test
        @DisplayName("Should run inference successfully")
        void shouldRunInferenceSuccessfully() {
            InferenceRequestDto request = new InferenceRequestDto(
                    "model-001",
                    Map.of("key", "value"),
                    false,
                    30
            );

            LoadedModel loadedModel = new LoadedModel("tenant-123", "model-001");
            loadedModel.markAsLoaded();

            when(loadedModelRepository.findByModelIdAndTenantId(anyString(), anyString()))
                    .thenReturn(Optional.of(loadedModel));
            when(loadedModelRepository.save(any(LoadedModel.class))).thenReturn(loadedModel);
            when(resultRepository.save(any(InferenceResult.class))).thenAnswer(inv -> inv.getArgument(0));

            InferenceResponseDto result = service.runInference(request);

            assertNotNull(result);
            assertEquals("model-001", result.modelId());
            assertNotNull(result.predictions());
            assertTrue(result.latencyMs() >= 0);
        }

        @Test
        @DisplayName("Should load model if not cached")
        void shouldLoadModelIfNotCached() {
            InferenceRequestDto request = new InferenceRequestDto(
                    "model-001",
                    Map.of("key", "value"),
                    false,
                    30
            );

            LoadedModel loadedModel = new LoadedModel("tenant-123", "model-001");
            loadedModel.markAsLoaded();

            when(loadedModelRepository.findByModelIdAndTenantId(anyString(), anyString()))
                    .thenReturn(Optional.empty());
            when(loadedModelRepository.save(any(LoadedModel.class))).thenReturn(loadedModel);
            when(resultRepository.save(any(InferenceResult.class))).thenAnswer(inv -> inv.getArgument(0));

            InferenceResponseDto result = service.runInference(request);

            assertNotNull(result);
            verify(loadedModelRepository).save(any(LoadedModel.class));
        }
    }

    @Nested
    @DisplayName("Get Model Status Tests")
    class GetModelStatusTests {

        @Test
        @DisplayName("Should return model status when model is loaded")
        void shouldReturnModelStatusWhenModelIsLoaded() {
            LoadedModel loadedModel = new LoadedModel("tenant-123", "model-001");
            loadedModel.markAsLoaded();

            when(loadedModelRepository.findByModelIdAndTenantId("model-001", "tenant-123"))
                    .thenReturn(Optional.of(loadedModel));

            var result = service.getModelStatus("model-001", "tenant-123");

            assertNotNull(result);
            assertEquals("model-001", result.modelId());
        }

        @Test
        @DisplayName("Should return unloaded status when model not found")
        void shouldReturnUnloadedStatusWhenModelNotFound() {
            when(loadedModelRepository.findByModelIdAndTenantId("model-001", "tenant-123"))
                    .thenReturn(Optional.empty());

            var result = service.getModelStatus("model-001", "tenant-123");

            assertNotNull(result);
            assertEquals(com.gogidix.aiservices.aiinferenceservice.domain.model.ModelStatus.UNLOADED, result.status());
        }
    }

    @Nested
    @DisplayName("Unload Model Tests")
    class UnloadModelTests {

        @Test
        @DisplayName("Should unload model successfully")
        void shouldUnloadModelSuccessfully() {
            service.unloadModel("model-001", "tenant-123");

            verify(loadedModelRepository).deleteByModelIdAndTenantId("model-001", "tenant-123");
        }
    }
}
