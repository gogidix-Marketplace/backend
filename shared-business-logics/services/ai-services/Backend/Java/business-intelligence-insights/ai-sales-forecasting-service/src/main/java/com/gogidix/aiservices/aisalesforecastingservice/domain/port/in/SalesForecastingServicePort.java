package com.gogidix.aiservices.aisalesforecastingservice.domain.port.in;

import com.gogidix.aiservices.aisalesforecastingservice.application.command.AddForecastModelsCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.AnalyzeForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.CreateForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.DeleteForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.RemoveForecastModelsCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.UpdateForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.SalesForecastingResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.ForecastAnalysisResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;

/**
 * Input port for sales forecast operations.
 * This interface defines the contract for the application service layer.
 */
public interface SalesForecastingServicePort {

    /**
     * Create a new sales forecast.
     */
    SalesForecastingResponseDto createForecast(CreateForecastCommand command);

    /**
     * Update an existing sales forecast.
     */
    SalesForecastingResponseDto updateForecast(UpdateForecastCommand command);

    /**
     * Delete a sales forecast.
     */
    void deleteForecast(String segmentId, String tenantId, String userId);

    /**
     * Find a segment by ID.
     */
    SalesForecastingResponseDto getForecastById(String segmentId, String tenantId);

    /**
     * Find segments by tenant with filtering.
     */
    PagedResponseDto<SalesForecastingResponseDto> getForecastsByTenant(
            String tenantId,
            ForecastType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    /**
     * Add customers to a segment.
     */
    SalesForecastingResponseDto addForecastModelsToForecast(AddForecastModelsCommand command);

    /**
     * Remove customers from a segment.
     */
    SalesForecastingResponseDto removeForecastModelsFromForecast(RemoveForecastModelsCommand command);

    /**
     * Analyze a segment.
     */
    ForecastAnalysisResponseDto analyzeForecast(AnalyzeForecastCommand command);
}
