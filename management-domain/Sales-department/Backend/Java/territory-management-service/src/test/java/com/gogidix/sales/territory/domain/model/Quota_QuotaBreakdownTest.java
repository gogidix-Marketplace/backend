package com.gogidix.sales.territory.domain.model;

import com.gogidix.sales.territory.domain.model.Quota;
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
class Quota_QuotaBreakdownTest {

        @Test
    void testBuilder() {
        Quota.QuotaBreakdown dto = Quota.QuotaBreakdown.builder()
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
    void testSettersAndGetters() {
        Quota.QuotaBreakdown dto = new Quota.QuotaBreakdown();
        dto.setCategory("val-category");
        dto.setAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setProductId("val-productId");
        dto.setProductCategoryId("val-productCategoryId");
        assertEquals("val-category", dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-productId", dto.getProductId());
        assertEquals("val-productCategoryId", dto.getProductCategoryId());
    }

    @Test
    void testEqualsAndHashCode() {
        Quota.QuotaBreakdown dto1 = Quota.QuotaBreakdown.builder()
                        .category("test-category")
            .amount(BigDecimal.TEN)
            .description("test-description")
            .productId("test-productId")
            .productCategoryId("test-productCategoryId")
            .build();
        Quota.QuotaBreakdown dto2 = Quota.QuotaBreakdown.builder()
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
        Quota.QuotaBreakdown dto = Quota.QuotaBreakdown.builder()
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