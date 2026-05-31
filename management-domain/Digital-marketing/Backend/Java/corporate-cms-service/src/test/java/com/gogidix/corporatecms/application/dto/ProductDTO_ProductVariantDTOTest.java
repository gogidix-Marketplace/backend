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
class ProductDTO_ProductVariantDTOTest {

        @Test
    void testBuilder() {
        ProductDTO.ProductVariantDTO dto = ProductDTO.ProductVariantDTO.builder()
                        .id("test-id")
            .name("test-name")
            .sku("test-sku")
            .attributes(Collections.emptyMap())
            .price(BigDecimal.TEN)
            .available(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-sku", dto.getSku());
        assertEquals(BigDecimal.TEN, dto.getPrice());
        assertTrue(dto.getAvailable());
    }

    @Test
    void testSettersAndGetters() {
        ProductDTO.ProductVariantDTO dto = new ProductDTO.ProductVariantDTO();
        dto.setId("val-id");
        dto.setName("val-name");
        dto.setSku("val-sku");
        dto.setPrice(BigDecimal.ONE);
        dto.setAvailable(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-sku", dto.getSku());
        assertEquals(BigDecimal.ONE, dto.getPrice());
        assertTrue(dto.getAvailable());
    }

    @Test
    void testEqualsAndHashCode() {
        ProductDTO.ProductVariantDTO dto1 = ProductDTO.ProductVariantDTO.builder()
                        .id("test-id")
            .name("test-name")
            .sku("test-sku")
            .attributes(Collections.emptyMap())
            .price(BigDecimal.TEN)
            .available(true)
            .build();
        ProductDTO.ProductVariantDTO dto2 = ProductDTO.ProductVariantDTO.builder()
                        .id("test-id")
            .name("test-name")
            .sku("test-sku")
            .attributes(Collections.emptyMap())
            .price(BigDecimal.TEN)
            .available(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProductDTO.ProductVariantDTO dto = ProductDTO.ProductVariantDTO.builder()
                        .id("test-id")
            .name("test-name")
            .sku("test-sku")
            .attributes(Collections.emptyMap())
            .price(BigDecimal.TEN)
            .available(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}