package com.gogidix.customersupport.countrysupportdashboard.application.dto;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.RegionalTicketStatsResponseDto;
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
class RegionalTicketStatsResponseDto_CountryTicketSummaryDtoTest {

        @Test
    void testBuilder() {
        RegionalTicketStatsResponseDto.CountryTicketSummaryDto dto = RegionalTicketStatsResponseDto.CountryTicketSummaryDto.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .totalTickets(42)
            .openTickets(42)
            .resolvedTickets(42)
            .satisfactionScore(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-countryName", dto.getCountryName());
        assertEquals(42, dto.getTotalTickets());
        assertEquals(42, dto.getOpenTickets());
        assertEquals(42, dto.getResolvedTickets());
    }

    @Test
    void testSettersAndGetters() {
        RegionalTicketStatsResponseDto.CountryTicketSummaryDto dto = new RegionalTicketStatsResponseDto.CountryTicketSummaryDto();
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setTotalTickets(99);
        dto.setOpenTickets(99);
        dto.setResolvedTickets(99);
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals(99, dto.getTotalTickets());
        assertEquals(99, dto.getOpenTickets());
        assertEquals(99, dto.getResolvedTickets());
    }

    @Test
    void testEqualsAndHashCode() {
        RegionalTicketStatsResponseDto.CountryTicketSummaryDto dto1 = RegionalTicketStatsResponseDto.CountryTicketSummaryDto.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .totalTickets(42)
            .openTickets(42)
            .resolvedTickets(42)
            .satisfactionScore(null)
            .build();
        RegionalTicketStatsResponseDto.CountryTicketSummaryDto dto2 = RegionalTicketStatsResponseDto.CountryTicketSummaryDto.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .totalTickets(42)
            .openTickets(42)
            .resolvedTickets(42)
            .satisfactionScore(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RegionalTicketStatsResponseDto.CountryTicketSummaryDto dto = RegionalTicketStatsResponseDto.CountryTicketSummaryDto.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .totalTickets(42)
            .openTickets(42)
            .resolvedTickets(42)
            .satisfactionScore(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}