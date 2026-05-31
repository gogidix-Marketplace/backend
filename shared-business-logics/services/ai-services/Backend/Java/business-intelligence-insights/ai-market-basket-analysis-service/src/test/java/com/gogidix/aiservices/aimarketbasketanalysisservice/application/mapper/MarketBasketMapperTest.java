package com.gogidix.aiservices.aimarketbasketanalysisservice.application.mapper;

import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.MarketBasketResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MarketBasketMapper Tests")
class MarketBasketMapperTest {

    private MarketBasketMapper mapper;
    private MarketBasket testBasket;

    @BeforeEach
    void setUp() {
        mapper = new MarketBasketMapper();
        
        BasketCriteria criteria = BasketCriteria.builder()
                .type(BasketCriteria.CriteriaType.CUSTOM)
                .operator(BasketCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(1000)
                .logicalOperator(BasketCriteria.LogicalOperator.AND)
                .build();

        testBasket = MarketBasket.builder()
                .segmentId("seg-123")
                .tenantId("tenant-456")
                .name("Premium Customers")
                .description("High value")
                .criteria(criteria)
                .status(BasketStatus.ACTIVE)
                .segmentType(BasketType.BEHAVIORAL)
                .customerCount(500L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Nested
    @DisplayName("toResponseDto Tests")
    class ToResponseDtoTests {

        @Test
        @DisplayName("Should map all fields correctly")
        void shouldMapAllFields() {
            MarketBasketResponseDto dto = mapper.toResponseDto(testBasket);

            assertThat(dto.id()).isEqualTo("seg-123");
            assertThat(dto.name()).isEqualTo("Premium Customers");
            assertThat(dto.description()).isEqualTo("High value");
            assertThat(dto.segmentType()).isEqualTo(BasketType.BEHAVIORAL);
            assertThat(dto.customerCount()).isEqualTo(500);
            assertThat(dto.active()).isTrue();
            assertThat(dto.tenantId()).isEqualTo("tenant-456");
            assertThat(dto.criteria()).isNotNull();
            assertThat(dto.criteria()).containsEntry("field", "lifetimeValue");
            assertThat(dto.criteria()).containsEntry("operator", "GREATER_THAN");
            assertThat(dto.criteria()).containsEntry("logicalOperator", "AND");
        }
    }
}
