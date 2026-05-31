package com.gogidix.aiservices.aifrauddetectionservice.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for compliance report responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceReportResponseDTO {

    private String reportId;

    private String reportType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime periodStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime periodEnd;

    private String status;

    private Double complianceScore;

    private Map<String, Object> thresholdResults;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime generatedAt;
}
