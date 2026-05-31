package com.gogidix.aiservices.aidocumentprocessingservice.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ProcessingFailedEvent(
        UUID jobId,
        String documentUrl,
        String errorMessage,
        String userId,
        Instant timestamp
) {
    public ProcessingFailedEvent {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }
}
