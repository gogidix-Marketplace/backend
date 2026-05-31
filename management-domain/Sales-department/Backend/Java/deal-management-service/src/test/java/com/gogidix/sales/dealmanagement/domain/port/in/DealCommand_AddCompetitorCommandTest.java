package com.gogidix.sales.dealmanagement.domain.port.in;

import com.gogidix.sales.dealmanagement.domain.model.Competitor;
import com.gogidix.sales.dealmanagement.domain.port.in.DealCommand;
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
class DealCommand_AddCompetitorCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.AddCompetitorCommand dto = new DealCommand.AddCompetitorCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        dto.setCompetitorName("val-competitorName");
        dto.setEstimatedDealValue(BigDecimal.ONE);
        dto.setCompetingProduct("val-competingProduct");
        dto.setCompetitorStrengths("val-competitorStrengths");
        dto.setCompetitorWeaknesses("val-competitorWeaknesses");
        dto.setOurAdvantage("val-ourAdvantage");
        dto.setProbabilityOfWin(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-competitorName", dto.getCompetitorName());
        assertEquals(BigDecimal.ONE, dto.getEstimatedDealValue());
        assertEquals("val-competingProduct", dto.getCompetingProduct());
        assertEquals("val-competitorStrengths", dto.getCompetitorStrengths());
        assertEquals("val-competitorWeaknesses", dto.getCompetitorWeaknesses());
        assertEquals("val-ourAdvantage", dto.getOurAdvantage());
        assertEquals(99, dto.getProbabilityOfWin());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.AddCompetitorCommand dto1 = new DealCommand.AddCompetitorCommand();
        DealCommand.AddCompetitorCommand dto2 = new DealCommand.AddCompetitorCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto1.setCompetitorName("test");
        dto1.setStrength(Competitor.StrengthLevel.VERY_WEAK);
        dto1.setThreat(Competitor.ThreatLevel.VERY_LOW);
        dto1.setEstimatedDealValue(BigDecimal.TEN);
        dto1.setCompetingProduct("test");
        dto1.setCompetitorStrengths("test");
        dto1.setCompetitorWeaknesses("test");
        dto1.setOurAdvantage("test");
        dto1.setProbabilityOfWin(42);
        dto2.setTenantId("test");
        dto2.setDealId("test");
        dto2.setCompetitorName("test");
        dto2.setStrength(Competitor.StrengthLevel.VERY_WEAK);
        dto2.setThreat(Competitor.ThreatLevel.VERY_LOW);
        dto2.setEstimatedDealValue(BigDecimal.TEN);
        dto2.setCompetingProduct("test");
        dto2.setCompetitorStrengths("test");
        dto2.setCompetitorWeaknesses("test");
        dto2.setOurAdvantage("test");
        dto2.setProbabilityOfWin(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.AddCompetitorCommand dto = new DealCommand.AddCompetitorCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setCompetitorName("test");
        dto.setStrength(Competitor.StrengthLevel.VERY_WEAK);
        dto.setThreat(Competitor.ThreatLevel.VERY_LOW);
        dto.setEstimatedDealValue(BigDecimal.TEN);
        dto.setCompetingProduct("test");
        dto.setCompetitorStrengths("test");
        dto.setCompetitorWeaknesses("test");
        dto.setOurAdvantage("test");
        dto.setProbabilityOfWin(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.AddCompetitorCommand dto = new DealCommand.AddCompetitorCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setCompetitorName("test");
        dto.setStrength(Competitor.StrengthLevel.VERY_WEAK);
        dto.setThreat(Competitor.ThreatLevel.VERY_LOW);
        dto.setEstimatedDealValue(BigDecimal.TEN);
        dto.setCompetingProduct("test");
        dto.setCompetitorStrengths("test");
        dto.setCompetitorWeaknesses("test");
        dto.setOurAdvantage("test");
        dto.setProbabilityOfWin(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}