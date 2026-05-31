package com.gogidix.finance.revenuetracking.application.dto.response;

import com.gogidix.finance.revenuetracking.application.dto.response.RevenueStreamResponseDto;
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
class RevenueStreamResponseDto_StreamTierDtoTest {

        @Test
    void testBuilder() {
        RevenueStreamResponseDto.StreamTierDto dto = RevenueStreamResponseDto.StreamTierDto.builder()
                        .tierId("test-tierId")
            .name("test-name")
            .description("test-description")
            .minAmount(BigDecimal.TEN)
            .maxAmount(BigDecimal.TEN)
            .pricePerUnit(BigDecimal.TEN)
            .included(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-tierId", dto.getTierId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getMinAmount());
        assertEquals(BigDecimal.TEN, dto.getMaxAmount());
        assertEquals(BigDecimal.TEN, dto.getPricePerUnit());
        assertTrue(dto.getIncluded());
    }

    @Test
    void testSettersAndGetters() {
        RevenueStreamResponseDto.StreamTierDto dto = new RevenueStreamResponseDto.StreamTierDto();
        dto.setTierId("val-tierId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setMinAmount(BigDecimal.ONE);
        dto.setMaxAmount(BigDecimal.ONE);
        dto.setPricePerUnit(BigDecimal.ONE);
        dto.setIncluded(true);
        assertEquals("val-tierId", dto.getTierId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getMinAmount());
        assertEquals(BigDecimal.ONE, dto.getMaxAmount());
        assertEquals(BigDecimal.ONE, dto.getPricePerUnit());
        assertTrue(dto.getIncluded());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueStreamResponseDto.StreamTierDto dto1 = RevenueStreamResponseDto.StreamTierDto.builder()
                        .tierId("test-tierId")
            .name("test-name")
            .description("test-description")
            .minAmount(BigDecimal.TEN)
            .maxAmount(BigDecimal.TEN)
            .pricePerUnit(BigDecimal.TEN)
            .included(true)
            .build();
        RevenueStreamResponseDto.StreamTierDto dto2 = RevenueStreamResponseDto.StreamTierDto.builder()
                        .tierId("test-tierId")
            .name("test-name")
            .description("test-description")
            .minAmount(BigDecimal.TEN)
            .maxAmount(BigDecimal.TEN)
            .pricePerUnit(BigDecimal.TEN)
            .included(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueStreamResponseDto.StreamTierDto dto = RevenueStreamResponseDto.StreamTierDto.builder()
                        .tierId("test-tierId")
            .name("test-name")
            .description("test-description")
            .minAmount(BigDecimal.TEN)
            .maxAmount(BigDecimal.TEN)
            .pricePerUnit(BigDecimal.TEN)
            .included(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}