package com.gogidix.aiservices.aiprediction.infrastructure.persistence;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryPredictionDataSource implements PredictionDataSource {

    private final Map<String, PredictionEntity> executions = new ConcurrentHashMap<>();

    @Override
    public PredictionEntity save(PredictionEntity entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(Instant.now());
        }
        executions.put(entity.getExecutionId(), entity);
        return entity;
    }

    @Override
    public Optional<PredictionEntity> findById(String id) {
        return Optional.ofNullable(executions.get(id));
    }

    @Override
    public void delete(String id) {
        executions.remove(id);
    }
}
