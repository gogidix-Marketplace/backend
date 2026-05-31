package com.gogidix.foundation.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for log statistics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogStatistics {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long totalCount;
    private Long errorCount;
    private Long warnCount;
    private Long infoCount;
    private Long debugCount;
}
