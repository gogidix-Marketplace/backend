package com.gogidix.sales.revenue.domain.model.dto;

import com.gogidix.sales.revenue.domain.model.dto.MRRSummaryDto;
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
class MRRSummaryDtoTest {

        @Test
    void testBuilder() {
        MRRSummaryDto dto = MRRSummaryDto.builder()
                        .tenantId("test-tenantId")
            .reportingPeriod(null)
            .newBusinessMRR(BigDecimal.TEN)
            .expansionMRR(BigDecimal.TEN)
            .contractionMRR(BigDecimal.TEN)
            .churnMRR(BigDecimal.TEN)
            .totalMRR(BigDecimal.TEN)
            .previousMRR(BigDecimal.TEN)
            .mrrChange(BigDecimal.TEN)
            .mrrGrowthRate(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(BigDecimal.TEN, dto.getNewBusinessMRR());
        assertEquals(BigDecimal.TEN, dto.getExpansionMRR());
        assertEquals(BigDecimal.TEN, dto.getContractionMRR());
        assertEquals(BigDecimal.TEN, dto.getChurnMRR());
        assertEquals(BigDecimal.TEN, dto.getTotalMRR());
        assertEquals(BigDecimal.TEN, dto.getPreviousMRR());
        assertEquals(BigDecimal.TEN, dto.getMrrChange());
        assertEquals(BigDecimal.TEN, dto.getMrrGrowthRate());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testSettersAndGetters() {
        MRRSummaryDto dto = new MRRSummaryDto();
        dto.setTenantId("val-tenantId");
        dto.setNewBusinessMRR(BigDecimal.ONE);
        dto.setExpansionMRR(BigDecimal.ONE);
        dto.setContractionMRR(BigDecimal.ONE);
        dto.setChurnMRR(BigDecimal.ONE);
        dto.setTotalMRR(BigDecimal.ONE);
        dto.setPreviousMRR(BigDecimal.ONE);
        dto.setMrrChange(BigDecimal.ONE);
        dto.setMrrGrowthRate(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(BigDecimal.ONE, dto.getNewBusinessMRR());
        assertEquals(BigDecimal.ONE, dto.getExpansionMRR());
        assertEquals(BigDecimal.ONE, dto.getContractionMRR());
        assertEquals(BigDecimal.ONE, dto.getChurnMRR());
        assertEquals(BigDecimal.ONE, dto.getTotalMRR());
        assertEquals(BigDecimal.ONE, dto.getPreviousMRR());
        assertEquals(BigDecimal.ONE, dto.getMrrChange());
        assertEquals(BigDecimal.ONE, dto.getMrrGrowthRate());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        MRRSummaryDto dto1 = MRRSummaryDto.builder()
                        .tenantId("test-tenantId")
            .reportingPeriod(null)
            .newBusinessMRR(BigDecimal.TEN)
            .expansionMRR(BigDecimal.TEN)
            .contractionMRR(BigDecimal.TEN)
            .churnMRR(BigDecimal.TEN)
            .totalMRR(BigDecimal.TEN)
            .previousMRR(BigDecimal.TEN)
            .mrrChange(BigDecimal.TEN)
            .mrrGrowthRate(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        MRRSummaryDto dto2 = MRRSummaryDto.builder()
                        .tenantId("test-tenantId")
            .reportingPeriod(null)
            .newBusinessMRR(BigDecimal.TEN)
            .expansionMRR(BigDecimal.TEN)
            .contractionMRR(BigDecimal.TEN)
            .churnMRR(BigDecimal.TEN)
            .totalMRR(BigDecimal.TEN)
            .previousMRR(BigDecimal.TEN)
            .mrrChange(BigDecimal.TEN)
            .mrrGrowthRate(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MRRSummaryDto dto = MRRSummaryDto.builder()
                        .tenantId("test-tenantId")
            .reportingPeriod(null)
            .newBusinessMRR(BigDecimal.TEN)
            .expansionMRR(BigDecimal.TEN)
            .contractionMRR(BigDecimal.TEN)
            .churnMRR(BigDecimal.TEN)
            .totalMRR(BigDecimal.TEN)
            .previousMRR(BigDecimal.TEN)
            .mrrChange(BigDecimal.TEN)
            .mrrGrowthRate(BigDecimal.TEN)
            .currency("test-currency")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}