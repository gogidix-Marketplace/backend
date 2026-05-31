package com.gogidix.aiservices.anomalydetectionservice.application.service;

import com.gogidix.aiservices.anomalydetectionservice.domain.model.*;
import com.gogidix.aiservices.anomalydetectionservice.domain.port.out.AnomalyRepository;
import com.gogidix.aiservices.anomalydetectionservice.domain.port.out.MlDetectionPort;
import com.gogidix.aiservices.anomalydetectionservice.domain.policy.AnomalyDetectionPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnomalyDetectionService {

    private final AnomalyRepository anomalyRepository;
    private final MlDetectionPort mlDetectionPort;
    private final AnomalyDetectionPolicy policy;

    public AnomalyDetection detectAnomalies(String dataSource, Instant start, Instant end,
                                                 SensitivityLevel sensitivity,
                                                 List<DetectionAlgorithm> algorithms) {
        policy.validateTimeRange(start, end);
        policy.validateAlgorithms(algorithms);

        String analysisId = UUID.randomUUID().toString();

        // Run anomaly detection
        List<Anomaly> anomalies = mlDetectionPort.detect(
                dataSource, start, end, sensitivity, algorithms
        );

        // Calculate summary
        AnomalySummary summary = calculateSummary(anomalies);

        AnomalyDetection detection = AnomalyDetection.builder()
                .analysisId(analysisId)
                .dataSource(dataSource)
                .startTime(start)
                .endTime(end)
                .sensitivity(sensitivity)
                .algorithms(algorithms)
                .anomalies(anomalies)
                .summary(summary)
                .build();

        anomalyRepository.saveDetection(detection);

        return detection;
    }

    public AnomalyDetection getAnalysisResult(String analysisId) {
        return anomalyRepository.findById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("Analysis not found: " + analysisId));
    }

    public List<AnomalyDetection> getHistory(String dataSource, int limit) {
        return anomalyRepository.findByDataSource(dataSource, limit);
    }

    private AnomalySummary calculateSummary(List<Anomaly> anomalies) {
        int high = 0, medium = 0, low = 0;
        double totalScore = 0;

        for (Anomaly anomaly : anomalies) {
            if (anomaly.getScore() >= 0.8) high++;
            else if (anomaly.getScore() >= 0.5) medium++;
            else low++;
            totalScore += anomaly.getScore();
        }

        return AnomalySummary.builder()
                .totalAnomalies(anomalies.size())
                .highSeverity(high)
                .mediumSeverity(medium)
                .lowSeverity(low)
                .averageScore(anomalies.isEmpty() ? 0 : totalScore / anomalies.size())
                .build();
    }
}
