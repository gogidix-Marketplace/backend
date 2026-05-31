package com.gogidix.sales.crm.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactCreatedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant occurredAt;
    private String correlationId;
    private String contactId;
    private String customerId;
    private String fullName;
    private String email;
    private String title;
    private String contactType;

    public static ContactCreatedEvent create(String contactId, String tenantId, String customerId,
                                               String fullName, String email, String title,
                                               String contactType, String tenantId2) {
        return ContactCreatedEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .contactId(contactId)
            .tenantId(tenantId)
            .customerId(customerId)
            .fullName(fullName)
            .email(email)
            .title(title)
            .contactType(contactType)
            .eventType("CONTACT_CREATED")
            .occurredAt(Instant.now())
            .build();
    }
}
