package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInsightRequest {
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
    private String source;
    private String createdBy;
    private Boolean isAiGenerated;
    private List<String> tags;
    private Map<String, Object> attributes;
}
