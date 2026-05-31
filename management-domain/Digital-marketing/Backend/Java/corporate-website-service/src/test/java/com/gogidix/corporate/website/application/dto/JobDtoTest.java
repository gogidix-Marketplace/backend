package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.JobDto;
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
class JobDtoTest {

        @Test
    void testBuilder() {
        JobDto dto = JobDto.builder()
                        .id("test-id")
            .jobKey("test-jobKey")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .department("test-department")
            .employmentType("test-employmentType")
            .experienceLevel("test-experienceLevel")
            .location("test-location")
            .remote(true)
            .availableRegions(null)
            .responsibilities(Collections.emptyList())
            .requirements(Collections.emptyList())
            .benefits(Collections.emptyList())
            .salaryMin("test-salaryMin")
            .salaryMax("test-salaryMax")
            .salaryCurrency("test-salaryCurrency")
            .applicationUrl("test-applicationUrl")
            .applicationEmail("test-applicationEmail")
            .deadline(LocalDateTime.of(2025,1,15,10,0))
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .closeDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .featured(true)
            .sortOrder(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-jobKey", dto.getJobKey());
        assertEquals("test-slug", dto.getSlug());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-employmentType", dto.getEmploymentType());
        assertEquals("test-experienceLevel", dto.getExperienceLevel());
        assertEquals("test-location", dto.getLocation());
        assertTrue(dto.isRemote());
        assertEquals("test-salaryMin", dto.getSalaryMin());
        assertEquals("test-salaryMax", dto.getSalaryMax());
        assertEquals("test-salaryCurrency", dto.getSalaryCurrency());
        assertEquals("test-applicationUrl", dto.getApplicationUrl());
        assertEquals("test-applicationEmail", dto.getApplicationEmail());
        assertTrue(dto.isFeatured());
        assertEquals(42, dto.getSortOrder());
    }

    @Test
    void testSettersAndGetters() {
        JobDto dto = new JobDto();
        dto.setId("val-id");
        dto.setJobKey("val-jobKey");
        dto.setSlug("val-slug");
        dto.setDepartment("val-department");
        dto.setEmploymentType("val-employmentType");
        dto.setExperienceLevel("val-experienceLevel");
        dto.setLocation("val-location");
        dto.setRemote(true);
        dto.setSalaryMin("val-salaryMin");
        dto.setSalaryMax("val-salaryMax");
        dto.setSalaryCurrency("val-salaryCurrency");
        dto.setApplicationUrl("val-applicationUrl");
        dto.setApplicationEmail("val-applicationEmail");
        dto.setFeatured(true);
        dto.setSortOrder(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-jobKey", dto.getJobKey());
        assertEquals("val-slug", dto.getSlug());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-employmentType", dto.getEmploymentType());
        assertEquals("val-experienceLevel", dto.getExperienceLevel());
        assertEquals("val-location", dto.getLocation());
        assertTrue(dto.isRemote());
        assertEquals("val-salaryMin", dto.getSalaryMin());
        assertEquals("val-salaryMax", dto.getSalaryMax());
        assertEquals("val-salaryCurrency", dto.getSalaryCurrency());
        assertEquals("val-applicationUrl", dto.getApplicationUrl());
        assertEquals("val-applicationEmail", dto.getApplicationEmail());
        assertTrue(dto.isFeatured());
        assertEquals(99, dto.getSortOrder());
    }

    @Test
    void testEqualsAndHashCode() {
        JobDto dto1 = JobDto.builder()
                        .id("test-id")
            .jobKey("test-jobKey")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .department("test-department")
            .employmentType("test-employmentType")
            .experienceLevel("test-experienceLevel")
            .location("test-location")
            .remote(true)
            .availableRegions(null)
            .responsibilities(Collections.emptyList())
            .requirements(Collections.emptyList())
            .benefits(Collections.emptyList())
            .salaryMin("test-salaryMin")
            .salaryMax("test-salaryMax")
            .salaryCurrency("test-salaryCurrency")
            .applicationUrl("test-applicationUrl")
            .applicationEmail("test-applicationEmail")
            .deadline(LocalDateTime.of(2025,1,15,10,0))
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .closeDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .featured(true)
            .sortOrder(42)
            .build();
        JobDto dto2 = JobDto.builder()
                        .id("test-id")
            .jobKey("test-jobKey")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .department("test-department")
            .employmentType("test-employmentType")
            .experienceLevel("test-experienceLevel")
            .location("test-location")
            .remote(true)
            .availableRegions(null)
            .responsibilities(Collections.emptyList())
            .requirements(Collections.emptyList())
            .benefits(Collections.emptyList())
            .salaryMin("test-salaryMin")
            .salaryMax("test-salaryMax")
            .salaryCurrency("test-salaryCurrency")
            .applicationUrl("test-applicationUrl")
            .applicationEmail("test-applicationEmail")
            .deadline(LocalDateTime.of(2025,1,15,10,0))
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .closeDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .featured(true)
            .sortOrder(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        JobDto dto = JobDto.builder()
                        .id("test-id")
            .jobKey("test-jobKey")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .department("test-department")
            .employmentType("test-employmentType")
            .experienceLevel("test-experienceLevel")
            .location("test-location")
            .remote(true)
            .availableRegions(null)
            .responsibilities(Collections.emptyList())
            .requirements(Collections.emptyList())
            .benefits(Collections.emptyList())
            .salaryMin("test-salaryMin")
            .salaryMax("test-salaryMax")
            .salaryCurrency("test-salaryCurrency")
            .applicationUrl("test-applicationUrl")
            .applicationEmail("test-applicationEmail")
            .deadline(LocalDateTime.of(2025,1,15,10,0))
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .closeDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .featured(true)
            .sortOrder(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}