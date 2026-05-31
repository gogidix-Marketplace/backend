package com.gogidix.aiservices.intelligenceanalysisservice.domain.port.in;

import com.gogidix.aiservices.intelligenceanalysisservice.application.command.AddIntelligenceReportsToAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.AnalyzeAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.CreateAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.DeleteAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.RemoveIntelligenceReportsFromAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.UpdateAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.IntelligenceAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.AnalysisAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;

/**
 * Input port for customer segment operations.
 * This interface defines the contract for the application service layer.
 */
public interface IntelligenceAnalysisServicePort {

    /**
     * Create a new customer segment.
     */
    IntelligenceAnalysisResponseDto createAnalysis(CreateAnalysisCommand command);

    /**
     * Update an existing customer segment.
     */
    IntelligenceAnalysisResponseDto updateAnalysis(UpdateAnalysisCommand command);

    /**
     * Delete a customer segment.
     */
    void deleteAnalysis(String segmentId, String tenantId, String userId);

    /**
     * Find a segment by ID.
     */
    IntelligenceAnalysisResponseDto getAnalysisById(String segmentId, String tenantId);

    /**
     * Find segments by tenant with filtering.
     */
    PagedResponseDto<IntelligenceAnalysisResponseDto> getAnalysissByTenant(
            String tenantId,
            AnalysisType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    /**
     * Add customers to a segment.
     */
    IntelligenceAnalysisResponseDto addIntelligenceReportsToAnalysis(AddIntelligenceReportsToAnalysisCommand command);

    /**
     * Remove customers from a segment.
     */
    IntelligenceAnalysisResponseDto removeIntelligenceReportsFromAnalysis(RemoveIntelligenceReportsFromAnalysisCommand command);

    /**
     * Analyze a segment.
     */
    AnalysisAnalysisResponseDto analyzeAnalysis(AnalyzeAnalysisCommand command);
}
