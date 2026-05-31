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
public class TerritoryCreatedEvent {

    private String territoryId;
    private String tenantId;
    private String code;
    private String name;
    private String type;
    private String eventType;
    private Instant timestamp;
    private String eventId;
}
