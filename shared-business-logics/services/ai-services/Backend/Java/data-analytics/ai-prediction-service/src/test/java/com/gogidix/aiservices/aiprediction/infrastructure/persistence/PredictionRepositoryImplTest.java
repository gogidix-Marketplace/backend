package com.gogidix.aiservices.aiprediction.infrastructure.persistence;

import com.gogidix.aiservices.aiprediction.domain.aggregate.PredictionExecution;
import com.gogidix.aiservices.aiprediction.domain.model.PredictionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Prediction Repository Infrastructure Tests")
class PredictionRepositoryImplTest {

    @Mock
    private PredictionDataSource dataSource;

    @InjectMocks
    private PredictionRepositoryImpl repository;

    private static final String MODEL_ID = "model-abc";
    private static final String EXECUTION_ID = "exec-123";

    @Nested
    @DisplayName("Save Operations")
    class SaveTests {

        @Test
        @DisplayName("Should save prediction execution")
        void shouldSaveExecution() {
            PredictionExecution execution = PredictionExecution.create(MODEL_ID, "v1.0", Map.of());

            when(dataSource.save(any(PredictionEntity.class))).thenReturn(new PredictionEntity());

            PredictionExecution saved = repository.save(execution);

            assertThat(saved).isNotNull();
            verify(dataSource).save(any(PredictionEntity.class));
        }
    }

    @Nested
    @DisplayName("Find Operations")
    class FindTests {

        @Test
        @DisplayName("Should find execution by ID")
        void shouldFindById() {
            PredictionEntity entity = createPredictionEntity();
            when(dataSource.findById(EXECUTION_ID)).thenReturn(Optional.of(entity));

            Optional<PredictionExecution> result = repository.findById(EXECUTION_ID);

            assertThat(result).isPresent();
            verify(dataSource).findById(EXECUTION_ID);
        }

        @Test
        @DisplayName("Should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            when(dataSource.findById(EXECUTION_ID)).thenReturn(Optional.empty());

            Optional<PredictionExecution> result = repository.findById(EXECUTION_ID);

            assertThat(result).isEmpty();
        }
    }

    private PredictionEntity createPredictionEntity() {
        PredictionEntity entity = new PredictionEntity();
        entity.setExecutionId(EXECUTION_ID);
        entity.setModelId(MODEL_ID);
        entity.setModelVersion("v1.0");
        entity.setStatus(PredictionExecution.Status.PENDING);
        entity.setCreatedAt(Instant.now());
        return entity;
    }
}
