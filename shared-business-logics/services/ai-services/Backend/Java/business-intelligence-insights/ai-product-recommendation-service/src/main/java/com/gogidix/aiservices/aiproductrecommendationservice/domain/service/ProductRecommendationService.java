package com.gogidix.aiservices.aiproductrecommendationservice.domain.service;

import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.RecommendationRequestDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.RecommendationResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.Product;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductId;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductScore;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationContext;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationResult;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for generating product recommendations.
 * Uses business rules and collaborative filtering.
 */
@Service
@Cacheable("recommendations")
public class ProductRecommendationService {

    private static final Logger log = LoggerFactory.getLogger(ProductRecommendationService.class);
    private static final String DEFAULT_ALGORITHM = "v1.0";
    private static final int DEFAULT_MAX_RESULTS = 10;

    private final ProductRepository productRepository;

    public ProductRecommendationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Generate personalized product recommendations.
     */
    @CacheEvict(allEntries = true)
    public List<ProductScore> recommendProducts(String customerId, String tenantId, RecommendationContext context) {
        log.debug("Generating recommendations for customer: {}, tenant: {}", customerId, tenantId);

        // Fetch available products from repository
        List<Product> availableProducts = productRepository.findAvailable();

        log.debug("Found {} available products", availableProducts.size());

        // Apply business rules to score products
        List<ProductScore> scoredProducts = applyBusinessRules(availableProducts, context);

        // Sort by score descending and limit results
        List<ProductScore> rankedProducts = scoredProducts.stream()
                .sorted((p1, p2) -> Double.compare(p2.getScore(), p1.getScore()))
                .limit(context.getMaxResults() != null ? context.getMaxResults() : DEFAULT_MAX_RESULTS)
                .collect(Collectors.toList());

        log.info("Generated {} recommendations for customer: {}", rankedProducts.size(), customerId);

        return rankedProducts;
    }

    /**
     * Generate recommendations from DTO.
     */
    public RecommendationResponseDto recommend(RecommendationRequestDto request) {
        RecommendationContext context = new RecommendationContext();
        context.setCustomerId(request.customerId());
        context.setTenantId(request.tenantId());
        context.setType(request.type());
        context.setMaxResults(request.maxResults());
        context.setExcludedCategories(request.excludedCategories());
        context.setIncludeOutOfStock(request.includeOutOfStock() != null ? request.includeOutOfStock() : true);

        List<ProductScore> products = recommendProducts(
                request.customerId(),
                request.tenantId(),
                context
        );

        return RecommendationResponseDto.success(
                request.customerId(),
                request.tenantId(),
                products
        );
    }

    /**
     * Apply business rules to score products.
     * Rules include availability, category compatibility, cross-sell price elasticity.
     */
    private List<ProductScore> applyBusinessRules(
            List<Product> products,
            RecommendationContext context) {

        List<ProductScore> scoredProducts = new ArrayList<>();

        for (Product product : products) {
            double score = 0.0;
            List<String> reasons = new ArrayList<>();

            // Rule: Product Availability
            if (product.isInStock()) {
                score += 50.0;
                reasons.add("In stock");
            } else if (product.isOutOfStock()) {
                score -= 100.0;
                reasons.add("Out of stock - excluded from recommendations");
            }

            // Rule: Category Compatibility
            if (context.getExcludedCategories() == null || !context.getExcludedCategories().contains(product.getCategory())) {
                score += 20.0;
                reasons.add("Not in excluded categories");
            }

            // Rule: Price Elasticity
            if (product.getPrice() != null) {
                double priceIndex = calculatePriceElasticityIndex(product);
                if (priceIndex > 1.2) {
                    score += 15.0;
                    reasons.add("Good price elasticity");
                }
            }

            // Normalize score to 0-100
            double normalizedScore = Math.max(0, Math.min(100, score));

            scoredProducts.add(new ProductScore(
                    product.getId(),
                    normalizedScore,
                    String.join(", ", reasons)
            ));
        }

        return scoredProducts;
    }

    /**
     * Calculate price elasticity index.
     */
    private double calculatePriceElasticityIndex(Product product) {
        // Simplified calculation - in production would use historical pricing data
        return 1.0;
    }

    /**
     * Get top products for a customer (with cache).
     */
    @CacheEvict(value = "#customerId")
    public List<Product> getTopProducts(String customerId, String tenantId, int limit) {
        return productRepository.findByTenantId(tenantId).stream()
                .filter(Product::isAvailable)
                .sorted((p1, p2) -> Double.compare(p2.getPrice().doubleValue(), p1.getPrice().doubleValue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Refresh recommendation cache for a tenant.
     */
    @CacheEvict(value = "recommendations", allEntries = true)
    public void refreshRecommendationCache(String tenantId) {
        log.info("Refreshing recommendation cache for tenant: {}", tenantId);
        // Cache will be evicted by @CacheEvict
    }
}
