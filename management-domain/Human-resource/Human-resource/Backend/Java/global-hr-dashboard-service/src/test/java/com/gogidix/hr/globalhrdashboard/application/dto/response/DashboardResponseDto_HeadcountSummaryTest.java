package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.gogidix.hr.globalhrdashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_HeadcountSummaryTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.HeadcountSummary dto = DashboardResponseDto.HeadcountSummary.builder()
                        .totalHeadcount(42)
            .permanentEmployees(42)
            .contractors(42)
            .interns(42)
            .yearOverYearChange(42)
            .monthOverMonthChange(42)
            .totalCountries(42)
            .totalRegions(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getTotalHeadcount());
        assertEquals(42, dto.getPermanentEmployees());
        assertEquals(42, dto.getContractors());
        assertEquals(42, dto.getInterns());
        assertEquals(42, dto.getYearOverYearChange());
        assertEquals(42, dto.getMonthOverMonthChange());
        assertEquals(42, dto.getTotalCountries());
        assertEquals(42, dto.getTotalRegions());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.HeadcountSummary dto = new DashboardResponseDto.HeadcountSummary();
        dto.setTotalHeadcount(99);
        dto.setPermanentEmployees(99);
        dto.setContractors(99);
        dto.setInterns(99);
        dto.setYearOverYearChange(99);
        dto.setMonthOverMonthChange(99);
        dto.setTotalCountries(99);
        dto.setTotalRegions(99);
        assertEquals(99, dto.getTotalHeadcount());
        assertEquals(99, dto.getPermanentEmployees());
        assertEquals(99, dto.getContractors());
        assertEquals(99, dto.getInterns());
        assertEquals(99, dto.getYearOverYearChange());
        assertEquals(99, dto.getMonthOverMonthChange());
        assertEquals(99, dto.getTotalCountries());
        assertEquals(99, dto.getTotalRegions());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.HeadcountSummary dto1 = DashboardResponseDto.HeadcountSummary.builder()
                        .totalHeadcount(42)
            .permanentEmployees(42)
            .contractors(42)
            .interns(42)
            .yearOverYearChange(42)
            .monthOverMonthChange(42)
            .totalCountries(42)
            .totalRegions(42)
            .build();
        DashboardResponseDto.HeadcountSummary dto2 = DashboardResponseDto.HeadcountSummary.builder()
                        .totalHeadcount(42)
            .permanentEmployees(42)
            .contractors(42)
            .interns(42)
            .yearOverYearChange(42)
            .monthOverMonthChange(42)
            .totalCountries(42)
            .totalRegions(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.HeadcountSummary dto = DashboardResponseDto.HeadcountSummary.builder()
                        .totalHeadcount(42)
            .permanentEmployees(42)
            .contractors(42)
            .interns(42)
            .yearOverYearChange(42)
            .monthOverMonthChange(42)
            .totalCountries(42)
            .totalRegions(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}