package com.gogidix.finance.consolidation.domain.port.in;

import com.gogidix.finance.consolidation.domain.port.in.ConsolidationCommand;
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
class ConsolidationCommand_UpdateExchangeRateCommandTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationCommand.UpdateExchangeRateCommand dto = new ConsolidationCommand.UpdateExchangeRateCommand();
        dto.setTenantId("val-tenantId");
        dto.setRuleId("val-ruleId");
        dto.setExchangeRate(BigDecimal.ONE);
        dto.setSource("val-source");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ruleId", dto.getRuleId());
        assertEquals(BigDecimal.ONE, dto.getExchangeRate());
        assertEquals("val-source", dto.getSource());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationCommand.UpdateExchangeRateCommand dto1 = new ConsolidationCommand.UpdateExchangeRateCommand();
        ConsolidationCommand.UpdateExchangeRateCommand dto2 = new ConsolidationCommand.UpdateExchangeRateCommand();
        dto1.setTenantId("test");
        dto1.setRuleId("test");
        dto1.setExchangeRate(BigDecimal.TEN);
        dto1.setSource("test");
        dto1.setUpdatedBy("test");
        dto2.setTenantId("test");
        dto2.setRuleId("test");
        dto2.setExchangeRate(BigDecimal.TEN);
        dto2.setSource("test");
        dto2.setUpdatedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationCommand.UpdateExchangeRateCommand dto = new ConsolidationCommand.UpdateExchangeRateCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setExchangeRate(BigDecimal.TEN);
        dto.setSource("test");
        dto.setUpdatedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationCommand.UpdateExchangeRateCommand dto = new ConsolidationCommand.UpdateExchangeRateCommand();
        dto.setTenantId("test");
        dto.setRuleId("test");
        dto.setExchangeRate(BigDecimal.TEN);
        dto.setSource("test");
        dto.setUpdatedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}