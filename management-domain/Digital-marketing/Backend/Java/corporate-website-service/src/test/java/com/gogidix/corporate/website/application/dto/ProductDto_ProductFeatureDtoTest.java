package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.ProductDto;
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
class ProductDto_ProductFeatureDtoTest {

        @Test
    void testBuilder() {
        ProductDto.ProductFeatureDto dto = ProductDto.ProductFeatureDto.builder()
                        .icon("test-icon")
            .title("test-title")
            .description("test-description")
            .included(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-icon", dto.getIcon());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-description", dto.getDescription());
        assertTrue(dto.isIncluded());
    }

    @Test
    void testSettersAndGetters() {
        ProductDto.ProductFeatureDto dto = new ProductDto.ProductFeatureDto();
        dto.setIcon("val-icon");
        dto.setTitle("val-title");
        dto.setDescription("val-description");
        dto.setIncluded(true);
        assertEquals("val-icon", dto.getIcon());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.isIncluded());
    }

    @Test
    void testEqualsAndHashCode() {
        ProductDto.ProductFeatureDto dto1 = ProductDto.ProductFeatureDto.builder()
                        .icon("test-icon")
            .title("test-title")
            .description("test-description")
            .included(true)
            .build();
        ProductDto.ProductFeatureDto dto2 = ProductDto.ProductFeatureDto.builder()
                        .icon("test-icon")
            .title("test-title")
            .description("test-description")
            .included(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProductDto.ProductFeatureDto dto = ProductDto.ProductFeatureDto.builder()
                        .icon("test-icon")
            .title("test-title")
            .description("test-description")
            .included(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}