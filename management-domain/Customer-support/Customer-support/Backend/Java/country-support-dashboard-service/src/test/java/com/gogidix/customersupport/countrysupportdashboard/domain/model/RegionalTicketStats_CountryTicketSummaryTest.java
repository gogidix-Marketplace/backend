package com.gogidix.customersupport.countrysupportdashboard.domain.model;

import com.gogidix.customersupport.countrysupportdashboard.domain.model.RegionalTicketStats;
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
class RegionalTicketStats_CountryTicketSummaryTest {

        @Test
    void testBuilder() {
        RegionalTicketStats.CountryTicketSummary dto = RegionalTicketStats.CountryTicketSummary.builder()
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
        RegionalTicketStats.CountryTicketSummary dto = new RegionalTicketStats.CountryTicketSummary();
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
        RegionalTicketStats.CountryTicketSummary dto1 = RegionalTicketStats.CountryTicketSummary.builder()
                        .countryCode("test-countryCode")
            .countryName("test-countryName")
            .totalTickets(42)
            .openTickets(42)
            .resolvedTickets(42)
            .satisfactionScore(null)
            .build();
        RegionalTicketStats.CountryTicketSummary dto2 = RegionalTicketStats.CountryTicketSummary.builder()
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
        RegionalTicketStats.CountryTicketSummary dto = RegionalTicketStats.CountryTicketSummary.builder()
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