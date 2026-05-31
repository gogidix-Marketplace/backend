package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RecommendationResult.
 */
@DisplayName("RecommendationResult Tests")
class RecommendationResultTest {

    @Test
    @DisplayName("Should create result with builder")
    void shouldCreateResultWithBuilder() {
        List<ProductScore> products = new ArrayList<>();
        ProductScore score1 = new ProductScore(
                ProductId.randomProductId(),
                0.95,
                "High purchase affinity"
        );
        products.add(score1);

        RecommendationResult result = RecommendationResult.builder()
                .customerId("cust-001")
                .tenantId("tenant-001")
                .rankedProducts(products)
                .executedAt(Instant.now())
                .algorithmVersion("v1.0")
                .build();

        assertNotNull(result.getId());
        assertEquals("cust-001", result.getCustomerId());
        assertEquals("tenant-001", result.getTenantId());
        assertEquals(1, result.getRankedProducts().size());
        assertNotNull(result.getExecutedAt());
        assertEquals("v1.0", result.getAlgorithmVersion());
    }

    @Test
    @DisplayName("Should create result with default values")
    void shouldCreateResultWithDefaults() {
        RecommendationResult result = new RecommendationResult();

        assertNotNull(result.getId());  // Auto-generated
        assertNull(result.getCustomerId());
        assertNull(result.getTenantId());
        assertTrue(result.getRankedProducts().isEmpty());
        assertNotNull(result.getExecutedAt());  // Auto-generated
        assertNull(result.getAlgorithmVersion());
    }

    @Test
    @DisplayName("Should add ranked products")
    void shouldAddRankedProducts() {
        List<ProductScore> products = new ArrayList<>();
        products.add(new ProductScore(ProductId.randomProductId(), 0.85, "Good price"));
        products.add(new ProductScore(ProductId.randomProductId(), 0.75, "Recent purchase"));

        RecommendationResult result = RecommendationResult.builder()
                .customerId("cust-002")
                .rankedProducts(products)
                .build();

        assertEquals(2, result.getRankedProducts().size());
        assertEquals(0.85, result.getRankedProducts().get(0).getScore());
    }

    @Test
    @DisplayName("Should set algorithm version")
    void shouldSetAlgorithmVersion() {
        RecommendationResult result = RecommendationResult.builder()
                .customerId("cust-003")
                .rankedProducts(List.of())
                .algorithmVersion("v2.5.1")
                .build();

        assertEquals("v2.5.1", result.getAlgorithmVersion());
    }

    @Test
    @DisplayName("Should generate unique IDs")
    void shouldGenerateUniqueIds() {
        RecommendationResult result1 = RecommendationResult.builder()
                .customerId("cust-004")
                .rankedProducts(List.of())
                .build();

        RecommendationResult result2 = RecommendationResult.builder()
                .customerId("cust-004")
                .rankedProducts(List.of())
                .build();

        assertNotEquals(result1.getId(), result2.getId(), "IDs should be unique");
    }

    @Test
    @DisplayName("Should handle empty results")
    void shouldHandleEmptyResults() {
        RecommendationResult result = RecommendationResult.builder()
                .customerId("cust-005")
                .rankedProducts(List.of())
                .build();

        assertTrue(result.getRankedProducts().isEmpty());
    }

    @Test
    @DisplayName("Should indicate success with products")
    void shouldIndicateSuccess() {
        List<ProductScore> products = new ArrayList<>();
        products.add(new ProductScore(ProductId.randomProductId(), 0.90, "Recommended"));

        RecommendationResult result = RecommendationResult.builder()
                .customerId("cust-006")
                .rankedProducts(products)
                .build();

        assertFalse(result.getRankedProducts().isEmpty());
        assertEquals(1, result.getRankedProducts().size());
    }

    @Test
    @DisplayName("Should format toString correctly")
    void shouldFormatToStringCorrectly() {
        List<ProductScore> products = new ArrayList<>();
        products.add(new ProductScore(ProductId.randomProductId(), 0.95, "Top product"));

        RecommendationResult result = RecommendationResult.builder()
                .customerId("cust-007")
                .tenantId("tenant-007")
                .rankedProducts(products)
                .executedAt(Instant.parse("2025-02-12T10:30:45Z"))
                .algorithmVersion("v3.2.0")
                .build();

        String resultString = result.toString();
        assertTrue(resultString.contains("customerId="));
        assertTrue(resultString.contains("tenantId="));
        assertTrue(resultString.contains("products=1"));
        assertTrue(resultString.contains("executedAt="));
        assertTrue(resultString.contains("algorithm="));
    }

    @Test
    @DisplayName("Should validate tenant ID is present")
    void shouldValidateTenantIdPresence() {
        RecommendationResult result = RecommendationResult.builder()
                .customerId("cust-008")
                .tenantId("tenant-008")
                .rankedProducts(List.of())
                .build();

        assertEquals("tenant-008", result.getTenantId());
    }

    @Test
    @DisplayName("Should compare results correctly")
    void shouldCompareResults() {
        List<ProductScore> products = List.of(
                new ProductScore(ProductId.randomProductId(), 0.92, "Product A")
        );

        RecommendationResult result1 = RecommendationResult.builder()
                .customerId("cust-009")
                .rankedProducts(products)
                .build();

        RecommendationResult result2 = RecommendationResult.builder()
                .customerId("cust-009")
                .rankedProducts(products)
                .build();

        assertNotEquals(result1, result2);  // Different IDs
    }

    @Test
    @DisplayName("Should track execution time")
    void shouldTrackExecutionTime() {
        Instant before = Instant.now().minusSeconds(5);
        RecommendationResult result = RecommendationResult.builder()
                .executedAt(Instant.now())
                .build();

        assertTrue(result.getExecutedAt().isAfter(before));
    }
}
