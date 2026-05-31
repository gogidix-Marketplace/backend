package com.gogidix.aiservices.aichurnpredictionservice.domain.port.in;

import com.gogidix.aiservices.aichurnpredictionservice.application.command.AddModelsToPredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.AnalyzePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.CreatePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.DeletePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.RemoveModelsFromPredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.command.UpdatePredictionCommand;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.ChurnPredictionResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.application.dto.PredictionAnalysisResponseDto;
import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;

/**
 * Input port for churn prediction operations.
 * This interface defines the contract for the application service layer.
 */
public interface ChurnPredictionServicePort {

    /**
     * Create a new churn prediction.
     */
    ChurnPredictionResponseDto createPrediction(CreatePredictionCommand command);

    /**
     * Update an existing churn prediction.
     */
    ChurnPredictionResponseDto updatePrediction(UpdatePredictionCommand command);

    /**
     * Delete a churn prediction.
     */
    void deletePrediction(String segmentId, String tenantId, String userId);

    /**
     * Find a segment by ID.
     */
    ChurnPredictionResponseDto getPredictionById(String segmentId, String tenantId);

    /**
     * Find segments by tenant with filtering.
     */
    PagedResponseDto<ChurnPredictionResponseDto> getPredictionsByTenant(
            String tenantId,
            PredictionType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    /**
     * Add customers to a segment.
     */
    ChurnPredictionResponseDto addCustomersToPrediction(AddModelsToPredictionCommand command);

    /**
     * Remove customers from a segment.
     */
    ChurnPredictionResponseDto removeCustomersFromPrediction(RemoveModelsFromPredictionCommand command);

    /**
     * Analyze a segment.
     */
    PredictionAnalysisResponseDto analyzePrediction(AnalyzePredictionCommand command);
}
