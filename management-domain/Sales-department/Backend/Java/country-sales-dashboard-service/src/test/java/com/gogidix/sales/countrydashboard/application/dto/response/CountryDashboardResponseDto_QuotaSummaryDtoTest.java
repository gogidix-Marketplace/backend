package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.CountryDashboardResponseDto;
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
class CountryDashboardResponseDto_QuotaSummaryDtoTest {

        @Test
    void testBuilder() {
        CountryDashboardResponseDto.QuotaSummaryDto dto = CountryDashboardResponseDto.QuotaSummaryDto.builder()
                        .annualQuota(BigDecimal.TEN)
            .currency("test-currency")
            .yearToDateAchievement(BigDecimal.TEN)
            .remainingPercentage(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .onTrack(true)
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getAnnualQuota());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getYearToDateAchievement());
        assertEquals(BigDecimal.TEN, dto.getRemainingPercentage());
        assertEquals(BigDecimal.TEN, dto.getRemainingAmount());
        assertTrue(dto.getOnTrack());
    }

    @Test
    void testSettersAndGetters() {
        CountryDashboardResponseDto.QuotaSummaryDto dto = new CountryDashboardResponseDto.QuotaSummaryDto();
        dto.setAnnualQuota(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setYearToDateAchievement(BigDecimal.ONE);
        dto.setRemainingPercentage(BigDecimal.ONE);
        dto.setRemainingAmount(BigDecimal.ONE);
        dto.setOnTrack(true);
        assertEquals(BigDecimal.ONE, dto.getAnnualQuota());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getYearToDateAchievement());
        assertEquals(BigDecimal.ONE, dto.getRemainingPercentage());
        assertEquals(BigDecimal.ONE, dto.getRemainingAmount());
        assertTrue(dto.getOnTrack());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDashboardResponseDto.QuotaSummaryDto dto1 = CountryDashboardResponseDto.QuotaSummaryDto.builder()
                        .annualQuota(BigDecimal.TEN)
            .currency("test-currency")
            .yearToDateAchievement(BigDecimal.TEN)
            .remainingPercentage(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .onTrack(true)
            .build();
        CountryDashboardResponseDto.QuotaSummaryDto dto2 = CountryDashboardResponseDto.QuotaSummaryDto.builder()
                        .annualQuota(BigDecimal.TEN)
            .currency("test-currency")
            .yearToDateAchievement(BigDecimal.TEN)
            .remainingPercentage(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .onTrack(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryDashboardResponseDto.QuotaSummaryDto dto = CountryDashboardResponseDto.QuotaSummaryDto.builder()
                        .annualQuota(BigDecimal.TEN)
            .currency("test-currency")
            .yearToDateAchievement(BigDecimal.TEN)
            .remainingPercentage(BigDecimal.TEN)
            .remainingAmount(BigDecimal.TEN)
            .onTrack(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}