package com.gogidix.shared.audit.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for audit statistics responses.
 * Contains aggregated audit metrics and analytics.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditStatisticsDTO {
    
    private long totalEvents;
    private LocalDateTime generatedAt;
    
}