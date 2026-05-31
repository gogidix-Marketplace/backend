package com.gogidix.shared.warehousing.quality.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityCheckCompletedEvent {

    private String eventId;
    private String qualityCheckId;
    private String referenceId;
    private String referenceType;
    private Boolean passed;
    private Integer qualityScore;
    private String tenantId;
    private LocalDateTime timestamp;

    public static QualityCheckCompletedEventBuilder builder() {
        return new QualityCheckCompletedEventBuilder()
            .eventId(java.util.UUID.randomUUID().toString())
            .timestamp(LocalDateTime.now());
    }
}
