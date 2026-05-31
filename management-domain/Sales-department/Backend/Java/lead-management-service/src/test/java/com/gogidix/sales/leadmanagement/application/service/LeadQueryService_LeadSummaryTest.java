package com.gogidix.sales.leadmanagement.application.service;

import com.gogidix.sales.leadmanagement.application.service.LeadQueryService;
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
class LeadQueryService_LeadSummaryTest {

        @Test
    void testBuilder() {
        LeadQueryService.LeadSummary dto = LeadQueryService.LeadSummary.builder()
                        .totalLeads(42)
            .newLeads(42)
            .contactedLeads(42)
            .qualifiedLeads(42)
            .convertedLeads(42)
            .lostLeads(42)
            .activeLeads(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getTotalLeads());
        assertEquals(42, dto.getNewLeads());
        assertEquals(42, dto.getContactedLeads());
        assertEquals(42, dto.getQualifiedLeads());
        assertEquals(42, dto.getConvertedLeads());
        assertEquals(42, dto.getLostLeads());
        assertEquals(42, dto.getActiveLeads());
    }

    @Test
    void testBuilderWithValues() {
        LeadQueryService.LeadSummary dto = LeadQueryService.LeadSummary.builder()
            .totalLeads(99)
            .newLeads(99)
            .contactedLeads(99)
            .qualifiedLeads(99)
            .convertedLeads(99)
            .lostLeads(99)
            .activeLeads(99)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        LeadQueryService.LeadSummary dto1 = LeadQueryService.LeadSummary.builder()
                        .totalLeads(42)
            .newLeads(42)
            .contactedLeads(42)
            .qualifiedLeads(42)
            .convertedLeads(42)
            .lostLeads(42)
            .activeLeads(42)
            .build();
        LeadQueryService.LeadSummary dto2 = LeadQueryService.LeadSummary.builder()
                        .totalLeads(42)
            .newLeads(42)
            .contactedLeads(42)
            .qualifiedLeads(42)
            .convertedLeads(42)
            .lostLeads(42)
            .activeLeads(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeadQueryService.LeadSummary dto = LeadQueryService.LeadSummary.builder()
                        .totalLeads(42)
            .newLeads(42)
            .contactedLeads(42)
            .qualifiedLeads(42)
            .convertedLeads(42)
            .lostLeads(42)
            .activeLeads(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}