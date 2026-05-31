package com.gogidix.aiservices.aimarketbasketanalysisservice.domain.port.in;

import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.AddCustomersToBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.AnalyzeBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.CreateBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.DeleteBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.RemoveCustomersFromBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.UpdateBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.MarketBasketResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.BasketAnalysisResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;

/**
 * Input port for market basket operations.
 * This interface defines the contract for the application service layer.
 */
public interface MarketBasketServicePort {

    /**
     * Create a new market basket.
     */
    MarketBasketResponseDto createBasket(CreateBasketCommand command);

    /**
     * Update an existing market basket.
     */
    MarketBasketResponseDto updateBasket(UpdateBasketCommand command);

    /**
     * Delete a market basket.
     */
    void deleteBasket(String segmentId, String tenantId, String userId);

    /**
     * Find a segment by ID.
     */
    MarketBasketResponseDto getBasketById(String segmentId, String tenantId);

    /**
     * Find segments by tenant with filtering.
     */
    PagedResponseDto<MarketBasketResponseDto> getBasketsByTenant(
            String tenantId,
            BasketType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    /**
     * Add customers to a segment.
     */
    MarketBasketResponseDto addCustomersToBasket(AddCustomersToBasketCommand command);

    /**
     * Remove customers from a segment.
     */
    MarketBasketResponseDto removeCustomersFromBasket(RemoveCustomersFromBasketCommand command);

    /**
     * Analyze a segment.
     */
    BasketAnalysisResponseDto analyzeBasket(AnalyzeBasketCommand command);
}
