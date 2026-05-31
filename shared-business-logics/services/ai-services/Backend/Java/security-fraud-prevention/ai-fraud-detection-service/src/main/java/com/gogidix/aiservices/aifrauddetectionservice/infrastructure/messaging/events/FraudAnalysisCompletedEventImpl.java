package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of FraudAnalysisCompletedEvent for Kafka messaging.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudAnalysisCompletedEventImpl {

    private String eventId;
    private String analysisId;
    private Map<String, Object> result;
    private Double score;
    private LocalDateTime timestamp;
    private String tenantId;

    public String getEventType() {
        return "FRAUD_ANALYSIS_COMPLETED";
    }
}
