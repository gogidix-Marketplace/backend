package com.gogidix.sales.leadmanagement.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadCreatedEvent {

    private String leadId;
    private String tenantId;
    private String firstName;
    private String lastName;
    private String email;
    private String company;
    private String source;
    private String ownerId;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
