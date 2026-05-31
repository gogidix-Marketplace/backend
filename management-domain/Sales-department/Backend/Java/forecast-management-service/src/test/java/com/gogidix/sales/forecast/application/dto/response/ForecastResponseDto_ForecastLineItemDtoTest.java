package com.gogidix.sales.forecast.application.dto.response;

import com.gogidix.sales.forecast.application.dto.response.ForecastResponseDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ForecastResponseDto_ForecastLineItemDtoTest {

        @Test
    void testBuilder() {
        ForecastResponseDto.ForecastLineItemDto dto = ForecastResponseDto.ForecastLineItemDto.builder()
                        .lineItemId("test-lineItemId")
            .name("test-name")
            .description("test-description")
            .category("test-category")
            .type("test-type")
            .bestCase(BigDecimal.TEN)
            .likely(BigDecimal.TEN)
            .worstCase(BigDecimal.TEN)
            .currency("test-currency")
            .productId("test-productId")
            .productName("test-productName")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .customerSegmentId("test-customerSegmentId")
            .customerSegmentName("test-customerSegmentName")
            .salesChannel("test-salesChannel")
            .notes("test-notes")
            .owner("test-owner")
            .active(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-lineItemId", dto.getLineItemId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-type", dto.getType());
        assertEquals(BigDecimal.TEN, dto.getBestCase());
        assertEquals(BigDecimal.TEN, dto.getLikely());
        assertEquals(BigDecimal.TEN, dto.getWorstCase());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-productId", dto.getProductId());
        assertEquals("test-productName", dto.getProductName());
        assertEquals("test-territoryId", dto.getTerritoryId());
        assertEquals("test-territoryName", dto.getTerritoryName());
        assertEquals("test-customerSegmentId", dto.getCustomerSegmentId());
        assertEquals("test-customerSegmentName", dto.getCustomerSegmentName());
        assertEquals("test-salesChannel", dto.getSalesChannel());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-owner", dto.getOwner());
        assertTrue(dto.getActive());
    }

    @Test
    void testSettersAndGetters() {
        ForecastResponseDto.ForecastLineItemDto dto = new ForecastResponseDto.ForecastLineItemDto();
        dto.setLineItemId("val-lineItemId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setCategory("val-category");
        dto.setType("val-type");
        dto.setBestCase(BigDecimal.ONE);
        dto.setLikely(BigDecimal.ONE);
        dto.setWorstCase(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setProductId("val-productId");
        dto.setProductName("val-productName");
        dto.setTerritoryId("val-territoryId");
        dto.setTerritoryName("val-territoryName");
        dto.setCustomerSegmentId("val-customerSegmentId");
        dto.setCustomerSegmentName("val-customerSegmentName");
        dto.setSalesChannel("val-salesChannel");
        dto.setNotes("val-notes");
        dto.setOwner("val-owner");
        dto.setActive(true);
        assertEquals("val-lineItemId", dto.getLineItemId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-type", dto.getType());
        assertEquals(BigDecimal.ONE, dto.getBestCase());
        assertEquals(BigDecimal.ONE, dto.getLikely());
        assertEquals(BigDecimal.ONE, dto.getWorstCase());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-productId", dto.getProductId());
        assertEquals("val-productName", dto.getProductName());
        assertEquals("val-territoryId", dto.getTerritoryId());
        assertEquals("val-territoryName", dto.getTerritoryName());
        assertEquals("val-customerSegmentId", dto.getCustomerSegmentId());
        assertEquals("val-customerSegmentName", dto.getCustomerSegmentName());
        assertEquals("val-salesChannel", dto.getSalesChannel());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-owner", dto.getOwner());
        assertTrue(dto.getActive());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastResponseDto.ForecastLineItemDto dto1 = ForecastResponseDto.ForecastLineItemDto.builder()
                        .lineItemId("test-lineItemId")
            .name("test-name")
            .description("test-description")
            .category("test-category")
            .type("test-type")
            .bestCase(BigDecimal.TEN)
            .likely(BigDecimal.TEN)
            .worstCase(BigDecimal.TEN)
            .currency("test-currency")
            .productId("test-productId")
            .productName("test-productName")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .customerSegmentId("test-customerSegmentId")
            .customerSegmentName("test-customerSegmentName")
            .salesChannel("test-salesChannel")
            .notes("test-notes")
            .owner("test-owner")
            .active(true)
            .build();
        ForecastResponseDto.ForecastLineItemDto dto2 = ForecastResponseDto.ForecastLineItemDto.builder()
                        .lineItemId("test-lineItemId")
            .name("test-name")
            .description("test-description")
            .category("test-category")
            .type("test-type")
            .bestCase(BigDecimal.TEN)
            .likely(BigDecimal.TEN)
            .worstCase(BigDecimal.TEN)
            .currency("test-currency")
            .productId("test-productId")
            .productName("test-productName")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .customerSegmentId("test-customerSegmentId")
            .customerSegmentName("test-customerSegmentName")
            .salesChannel("test-salesChannel")
            .notes("test-notes")
            .owner("test-owner")
            .active(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ForecastResponseDto.ForecastLineItemDto dto = ForecastResponseDto.ForecastLineItemDto.builder()
                        .lineItemId("test-lineItemId")
            .name("test-name")
            .description("test-description")
            .category("test-category")
            .type("test-type")
            .bestCase(BigDecimal.TEN)
            .likely(BigDecimal.TEN)
            .worstCase(BigDecimal.TEN)
            .currency("test-currency")
            .productId("test-productId")
            .productName("test-productName")
            .territoryId("test-territoryId")
            .territoryName("test-territoryName")
            .customerSegmentId("test-customerSegmentId")
            .customerSegmentName("test-customerSegmentName")
            .salesChannel("test-salesChannel")
            .notes("test-notes")
            .owner("test-owner")
            .active(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}