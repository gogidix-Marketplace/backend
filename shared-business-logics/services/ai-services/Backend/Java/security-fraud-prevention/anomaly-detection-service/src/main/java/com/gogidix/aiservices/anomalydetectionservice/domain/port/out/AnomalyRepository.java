package com.gogidix.aiservices.anomalydetectionservice.domain.port.out;

import com.gogidix.aiservices.anomalydetectionservice.domain.model.Anomaly;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.AnomalyDetection;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.DetectionAlgorithm;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.SensitivityLevel;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AnomalyRepository {
    void saveDetection(AnomalyDetection detection);
    Optional<AnomalyDetection> findById(String analysisId);
    List<AnomalyDetection> findByDataSource(String dataSource, int limit);
}
