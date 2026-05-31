package com.gogidix.aiservices.aiprediction.infrastructure.persistence;

import com.gogidix.aiservices.aiprediction.domain.aggregate.PredictionExecution;
import com.gogidix.aiservices.aiprediction.domain.model.PredictionResult;
import com.gogidix.aiservices.aiprediction.domain.port.out.PredictionRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public class PredictionRepositoryImpl implements PredictionRepository {

    private final PredictionDataSource dataSource;

    public PredictionRepositoryImpl(PredictionDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public PredictionExecution save(PredictionExecution execution) {
        PredictionEntity entity = toEntity(execution);
        dataSource.save(entity);
        return execution;
    }

    @Override
    public Optional<PredictionExecution> findById(String executionId) {
        return dataSource.findById(executionId).map(this::toDomain);
    }

    @Override
    public void delete(String executionId) {
        dataSource.delete(executionId);
    }

    private PredictionEntity toEntity(PredictionExecution execution) {
        PredictionEntity entity = new PredictionEntity();
        entity.setExecutionId(execution.getExecutionId());
        entity.setModelId(execution.getModelId());
        entity.setModelVersion(execution.getModelVersion());
        entity.setInputData(execution.getInputData());
        entity.setStatus(execution.getStatus());
        entity.setCreatedAt(execution.getCreatedAt());
        entity.setStartedAt(execution.getStartedAt());
        entity.setCompletedAt(execution.getCompletedAt());
        entity.setTimeout(execution.getTimeout());
        entity.setErrorMessage(execution.getErrorMessage());
        return entity;
    }

    private PredictionExecution toDomain(PredictionEntity entity) {
        PredictionExecution execution = PredictionExecution.restore(
                entity.getExecutionId(),
                entity.getModelId(),
                entity.getModelVersion(),
                entity.getInputData(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getStartedAt(),
                entity.getCompletedAt(),
                entity.getTimeout(),
                entity.getErrorMessage()
        );
        return execution;
    }
}
