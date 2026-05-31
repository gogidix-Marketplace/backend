package com.gogidix.sales.onboarding.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadedEvent {

    private String onboardingId;
    private String tenantId;
    private String customerId;
    private String documentChecklistId;
    private String documentName;
    private String documentType;
    private String documentUrl;
    private Long fileSizeBytes;
    private String uploadedBy;
    private String eventId;
    private String eventType;
    private Instant timestamp;
}
