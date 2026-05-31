package com.gogidix.aiservices.aiprediction.infrastructure.persistence;

import java.util.Optional;

public interface PredictionDataSource {
    PredictionEntity save(PredictionEntity entity);
    Optional<PredictionEntity> findById(String id);
    void delete(String id);
}
