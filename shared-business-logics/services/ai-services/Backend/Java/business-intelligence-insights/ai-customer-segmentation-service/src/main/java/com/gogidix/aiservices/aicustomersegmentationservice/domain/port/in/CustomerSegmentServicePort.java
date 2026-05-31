package com.gogidix.aiservices.aicustomersegmentationservice.domain.port.in;

import com.gogidix.aiservices.aicustomersegmentationservice.application.command.AddCustomersToSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.AnalyzeSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.CreateSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.DeleteSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.RemoveCustomersFromSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.UpdateSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.CustomerSegmentResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.SegmentAnalysisResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentType;

/**
 * Input port for customer segment operations.
 * This interface defines the contract for the application service layer.
 */
public interface CustomerSegmentServicePort {

    /**
     * Create a new customer segment.
     */
    CustomerSegmentResponseDto createSegment(CreateSegmentCommand command);

    /**
     * Update an existing customer segment.
     */
    CustomerSegmentResponseDto updateSegment(UpdateSegmentCommand command);

    /**
     * Delete a customer segment.
     */
    void deleteSegment(String segmentId, String tenantId, String userId);

    /**
     * Find a segment by ID.
     */
    CustomerSegmentResponseDto getSegmentById(String segmentId, String tenantId);

    /**
     * Find segments by tenant with filtering.
     */
    PagedResponseDto<CustomerSegmentResponseDto> getSegmentsByTenant(
            String tenantId,
            SegmentType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    /**
     * Add customers to a segment.
     */
    CustomerSegmentResponseDto addCustomersToSegment(AddCustomersToSegmentCommand command);

    /**
     * Remove customers from a segment.
     */
    CustomerSegmentResponseDto removeCustomersFromSegment(RemoveCustomersFromSegmentCommand command);

    /**
     * Analyze a segment.
     */
    SegmentAnalysisResponseDto analyzeSegment(AnalyzeSegmentCommand command);
}
