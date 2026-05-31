package com.gogidix.shared.warehousing.cyclecounting.application.dto;

import com.gogidix.shared.warehousing.cyclecounting.domain.entity.CountSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Count Session
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountSessionDTO {

    private String id;
    private String tenantId;
    private String cycleCountId;
    private String sessionNumber;
    private String counterId;
    private String counterName;
    private CountSession.SessionStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer itemsCounted;
    private Integer itemsDiscrepant;
    private Long durationSeconds;
    private String zoneId;
    private String location;
    private String notes;
    private LocalDateTime createdAt;
}
