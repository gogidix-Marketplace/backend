package com.gogidix.hr.globalcompliance.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Upcoming Check DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingCheckDto {
    private String checkId;
    private String checkNumber;
    private String requirementId;
    private String requirementName;
    private LocalDate scheduledDate;
    private String status;
    private String countryCode;
    private Long daysUntilScheduled;
}
