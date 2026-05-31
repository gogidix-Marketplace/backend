package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsightResponseDto {
    private String id;
    private String title;
    private String summary;
    private String description;
    private String insightType;
    private String impactLevel;
    private BigDecimal confidenceScore;
    private String sentiment;
    private String entityCode;
    private String entityType;
    private String periodId;
    private String status;
    private String source;
    private String createdBy;
    private Boolean isVerified;
    private Boolean isAiGenerated;
    private List<String> tags;
    private Instant detectedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
