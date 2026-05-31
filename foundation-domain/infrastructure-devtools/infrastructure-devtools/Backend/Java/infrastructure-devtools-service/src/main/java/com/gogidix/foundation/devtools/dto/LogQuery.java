package com.gogidix.foundation.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for log queries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogQuery {

    private List<String> levels;
    private List<String> sources;
    private String search;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String userId;
    private String sessionId;
    private String requestId;
}
