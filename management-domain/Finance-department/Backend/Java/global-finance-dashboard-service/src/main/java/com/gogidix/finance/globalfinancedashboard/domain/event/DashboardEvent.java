package com.gogidix.finance.globalfinancedashboard.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant timestamp;
}
