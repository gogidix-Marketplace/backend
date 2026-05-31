package com.gogidix.aiservices.aiproductrecommendationservice.domain.port.in;

import com.gogidix.aiservices.aiproductrecommendationservice.application.command.AddProductsToRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.AnalyzeRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.CreateRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.DeleteRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.RemoveProductsFromRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.UpdateRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.ProductRecommendationResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.RecommendationAnalysisResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;

/**
 * Input port for product recommendation operations.
 * This interface defines the contract for the application service layer.
 */
public interface ProductRecommendationServicePort {

    /**
     * Create a new product recommendation.
     */
    ProductRecommendationResponseDto createRecommendation(CreateRecommendationCommand command);

    /**
     * Update an existing product recommendation.
     */
    ProductRecommendationResponseDto updateRecommendation(UpdateRecommendationCommand command);

    /**
     * Delete a product recommendation.
     */
    void deleteRecommendation(String segmentId, String tenantId, String userId);

    /**
     * Find a segment by ID.
     */
    ProductRecommendationResponseDto getRecommendationById(String segmentId, String tenantId);

    /**
     * Find segments by tenant with filtering.
     */
    PagedResponseDto<ProductRecommendationResponseDto> getRecommendationsByTenant(
            String tenantId,
            RecommendationType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    /**
     * Add customers to a segment.
     */
    ProductRecommendationResponseDto addProductsToRecommendation(AddProductsToRecommendationCommand command);

    /**
     * Remove customers from a segment.
     */
    ProductRecommendationResponseDto removeProductsFromRecommendation(RemoveProductsFromRecommendationCommand command);

    /**
     * Analyze a segment.
     */
    RecommendationAnalysisResponseDto analyzeRecommendation(AnalyzeRecommendationCommand command);
}
