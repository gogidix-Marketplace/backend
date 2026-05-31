package com.gogidix.corporatecms.application.dto;

import com.gogidix.corporatecms.application.dto.ProductDTO;
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
class ProductDTO_ProductPricingDTOTest {

        @Test
    void testBuilder() {
        ProductDTO.ProductPricingDTO dto = ProductDTO.ProductPricingDTO.builder()
                        .planType("test-planType")
            .name("test-name")
            .price(BigDecimal.TEN)
            .billingCycle("test-billingCycle")
            .features(Collections.emptyMap())
            .popular(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-planType", dto.getPlanType());
        assertEquals("test-name", dto.getName());
        assertEquals(BigDecimal.TEN, dto.getPrice());
        assertEquals("test-billingCycle", dto.getBillingCycle());
        assertTrue(dto.getPopular());
    }

    @Test
    void testSettersAndGetters() {
        ProductDTO.ProductPricingDTO dto = new ProductDTO.ProductPricingDTO();
        dto.setPlanType("val-planType");
        dto.setName("val-name");
        dto.setPrice(BigDecimal.ONE);
        dto.setBillingCycle("val-billingCycle");
        dto.setPopular(true);
        assertEquals("val-planType", dto.getPlanType());
        assertEquals("val-name", dto.getName());
        assertEquals(BigDecimal.ONE, dto.getPrice());
        assertEquals("val-billingCycle", dto.getBillingCycle());
        assertTrue(dto.getPopular());
    }

    @Test
    void testEqualsAndHashCode() {
        ProductDTO.ProductPricingDTO dto1 = ProductDTO.ProductPricingDTO.builder()
                        .planType("test-planType")
            .name("test-name")
            .price(BigDecimal.TEN)
            .billingCycle("test-billingCycle")
            .features(Collections.emptyMap())
            .popular(true)
            .build();
        ProductDTO.ProductPricingDTO dto2 = ProductDTO.ProductPricingDTO.builder()
                        .planType("test-planType")
            .name("test-name")
            .price(BigDecimal.TEN)
            .billingCycle("test-billingCycle")
            .features(Collections.emptyMap())
            .popular(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProductDTO.ProductPricingDTO dto = ProductDTO.ProductPricingDTO.builder()
                        .planType("test-planType")
            .name("test-name")
            .price(BigDecimal.TEN)
            .billingCycle("test-billingCycle")
            .features(Collections.emptyMap())
            .popular(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}