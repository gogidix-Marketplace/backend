package com.gogidix.aiservices.anomalydetectionservice.application.dto.request;

import com.gogidix.aiservices.anomalydetectionservice.domain.model.DetectionAlgorithm;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.SensitivityLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class DetectionRequest {
    @NotBlank
    private String dataSource;

    private Instant startTime;
    private Instant endTime;
    private SensitivityLevel sensitivity = SensitivityLevel.MEDIUM;
    private List<DetectionAlgorithm> algorithms;
}
