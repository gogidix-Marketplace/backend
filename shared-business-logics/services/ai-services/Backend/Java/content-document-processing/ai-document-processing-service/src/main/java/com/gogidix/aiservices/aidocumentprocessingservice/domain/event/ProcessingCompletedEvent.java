package com.gogidix.aiservices.aidocumentprocessingservice.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ProcessingCompletedEvent(
        UUID jobId,
        String documentUrl,
        int fieldsCount,
        int pagesProcessed,
        double confidence,
        String userId,
        Instant timestamp
) {
    public ProcessingCompletedEvent {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }
}
