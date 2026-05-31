package com.gogidix.aiservices.aiproductrecommendationservice.application.dto;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RecommendationRequestDto.
 */
@DisplayName("RecommendationRequestDto Tests")
class RecommendationRequestDtoTest {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @DisplayName("Should create valid request with all fields")
    void shouldCreateValidRequest() {
        RecommendationRequestDto request = new RecommendationRequestDto(
                "cust-001",
                "tenant-001",
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                List.of("Electronics"),
                true
        );

        assertEquals("cust-001", request.customerId());
        assertEquals("tenant-001", request.tenantId());
        assertEquals(RecommendationType.PERSONALIZED_BASED_ON_HISTORY, request.type());
        assertEquals(Integer.valueOf(10), request.maxResults());
        assertEquals(1, request.excludedCategories().size());
        assertTrue(request.includeOutOfStock());
    }

    @Test
    @DisplayName("Should create cross-sell request using factory method")
    void shouldCreateCrossSellRequest() {
        RecommendationRequestDto request = RecommendationRequestDto.forCrossSell(
                "cust-002",
                "tenant-002",
                List.of("Electronics", "Books")
        );

        assertEquals("cust-002", request.customerId());
        assertEquals("tenant-002", request.tenantId());
        assertEquals(RecommendationType.COLLABORATIVE_FILTERING, request.type());
        assertEquals(Integer.valueOf(10), request.maxResults());
        assertEquals(2, request.excludedCategories().size());
        assertTrue(request.includeOutOfStock());
    }

    @Test
    @DisplayName("Should create trending request using factory method")
    void shouldCreateTrendingRequest() {
        RecommendationRequestDto request = RecommendationRequestDto.forTrending(
                "cust-003",
                "tenant-003"
        );

        assertEquals("cust-003", request.customerId());
        assertEquals("tenant-003", request.tenantId());
        assertEquals(RecommendationType.TREND_BASED, request.type());
        assertEquals(Integer.valueOf(20), request.maxResults());
        assertTrue(request.includeOutOfStock());
    }

    @Test
    @DisplayName("Should create purchase history request using factory method")
    void shouldCreatePurchaseHistoryRequest() {
        RecommendationRequestDto request = RecommendationRequestDto.forPurchaseHistory(
                "cust-004",
                "tenant-004"
        );

        assertEquals("cust-004", request.customerId());
        assertEquals(RecommendationType.PERSONALIZED_BASED_ON_HISTORY, request.type());
        assertEquals(Integer.valueOf(10), request.maxResults());
        assertTrue(request.excludedCategories().isEmpty());
    }

    @Test
    @DisplayName("Should validate null customerId fails")
    void shouldRejectNullCustomerId() {
        RecommendationRequestDto request = new RecommendationRequestDto(
                null,
                "tenant-001",
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                List.of(),
                true
        );

        Set<ConstraintViolation<RecommendationRequestDto>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("customerId")));
    }

    @Test
    @DisplayName("Should validate null tenantId fails")
    void shouldRejectNullTenantId() {
        RecommendationRequestDto request = new RecommendationRequestDto(
                "cust-001",
                null,
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                List.of(),
                true
        );

        Set<ConstraintViolation<RecommendationRequestDto>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("tenantId")));
    }

    @Test
    @DisplayName("Should validate null type fails")
    void shouldRejectNullType() {
        RecommendationRequestDto request = new RecommendationRequestDto(
                "cust-001",
                "tenant-001",
                null,
                10,
                List.of(),
                true
        );

        Set<ConstraintViolation<RecommendationRequestDto>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
    }

    @Test
    @DisplayName("Should validate null maxResults fails")
    void shouldRejectNullMaxResults() {
        RecommendationRequestDto request = new RecommendationRequestDto(
                "cust-001",
                "tenant-001",
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                null,
                List.of(),
                true
        );

        Set<ConstraintViolation<RecommendationRequestDto>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should validate exclude out of stock flag")
    void shouldValidateExcludeOutOfStock() {
        RecommendationRequestDto requestWithFlag = new RecommendationRequestDto(
                "cust-006",
                "tenant-006",
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                List.of(),
                false
        );

        RecommendationRequestDto requestWithoutFlag = new RecommendationRequestDto(
                "cust-007",
                "tenant-007",
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                List.of(),
                null
        );

        assertFalse(requestWithFlag.includeOutOfStock());
        assertNull(requestWithoutFlag.includeOutOfStock());
    }

    @Test
    @DisplayName("Should validate null excludedCategories")
    void shouldValidateNullExcludedCategories() {
        RecommendationRequestDto request = new RecommendationRequestDto(
                "cust-015",
                "tenant-015",
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                null,
                true
        );

        assertNull(request.excludedCategories());
    }

    @Test
    @DisplayName("Should serialize correctly")
    void shouldSerializeCorrectly() {
        RecommendationRequestDto request = new RecommendationRequestDto(
                "cust-011",
                "tenant-011",
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                List.of("Books"),
                true
        );

        String result = request.toString();
        assertTrue(result.contains("customerId="));
        assertTrue(result.contains("tenantId="));
        assertTrue(result.contains("type=PERSONALIZED_BASED_ON_HISTORY"));
    }

    @Test
    @DisplayName("Should apply validation annotations correctly")
    void shouldApplyValidationAnnotations() {
        RecommendationRequestDto request = new RecommendationRequestDto(
                "cust-012",
                "tenant-012",
                RecommendationType.CROSS_SELL,
                10,
                List.of("Electronics"),
                true
        );

        Set<ConstraintViolation<RecommendationRequestDto>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty excluded categories")
    void shouldHandleEmptyExcludedCategories() {
        RecommendationRequestDto request = new RecommendationRequestDto(
                "cust-013",
                "tenant-013",
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                List.of(),
                true
        );

        assertTrue(request.excludedCategories().isEmpty());
    }

    @Test
    @DisplayName("Should handle multiple excluded categories")
    void shouldHandleMultipleExcludedCategories() {
        List<String> categories = List.of("Electronics", "Books", "Clothing");
        RecommendationRequestDto request = new RecommendationRequestDto(
                "cust-014",
                "tenant-014",
                RecommendationType.PERSONALIZED_BASED_ON_HISTORY,
                10,
                categories,
                true
        );

        assertEquals(3, request.excludedCategories().size());
        assertTrue(request.excludedCategories().contains("Electronics"));
        assertTrue(request.excludedCategories().contains("Books"));
        assertTrue(request.excludedCategories().contains("Clothing"));
    }
}
