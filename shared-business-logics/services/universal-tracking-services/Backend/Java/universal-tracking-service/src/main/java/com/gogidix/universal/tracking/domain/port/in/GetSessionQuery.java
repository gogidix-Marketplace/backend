package com.gogidix.universal.tracking.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query to retrieve a tracking session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSessionQuery {

    private String sessionId;

    private Boolean includeDetails;

    private Boolean includeEvents;
}
