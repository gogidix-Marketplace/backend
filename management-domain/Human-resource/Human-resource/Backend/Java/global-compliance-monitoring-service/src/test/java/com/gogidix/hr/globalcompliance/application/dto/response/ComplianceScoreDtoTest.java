package com.gogidix.hr.globalcompliance.application.dto.response;

import com.gogidix.hr.globalcompliance.application.dto.response.ComplianceScoreDto;
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
class ComplianceScoreDtoTest {

        @Test
    void testBuilder() {
        ComplianceScoreDto dto = ComplianceScoreDto.builder()
                        .overallScore(null)
            .rating("test-rating")
            .passedChecks(42L)
            .failedChecks(42L)
            .partialChecks(42L)
            .totalChecks(42L)
            .compliancePercentage(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-rating", dto.getRating());
        assertEquals(42L, dto.getPassedChecks());
        assertEquals(42L, dto.getFailedChecks());
        assertEquals(42L, dto.getPartialChecks());
        assertEquals(42L, dto.getTotalChecks());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceScoreDto dto = new ComplianceScoreDto();
        dto.setRating("val-rating");
        assertEquals("val-rating", dto.getRating());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceScoreDto dto1 = ComplianceScoreDto.builder()
                        .overallScore(null)
            .rating("test-rating")
            .passedChecks(42L)
            .failedChecks(42L)
            .partialChecks(42L)
            .totalChecks(42L)
            .compliancePercentage(null)
            .build();
        ComplianceScoreDto dto2 = ComplianceScoreDto.builder()
                        .overallScore(null)
            .rating("test-rating")
            .passedChecks(42L)
            .failedChecks(42L)
            .partialChecks(42L)
            .totalChecks(42L)
            .compliancePercentage(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceScoreDto dto = ComplianceScoreDto.builder()
                        .overallScore(null)
            .rating("test-rating")
            .passedChecks(42L)
            .failedChecks(42L)
            .partialChecks(42L)
            .totalChecks(42L)
            .compliancePercentage(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}