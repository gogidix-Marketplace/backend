package com.gogidix.aiservices.aiprediction.domain.port.out;

import com.gogidix.aiservices.aiprediction.domain.aggregate.PredictionExecution;

import java.util.Optional;

public interface PredictionRepository {
    PredictionExecution save(PredictionExecution execution);
    Optional<PredictionExecution> findById(String executionId);
    void delete(String executionId);
}
