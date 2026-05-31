package com.gogidix.aiservices.anomalydetectionservice.interfaces.rest;

import com.gogidix.aiservices.anomalydetectionservice.application.dto.request.DetectionRequest;
import com.gogidix.aiservices.anomalydetectionservice.application.dto.response.DetectionResponse;
import com.gogidix.aiservices.anomalydetectionservice.application.service.AnomalyDetectionService;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.AnomalyDetection;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/anomalies")
@RequiredArgsConstructor
public class AnomalyDetectionController {

    private final AnomalyDetectionService anomalyDetectionService;

    @PostMapping("/detect")
    public ResponseEntity<DetectionResponse> detect(@Valid @RequestBody DetectionRequest request) {
        AnomalyDetection detection = anomalyDetectionService.detectAnomalies(
                request.getDataSource(),
                request.getStartTime(),
                request.getEndTime(),
                request.getSensitivity(),
                request.getAlgorithms()
        );
        return ResponseEntity.ok(DetectionResponse.fromDomain(detection));
    }

    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<AnomalyDetection> getResult(@PathVariable String analysisId) {
        return ResponseEntity.ok(anomalyDetectionService.getAnalysisResult(analysisId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<AnomalyDetection>> getHistory(
            @RequestParam String dataSource,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(anomalyDetectionService.getHistory(dataSource, limit));
    }
}
