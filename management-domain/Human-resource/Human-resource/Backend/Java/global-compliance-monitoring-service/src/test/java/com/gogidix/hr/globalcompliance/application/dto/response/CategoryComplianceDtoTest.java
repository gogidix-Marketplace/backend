package com.gogidix.hr.globalcompliance.application.dto.response;

import com.gogidix.hr.globalcompliance.application.dto.response.CategoryComplianceDto;
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
class CategoryComplianceDtoTest {

        @Test
    void testBuilder() {
        CategoryComplianceDto dto = CategoryComplianceDto.builder()
                        .category("test-category")
            .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .passedChecks(42L)
            .complianceScore(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-category", dto.getCategory());
        assertEquals(42L, dto.getTotalRequirements());
        assertEquals(42L, dto.getActiveRequirements());
        assertEquals(42L, dto.getTotalChecks());
        assertEquals(42L, dto.getPassedChecks());
    }

    @Test
    void testSettersAndGetters() {
        CategoryComplianceDto dto = new CategoryComplianceDto();
        dto.setCategory("val-category");
        assertEquals("val-category", dto.getCategory());
    }

    @Test
    void testEqualsAndHashCode() {
        CategoryComplianceDto dto1 = CategoryComplianceDto.builder()
                        .category("test-category")
            .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .passedChecks(42L)
            .complianceScore(null)
            .build();
        CategoryComplianceDto dto2 = CategoryComplianceDto.builder()
                        .category("test-category")
            .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .passedChecks(42L)
            .complianceScore(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CategoryComplianceDto dto = CategoryComplianceDto.builder()
                        .category("test-category")
            .totalRequirements(42L)
            .activeRequirements(42L)
            .totalChecks(42L)
            .passedChecks(42L)
            .complianceScore(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}