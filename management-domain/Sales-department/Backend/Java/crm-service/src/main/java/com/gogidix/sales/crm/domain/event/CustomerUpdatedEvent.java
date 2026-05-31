package com.gogidix.sales.crm.domain.event;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdatedEvent {

    private String customerId;
    private String tenantId;
    private String companyName;
    private String segment;
    private String previousStage;
    private String newStage;
    private String churnReason;
    private String updatedBy;
    private String eventType;
    private Instant timestamp;
}
