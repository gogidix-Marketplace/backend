package com.gogidix.sales.revenue.domain.model.dto;

import com.gogidix.sales.revenue.domain.model.dto.ARRSummaryDto;
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
class ARRSummaryDtoTest {

        @Test
    void testBuilder() {
        ARRSummaryDto dto = ARRSummaryDto.builder()
                        .tenantId("test-tenantId")
            .reportingPeriod(null)
            .newBusinessARR(BigDecimal.TEN)
            .expansionARR(BigDecimal.TEN)
            .contractionARR(BigDecimal.TEN)
            .churnARR(BigDecimal.TEN)
            .totalARR(BigDecimal.TEN)
            .previousARR(BigDecimal.TEN)
            .arrChange(BigDecimal.TEN)
            .arrGrowthRate(BigDecimal.TEN)
            .averageContractValue(BigDecimal.TEN)
            .customerCount(42L)
            .currency("test-currency")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals(BigDecimal.TEN, dto.getNewBusinessARR());
        assertEquals(BigDecimal.TEN, dto.getExpansionARR());
        assertEquals(BigDecimal.TEN, dto.getContractionARR());
        assertEquals(BigDecimal.TEN, dto.getChurnARR());
        assertEquals(BigDecimal.TEN, dto.getTotalARR());
        assertEquals(BigDecimal.TEN, dto.getPreviousARR());
        assertEquals(BigDecimal.TEN, dto.getArrChange());
        assertEquals(BigDecimal.TEN, dto.getArrGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getAverageContractValue());
        assertEquals(42L, dto.getCustomerCount());
        assertEquals("test-currency", dto.getCurrency());
    }

    @Test
    void testSettersAndGetters() {
        ARRSummaryDto dto = new ARRSummaryDto();
        dto.setTenantId("val-tenantId");
        dto.setNewBusinessARR(BigDecimal.ONE);
        dto.setExpansionARR(BigDecimal.ONE);
        dto.setContractionARR(BigDecimal.ONE);
        dto.setChurnARR(BigDecimal.ONE);
        dto.setTotalARR(BigDecimal.ONE);
        dto.setPreviousARR(BigDecimal.ONE);
        dto.setArrChange(BigDecimal.ONE);
        dto.setArrGrowthRate(BigDecimal.ONE);
        dto.setAverageContractValue(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals(BigDecimal.ONE, dto.getNewBusinessARR());
        assertEquals(BigDecimal.ONE, dto.getExpansionARR());
        assertEquals(BigDecimal.ONE, dto.getContractionARR());
        assertEquals(BigDecimal.ONE, dto.getChurnARR());
        assertEquals(BigDecimal.ONE, dto.getTotalARR());
        assertEquals(BigDecimal.ONE, dto.getPreviousARR());
        assertEquals(BigDecimal.ONE, dto.getArrChange());
        assertEquals(BigDecimal.ONE, dto.getArrGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getAverageContractValue());
        assertEquals("val-currency", dto.getCurrency());
    }

    @Test
    void testEqualsAndHashCode() {
        ARRSummaryDto dto1 = ARRSummaryDto.builder()
                        .tenantId("test-tenantId")
            .reportingPeriod(null)
            .newBusinessARR(BigDecimal.TEN)
            .expansionARR(BigDecimal.TEN)
            .contractionARR(BigDecimal.TEN)
            .churnARR(BigDecimal.TEN)
            .totalARR(BigDecimal.TEN)
            .previousARR(BigDecimal.TEN)
            .arrChange(BigDecimal.TEN)
            .arrGrowthRate(BigDecimal.TEN)
            .averageContractValue(BigDecimal.TEN)
            .customerCount(42L)
            .currency("test-currency")
            .build();
        ARRSummaryDto dto2 = ARRSummaryDto.builder()
                        .tenantId("test-tenantId")
            .reportingPeriod(null)
            .newBusinessARR(BigDecimal.TEN)
            .expansionARR(BigDecimal.TEN)
            .contractionARR(BigDecimal.TEN)
            .churnARR(BigDecimal.TEN)
            .totalARR(BigDecimal.TEN)
            .previousARR(BigDecimal.TEN)
            .arrChange(BigDecimal.TEN)
            .arrGrowthRate(BigDecimal.TEN)
            .averageContractValue(BigDecimal.TEN)
            .customerCount(42L)
            .currency("test-currency")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ARRSummaryDto dto = ARRSummaryDto.builder()
                        .tenantId("test-tenantId")
            .reportingPeriod(null)
            .newBusinessARR(BigDecimal.TEN)
            .expansionARR(BigDecimal.TEN)
            .contractionARR(BigDecimal.TEN)
            .churnARR(BigDecimal.TEN)
            .totalARR(BigDecimal.TEN)
            .previousARR(BigDecimal.TEN)
            .arrChange(BigDecimal.TEN)
            .arrGrowthRate(BigDecimal.TEN)
            .averageContractValue(BigDecimal.TEN)
            .customerCount(42L)
            .currency("test-currency")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}