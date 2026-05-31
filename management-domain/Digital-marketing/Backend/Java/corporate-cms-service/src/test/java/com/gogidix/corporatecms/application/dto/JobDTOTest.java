package com.gogidix.corporatecms.application.dto;

import com.gogidix.corporatecms.application.dto.JobDTO;
import com.gogidix.corporatecms.domain.enums.JobStatus;
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
class JobDTOTest {

        @Test
    void testBuilder() {
        JobDTO dto = JobDTO.builder()
                        .id("test-id")
            .slug("test-slug")
            .status(JobStatus.DRAFT)
            .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .responsibilities(Collections.emptyList())
            .requirements(Collections.emptyList())
            .benefits(Collections.emptyList())
            .skills(Collections.emptyList())
            .departmentId("test-departmentId")
            .departmentName("test-departmentName")
            .location("test-location")
            .locationType("test-locationType")
            .employmentType("test-employmentType")
            .experienceLevel("test-experienceLevel")
            .salaryMin("test-salaryMin")
            .salaryMax("test-salaryMax")
            .salaryCurrency("test-salaryCurrency")
            .salaryDisplay("test-salaryDisplay")
            .featuredImageId("test-featuredImageId")
            .galleryImageIds(Collections.emptyList())
            .externalApplyUrl("test-externalApplyUrl")
            .sortOrder(42)
            .featured(true)
            .applicationDeadline(LocalDateTime.of(2025,1,15,10,0))
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .viewCount(42)
            .applicationCount(42)
            .metadata(Collections.emptyMap())
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-slug", dto.getSlug());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-summary", dto.getSummary());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-departmentId", dto.getDepartmentId());
        assertEquals("test-departmentName", dto.getDepartmentName());
        assertEquals("test-location", dto.getLocation());
        assertEquals("test-locationType", dto.getLocationType());
        assertEquals("test-employmentType", dto.getEmploymentType());
        assertEquals("test-experienceLevel", dto.getExperienceLevel());
        assertEquals("test-salaryMin", dto.getSalaryMin());
        assertEquals("test-salaryMax", dto.getSalaryMax());
        assertEquals("test-salaryCurrency", dto.getSalaryCurrency());
        assertEquals("test-salaryDisplay", dto.getSalaryDisplay());
        assertEquals("test-featuredImageId", dto.getFeaturedImageId());
        assertEquals("test-externalApplyUrl", dto.getExternalApplyUrl());
        assertEquals(42, dto.getSortOrder());
        assertTrue(dto.getFeatured());
        assertEquals(42, dto.getViewCount());
        assertEquals(42, dto.getApplicationCount());
    }

    @Test
    void testSettersAndGetters() {
        JobDTO dto = new JobDTO();
        dto.setId("val-id");
        dto.setSlug("val-slug");
        dto.setTitle("val-title");
        dto.setSummary("val-summary");
        dto.setDescription("val-description");
        dto.setDepartmentId("val-departmentId");
        dto.setDepartmentName("val-departmentName");
        dto.setLocation("val-location");
        dto.setLocationType("val-locationType");
        dto.setEmploymentType("val-employmentType");
        dto.setExperienceLevel("val-experienceLevel");
        dto.setSalaryMin("val-salaryMin");
        dto.setSalaryMax("val-salaryMax");
        dto.setSalaryCurrency("val-salaryCurrency");
        dto.setSalaryDisplay("val-salaryDisplay");
        dto.setFeaturedImageId("val-featuredImageId");
        dto.setExternalApplyUrl("val-externalApplyUrl");
        dto.setSortOrder(99);
        dto.setFeatured(true);
        dto.setViewCount(99);
        dto.setApplicationCount(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-slug", dto.getSlug());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-summary", dto.getSummary());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-departmentId", dto.getDepartmentId());
        assertEquals("val-departmentName", dto.getDepartmentName());
        assertEquals("val-location", dto.getLocation());
        assertEquals("val-locationType", dto.getLocationType());
        assertEquals("val-employmentType", dto.getEmploymentType());
        assertEquals("val-experienceLevel", dto.getExperienceLevel());
        assertEquals("val-salaryMin", dto.getSalaryMin());
        assertEquals("val-salaryMax", dto.getSalaryMax());
        assertEquals("val-salaryCurrency", dto.getSalaryCurrency());
        assertEquals("val-salaryDisplay", dto.getSalaryDisplay());
        assertEquals("val-featuredImageId", dto.getFeaturedImageId());
        assertEquals("val-externalApplyUrl", dto.getExternalApplyUrl());
        assertEquals(99, dto.getSortOrder());
        assertTrue(dto.getFeatured());
        assertEquals(99, dto.getViewCount());
        assertEquals(99, dto.getApplicationCount());
    }

    @Test
    void testEqualsAndHashCode() {
        JobDTO dto1 = JobDTO.builder()
                        .id("test-id")
            .slug("test-slug")
            .status(JobStatus.DRAFT)
            .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .responsibilities(Collections.emptyList())
            .requirements(Collections.emptyList())
            .benefits(Collections.emptyList())
            .skills(Collections.emptyList())
            .departmentId("test-departmentId")
            .departmentName("test-departmentName")
            .location("test-location")
            .locationType("test-locationType")
            .employmentType("test-employmentType")
            .experienceLevel("test-experienceLevel")
            .salaryMin("test-salaryMin")
            .salaryMax("test-salaryMax")
            .salaryCurrency("test-salaryCurrency")
            .salaryDisplay("test-salaryDisplay")
            .featuredImageId("test-featuredImageId")
            .galleryImageIds(Collections.emptyList())
            .externalApplyUrl("test-externalApplyUrl")
            .sortOrder(42)
            .featured(true)
            .applicationDeadline(LocalDateTime.of(2025,1,15,10,0))
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .viewCount(42)
            .applicationCount(42)
            .metadata(Collections.emptyMap())
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        JobDTO dto2 = JobDTO.builder()
                        .id("test-id")
            .slug("test-slug")
            .status(JobStatus.DRAFT)
            .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .responsibilities(Collections.emptyList())
            .requirements(Collections.emptyList())
            .benefits(Collections.emptyList())
            .skills(Collections.emptyList())
            .departmentId("test-departmentId")
            .departmentName("test-departmentName")
            .location("test-location")
            .locationType("test-locationType")
            .employmentType("test-employmentType")
            .experienceLevel("test-experienceLevel")
            .salaryMin("test-salaryMin")
            .salaryMax("test-salaryMax")
            .salaryCurrency("test-salaryCurrency")
            .salaryDisplay("test-salaryDisplay")
            .featuredImageId("test-featuredImageId")
            .galleryImageIds(Collections.emptyList())
            .externalApplyUrl("test-externalApplyUrl")
            .sortOrder(42)
            .featured(true)
            .applicationDeadline(LocalDateTime.of(2025,1,15,10,0))
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .viewCount(42)
            .applicationCount(42)
            .metadata(Collections.emptyMap())
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        JobDTO dto = JobDTO.builder()
                        .id("test-id")
            .slug("test-slug")
            .status(JobStatus.DRAFT)
            .title("test-title")
            .summary("test-summary")
            .description("test-description")
            .responsibilities(Collections.emptyList())
            .requirements(Collections.emptyList())
            .benefits(Collections.emptyList())
            .skills(Collections.emptyList())
            .departmentId("test-departmentId")
            .departmentName("test-departmentName")
            .location("test-location")
            .locationType("test-locationType")
            .employmentType("test-employmentType")
            .experienceLevel("test-experienceLevel")
            .salaryMin("test-salaryMin")
            .salaryMax("test-salaryMax")
            .salaryCurrency("test-salaryCurrency")
            .salaryDisplay("test-salaryDisplay")
            .featuredImageId("test-featuredImageId")
            .galleryImageIds(Collections.emptyList())
            .externalApplyUrl("test-externalApplyUrl")
            .sortOrder(42)
            .featured(true)
            .applicationDeadline(LocalDateTime.of(2025,1,15,10,0))
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .viewCount(42)
            .applicationCount(42)
            .metadata(Collections.emptyMap())
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}