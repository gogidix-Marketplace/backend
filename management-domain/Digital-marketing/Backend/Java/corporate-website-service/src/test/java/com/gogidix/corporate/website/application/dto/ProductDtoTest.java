package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.ProductDto;
import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
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
class ProductDtoTest {

        @Test
    void testBuilder() {
        ProductDto dto = ProductDto.builder()
                        .id("test-id")
            .productKey("test-productKey")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .productType("test-productType")
            .category("test-category")
            .features(Collections.emptyList())
            .availableRegions(null)
            .imageUrl("test-imageUrl")
            .imageAlt("test-imageAlt")
            .gallery(Collections.emptyList())
            .pricing(null)
            .requiresContact(true)
            .ctaText("test-ctaText")
            .ctaLink("test-ctaLink")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .launchDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .sortOrder(42)
            .featured(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-productKey", dto.getProductKey());
        assertEquals("test-slug", dto.getSlug());
        assertEquals("test-productType", dto.getProductType());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-imageUrl", dto.getImageUrl());
        assertEquals("test-imageAlt", dto.getImageAlt());
        assertTrue(dto.isRequiresContact());
        assertEquals("test-ctaText", dto.getCtaText());
        assertEquals("test-ctaLink", dto.getCtaLink());
        assertEquals(42, dto.getSortOrder());
        assertTrue(dto.isFeatured());
    }

    @Test
    void testSettersAndGetters() {
        ProductDto dto = new ProductDto();
        dto.setId("val-id");
        dto.setProductKey("val-productKey");
        dto.setSlug("val-slug");
        dto.setProductType("val-productType");
        dto.setCategory("val-category");
        dto.setImageUrl("val-imageUrl");
        dto.setImageAlt("val-imageAlt");
        dto.setRequiresContact(true);
        dto.setCtaText("val-ctaText");
        dto.setCtaLink("val-ctaLink");
        dto.setSortOrder(99);
        dto.setFeatured(true);
        assertEquals("val-id", dto.getId());
        assertEquals("val-productKey", dto.getProductKey());
        assertEquals("val-slug", dto.getSlug());
        assertEquals("val-productType", dto.getProductType());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-imageUrl", dto.getImageUrl());
        assertEquals("val-imageAlt", dto.getImageAlt());
        assertTrue(dto.isRequiresContact());
        assertEquals("val-ctaText", dto.getCtaText());
        assertEquals("val-ctaLink", dto.getCtaLink());
        assertEquals(99, dto.getSortOrder());
        assertTrue(dto.isFeatured());
    }

    @Test
    void testEqualsAndHashCode() {
        ProductDto dto1 = ProductDto.builder()
                        .id("test-id")
            .productKey("test-productKey")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .productType("test-productType")
            .category("test-category")
            .features(Collections.emptyList())
            .availableRegions(null)
            .imageUrl("test-imageUrl")
            .imageAlt("test-imageAlt")
            .gallery(Collections.emptyList())
            .pricing(null)
            .requiresContact(true)
            .ctaText("test-ctaText")
            .ctaLink("test-ctaLink")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .launchDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .sortOrder(42)
            .featured(true)
            .build();
        ProductDto dto2 = ProductDto.builder()
                        .id("test-id")
            .productKey("test-productKey")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .productType("test-productType")
            .category("test-category")
            .features(Collections.emptyList())
            .availableRegions(null)
            .imageUrl("test-imageUrl")
            .imageAlt("test-imageAlt")
            .gallery(Collections.emptyList())
            .pricing(null)
            .requiresContact(true)
            .ctaText("test-ctaText")
            .ctaLink("test-ctaLink")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .launchDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .sortOrder(42)
            .featured(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProductDto dto = ProductDto.builder()
                        .id("test-id")
            .productKey("test-productKey")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .productType("test-productType")
            .category("test-category")
            .features(Collections.emptyList())
            .availableRegions(null)
            .imageUrl("test-imageUrl")
            .imageAlt("test-imageAlt")
            .gallery(Collections.emptyList())
            .pricing(null)
            .requiresContact(true)
            .ctaText("test-ctaText")
            .ctaLink("test-ctaLink")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .launchDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .sortOrder(42)
            .featured(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}