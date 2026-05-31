package com.gogidix.sales.territory.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerritoryAssignedEvent {

    private String assignmentId;
    private String tenantId;
    private String territoryId;
    private String salesRepresentativeId;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
