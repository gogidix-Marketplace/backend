package com.gogidix.sales.territory.application.dto.response;

import com.gogidix.sales.territory.application.dto.response.QuotaResponseDto;
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
class QuotaResponseDto_QuotaBreakdownDtoTest {

        @Test
    void testBuilder() {
        QuotaResponseDto.QuotaBreakdownDto dto = QuotaResponseDto.QuotaBreakdownDto.builder()
                        .category("test-category")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .productId("test-productId")
            .productCategoryId("test-productCategoryId")
            .build();
        assertNotNull(dto);
        assertEquals("test-category", dto.getCategory());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-productId", dto.getProductId());
        assertEquals("test-productCategoryId", dto.getProductCategoryId());
    }

    @Test
    void testBuilderWithValues() {
        QuotaResponseDto.QuotaBreakdownDto dto = QuotaResponseDto.QuotaBreakdownDto.builder()
            .category("val-category")
            .amount(BigDecimal.ONE)
            .description("val-description")
            .productId("val-productId")
            .productCategoryId("val-productCategoryId")
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaResponseDto.QuotaBreakdownDto dto1 = QuotaResponseDto.QuotaBreakdownDto.builder()
                        .category("test-category")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .productId("test-productId")
            .productCategoryId("test-productCategoryId")
            .build();
        QuotaResponseDto.QuotaBreakdownDto dto2 = QuotaResponseDto.QuotaBreakdownDto.builder()
                        .category("test-category")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .productId("test-productId")
            .productCategoryId("test-productCategoryId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        QuotaResponseDto.QuotaBreakdownDto dto = QuotaResponseDto.QuotaBreakdownDto.builder()
                        .category("test-category")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .productId("test-productId")
            .productCategoryId("test-productCategoryId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}