package com.gogidix.hr.documentmanagement.domain.event;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCreatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant occurredAt;
    private String documentId;
    private String documentType;
    private String category;
    private String employeeId;
    private Instant timestamp;
    private String parentDocumentId;
    private Integer version;
}
