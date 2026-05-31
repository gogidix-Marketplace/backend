package com.gogidix.aiservices.intelligenceanalysisservice.application.mapper;

import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.IntelligenceAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.IntelligenceAnalysis;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper for IntelligenceAnalysis entity and DTOs.
 */
@Component
public class IntelligenceAnalysisMapper {

    /**
     * Convert domain entity to response DTO.
     */
    public IntelligenceAnalysisResponseDto toResponseDto(IntelligenceAnalysis segment) {
        return new IntelligenceAnalysisResponseDto(
                segment.getId(),
                segment.getName(),
                segment.getDescription(),
                segment.getAnalysisType(),
                criteriaToMap(segment.getCriteria()),
                segment.getIntelligenceReportIds() != null ? java.util.Set.copyOf(segment.getIntelligenceReportIds()) : java.util.Set.of(),
                (int) (long) segment.getIntelligenceReportCount(),
                segment.isActive(),
                segment.getTenantId(),
                segment.getCreatedAt(),
                segment.getUpdatedAt(),
                0L // version field - not in domain model yet
        );
    }

    /**
     * Convert AnalysisCriteria to Map for DTO.
     */
    private Map<String, Object> criteriaToMap(AnalysisCriteria criteria) {
        if (criteria == null) {
            return new HashMap<>();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", criteria.getType().name());
        map.put("operator", criteria.getOperator().name());
        map.put("field", criteria.getField());
        map.put("value", criteria.getValue());
        map.put("logicalOperator", criteria.getLogicalOperator().name());
        return map;
    }
}
