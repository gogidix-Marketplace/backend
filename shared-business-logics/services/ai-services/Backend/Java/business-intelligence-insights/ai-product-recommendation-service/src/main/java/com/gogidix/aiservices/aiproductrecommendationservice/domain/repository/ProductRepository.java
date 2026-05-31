package com.gogidix.aiservices.aiproductrecommendationservice.domain.repository;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.Product;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductId;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductScore;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationContext;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationResult;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;

import java.util.List;
import java.util.Optional;

/**
 * Repository port for product persistence operations.
 */
public interface ProductRepository {

    /**
     * Save a product.
     *
     * @param product the product to save
     * @return the saved product
     */
    Product save(Product product);

    /**
     * Find a product by ID.
     *
     * @param id the product ID
     * @return the product if found
     */
    Optional<Product> findById(ProductId id);

    /**
     * Find all products in a category.
     *
     * @param category the category to filter
     * @return list of products in the category
     */
    List<Product> findByCategory(String category);

    /**
     * Find all available products (in stock).
     *
     * @return list of available products
     */
    List<Product> findAvailable();

    /**
     * Find products by tenant ID.
     *
     * @param tenantId the tenant ID
     * @return list of products for the tenant
     */
    List<Product> findByTenantId(String tenantId);

    /**
     * Count products by tenant ID.
     *
     * @param tenantId the tenant ID
     * @return count of products for the tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Delete a product by ID.
     *
     * @param id the product ID
     */
    void delete(ProductId id);
}
