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
class ProductDTOTest {

        @Test
    void testBuilder() {
        ProductDTO dto = ProductDTO.builder()
                        .id("test-id")
            .sku("test-sku")
            .slug("test-slug")
            .name("test-name")
            .tagline("test-tagline")
            .description("test-description")
            .longDescription("test-longDescription")
            .featuredImageId("test-featuredImageId")
            .galleryImageIds(Collections.emptyList())
            .demoVideoId("test-demoVideoId")
            .categoryId("test-categoryId")
            .categoryName("test-categoryName")
            .tags(Collections.emptyList())
            .features(Collections.emptyMap())
            .specifications(Collections.emptyMap())
            .variants(Collections.emptyList())
            .pricing(Collections.emptyList())
            .published(true)
            .documentationLink("test-documentationLink")
            .apiReferenceLink("test-apiReferenceLink")
            .supportLink("test-supportLink")
            .releaseNotesLink("test-releaseNotesLink")
            .version("test-version")
            .regionalPricing(Collections.emptyMap())
            .compatibleProducts(Collections.emptyList())
            .requiredProducts(Collections.emptyList())
            .sortOrder(42)
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-sku", dto.getSku());
        assertEquals("test-slug", dto.getSlug());
        assertEquals("test-name", dto.getName());
        assertEquals("test-tagline", dto.getTagline());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-longDescription", dto.getLongDescription());
        assertEquals("test-featuredImageId", dto.getFeaturedImageId());
        assertEquals("test-demoVideoId", dto.getDemoVideoId());
        assertEquals("test-categoryId", dto.getCategoryId());
        assertEquals("test-categoryName", dto.getCategoryName());
        assertTrue(dto.getPublished());
        assertEquals("test-documentationLink", dto.getDocumentationLink());
        assertEquals("test-apiReferenceLink", dto.getApiReferenceLink());
        assertEquals("test-supportLink", dto.getSupportLink());
        assertEquals("test-releaseNotesLink", dto.getReleaseNotesLink());
        assertEquals("test-version", dto.getVersion());
        assertEquals(42, dto.getSortOrder());
    }

    @Test
    void testSettersAndGetters() {
        ProductDTO dto = new ProductDTO();
        dto.setId("val-id");
        dto.setSku("val-sku");
        dto.setSlug("val-slug");
        dto.setName("val-name");
        dto.setTagline("val-tagline");
        dto.setDescription("val-description");
        dto.setLongDescription("val-longDescription");
        dto.setFeaturedImageId("val-featuredImageId");
        dto.setDemoVideoId("val-demoVideoId");
        dto.setCategoryId("val-categoryId");
        dto.setCategoryName("val-categoryName");
        dto.setPublished(true);
        dto.setDocumentationLink("val-documentationLink");
        dto.setApiReferenceLink("val-apiReferenceLink");
        dto.setSupportLink("val-supportLink");
        dto.setReleaseNotesLink("val-releaseNotesLink");
        dto.setVersion("val-version");
        dto.setSortOrder(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-sku", dto.getSku());
        assertEquals("val-slug", dto.getSlug());
        assertEquals("val-name", dto.getName());
        assertEquals("val-tagline", dto.getTagline());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-longDescription", dto.getLongDescription());
        assertEquals("val-featuredImageId", dto.getFeaturedImageId());
        assertEquals("val-demoVideoId", dto.getDemoVideoId());
        assertEquals("val-categoryId", dto.getCategoryId());
        assertEquals("val-categoryName", dto.getCategoryName());
        assertTrue(dto.getPublished());
        assertEquals("val-documentationLink", dto.getDocumentationLink());
        assertEquals("val-apiReferenceLink", dto.getApiReferenceLink());
        assertEquals("val-supportLink", dto.getSupportLink());
        assertEquals("val-releaseNotesLink", dto.getReleaseNotesLink());
        assertEquals("val-version", dto.getVersion());
        assertEquals(99, dto.getSortOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        ProductDTO dto1 = ProductDTO.builder()
                        .id("test-id")
            .sku("test-sku")
            .slug("test-slug")
            .name("test-name")
            .tagline("test-tagline")
            .description("test-description")
            .longDescription("test-longDescription")
            .featuredImageId("test-featuredImageId")
            .galleryImageIds(Collections.emptyList())
            .demoVideoId("test-demoVideoId")
            .categoryId("test-categoryId")
            .categoryName("test-categoryName")
            .tags(Collections.emptyList())
            .features(Collections.emptyMap())
            .specifications(Collections.emptyMap())
            .variants(Collections.emptyList())
            .pricing(Collections.emptyList())
            .published(true)
            .documentationLink("test-documentationLink")
            .apiReferenceLink("test-apiReferenceLink")
            .supportLink("test-supportLink")
            .releaseNotesLink("test-releaseNotesLink")
            .version("test-version")
            .regionalPricing(Collections.emptyMap())
            .compatibleProducts(Collections.emptyList())
            .requiredProducts(Collections.emptyList())
            .sortOrder(42)
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        ProductDTO dto2 = ProductDTO.builder()
                        .id("test-id")
            .sku("test-sku")
            .slug("test-slug")
            .name("test-name")
            .tagline("test-tagline")
            .description("test-description")
            .longDescription("test-longDescription")
            .featuredImageId("test-featuredImageId")
            .galleryImageIds(Collections.emptyList())
            .demoVideoId("test-demoVideoId")
            .categoryId("test-categoryId")
            .categoryName("test-categoryName")
            .tags(Collections.emptyList())
            .features(Collections.emptyMap())
            .specifications(Collections.emptyMap())
            .variants(Collections.emptyList())
            .pricing(Collections.emptyList())
            .published(true)
            .documentationLink("test-documentationLink")
            .apiReferenceLink("test-apiReferenceLink")
            .supportLink("test-supportLink")
            .releaseNotesLink("test-releaseNotesLink")
            .version("test-version")
            .regionalPricing(Collections.emptyMap())
            .compatibleProducts(Collections.emptyList())
            .requiredProducts(Collections.emptyList())
            .sortOrder(42)
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProductDTO dto = ProductDTO.builder()
                        .id("test-id")
            .sku("test-sku")
            .slug("test-slug")
            .name("test-name")
            .tagline("test-tagline")
            .description("test-description")
            .longDescription("test-longDescription")
            .featuredImageId("test-featuredImageId")
            .galleryImageIds(Collections.emptyList())
            .demoVideoId("test-demoVideoId")
            .categoryId("test-categoryId")
            .categoryName("test-categoryName")
            .tags(Collections.emptyList())
            .features(Collections.emptyMap())
            .specifications(Collections.emptyMap())
            .variants(Collections.emptyList())
            .pricing(Collections.emptyList())
            .published(true)
            .documentationLink("test-documentationLink")
            .apiReferenceLink("test-apiReferenceLink")
            .supportLink("test-supportLink")
            .releaseNotesLink("test-releaseNotesLink")
            .version("test-version")
            .regionalPricing(Collections.emptyMap())
            .compatibleProducts(Collections.emptyList())
            .requiredProducts(Collections.emptyList())
            .sortOrder(42)
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}