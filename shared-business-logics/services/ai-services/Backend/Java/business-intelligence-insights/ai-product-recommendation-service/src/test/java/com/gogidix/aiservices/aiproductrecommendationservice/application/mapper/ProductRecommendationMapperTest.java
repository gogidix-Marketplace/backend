package com.gogidix.aiservices.aiproductrecommendationservice.application.mapper;

import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.ProductRecommendationResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductRecommendationMapper Tests")
class ProductRecommendationMapperTest {
    private ProductRecommendationMapper mapper;
    private ProductRecommendation testRec;

    @BeforeEach
    void setUp() {
        mapper = new ProductRecommendationMapper();
        RecommendationCriteria criteria = RecommendationCriteria.builder()
                .type(RecommendationCriteria.CriteriaType.CUSTOM)
                .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .logicalOperator(RecommendationCriteria.LogicalOperator.AND)
                .build();
        testRec = ProductRecommendation.builder()
                .segmentId("seg-123")
                .tenantId("tenant-456")
                .name("Premium Products")
                .description("High value")
                .criteria(criteria)
                .status(RecommendationStatus.ACTIVE)
                .segmentType(RecommendationType.BEHAVIORAL)
                .customerCount(500L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should map all fields correctly")
    void shouldMapAllFields() {
        ProductRecommendationResponseDto dto = mapper.toResponseDto(testRec);
        assertThat(dto.id()).isEqualTo("seg-123");
        assertThat(dto.name()).isEqualTo("Premium Products");
        assertThat(dto.segmentType()).isEqualTo(RecommendationType.BEHAVIORAL);
        assertThat(dto.tenantId()).isEqualTo("tenant-456");
        assertThat(dto.criteria()).isNotNull();
    }
}
