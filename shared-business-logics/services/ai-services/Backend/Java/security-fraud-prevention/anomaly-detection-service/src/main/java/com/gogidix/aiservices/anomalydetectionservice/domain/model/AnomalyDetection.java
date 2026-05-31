package com.gogidix.aiservices.anomalydetectionservice.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class AnomalyDetection {
    private String analysisId;
    private String dataSource;
    private Instant startTime;
    private Instant endTime;
    private SensitivityLevel sensitivity;
    private List<DetectionAlgorithm> algorithms;
    private List<Anomaly> anomalies;
    private AnomalySummary summary;
}
