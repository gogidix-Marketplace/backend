package com.gogidix.shared.warehousing.returns.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnUpdatedEvent {

    private String eventId;
    private String returnId;
    private String rmaNumber;
    private String status;
    private String changeType;
    private String tenantId;
    private LocalDateTime timestamp;

    public static ReturnUpdatedEventBuilder builder() {
        return new ReturnUpdatedEventBuilder()
            .eventId(java.util.UUID.randomUUID().toString())
            .timestamp(LocalDateTime.now());
    }
}
