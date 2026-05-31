package com.gogidix.aiservices.anomalydetectionservice.application.dto.response;

import com.gogidix.aiservices.anomalydetectionservice.domain.model.Anomaly;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.AnomalyDetection;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.AnomalySummary;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class DetectionResponse {
    private String analysisId;
    private String dataSource;
    private Instant startTime;
    private Instant endTime;
    private String sensitivity;
    private List<String> algorithms;
    private List<Anomaly> anomalies;
    private AnomalySummary summary;

    public static DetectionResponse fromDomain(AnomalyDetection detection) {
        DetectionResponse response = new DetectionResponse();
        response.setAnalysisId(detection.getAnalysisId());
        response.setDataSource(detection.getDataSource());
        response.setStartTime(detection.getStartTime());
        response.setEndTime(detection.getEndTime());
        response.setSensitivity(detection.getSensitivity().name());
        response.setAlgorithms(detection.getAlgorithms().stream().map(Enum::name).toList());
        response.setAnomalies(detection.getAnomalies());
        response.setSummary(detection.getSummary());
        return response;
    }
}
