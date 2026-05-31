package com.gogidix.universal.tracking.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Query to retrieve tracking events with filters.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEventsQuery {

    private String tenantId;

    private String eventType;

    private String sessionId;

    private String userId;

    private String source;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private List<String> eventTypes;

    private Boolean processed;

    private Integer page;

    private Integer size;

    private String sortBy;

    private String sortDirection;
}
