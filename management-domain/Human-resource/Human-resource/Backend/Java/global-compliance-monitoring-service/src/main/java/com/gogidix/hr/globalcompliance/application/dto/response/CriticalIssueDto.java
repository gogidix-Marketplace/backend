package com.gogidix.hr.globalcompliance.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Critical Issue DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriticalIssueDto {
    private String issueId;
    private String issueNumber;
    private String title;
    private String severity;
    private String status;
    private String countryCode;
    private LocalDate identifiedDate;
    private LocalDate dueDate;
    private String assignedTo;
    private String assignedToName;
    private Long daysUntilDue;
    private Boolean requiresImmediateAttention;
}
