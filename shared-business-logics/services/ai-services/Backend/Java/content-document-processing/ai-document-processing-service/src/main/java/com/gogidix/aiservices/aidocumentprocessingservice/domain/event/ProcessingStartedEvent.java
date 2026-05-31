package com.gogidix.aiservices.aidocumentprocessingservice.domain.event;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.DocumentType;

import java.time.Instant;
import java.util.UUID;

public record ProcessingStartedEvent(
        UUID jobId,
        String documentUrl,
        DocumentType documentType,
        String userId,
        Instant timestamp
) {
    public ProcessingStartedEvent {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }
}
