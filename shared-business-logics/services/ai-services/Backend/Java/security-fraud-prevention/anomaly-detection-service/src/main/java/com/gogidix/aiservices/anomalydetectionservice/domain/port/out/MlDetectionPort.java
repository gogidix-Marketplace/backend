package com.gogidix.aiservices.anomalydetectionservice.domain.port.out;

import com.gogidix.aiservices.anomalydetectionservice.domain.model.Anomaly;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.DetectionAlgorithm;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.SensitivityLevel;

import java.time.Instant;
import java.util.List;

public interface MlDetectionPort {
    List<Anomaly> detect(String dataSource, Instant start, Instant end,
                           SensitivityLevel sensitivity, List<DetectionAlgorithm> algorithms);
}
