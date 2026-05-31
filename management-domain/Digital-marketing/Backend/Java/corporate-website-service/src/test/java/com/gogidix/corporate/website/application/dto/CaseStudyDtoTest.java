package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.CaseStudyDto;
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
class CaseStudyDtoTest {

        @Test
    void testBuilder() {
        CaseStudyDto dto = CaseStudyDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .clientName("test-clientName")
            .clientLogo("test-clientLogo")
            .industry("test-industry")
            .projectDuration("test-projectDuration")
            .availableRegions(null)
            .challenge("test-challenge")
            .solution("test-solution")
            .results("test-results")
            .metrics(Collections.emptyList())
            .technologies(Collections.emptyList())
            .services(Collections.emptyList())
            .heroImage("test-heroImage")
            .heroImageAlt("test-heroImageAlt")
            .gallery(Collections.emptyList())
            .testimonial("test-testimonial")
            .testimonialAuthor("test-testimonialAuthor")
            .testimonialRole("test-testimonialRole")
            .testimonialImage("test-testimonialImage")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .featured(true)
            .sortOrder(42)
            .viewCount(42L)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-slug", dto.getSlug());
        assertEquals("test-clientName", dto.getClientName());
        assertEquals("test-clientLogo", dto.getClientLogo());
        assertEquals("test-industry", dto.getIndustry());
        assertEquals("test-projectDuration", dto.getProjectDuration());
        assertEquals("test-challenge", dto.getChallenge());
        assertEquals("test-solution", dto.getSolution());
        assertEquals("test-results", dto.getResults());
        assertEquals("test-heroImage", dto.getHeroImage());
        assertEquals("test-heroImageAlt", dto.getHeroImageAlt());
        assertEquals("test-testimonial", dto.getTestimonial());
        assertEquals("test-testimonialAuthor", dto.getTestimonialAuthor());
        assertEquals("test-testimonialRole", dto.getTestimonialRole());
        assertEquals("test-testimonialImage", dto.getTestimonialImage());
        assertTrue(dto.isFeatured());
        assertEquals(42, dto.getSortOrder());
        assertEquals(42L, dto.getViewCount());
    }

    @Test
    void testSettersAndGetters() {
        CaseStudyDto dto = new CaseStudyDto();
        dto.setId("val-id");
        dto.setSlug("val-slug");
        dto.setClientName("val-clientName");
        dto.setClientLogo("val-clientLogo");
        dto.setIndustry("val-industry");
        dto.setProjectDuration("val-projectDuration");
        dto.setChallenge("val-challenge");
        dto.setSolution("val-solution");
        dto.setResults("val-results");
        dto.setHeroImage("val-heroImage");
        dto.setHeroImageAlt("val-heroImageAlt");
        dto.setTestimonial("val-testimonial");
        dto.setTestimonialAuthor("val-testimonialAuthor");
        dto.setTestimonialRole("val-testimonialRole");
        dto.setTestimonialImage("val-testimonialImage");
        dto.setFeatured(true);
        dto.setSortOrder(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-slug", dto.getSlug());
        assertEquals("val-clientName", dto.getClientName());
        assertEquals("val-clientLogo", dto.getClientLogo());
        assertEquals("val-industry", dto.getIndustry());
        assertEquals("val-projectDuration", dto.getProjectDuration());
        assertEquals("val-challenge", dto.getChallenge());
        assertEquals("val-solution", dto.getSolution());
        assertEquals("val-results", dto.getResults());
        assertEquals("val-heroImage", dto.getHeroImage());
        assertEquals("val-heroImageAlt", dto.getHeroImageAlt());
        assertEquals("val-testimonial", dto.getTestimonial());
        assertEquals("val-testimonialAuthor", dto.getTestimonialAuthor());
        assertEquals("val-testimonialRole", dto.getTestimonialRole());
        assertEquals("val-testimonialImage", dto.getTestimonialImage());
        assertTrue(dto.isFeatured());
        assertEquals(99, dto.getSortOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        CaseStudyDto dto1 = CaseStudyDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .clientName("test-clientName")
            .clientLogo("test-clientLogo")
            .industry("test-industry")
            .projectDuration("test-projectDuration")
            .availableRegions(null)
            .challenge("test-challenge")
            .solution("test-solution")
            .results("test-results")
            .metrics(Collections.emptyList())
            .technologies(Collections.emptyList())
            .services(Collections.emptyList())
            .heroImage("test-heroImage")
            .heroImageAlt("test-heroImageAlt")
            .gallery(Collections.emptyList())
            .testimonial("test-testimonial")
            .testimonialAuthor("test-testimonialAuthor")
            .testimonialRole("test-testimonialRole")
            .testimonialImage("test-testimonialImage")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .featured(true)
            .sortOrder(42)
            .viewCount(42L)
            .build();
        CaseStudyDto dto2 = CaseStudyDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .clientName("test-clientName")
            .clientLogo("test-clientLogo")
            .industry("test-industry")
            .projectDuration("test-projectDuration")
            .availableRegions(null)
            .challenge("test-challenge")
            .solution("test-solution")
            .results("test-results")
            .metrics(Collections.emptyList())
            .technologies(Collections.emptyList())
            .services(Collections.emptyList())
            .heroImage("test-heroImage")
            .heroImageAlt("test-heroImageAlt")
            .gallery(Collections.emptyList())
            .testimonial("test-testimonial")
            .testimonialAuthor("test-testimonialAuthor")
            .testimonialRole("test-testimonialRole")
            .testimonialImage("test-testimonialImage")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .featured(true)
            .sortOrder(42)
            .viewCount(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CaseStudyDto dto = CaseStudyDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .clientName("test-clientName")
            .clientLogo("test-clientLogo")
            .industry("test-industry")
            .projectDuration("test-projectDuration")
            .availableRegions(null)
            .challenge("test-challenge")
            .solution("test-solution")
            .results("test-results")
            .metrics(Collections.emptyList())
            .technologies(Collections.emptyList())
            .services(Collections.emptyList())
            .heroImage("test-heroImage")
            .heroImageAlt("test-heroImageAlt")
            .gallery(Collections.emptyList())
            .testimonial("test-testimonial")
            .testimonialAuthor("test-testimonialAuthor")
            .testimonialRole("test-testimonialRole")
            .testimonialImage("test-testimonialImage")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .featured(true)
            .sortOrder(42)
            .viewCount(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}